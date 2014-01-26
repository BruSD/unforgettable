package net.brusd.unforgettable.FragmentPackeg;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.brusd.unforgettable.AdsAndAnalytics.Constant;
import net.brusd.unforgettable.AdsAndAnalytics.GAClass;
import net.brusd.unforgettable.R;

/**
 * Created by BruSD on 11.01.14.
 */
public class NowFavoriteQuoteFragment extends Fragment {

    private Activity parentActivity = null;
    private View v;

    private GAClass gaClass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        parentActivity = getActivity();
        v = LayoutInflater.from(parentActivity).inflate(R.layout.fragment_now_favorite_quote, null);

        gaClass = new GAClass(parentActivity);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        gaClass.sendScreenView(Constant.SCREEN_NOW_FAVORITE_QUOTE);
    }
}
