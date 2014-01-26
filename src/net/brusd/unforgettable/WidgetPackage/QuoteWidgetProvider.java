package net.brusd.unforgettable.WidgetPackage;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.RemoteViews;

import net.brusd.unforgettable.AppDatabase.AppDB;
import net.brusd.unforgettable.GlobalPackeg.Constants;
import net.brusd.unforgettable.GlobalPackeg.DataStoreg;
import net.brusd.unforgettable.GlobalPackeg.Quote;
import net.brusd.unforgettable.GlobalPackeg.SharedPreferencesSticker;
import net.brusd.unforgettable.R;

/**
 * Created by BruSD on 16.01.14.
 */
public class QuoteWidgetProvider extends AppWidgetProvider {


    private Quote quote;
    private Context context;
    private static AppDB appDB = null;

    private int quoteFavoritStatusImage = R.drawable.ic_status_quote_unfavorite;
    private int refreshButtonVisability = View.VISIBLE;

    public static String CLICK_ACTION_REFRESH_QUOTE= "net.brusd.unforgettable.WidgetPackage.CLICK_REFRESH_QUOTE";
    public static String CLICK_ACTION_FAVORITE_QUOTE = "net.brusd.unforgettable.WidgetPackage.CLICK_FAVORITE_QUOTE";
    @Override
    public void onReceive(Context _context, Intent intent) {
        final String action = intent.getAction();
        context = _context;

        if (action.equals(CLICK_ACTION_REFRESH_QUOTE)) {

            final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            appDB = AppDB.getInstance(context);
            setDataToView();

            final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_quote_layout);


            rv.setTextViewText(R.id.theme_name_in_widget_text_view, quote.getThemeQuoteName());

            rv.setTextViewText(R.id.quote_widget_text_view, quote.getQuote());
            rv.setTextViewText(R.id.quote_source_widget_text_view, Html.fromHtml("&#169;") + quote.getQuoteSource());


            rv.setViewVisibility(R.id.refresh_quote_image_button , refreshButtonVisability);
            rv.setImageViewResource(R.id.favorite_widget_image_button, quoteFavoritStatusImage);

            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, QuoteWidgetProvider.class);
            mgr.updateAppWidget(mgr.getAppWidgetIds(cn), rv);

        }else if (action.equals(CLICK_ACTION_FAVORITE_QUOTE)){
            final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_quote_layout);

            appDB = AppDB.getInstance(context);
            quote = DataStoreg.getWidgetQuote();

            addQuoteToFavorite();
            setQuoteFavoritStatusImage();
            rv.setImageViewResource(R.id.favorite_widget_image_button, quoteFavoritStatusImage);

            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, QuoteWidgetProvider.class);
            mgr.updateAppWidget(mgr.getAppWidgetIds(cn), rv);
        }
        super.onReceive(context, intent);
    }
    public void onUpdate(Context _context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        context = _context;



        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            appDB = AppDB.getInstance(context);
            setDataToView();

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_quote_layout);

            rv.setTextViewText(R.id.theme_name_in_widget_text_view, quote.getThemeQuoteName());

            rv.setTextViewText(R.id.quote_widget_text_view, quote.getQuote());
            rv.setTextViewText(R.id.quote_source_widget_text_view, Html.fromHtml("&#169;") + quote.getQuoteSource());


            rv.setViewVisibility(R.id.refresh_quote_image_button , refreshButtonVisability);

            rv.setImageViewResource(R.id.favorite_widget_image_button, quoteFavoritStatusImage);

            final Intent onClickLocalIntent = new Intent(context, QuoteWidgetProvider.class);
            onClickLocalIntent.setAction(QuoteWidgetProvider.CLICK_ACTION_REFRESH_QUOTE);
            final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0, onClickLocalIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.refresh_quote_image_button, onClickPendingIntent);


            final Intent onClickServerIntent = new Intent(context, QuoteWidgetProvider.class);
            onClickServerIntent.setAction(QuoteWidgetProvider.CLICK_ACTION_FAVORITE_QUOTE);
            final PendingIntent onClickServerPendingIntent = PendingIntent.getBroadcast(context, 0, onClickServerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.favorite_widget_image_button, onClickServerPendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void setDataToView(){
        int showingMode = SharedPreferencesSticker.getWidgetShowingMode(context);
        switch (showingMode)  {
            case Constants.WIDGET_PREFERENCE_SHOW_QUOTE_OF_THE_DAY:
                loadQuoteOfTheDayToWidgetView();
                break;

            case Constants.WIDGET_PREFERENCE_SHOW_RANDOM_QUOTE_OF_ALL_QUOTE:
                loadRandomQuotToWidgetView();
                break;

            case Constants.WIDGET_PREFERENCE_SHOW_RANDOM_QUOTE_OF_FAVORITE_QUOTE:

                break;

            case Constants.WIDGET_PREFERENCE_SHOW_RANDOM_QUOTE_OF_SELECTED_THEME:

                break;

        }
    }


    private void loadQuoteOfTheDayToWidgetView(){
        int quoteID = SharedPreferencesSticker.getQuoteOfTheDayID(context);

        quote = appDB.getQuoteByID(quoteID);
        DataStoreg.setWidgetQuote(quote);
        quote.setQuoteIsFavorite(appDB.isQuoteFavorite(quoteID));
        quote.setThemeQuoteName(appDB.getThemeNameByThemeID(quote.getQuoteThemeID()));
        refreshButtonVisability = View.INVISIBLE;

        setQuoteFavoritStatusImage();
    }

    private void loadRandomQuotToWidgetView(){
        quote = appDB.getRandomQuote();
        if (quote != null){
            DataStoreg.setWidgetQuote(quote);
            quote.setQuoteIsFavorite(appDB.isQuoteFavorite(quote.getQuoteID()));
            quote.setThemeQuoteName(appDB.getThemeNameByThemeID(quote.getQuoteThemeID()));
            refreshButtonVisability = View.VISIBLE;

            setQuoteFavoritStatusImage();
        }
    }

    private void setQuoteFavoritStatusImage(){
        if(quote.isFavorite()){
            quoteFavoritStatusImage = R.drawable.ic_status_quote_favorite;
        }else {
            quoteFavoritStatusImage = R.drawable.ic_status_quote_unfavorite;
        }
    }

    private void addQuoteToFavorite(){
        if(quote.isFavorite()){
            appDB.removeQuoteFromFavorite(quote.getQuoteID());
            quote.setQuoteIsFavorite(appDB.isQuoteFavorite(quote.getQuoteID()));
        }else {
            appDB.addQuoteToFavoriteById(quote.getQuoteID());
            quote.setQuoteIsFavorite(appDB.isQuoteFavorite(quote.getQuoteID()));
        }
    }
}
