package net.brusd.unforgettable.AdsAndAnalytics;

import android.app.Activity;
import android.content.Context;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import net.brusd.unforgettable.GlobalPackeg.SharedPreferencesSticker;

/**
 * Created by BruSD on 13.01.14.
 */
public class GAClass  {
    private Tracker tracker;
    private Context context;

    public GAClass(Context _context){
        this.context = _context;
        tracker = GoogleAnalytics.getInstance(context).getTracker(Constant.GA_ACCOUNT_ID);



    }

    public void sendScreenView(String screenName){
        if(SharedPreferencesSticker.getAnalyticsStatus(context)){
            tracker.send(MapBuilder
                    .createAppView()
                    .set(Fields.SCREEN_NAME, screenName)
                    .build() );
        }
    }


    public void sendEvent(String categoryName, String actionType ){
        if(SharedPreferencesSticker.getAnalyticsStatus(context)){
            tracker.send(MapBuilder
                    .createEvent(categoryName, actionType, categoryName, null)
                    .build()
            );
        }
    }

}
