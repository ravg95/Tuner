package com.ravg95.tuner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ravg95.tuner.R;

/**
 * Created by rafal on 16/05/2018.
 */


public class PagerObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        Bundle args = getArguments();
        int num = args.getInt(ARG_OBJECT);
        View rootView;
        if(num%2 == 0 ) {
           rootView = inflater.inflate(
                    R.layout.string_view_fragment, container, false);
        } else {
            rootView = inflater.inflate(
                    R.layout.gauge_view_fragment, container, false);
        }

        return rootView;
    }
}