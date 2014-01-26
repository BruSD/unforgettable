package net.brusd.unforgettable.GlobalPackeg;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BruSD on 07.01.14.
 */
public class SharedPreferencesSticker {
    public static final String PREFS_NAME = "StickerSettings";
    public static final String PREFS_IS_DATA_LOAD_FROM_XML = "isDataLoadFromXML";
    public static final String PREFS_LOST_DATE_UPDATE = "lostDateUpdate";
    public static final String PREFS_QUOTE_OF_THE_DAY_ID = "quoteOfTheDayID";
    public static final String PREFS_ANALYTCS = "GoogleAnalytics";
    public static final String PREFS_WIDGET_SHOWING_MODE = "WidgetShowingMode";


    private  static SharedPreferences settings;
    private static Context context = null;

    public static SharedPreferences getSharedPreferencesOfStiker(Context _context){
        context = _context;
        SharedPreferences settingsTemp = context.getSharedPreferences(PREFS_NAME, 0);


        return settingsTemp;
    }

    public static boolean isDataLoadFromXML(Activity activity){
        settings =  getSharedPreferencesOfStiker(activity);
        return  settings.getBoolean(PREFS_IS_DATA_LOAD_FROM_XML, false);
    }
    public static void setDataLoadFromXMLTrue(Activity activity1){
        settings =  getSharedPreferencesOfStiker(activity1);

        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREFS_IS_DATA_LOAD_FROM_XML, true);

        // Commit the edits!
        editor.commit();
    }

    public static long getLostDateUpdate(Activity activity){
        settings =  getSharedPreferencesOfStiker(activity);
        return  settings.getLong(PREFS_LOST_DATE_UPDATE, 0);
    }
    public static void setLostDateUpdate(Activity activity1, long lostDateUpDate){
        settings =  getSharedPreferencesOfStiker(activity1);

        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(PREFS_LOST_DATE_UPDATE, lostDateUpDate);

        // Commit the edits!
        editor.commit();
    }

    public static int getQuoteOfTheDayID(Context _context){
        settings =  getSharedPreferencesOfStiker(_context);
        return  settings.getInt(PREFS_QUOTE_OF_THE_DAY_ID, 0);
    }
    public static void setQuoteOfTheDayID(Activity activity1, int quoteOfTheDayID){
        settings =  getSharedPreferencesOfStiker(activity1);

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(PREFS_QUOTE_OF_THE_DAY_ID, quoteOfTheDayID);

        // Commit the edits!
        editor.commit();
    }

    public static void setAnalyticsStatus(Activity activity, boolean status){
        settings =  getSharedPreferencesOfStiker(activity);

        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREFS_ANALYTCS, status);
        // Commit the edits!
        editor.commit();
    }
    public static boolean getAnalyticsStatus(Activity activity){
        settings =  getSharedPreferencesOfStiker(activity);
        return  settings.getBoolean(PREFS_ANALYTCS, true);
    }

    public static int getWidgetShowingMode(Context context){
        settings =  getSharedPreferencesOfStiker(context);
        return  settings.getInt(PREFS_WIDGET_SHOWING_MODE, 1);
    }

    public static void setWidgetShowingMode(Context context, int showingMode){
        settings =  getSharedPreferencesOfStiker(context);

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(PREFS_WIDGET_SHOWING_MODE, showingMode);
        // Commit the edits!
        editor.commit();
    }
}
