package net.brusd.unforgettable.LoadAndParseData;

import android.app.Activity;
import android.database.Cursor;

import net.brusd.unforgettable.AppDatabase.AppDB;
import net.brusd.unforgettable.AppDatabase.AppOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by BruSD on 05.01.14.
 */
public class PrepareDataToView {

    private static AppDB appDB = null;

    public static ArrayList<HashMap<String, String>> getAllThemeWithQuoteCount(Activity activity){
        ArrayList<HashMap<String, String>> allTheme = new ArrayList<>();
        int themeQuoteCount = 0;
        appDB = AppDB.getInstance(activity);

        Cursor cursor = appDB.getAllThemeName();

        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            themeQuoteCount = appDB.getQuoteCountByThemeID(id);

            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put(AppOpenHelper.TABLE_THEME_COLUMN_ID_Theme, String.valueOf(cursor.getInt(0)));
            temp.put(AppOpenHelper.TABLE_THEME_COLUMN_Theme_Name, cursor.getString(1).replace("''", "'"));
            temp.put("quote_count", String.valueOf(themeQuoteCount));

            allTheme.add(temp);
        }

        appDB.close();
        return allTheme;
    }



}
