package net.brusd.unforgettable.AdsAndAnalytics;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import net.brusd.unforgettable.GlobalPackeg.DataStoreg;

/**
 * Created by BruSD on 13.01.14.
 */
public class AdMobAds {

    private LinearLayout adViewLayout;
    private Activity activity;
    private AdView adView;

    public AdMobAds(Activity _activity, LinearLayout _adViewLayout){
        this.activity = _activity;
        this.adViewLayout = _adViewLayout;
    }
    public void createAdView(){
        if(DataStoreg.isOnline(activity)){
            makeAdViewVisibil(adViewLayout);
            // Создание adView
            adView = new AdView(activity, AdSize.BANNER, Constant.AD_MOB_PUBLISHER_ID);

            // Добавление adView
            adViewLayout.addView(adView);

            // Инициирование общего запроса на загрузку вместе с объявлением
            adView.loadAd(new AdRequest());
        }else{
            makeAdViewGone(adViewLayout);
        }

    }
    public void destroiAdView(){
        if(adView != null){
            adView.destroy();
        }
    }

    private void makeAdViewVisibil(LinearLayout adsLayout){
        if(adsLayout.getVisibility() != View.VISIBLE){
           adsLayout.setVisibility(View.VISIBLE);
        }
    }

    private void makeAdViewGone(LinearLayout adsLayout){
        if(adsLayout.getVisibility() != View.GONE){
            adsLayout.setVisibility(View.GONE);
        }
    }

}
