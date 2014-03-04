package net.brusd.unforgettable.ActivityPackeg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import net.brusd.unforgettable.AdsAndAnalytics.Constant;
import net.brusd.unforgettable.AppDatabase.AppDB;
import net.brusd.unforgettable.GlobalPackeg.DataStoreg;
import net.brusd.unforgettable.FragmentPackeg.QuoteFragment;
import net.brusd.unforgettable.GlobalPackeg.OnSwipeTouchListener;
import net.brusd.unforgettable.R;

/**
 * Created by BruSD on 05.01.14.
 */
public class DetailsQuoteActivity extends ActionBarActivity {

    private ShareActionProvider mShareActionProvider;
    private FragmentTransaction ft;
    private static AppDB appDB = null;
    private ImageButton nextQuoteImageButton, previousQuoteImageButton;
    private TextView themeNameTextView;
    private  Intent shareIntent;
    private RelativeLayout swipeContainer;
    private FrameLayout frameLayout;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qoute_details_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appDB = AppDB.getInstance(this);
        //swipeContainer = (RelativeLayout)findViewById(R.id.swipe_container_relative_layout);
        //swipeContainer.setOnTouchListener(new FragmentSwipeDetector());
        frameLayout = (FrameLayout)findViewById(R.id.quote_frame_layout);
        //frameLayout.setOnTouchListener(new FragmentSwipeDetector());
        commitQuoteFragmentFirstTime();

        previousQuoteImageButton = (ImageButton)findViewById(R.id.go_to_previous_quote_image_button);
        nextQuoteImageButton = (ImageButton)findViewById(R.id.go_to_next_quote_image_button);
        themeNameTextView = (TextView)findViewById(R.id.theme_quote_name_text_view);

        previousQuoteImageButton.setOnClickListener(new PreviousQuoteOnClickListener());
        nextQuoteImageButton.setOnClickListener(new NextQuoteOnClickListener());

        themeNameTextView.setText(appDB.getThemeNameByThemeID(DataStoreg.getSelectedThemeID()));

        checkAvailableNavigationButton();

        initializeAdLayout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null)
            adView.resume();
    }

    @Override
    protected void onPause() {
        if (adView != null)
            adView.pause();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        if (adView != null)
            adView.destroy();
        super.onPause();

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
        String textToShare = DataStoreg.quoteSpeckCursor.getString(1)+" "+ Html.fromHtml("&#169;") + DataStoreg.quoteSpeckCursor.getString(2);
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
        String textToShare = DataStoreg.quoteSpeckCursor.getString(1)+" "+ Html.fromHtml("&#169;") + DataStoreg.quoteSpeckCursor.getString(2);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);

        mShareActionProvider.setShareIntent(shareIntent);
    }

    public void commitQuoteFragmentFirstTime(){

        DataStoreg.quoteSpeckCursor = appDB.getAllQuoteByThemeID(DataStoreg.getSelectedThemeID());
        DataStoreg.quoteSpeckCursor.moveToFirst();


        ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.quote_frame_layout, new QuoteFragment());
        ft.show(new QuoteFragment());
        ft.commitAllowingStateLoss();

    }

    public void commitQuoteFragment(){
        checkAvailableNavigationButton();

        updateShareIntent();

        ft.replace(R.id.quote_frame_layout, new QuoteFragment());
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();


    }

    private void checkAvailableNavigationButton(){
        if(DataStoreg.quoteSpeckCursor.isFirst()){
            previousQuoteImageButton.setVisibility(View.INVISIBLE);
        }else{
            previousQuoteImageButton.setVisibility(View.VISIBLE);
        }
        if (DataStoreg.quoteSpeckCursor.isLast()){
            nextQuoteImageButton.setVisibility(View.INVISIBLE);
        }else{
            nextQuoteImageButton.setVisibility(View.VISIBLE);
        }
    }

    class NextQuoteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            DataStoreg.quoteSpeckCursor.moveToNext();
            ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.setCustomAnimations(R.anim.show_fragment_from_right_animation, R.anim.hide_fragment_to_left_animation);

            commitQuoteFragment();

        }
    }

    class PreviousQuoteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            DataStoreg.quoteSpeckCursor.moveToPrevious();
            ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.setCustomAnimations(R.anim.show_fragment_from_left_animation, R.anim.hide_fragment_to_right_animation);


            commitQuoteFragment();
        }
    }
    public void onSwipeLeftDetected() {
        if (!DataStoreg.quoteSpeckCursor.isLast()){
            DataStoreg.quoteSpeckCursor.moveToNext();
            ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.setCustomAnimations(R.anim.show_fragment_from_right_animation, R.anim.hide_fragment_to_left_animation);

            commitQuoteFragment();
        }
    }
    public void onSwipeRightDetected(){
        if(!DataStoreg.quoteSpeckCursor.isFirst()){

            DataStoreg.quoteSpeckCursor.moveToPrevious();
            ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.setCustomAnimations(R.anim.show_fragment_from_left_animation, R.anim.hide_fragment_to_right_animation);


            commitQuoteFragment();
        }
    }

    private class FragmentSwipeDetector extends OnSwipeTouchListener{
        @Override
        public void onSwipeLeft() {

            super.onSwipeLeft();
        }

        @Override
        public void onSwipeRight() {

            super.onSwipeRight();
        }
    }
    private void initializeAdLayout(){
        LinearLayout adLinerLayout = (LinearLayout)findViewById(R.id.ads_layout_on_detail_quote);
        if(DataStoreg.isOnline(this)){


            if (adLinerLayout.getVisibility() == View.GONE){
                adLinerLayout.setVisibility(View.VISIBLE);
            }

            // Создание экземпляра adView.
            adView = new AdView(this);
            adView.setAdUnitId(Constant.MY_AD_UNIT_ID);
            adView.setAdSize(AdSize.BANNER);

            // Добавление в разметку экземпляра adView.
            adLinerLayout.addView(adView);

            // Инициирование общего запроса.
            AdRequest adRequest = new AdRequest.Builder().build();

            // Загрузка adView с объявлением.
            adView.loadAd(adRequest);
        }else {
            if (adLinerLayout.getVisibility() == View.VISIBLE){
                adLinerLayout.setVisibility(View.GONE);
            }
        }
    }
}
