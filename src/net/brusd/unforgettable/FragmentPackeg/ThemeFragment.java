package net.brusd.unforgettable.FragmentPackeg;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.brusd.unforgettable.AdaptorsPackeg.ThemeSimpleAdapter;
import net.brusd.unforgettable.AdsAndAnalytics.Constant;
import net.brusd.unforgettable.AdsAndAnalytics.GAClass;
import net.brusd.unforgettable.AppDatabase.AppOpenHelper;
import net.brusd.unforgettable.LoadAndParseData.PrepareDataToView;
import net.brusd.unforgettable.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BruSD on 04.01.14.
 */
public class ThemeFragment extends Fragment {

    private Activity parentActivity = null;
    private View v;
    private ListView themeListView;
    private ArrayList<HashMap<String, String>> allThemeWithQuoteCount = new ArrayList<HashMap<String, String>> ();

    private GAClass gaClass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        parentActivity = getActivity();
        v = LayoutInflater.from(parentActivity).inflate(R.layout.fragment_theme_layout, null);

        themeListView = (ListView)v.findViewById(R.id.theme_list_view);

        gaClass = new GAClass(parentActivity);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        allThemeWithQuoteCount = PrepareDataToView.getAllThemeWithQuoteCount(parentActivity);
        ThemeSimpleAdapter adapterForThemeWithQuoteCount = new ThemeSimpleAdapter(
                parentActivity,
                allThemeWithQuoteCount,
                android.R.layout.simple_list_item_2,
                new String[]{AppOpenHelper.TABLE_THEME_COLUMN_Theme_Name, "quote_count"},
                new int[]{android.R.id.text1, android.R.id.text2}
                );

        themeListView.setAdapter(adapterForThemeWithQuoteCount);

    }

    @Override
    public void onResume() {
        super.onResume();
        gaClass.sendScreenView(Constant.SCREEN_QUOTE_THEME);
    }
}
