package net.brusd.unforgettable.AppDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by BruSD on 04.01.14.
 */
public class AppOpenHelper extends SQLiteOpenHelper {
    // DB
    public static final String DATABASE_NAME  = "QuoteData.sqlite3";
    public static final int DATABASE_VERSION = 1;

    //Theme Table
    public static final String TABLE_THEME = "theme_table";
    public static final String TABLE_THEME_COLUMN_ID_Theme = "id_theme";
    public static final String TABLE_THEME_COLUMN_Theme_Name = "theme_name";
    public static final String TABLE_THEME_COLUMN_Theme_Description = "theme_description";

    private static final String TABLE_THEME_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_THEME
            + " ("
            + TABLE_THEME_COLUMN_ID_Theme + " INTEGER PRIMARY KEY, "
            + TABLE_THEME_COLUMN_Theme_Name + " TEXT NOT NULL, "
            + TABLE_THEME_COLUMN_Theme_Description + " TEXT NOT NULL"
            + ");";

    private static final String TABLE_THEME_DROP = "DROP TABLE IF EXISTS " + TABLE_THEME;


    //Quote Table
    public static final String TABLE_QUOTE = "quote_table";
    public static final String TABLE_QUOTE_COLUMN_ID_Quote = "id_quote";
    public static final String TABLE_QUOTE_COLUMN_Quote = "quote";
    public static final String TABLE_QUOTE_COLUMN_Quote_Sorce = "quote_sorce";
    public static final String TABLE_QUOTE_COLUMN_Quote_Theme_ID = "quote_theme_id";

    private static final String TABLE_QUOTE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_QUOTE
            + " ("
            + TABLE_QUOTE_COLUMN_ID_Quote + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TABLE_QUOTE_COLUMN_Quote + " TEXT NOT NULL, "
            + TABLE_QUOTE_COLUMN_Quote_Sorce + " TEXT NOT NULL, "
            + TABLE_QUOTE_COLUMN_Quote_Theme_ID + " INTEGER"
            + ");";

    private static final String TABLE_QUOTE_DROP = "DROP TABLE IF EXISTS " + TABLE_QUOTE;

    //Favorite Quote Table
    public static final String TABLE_FAVORITE_QUOTE = "favorite_quote_table";
    public static final String TABLE_FAVORITE_QUOTE_COLUMN_ID_Quote = "id_quote";

    private static final String TABLE_FAVORITE_QUOTE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_FAVORITE_QUOTE
            + " ("
            + TABLE_FAVORITE_QUOTE_COLUMN_ID_Quote + " INTEGER PRIMARY KEY"
            + ");";

    private static final String TABLE_FAVORITE_QUOTE_DROP = "DROP TABLE IF EXISTS " + TABLE_FAVORITE_QUOTE;

    public AppOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_THEME_CREATE);
        db.execSQL(TABLE_QUOTE_CREATE);
        db.execSQL(TABLE_FAVORITE_QUOTE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL(TABLE_THEME_DROP);
        db.execSQL(TABLE_QUOTE_DROP);
        db.execSQL(TABLE_FAVORITE_QUOTE_DROP);

        onCreate(db);

    }
}
