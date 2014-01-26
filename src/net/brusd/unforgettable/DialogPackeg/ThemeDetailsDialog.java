package net.brusd.unforgettable.DialogPackeg;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import net.brusd.unforgettable.AdsAndAnalytics.Constant;
import net.brusd.unforgettable.AdsAndAnalytics.GAClass;
import net.brusd.unforgettable.AppDatabase.AppDB;
import net.brusd.unforgettable.AppDatabase.AppOpenHelper;
import net.brusd.unforgettable.R;

import java.util.HashMap;

/**
 * Created by BruSD on 05.01.14.
 */
public class ThemeDetailsDialog extends Dialog {

    private Context context;
    private TextView themeName, themeDescription;
    private static AppDB appDB = null;

    private GAClass gaClass;

    public ThemeDetailsDialog(Activity _activity, HashMap<String, ?>  curentTheme) {
        super(_activity, R.style.DialogWithoutTitle);
        setContentView(R.layout.dialog_details_theme_layout);
        this.context = _activity;
        themeName = (TextView)findViewById(R.id.theme_name_dialog_text_view);
        themeDescription = (TextView)findViewById(R.id.theme_description_dialog_text_view);

        themeName.setText(curentTheme.get(AppOpenHelper.TABLE_THEME_COLUMN_Theme_Name).toString());

        appDB = AppDB.getInstance(context);


        String themeDesc = appDB.getThemeDescriptionByThemeID(Integer.parseInt(curentTheme.get(AppOpenHelper.TABLE_THEME_COLUMN_ID_Theme).toString()));

        themeDescription.setText(themeDesc);

        gaClass = new GAClass(_activity);
        gaClass.sendScreenView(Constant.SCREEN_THEME_DESCRIPTION);
    }
}
