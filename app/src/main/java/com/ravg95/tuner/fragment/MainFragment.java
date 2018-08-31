package com.ravg95.tuner.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ravg95.tuner.presenter.MainFragmentPresenter;
import com.ravg95.tuner.R;
import com.ravg95.tuner.view.TunerView;
import com.ravg95.tuner.util.CollectionPagerAdapter;

import java.util.Optional;

/**
 * Created by rafal on 16/05/2018.
 */

public class MainFragment extends Fragment{

    CollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;
    private MainFragmentPresenter mainFragmentPresenter = new MainFragmentPresenter(this);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.main_fragment, container, false);

        collectionPagerAdapter =
                new CollectionPagerAdapter(
                        getChildFragmentManager());
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.setAdapter(collectionPagerAdapter);


        FloatingActionButton settingsFab = (FloatingActionButton) rootView.findViewById(R.id.settings);
        settingsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mainFragmentPresenter.settingsButton();
            }
        });


        return rootView;
    }

    public TunerView getTunerView() {
        try {
            return (TunerView) viewPager.getChildAt(viewPager.getCurrentItem()).findViewById(R.id.my_canvas);

        } catch (NullPointerException e){
            return null;
        }
    }
    public void invalidateCanvas() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TunerView tunerView = getTunerView();
                if(tunerView != null) {
                    tunerView.invalidate();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mainFragmentPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mainFragmentPresenter.pause();
    }

    public void openSettings() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new SettingsFragment(), "SETTINGS")
                .addToBackStack("SETTINGS")
                .commit();
    }
}
