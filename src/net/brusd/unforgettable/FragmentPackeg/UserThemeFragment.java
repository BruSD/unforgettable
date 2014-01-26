package net.brusd.unforgettable.FragmentPackeg;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.brusd.unforgettable.R;

/**
 * Created by BruSD on 04.01.14.
 */
public class UserThemeFragment extends Fragment {

    private Activity parentActivity = null;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        parentActivity = getActivity();
        v = LayoutInflater.from(parentActivity).inflate(R.layout.fragment_user_theme_layout, null);

        return v;

    }
}