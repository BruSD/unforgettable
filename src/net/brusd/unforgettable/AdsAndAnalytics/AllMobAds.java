package net.brusd.unforgettable.AdsAndAnalytics;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;


import com.google.android.gms.ads.*;

import net.brusd.unforgettable.GlobalPackeg.DataStoreg;

/**
 * Created by BruSD on 13.01.14.
 */
public class AllMobAds {

    private LinearLayout adViewLayout;
    private Activity activity;
    private AdView adView;

    public AllMobAds(Activity _activity, LinearLayout _adViewLayout){
        this.activity = _activity;
        this.adViewLayout = _adViewLayout;
    }
    public void createAdView(){
        if(DataStoreg.isOnline(activity)){
            makeAdViewVisibil(adViewLayout);

            // Создание экземпляра adView.
            adView = new AdView(activity);
            adView.setAdUnitId(Constant.MY_AD_UNIT_ID);
            adView.setAdSize(AdSize.BANNER);
            adViewLayout.addView(adView);

            // Инициирование общего запроса.
            AdRequest adRequest = new AdRequest.Builder().build();

            // Загрузка adView с объявлением.
            adView.loadAd(adRequest);

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

    public void pause() {
        adView.pause();
    }
    public void resume() {
        adView.resume();
    }


}
