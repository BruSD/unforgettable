package net.brusd.unforgettable.AdaptorsPackeg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.brusd.unforgettable.ActivityPackeg.DetailsQuoteActivity;
import net.brusd.unforgettable.AppDatabase.AppOpenHelper;
import net.brusd.unforgettable.GlobalPackeg.DataStoreg;
import net.brusd.unforgettable.DialogPackeg.ThemeDetailsDialog;
import net.brusd.unforgettable.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by BruSD on 05.01.14.
 */
public class ThemeSimpleAdapter extends SimpleAdapter {
    Activity activity = null;
    List<? extends HashMap<String, ?>> data = null;

    public ThemeSimpleAdapter(Activity _activity, List<? extends HashMap<String, ?>> _data, int resource, String[] from, int[] to) {
        super(_activity, _data, resource, from, to);
        this.activity =_activity;
        this.data = _data;
    }
    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        TextView themeName;
        TextView themeQuoteCountInTheme;
        ImageButton openThemeDetail;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.item_theme_name_count_layout, parent, false);
        final HashMap<String, ?> curentTheme = data.get(position);
        holder = new ViewHolder();
        holder.themeName = (TextView)convertView.findViewById(R.id.theme_name_text_view);
        holder.themeQuoteCountInTheme = (TextView)convertView.findViewById(R.id.quote_count_in_theme_text_view);
        holder.openThemeDetail = (ImageButton)convertView.findViewById(R.id.show_details_by_theme_image_button);

        holder.themeName.setText(curentTheme.get(AppOpenHelper.TABLE_THEME_COLUMN_Theme_Name).toString());
        holder.themeQuoteCountInTheme.setText(curentTheme.get("quote_count").toString());

        holder.openThemeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeDetailsDialog themeDetailsDialog = new ThemeDetailsDialog(activity, curentTheme);
                themeDetailsDialog.show();

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int themeID = Integer.parseInt(curentTheme.get(AppOpenHelper.TABLE_THEME_COLUMN_ID_Theme).toString());
                DataStoreg.setSelectedThemeID(themeID);
                Intent intent = new Intent(activity, DetailsQuoteActivity.class);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

}
