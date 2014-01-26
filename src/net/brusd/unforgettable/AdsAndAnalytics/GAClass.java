package net.brusd.unforgettable.AdsAndAnalytics;

import android.app.Activity;

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
    private Activity activity;

    public GAClass(Activity _activity){
        this.activity = _activity;
        tracker = GoogleAnalytics.getInstance(activity).getTracker(Constant.GA_ACCOUNT_ID);



    }

    public void sendScreenView(String screenName){
        if(SharedPreferencesSticker.getAnalyticsStatus(activity)){
            tracker.send(MapBuilder
                    .createAppView()
                    .set(Fields.SCREEN_NAME, screenName)
                    .build() );
        }
    }


    public void sendEvent(String eventName){
        if(SharedPreferencesSticker.getAnalyticsStatus(activity)){
            tracker.send(MapBuilder
                    .createEvent("UX", "touch", "menuButton", null)
                    .build()
            );
        }
    }

}
