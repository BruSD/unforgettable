package net.brusd.unforgettable.AppDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.brusd.unforgettable.GlobalPackeg.Quote;
import net.brusd.unforgettable.R;

import java.util.Random;

/**
 * Created by BruSD on 04.01.14.
 */
public class AppDB {
    private Context context;

    private SQLiteDatabase appDB;
    private AppOpenHelper appOpenHelper;

    private static AppDB instance = null;

    public static AppDB getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDB.class) {
                if (instance == null) {
                    instance = new AppDB(context);
                }
            }
        }
        return instance;
    }
    private AppDB(Context context) {
        this.context = context;
        this.appOpenHelper = new AppOpenHelper(context);
        appDB = appOpenHelper.getWritableDatabase();
    }

    //region Theme

    public int addThemeGetThemeId(String themeName, String themeDescription) {

        if (themeName == null )
            return -1;
        if (themeName.isEmpty())
            return -1;

        if (!appDB.isOpen())
            open();

        themeName = themeName.replace("'","''");
        themeDescription = themeDescription.replace("'","''");

        Cursor check = appDB.query(AppOpenHelper.TABLE_THEME,
                new String[]{AppOpenHelper.TABLE_THEME_COLUMN_Theme_Name},
                AppOpenHelper.TABLE_THEME_COLUMN_Theme_Name + " = '" + themeName + "'",
                null,
                null,
                null,
                null);

        int count = check.getCount();
        check.close();

        Cursor cursorForLastID = appDB.query(AppOpenHelper.TABLE_THEME,
                new String[]{AppOpenHelper.TABLE_THEME_COLUMN_ID_Theme},
                null,
                null,
                null,
                null,
                null);

        int lostThemeId = -1;
        int newThemeIndex ;

        if (cursorForLastID.getCount() != 0){
            cursorForLastID.moveToLast();
            newThemeIndex = cursorForLastID.getInt(0) + 1 ;
        }else {
            newThemeIndex =  lostThemeId + 1;

//            ContentValues values = new ContentValues();
//            values.put(AppOpenHelper.TABLE_THEME_COLUMN_ID_Theme, newThemeIndex);
//            values.put(AppOpenHelper.TABLE_THEME_COLUMN_Theme_Name, context.getString(R.string.user_theme_name_string));
//            values.put(AppOpenHelper.TABLE_THEME_COLUMN_Theme_Description, context.getString(R.string.user_theme_description_string));
//            appDB.insert(AppOpenHelper.TABLE_THEME, null, values);
//            newThemeIndex =  newThemeIndex + 1;
        }



        if (count == 0) {

            ContentValues values = new ContentValues();
            values.put(AppOpenHelper.TABLE_THEME_COLUMN_ID_Theme, newThemeIndex);
            values.put(AppOpenHelper.TABLE_THEME_COLUMN_Theme_Name, themeName);
            values.put(AppOpenHelper.TABLE_THEME_COLUMN_Theme_Description, themeDescription);
            appDB.insert(AppOpenHelper.TABLE_THEME, null, values);
        }

        close();

        return newThemeIndex;
    }

    public Cursor getAllThemeName(){
        if (!appDB.isOpen())
            open();

        Cursor allThemeName = appDB.query(AppOpenHelper.TABLE_THEME,
                new String[]{AppOpenHelper.TABLE_THEME_COLUMN_ID_Theme, AppOpenHelper.TABLE_THEME_COLUMN_Theme_Name},
                null,
                null,
                null,
                null,
                null);

        return allThemeName;

    }

    public int getQuoteCountByThemeID(int themeID){
        int quoteCount = 0;
        if (!appDB.isOpen())
            open();

        Cursor cursorQuoteByThemeCounter = appDB.query(AppOpenHelper.TABLE_QUOTE,
                new String[]{AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Theme_ID, },
                AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Theme_ID + " = "+themeID ,
                null,
                null,
                null,
                null);

        quoteCount = cursorQuoteByThemeCounter.getCount();

        return quoteCount;
    }

    public String getThemeDescriptionByThemeID(int themeID){
        String themeDescription = "";

        if (!appDB.isOpen())
            open();

        Cursor cursorQuoteByThemeCounter = appDB.query(AppOpenHelper.TABLE_THEME,
                new String[]{AppOpenHelper.TABLE_THEME_COLUMN_Theme_Description, },
                AppOpenHelper.TABLE_THEME_COLUMN_ID_Theme + " = "+themeID ,
                null,
                null,
                null,
                null);
        if(cursorQuoteByThemeCounter.moveToFirst()){
            themeDescription = cursorQuoteByThemeCounter.getString(0);
        }
        close();
        return themeDescription;
    }

    public String getThemeNameByThemeID(int themeID){
        String themeDescription = "";

        if (!appDB.isOpen())
            open();

        Cursor cursorQuoteByThemeCounter = appDB.query(AppOpenHelper.TABLE_THEME,
                new String[]{AppOpenHelper.TABLE_THEME_COLUMN_Theme_Name, },
                AppOpenHelper.TABLE_THEME_COLUMN_ID_Theme + " = "+themeID ,
                null,
                null,
                null,
                null);
        if(cursorQuoteByThemeCounter.moveToFirst()){
            themeDescription = cursorQuoteByThemeCounter.getString(0);
        }
        close();
        return themeDescription;
    }

    public int getThemeCount(){
        int themeCount = 0;
        if (!appDB.isOpen())
            open();

        Cursor cursorQuoteByThemeCounter = appDB.query(AppOpenHelper.TABLE_THEME,
                new String[]{AppOpenHelper.TABLE_THEME_COLUMN_ID_Theme, },
                null,
                null,
                null,
                null,
                null);
        if(cursorQuoteByThemeCounter.moveToFirst()){
            themeCount = cursorQuoteByThemeCounter.getCount();
        }
        close();
        return themeCount;
    }
    //endregion

    //region Quote
    public void addNewQuote(String _quote, String quoteSorce, int themeID) {

        if (_quote == null )
            return;
        if (_quote.isEmpty())
            return;
        if (!appDB.isOpen())
            open();

        _quote = _quote.replace("'","''");
        quoteSorce = quoteSorce.replace("'","''");

        Cursor check = appDB.query(AppOpenHelper.TABLE_QUOTE,
                new String[]{AppOpenHelper.TABLE_QUOTE_COLUMN_Quote},
                AppOpenHelper.TABLE_QUOTE_COLUMN_Quote + " = '" + _quote + "'",
                null,
                null,
                null,
                null);

        int count = check.getCount();
        check.close();

        if (count == 0) {

            ContentValues values = new ContentValues();
            values.put(AppOpenHelper.TABLE_QUOTE_COLUMN_Quote, _quote);
            values.put(AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Sorce, quoteSorce);
            values.put(AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Theme_ID, themeID);
            appDB.insert(AppOpenHelper.TABLE_QUOTE, null, values);
        }

    }

    public Cursor getAllQuoteByThemeID(int themeID){
        Cursor cursorAllQuote = null;
        if (!appDB.isOpen())
            open();

        cursorAllQuote = appDB.query(AppOpenHelper.TABLE_QUOTE,
                new String[]{AppOpenHelper.TABLE_QUOTE_COLUMN_ID_Quote, AppOpenHelper.TABLE_QUOTE_COLUMN_Quote,AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Sorce, },
                AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Theme_ID + " = "+themeID ,
                null,
                null,
                null,
                null);

        return cursorAllQuote;
    }

    public Cursor getCursorWithAllQuoteID(){
        if (!appDB.isOpen())
            open();
        Cursor cursorAllQuote = null;

        cursorAllQuote = appDB.query(AppOpenHelper.TABLE_QUOTE,
                new String[]{AppOpenHelper.TABLE_QUOTE_COLUMN_ID_Quote, },
                null,
                null,
                null,
                null,
                null);

        return cursorAllQuote;

    }

    public Cursor getCursorWithQuoteByID(int quoteID){
        if (!appDB.isOpen())
            open();
        Cursor cursorWithQuote = null;

        cursorWithQuote = appDB.query(AppOpenHelper.TABLE_QUOTE,
                new String[]{AppOpenHelper.TABLE_QUOTE_COLUMN_ID_Quote,
                        AppOpenHelper.TABLE_QUOTE_COLUMN_Quote,
                        AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Sorce,
                        AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Theme_ID, },
                AppOpenHelper.TABLE_QUOTE_COLUMN_ID_Quote + " = "+quoteID ,
                null,
                null,
                null,
                null);

        return cursorWithQuote;

    }
    public Quote getQuoteByID(int quoteID){
        Quote quote = null;
        if (!appDB.isOpen())
            open();
        Cursor cursorWithQuote = null;

        cursorWithQuote = appDB.query(AppOpenHelper.TABLE_QUOTE,
                new String[]{AppOpenHelper.TABLE_QUOTE_COLUMN_ID_Quote,
                        AppOpenHelper.TABLE_QUOTE_COLUMN_Quote,
                        AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Sorce,
                        AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Theme_ID, },
                AppOpenHelper.TABLE_QUOTE_COLUMN_ID_Quote + " = "+quoteID ,
                null,
                null,
                null,
                null);

        if (cursorWithQuote.moveToFirst()){
            quote = new Quote(cursorWithQuote.getInt(0),
                    cursorWithQuote.getString(1),
                    cursorWithQuote.getString(2),
                    cursorWithQuote.getInt(3));
        }
        close();
        return quote;

    }
    public int getQuoteCount(){
        int quoteCount = 0;
        if (!appDB.isOpen())
            open();

        Cursor cursorQuoteByThemeCounter = appDB.query(AppOpenHelper.TABLE_QUOTE,
                new String[]{AppOpenHelper.TABLE_QUOTE_COLUMN_ID_Quote, },
                null,
                null,
                null,
                null,
                null);
        if(cursorQuoteByThemeCounter.moveToFirst()){
            quoteCount = cursorQuoteByThemeCounter.getCount();
        }
        close();
        return quoteCount;
    }

    public  Quote getRandomQuote(){
        Quote quote  =  null;
        int randomQuote = 0;
        Random r = new Random();
        if (!appDB.isOpen())
            open();

        Cursor cursorAllQuote = appDB.query(AppOpenHelper.TABLE_QUOTE,
                new String[]{AppOpenHelper.TABLE_QUOTE_COLUMN_ID_Quote, },
                null,
                null,
                null,
                null,
                null);

        if(cursorAllQuote.moveToFirst()){
            int maxRandom = cursorAllQuote.getCount() - 1;
            randomQuote = r.nextInt(maxRandom);
        }

        Cursor cursorWithQuote = appDB.query(AppOpenHelper.TABLE_QUOTE,
                new String[]{AppOpenHelper.TABLE_QUOTE_COLUMN_ID_Quote,
                        AppOpenHelper.TABLE_QUOTE_COLUMN_Quote,
                        AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Sorce,
                        AppOpenHelper.TABLE_QUOTE_COLUMN_Quote_Theme_ID, },
                AppOpenHelper.TABLE_QUOTE_COLUMN_ID_Quote + " = "+ randomQuote ,
                null,
                null,
                null,
                null);

        if (cursorWithQuote.moveToFirst()){
            quote = new Quote(cursorWithQuote.getInt(0),
                    cursorWithQuote.getString(1),
                    cursorWithQuote.getString(2),
                    cursorWithQuote.getInt(3));
        }
        close();

        return quote;
    }
    //endregion


    //region Favorite Quote
    public void addQuoteToFavoriteById(int quoteID){
        if (quoteID == -1 )
            return;

        if (!appDB.isOpen())
            open();

        Cursor cursorQuoteByID = appDB.query(AppOpenHelper.TABLE_FAVORITE_QUOTE,
                new String[]{AppOpenHelper.TABLE_FAVORITE_QUOTE_COLUMN_ID_Quote, },
                AppOpenHelper.TABLE_FAVORITE_QUOTE_COLUMN_ID_Quote + " = "+quoteID ,
                null,
                null,
                null,
                null);

         if(cursorQuoteByID.getCount() == 0){

                ContentValues values = new ContentValues();
                values.put(AppOpenHelper.TABLE_FAVORITE_QUOTE_COLUMN_ID_Quote, quoteID);
                appDB.insert(AppOpenHelper.TABLE_FAVORITE_QUOTE, null, values);
        }
    }

    public void removeQuoteFromFavorite(int quoteID) {
        if (quoteID == -1)
            return;

        if (!appDB.isOpen())
            open();



        appDB.delete(AppOpenHelper.TABLE_FAVORITE_QUOTE, AppOpenHelper.TABLE_FAVORITE_QUOTE_COLUMN_ID_Quote +" = " + quoteID, null);

        close();
    }

    public boolean isQuoteFavorite(int quoteID){
        boolean isFavorite = false;
        if (quoteID == -1 )
            return isFavorite;

        if (!appDB.isOpen())
            open();

        Cursor cursorQuoteByID = appDB.query(AppOpenHelper.TABLE_FAVORITE_QUOTE,
                new String[]{AppOpenHelper.TABLE_FAVORITE_QUOTE_COLUMN_ID_Quote, },
                AppOpenHelper.TABLE_FAVORITE_QUOTE_COLUMN_ID_Quote + " = "+quoteID ,
                null,
                null,
                null,
                null);

        if(cursorQuoteByID.getCount() > 0){
            isFavorite = true;

        }
        close();
        return  isFavorite;
    }

    public Cursor getCursorWithAllFavoriteQuoteID(){
        if (!appDB.isOpen())
            open();
        Cursor cursorWithFavoriteQuoteID = null;

        cursorWithFavoriteQuoteID = appDB.query(AppOpenHelper.TABLE_FAVORITE_QUOTE,
                new String[]{AppOpenHelper.TABLE_FAVORITE_QUOTE_COLUMN_ID_Quote, },
                null,
                null,
                null,
                null,
                null);

        return cursorWithFavoriteQuoteID;

    }

    public int getFavoriteQuoteCount(){
        int favoriteQuoteCount = 0;
        if (!appDB.isOpen())
            open();

        Cursor cursorQuoteByThemeCounter = appDB.query(AppOpenHelper.TABLE_FAVORITE_QUOTE,
                new String[]{AppOpenHelper.TABLE_FAVORITE_QUOTE_COLUMN_ID_Quote, },
                null,
                null,
                null,
                null,
                null);
        if(cursorQuoteByThemeCounter.moveToFirst()){
            favoriteQuoteCount = cursorQuoteByThemeCounter.getCount();
        }
        close();
        return favoriteQuoteCount;
    }


    //endregion

    public void open(){
        appDB = appOpenHelper.getWritableDatabase();
    }

    public void close(){
        appDB.close();
    }
}
