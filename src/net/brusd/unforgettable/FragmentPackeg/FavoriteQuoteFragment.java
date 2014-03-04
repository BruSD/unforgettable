package net.brusd.unforgettable.FragmentPackeg;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.brusd.unforgettable.AdsAndAnalytics.Constant;
import net.brusd.unforgettable.AdsAndAnalytics.GAClass;
import net.brusd.unforgettable.AppDatabase.AppDB;
import net.brusd.unforgettable.GlobalPackeg.DataStoreg;
import net.brusd.unforgettable.R;

/**
 * Created by BruSD on 04.01.14.
 */
public class FavoriteQuoteFragment extends Fragment {

    private Activity parentActivity = null;
    private View v;
    private TextView favoriteQuoteText, favoriteQuoteSorceText;
    private ImageButton favoriteQuoteImageButtonOnFavoriteScreen;
    private static AppDB appDB = null;

    private boolean isQuoteFavorite = false;
    private int favoriteQuoteID;

    private GAClass gaClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        parentActivity = getActivity();
        v = LayoutInflater.from(parentActivity).inflate(R.layout.fragment_favorite_quote_layout, null);

        appDB = AppDB.getInstance(parentActivity);

        favoriteQuoteText = (TextView)v.findViewById(R.id.favorite_quote_text_view);
        favoriteQuoteSorceText = (TextView)v.findViewById(R.id.favorite_quote_sorce_text_view);
        favoriteQuoteImageButtonOnFavoriteScreen = (ImageButton)v.findViewById(R.id.favorite_unfavorite_image_button_on_favorite_screen);

        favoriteQuoteImageButtonOnFavoriteScreen.setOnClickListener(new FavoriteUnFavoriteOnFavoriteFragmentOnClickListener());

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
            fillQuoteViewFromCursor();
        gaClass.sendScreenView(Constant.SCREEN_FAVORITE_QUOTE);
    }

    private void fillQuoteViewFromCursor(){
        if (DataStoreg.favoriteQuoteIDSpeckCursor.getCount()!= 0){

            String quoteNumberOfSet =parentActivity.getString(R.string.quote_of_all_selected_string) +" "+ String.valueOf(DataStoreg.favoriteQuoteIDSpeckCursor.getPosition() + 1 ) + "/"+ String.valueOf(DataStoreg.favoriteQuoteIDSpeckCursor.getCount());
            ((ActionBarActivity)parentActivity).getSupportActionBar().setTitle(quoteNumberOfSet);

            favoriteQuoteID =  DataStoreg.favoriteQuoteIDSpeckCursor.getInt(0);
            isQuoteFavorite = appDB.isQuoteFavorite(favoriteQuoteID);



            favoriteQuoteText.setText(" - " + DataStoreg.cursorWithOneFavoriteQuote.getString(1));
            favoriteQuoteSorceText.setText(Html.fromHtml("&#169;") + DataStoreg.cursorWithOneFavoriteQuote.getString(2));



            if(isQuoteFavorite){
                favoriteQuoteImageButtonOnFavoriteScreen.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.ic_status_quote_favorite));
            }else {
                favoriteQuoteImageButtonOnFavoriteScreen.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.ic_status_quote_unfavorite));
            }

        }
    }

    class FavoriteUnFavoriteOnFavoriteFragmentOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if (isQuoteFavorite){
                //TODO: remow from favorite
                isQuoteFavorite = !isQuoteFavorite;

                appDB.removeQuoteFromFavorite(favoriteQuoteID);
                favoriteQuoteImageButtonOnFavoriteScreen.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.ic_status_quote_unfavorite));

                Toast.makeText(parentActivity, R.string.quote_remove_from_favorite_toast_string, Toast.LENGTH_LONG).show();
            }else {
                appDB.addQuoteToFavoriteById(favoriteQuoteID);
                isQuoteFavorite = !isQuoteFavorite;
                favoriteQuoteImageButtonOnFavoriteScreen.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.ic_status_quote_favorite));

                Toast.makeText(parentActivity, R.string.quote_add_to_favorite_toast_string, Toast.LENGTH_LONG).show();


            }
        }
    }
}
