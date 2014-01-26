package net.brusd.unforgettable.GlobalPackeg;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by BruSD on 05.01.14.
 */
public class DataStoreg {
    private static int _selectedThemeID;

    public static Cursor quoteSpeckCursor;
    public static Cursor favoriteQuoteIDSpeckCursor;

    public static Cursor cursorWithOneFavoriteQuote;
    private static Quote quote = null;

    public static int getSelectedThemeID(){
        return _selectedThemeID;
    }

    public static void setSelectedThemeID(int selectedThemeID){
        _selectedThemeID = selectedThemeID;
    }

    public static boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected())
            return true;
        else
            return false;
    }

    public static void setWidgetQuote(Quote _quote){
        quote = _quote;
    }

    public static Quote getWidgetQuote(){
        return quote;
    }
}
