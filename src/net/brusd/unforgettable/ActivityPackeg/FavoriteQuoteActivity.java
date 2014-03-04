package net.brusd.unforgettable.ActivityPackeg;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.brusd.unforgettable.AdsAndAnalytics.AllMobAds;
import net.brusd.unforgettable.AppDatabase.AppDB;
import net.brusd.unforgettable.GlobalPackeg.DataStoreg;
import net.brusd.unforgettable.FragmentPackeg.FavoriteQuoteFragment;
import net.brusd.unforgettable.FragmentPackeg.NowFavoriteQuoteFragment;
import net.brusd.unforgettable.GlobalPackeg.OnSwipeTouchListener;
import net.brusd.unforgettable.R;

/**
 * Created by BruSD on 11.01.14.
 */
public class FavoriteQuoteActivity extends ActionBarActivity{

    private ShareActionProvider mShareActionProvider;
    private FragmentTransaction ft;
    private Fragment fragment;
    private static AppDB appDB = null;
    private  Intent shareIntent;

    private ImageButton nextFavoriteQuoteImageButton, previousFavoriteQuoteImageButton;
    private TextView themeFavoriteNameTextView;
    private RelativeLayout swipeContainer;


    private AllMobAds allMobAds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite_quote_layout);

        swipeContainer = (RelativeLayout)findViewById(R.id.swipe_container);
        swipeContainer.setOnTouchListener(new FragmentSwipeDetector());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appDB = AppDB.getInstance(this);
        DataStoreg.favoriteQuoteIDSpeckCursor = appDB.getCursorWithAllFavoriteQuoteID();
        DataStoreg.favoriteQuoteIDSpeckCursor.moveToFirst();

        getCurentFavoriteQuote();

        previousFavoriteQuoteImageButton = (ImageButton)findViewById(R.id.go_to_previous_favorite_quote_image_button);
        nextFavoriteQuoteImageButton = (ImageButton)findViewById(R.id.go_to_next_favorite_quote_image_button);
        themeFavoriteNameTextView = (TextView)findViewById(R.id.theme_favorite_quote_name_text_view);

        previousFavoriteQuoteImageButton.setOnClickListener(new PreviousFavoriteQuoteOnClickListener());
        nextFavoriteQuoteImageButton.setOnClickListener(new NextFavoriteQuoteOnClickListener());

        checkAvailableNavigationButton();
        tryCommitFavoriteQuoteFragmentFirstTime();

        LinearLayout adLinerLayout = (LinearLayout)findViewById(R.id.ads_layout_on_favorite_quote);
        allMobAds = new AllMobAds(this, adLinerLayout);
    }


    @Override
    protected void onResume() {
        super.onResume();
        allMobAds.createAdView();
    }
    @Override
    protected void onDestroy() {
        super.onPause();
        allMobAds.destroiAdView();
    }

    private void checkAvailableNavigationButton(){
        if(DataStoreg.favoriteQuoteIDSpeckCursor.getCount() == 0){

            previousFavoriteQuoteImageButton.setVisibility(View.INVISIBLE);
            nextFavoriteQuoteImageButton.setVisibility(View.INVISIBLE);

        }else{
            if(DataStoreg.favoriteQuoteIDSpeckCursor.isFirst()){
                previousFavoriteQuoteImageButton.setVisibility(View.INVISIBLE);
            }else{
                previousFavoriteQuoteImageButton.setVisibility(View.VISIBLE);
            }
            if (DataStoreg.favoriteQuoteIDSpeckCursor.isLast()){
                nextFavoriteQuoteImageButton.setVisibility(View.INVISIBLE);
            }else{
                nextFavoriteQuoteImageButton.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent getDefaultIntent() {

        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String textToShare = "";
        if (DataStoreg.cursorWithOneFavoriteQuote != null){
            textToShare =  DataStoreg.cursorWithOneFavoriteQuote.getString(1)+" "+ Html.fromHtml("&#169;") + DataStoreg.cursorWithOneFavoriteQuote.getString(2);
        }

        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);

        // When you want to share set the share intent.
        mShareActionProvider.setShareIntent(shareIntent);

        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_detail_quote_menu, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem shareItem = menu.findItem(R.id.action_share);



        mShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);

        mShareActionProvider.setShareIntent(getDefaultIntent());

        return super.onCreateOptionsMenu(menu);
    }

    private void updateShareIntent(){

        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String textToShare = DataStoreg.cursorWithOneFavoriteQuote.getString(1)+" "+ Html.fromHtml("&#169;") + DataStoreg.cursorWithOneFavoriteQuote.getString(2);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);

        mShareActionProvider.setShareIntent(shareIntent);
    }
    public void tryCommitFavoriteQuoteFragmentFirstTime(){
        checkAvailableNavigationButton();

        if(DataStoreg.favoriteQuoteIDSpeckCursor.getCount() == 0){
            fragment = new NowFavoriteQuoteFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.favorite_quote_frame_layout, fragment);
            ft.show(fragment);
            ft.commitAllowingStateLoss();
        }else{


            fragment = new FavoriteQuoteFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.favorite_quote_frame_layout, fragment);
            ft.show(fragment);
            ft.commitAllowingStateLoss();

            setThemeName();
        }
    }


    public void commitFavoriteQuoteFragment(){
        getCurentFavoriteQuote();

        checkAvailableNavigationButton();




        updateShareIntent();
        fragment = new FavoriteQuoteFragment();
        ft.replace(R.id.favorite_quote_frame_layout, fragment );
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
        setThemeName();


    }
    class NextFavoriteQuoteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            DataStoreg.favoriteQuoteIDSpeckCursor.moveToNext();

            getCurentFavoriteQuote();


            ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.setCustomAnimations(R.anim.show_fragment_from_right_animation, R.anim.hide_fragment_to_left_animation);

            commitFavoriteQuoteFragment();

        }
    }

    class PreviousFavoriteQuoteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            DataStoreg.favoriteQuoteIDSpeckCursor.moveToPrevious();

            getCurentFavoriteQuote();

            ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.setCustomAnimations(R.anim.show_fragment_from_left_animation, R.anim.hide_fragment_to_right_animation);


            commitFavoriteQuoteFragment();
        }
    }

    private void getCurentFavoriteQuote(){
        if(DataStoreg.favoriteQuoteIDSpeckCursor.getCount() != 0){
            int quoteID = DataStoreg.favoriteQuoteIDSpeckCursor.getInt(0);
            DataStoreg.cursorWithOneFavoriteQuote = appDB.getCursorWithQuoteByID(quoteID);
            DataStoreg.cursorWithOneFavoriteQuote.moveToFirst();

        }

    }
    private  void setThemeName(){
        int themeD = DataStoreg.cursorWithOneFavoriteQuote.getInt(3);
        String themeName = appDB.getThemeNameByThemeID(themeD);
        themeFavoriteNameTextView.setText(themeName);
    }

    private class FragmentSwipeDetector extends OnSwipeTouchListener {
        @Override
        public void onSwipeLeft() {

            if (!DataStoreg.favoriteQuoteIDSpeckCursor.isLast()){

                DataStoreg.favoriteQuoteIDSpeckCursor.moveToNext();
                ft = getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.setCustomAnimations(R.anim.show_fragment_from_right_animation, R.anim.hide_fragment_to_left_animation);

                commitFavoriteQuoteFragment();
            }
            super.onSwipeLeft();
        }

        @Override
        public void onSwipeRight() {

            if(!DataStoreg.favoriteQuoteIDSpeckCursor.isFirst()){

                DataStoreg.favoriteQuoteIDSpeckCursor.moveToPrevious();

                ft = getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.setCustomAnimations(R.anim.show_fragment_from_left_animation, R.anim.hide_fragment_to_right_animation);


                commitFavoriteQuoteFragment();
            }
            super.onSwipeRight();
        }
    }
}
