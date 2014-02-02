package net.brusd.unforgettable.ActivityPackeg;




import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import net.brusd.unforgettable.AdsAndAnalytics.AdMobAds;
import net.brusd.unforgettable.AppDatabase.AppDB;
import net.brusd.unforgettable.FragmentPackeg.FavoriteQuoteFragment;
import net.brusd.unforgettable.FragmentPackeg.QuoteFragment;
import net.brusd.unforgettable.FragmentPackeg.ThemeFragment;
import net.brusd.unforgettable.FragmentPackeg.UserThemeFragment;
import net.brusd.unforgettable.GlobalPackeg.DataStoreg;
import net.brusd.unforgettable.GlobalPackeg.SharedPreferencesSticker;
import net.brusd.unforgettable.LoadAndParseData.LoadDataFromXML;
import net.brusd.unforgettable.R;


public class MainActivity extends ActionBarActivity {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Button quoteButton, themeButton, userThemeButton, showFavoriteButton, widgetSettingButton, aboutAppButton;

    private FragmentTransaction ft;


    private static AppDB appDB = null;
    private AdMobAds adMobAds;



//region Activities methods
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);




        mDrawerTitle = getString(R.string.drawer_open);
        mTitle = getString(R.string.quote_button_text);

        if (!SharedPreferencesSticker.isDataLoadFromXML(this)){
            LoadDataFromXML.LoadDataFromXML(this);
        }else{
            commitQuoteFragment();
        }
        initialDrawerContent();
        initializeAdMob();

        LinearLayout adLinerLayout = (LinearLayout)findViewById(R.id.ads_layout);
        adMobAds = new AdMobAds(this, adLinerLayout);


    }



    @Override
    protected void onResume() {
        super.onResume();
        adMobAds.createAdView();
        getSupportActionBar().setTitle(mTitle);

    }
    @Override
    protected void onDestroy() {
        super.onPause();
        adMobAds.destroiAdView();
    }
    private void setActualActionBarTitle(String title){
        mTitle = title;
    }


//endregion

//region Initial Info for Draver
    private void initialDrawerContent(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
        ){
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                quoteButton = (Button)drawerView.findViewById(R.id.show_last_quote_news_button);
                themeButton = (Button)drawerView.findViewById(R.id.show_all_qoute_theme_buton);
                userThemeButton = (Button)drawerView.findViewById(R.id.user_themes_button);
                showFavoriteButton = (Button)drawerView.findViewById(R.id.favorite_quote_button);
                widgetSettingButton = (Button)drawerView.findViewById(R.id.widget_seting_button);
                aboutAppButton = (Button)drawerView.findViewById(R.id.about_app_button);

                quoteButton.setOnClickListener(new showQuoteOnClickListener());
                themeButton.setOnClickListener(new showThemeOnClickListener());
                userThemeButton.setOnClickListener(new showUserThemeOnClickListener());
                showFavoriteButton.setOnClickListener(new showFavoriteQuoteOnClickListener());
                widgetSettingButton.setOnClickListener(new widgetSettingOnClickListener());
                aboutAppButton.setOnClickListener(new aboutAppOnClickListener());

            }
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (mDrawerLayout.isShown()){
                    mDrawerLayout.closeDrawers();
                }else {
                    mDrawerLayout.openDrawer(mDrawerLayout);
                }
                return super.onOptionsItemSelected(item);
            }

            @Override
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }



    class showQuoteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            mDrawerLayout.closeDrawers();
            commitQuoteFragment();
        }
    }
    class showThemeOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            mDrawerLayout.closeDrawers();
            commitThemeFragment();
        }
    }
    class showUserThemeOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            mDrawerLayout.closeDrawers();
            commitUserThemeFragment();
        }
    }
    class showFavoriteQuoteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            mDrawerLayout.closeDrawers();
            Intent intent = new Intent(MainActivity.this, FavoriteQuoteActivity.class);
            startActivity(intent);

        }
    }
    class widgetSettingOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            mDrawerLayout.closeDrawers();

            Intent intent = new Intent(MainActivity.this, WidgetSettingActivity.class);
            startActivity(intent);
        }
    }
    class aboutAppOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            mDrawerLayout.closeDrawers();
            Intent intent = new Intent(MainActivity.this, AboutAppActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

//endregion

//region Fragments commit
    public void commitQuoteFragment(){


        setActualActionBarTitle(getString(R.string.quote_button_text));

        Fragment fragment = new QuoteFragment();

        ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment_content_frame_layout, fragment);
        ft.show(fragment);
        ft.commitAllowingStateLoss();

    }

    public void commitThemeFragment(){


        setActualActionBarTitle(getString(R.string.theme_button_text));

        Fragment fragment = new ThemeFragment();

        ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.fragment_content_frame_layout, fragment);
        ft.show(fragment);
        ft.commitAllowingStateLoss();

    }

    public void commitUserThemeFragment(){


        setActualActionBarTitle(getString(R.string.user_themes_button_text));

        Fragment fragment = new UserThemeFragment();

        ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment_content_frame_layout, fragment);
        ft.show(fragment);
        ft.commitAllowingStateLoss();

    }

    public void commitFavoriteQuoteFragment(){
        DataStoreg.favoriteQuoteIDSpeckCursor =  appDB.getCursorWithAllFavoriteQuoteID();



        setActualActionBarTitle(getString(R.string.favorite_quote_button_text));

        Fragment fragment = new FavoriteQuoteFragment();

        ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment_content_frame_layout, fragment);
        ft.show(fragment);
        ft.commitAllowingStateLoss();

    }

    private void initializeAdMob(){

    }






//endregion
}
