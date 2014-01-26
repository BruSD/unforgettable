package net.brusd.unforgettable.ActivityPackeg;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import net.brusd.unforgettable.AdsAndAnalytics.Constant;
import net.brusd.unforgettable.AdsAndAnalytics.GAClass;
import net.brusd.unforgettable.AppDatabase.AppDB;
import net.brusd.unforgettable.R;

/**
 * Created by BruSD on 04.01.14.
 */
public class AboutAppActivity extends ActionBarActivity {

    private GAClass gaClass;
    private static AppDB appDB = null;

    private TextView themeCountTextView, quoteCountTextView, favoriteQuoteCountTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appDB = AppDB.getInstance(this);

        gaClass = new GAClass(this);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeViewAndData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gaClass.sendScreenView(Constant.SCREEN_ABOUT_APP);
    }

    private void initializeViewAndData(){
        themeCountTextView = (TextView)findViewById(R.id.count_of_theme_text_view);
        quoteCountTextView = (TextView)findViewById(R.id.count_of_quote_text_view);
        favoriteQuoteCountTextView = (TextView)findViewById(R.id.count_of_favorite_quote_text_view);

        String themeCount = String.valueOf(appDB.getThemeCount());
        themeCountTextView.setText(themeCount);

        String quoteCount = String.valueOf(appDB.getQuoteCount());
        quoteCountTextView.setText(quoteCount);

        String favoriteQuoteCount = String.valueOf(appDB.getFavoriteQuoteCount());
        favoriteQuoteCountTextView.setText(favoriteQuoteCount);


    }
}
