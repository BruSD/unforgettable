package net.brusd.unforgettable.FragmentPackeg;



import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.brusd.unforgettable.ActivityPackeg.DetailsQuoteActivity;
import net.brusd.unforgettable.AdsAndAnalytics.Constant;
import net.brusd.unforgettable.AdsAndAnalytics.GAClass;
import net.brusd.unforgettable.AppDatabase.AppDB;
import net.brusd.unforgettable.GlobalPackeg.DataStoreg;
import net.brusd.unforgettable.GlobalPackeg.OnSwipeTouchListener;
import net.brusd.unforgettable.GlobalPackeg.SharedPreferencesSticker;
import net.brusd.unforgettable.ActivityPackeg.MainActivity;
import net.brusd.unforgettable.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by BruSD on 04.01.14.
 */
public class QuoteFragment extends Fragment {
    private Activity parentActivity = null;
    private View v;
    private TextView quoteText, quoteSorceText;
    private ImageButton favoriteQuoteImageButton;
    private static AppDB appDB = null;
    private boolean isQuoteFavorite = false;
    private int curentQuoteID;

    private GAClass gaClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        parentActivity = getActivity();
        v = LayoutInflater.from(parentActivity).inflate(R.layout.fragment_qoute_news_layout, null);
        LinearLayout swipeLinerLayout = (LinearLayout)v.findViewById(R.id.swipe_container_liner_layout);
        swipeLinerLayout.setOnTouchListener(new FragmentSwipeDetector());

        appDB = AppDB.getInstance(parentActivity);

        quoteText = (TextView)v.findViewById(R.id.quote_text_view);
        quoteSorceText = (TextView)v.findViewById(R.id.quote_sorce_text_view);
        favoriteQuoteImageButton = (ImageButton)v.findViewById(R.id.favorite_unfavorite_image_button);

        favoriteQuoteImageButton.setOnClickListener(new FavoriteUnFavoriteOnClickListener());

        gaClass = new GAClass(parentActivity);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(parentActivity != null && parentActivity instanceof DetailsQuoteActivity){
            fillQuoteViewFromCursor();

            gaClass.sendScreenView(Constant.SCREEN_QUOTE_DETAIL);
        }else if(parentActivity != null && parentActivity instanceof MainActivity) {
            getRandomQuoteForToday();
            gaClass.sendScreenView(Constant.SCREEN_QUOTE_OF_THE_DAY);

        }
    }

    private void fillQuoteViewFromCursor(){
        if (DataStoreg.quoteSpeckCursor.getCount()!= 0){

            String quoteNumberOfSet =parentActivity.getString(R.string.quote_of_all_selected_string) +" "+ String.valueOf(DataStoreg.quoteSpeckCursor.getPosition() + 1 ) + "/"+ String.valueOf(DataStoreg.quoteSpeckCursor.getCount());
            ((ActionBarActivity)parentActivity).getSupportActionBar().setTitle(quoteNumberOfSet);

            quoteText.setText(" -" + DataStoreg.quoteSpeckCursor.getString(1));
            quoteSorceText.setText(Html.fromHtml("&#169;") +DataStoreg.quoteSpeckCursor.getString(2));

            curentQuoteID =  DataStoreg.quoteSpeckCursor.getInt(0);
            isQuoteFavorite = appDB.isQuoteFavorite(curentQuoteID);

            if(isQuoteFavorite){
                favoriteQuoteImageButton.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.ic_status_quote_favorite));
            }else {
                favoriteQuoteImageButton.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.ic_status_quote_unfavorite));
            }

        }
    }


    private void getRandomQuoteForToday(){
        if (iskLostDateUpDateToday()){
            getTodayQuoteAndSetDataToView();
        }else{
            generateNewRandomQuoteFoToday();
        }
    }

    private void getTodayQuoteAndSetDataToView(){
        Cursor cursorWithQuote = appDB.getCursorWithQuoteByID(SharedPreferencesSticker.getQuoteOfTheDayID(parentActivity));

        cursorWithQuote.moveToFirst();

        quoteText.setText(" - " + cursorWithQuote.getString(1));
        quoteSorceText.setText(Html.fromHtml("&#169;") +cursorWithQuote.getString(2));
        curentQuoteID = cursorWithQuote.getInt(0);

        isQuoteFavorite = appDB.isQuoteFavorite(curentQuoteID);

        if(isQuoteFavorite){
            favoriteQuoteImageButton.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.ic_status_quote_favorite));
        }else {
            favoriteQuoteImageButton.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.ic_status_quote_unfavorite));
        }

    }

    class FavoriteUnFavoriteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if (isQuoteFavorite){
                //TODO: remow from favorite
                isQuoteFavorite = !isQuoteFavorite;

                appDB.removeQuoteFromFavorite(curentQuoteID);
                favoriteQuoteImageButton.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.ic_status_quote_unfavorite));

                Toast.makeText(parentActivity, R.string.quote_remove_from_favorite_toast_string, Toast.LENGTH_LONG).show();
            }else {
                appDB.addQuoteToFavoriteById(curentQuoteID);
                isQuoteFavorite = !isQuoteFavorite;
                favoriteQuoteImageButton.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.ic_status_quote_favorite));

                Toast.makeText(parentActivity, R.string.quote_add_to_favorite_toast_string, Toast.LENGTH_LONG).show();


            }
        }
    }

    private void generateNewRandomQuoteFoToday(){
        Random r = new Random();

        Cursor cursorWithAllQuoteId = appDB.getCursorWithAllQuoteID();
        int maxRandom = cursorWithAllQuoteId.getCount() - 1;
        int randomQuote = r.nextInt(maxRandom);

        cursorWithAllQuoteId.move(randomQuote);

        SharedPreferencesSticker.setQuoteOfTheDayID(parentActivity, cursorWithAllQuoteId.getInt(0));
        Calendar calendar = Calendar.getInstance();


        long theDate = calendar.getTimeInMillis();
        SharedPreferencesSticker.setLostDateUpdate(parentActivity, theDate);
        getTodayQuoteAndSetDataToView();
    }

    private boolean iskLostDateUpDateToday(){
        boolean isActualQuoteId = false;



        Calendar curentDate = Calendar.getInstance();
        Calendar lostUpdate = Calendar.getInstance();
        Date lostDate =  new Date();

        if (SharedPreferencesSticker.getLostDateUpdate(parentActivity) != 0 ){

            lostDate.setTime(SharedPreferencesSticker.getLostDateUpdate(parentActivity));

            lostUpdate.setTime(lostDate);

            if (curentDate.get(Calendar.YEAR) == lostUpdate.get(Calendar.YEAR)
                    && curentDate.get(Calendar.DAY_OF_YEAR) == lostUpdate.get(Calendar.DAY_OF_YEAR)) {
                isActualQuoteId = true;
            }else {
                isActualQuoteId = false;
            }
        }else{
            isActualQuoteId = false;
        }
        return isActualQuoteId;
    }

    private class FragmentSwipeDetector extends OnSwipeTouchListener {
        @Override
        public void onSwipeLeft() {
            super.onSwipeLeft();
            ((DetailsQuoteActivity)parentActivity).onSwipeLeftDetected();
        }

        @Override
        public void onSwipeRight() {
            super.onSwipeRight();
            ((DetailsQuoteActivity)parentActivity).onSwipeRightDetected();
        }
    }
}
