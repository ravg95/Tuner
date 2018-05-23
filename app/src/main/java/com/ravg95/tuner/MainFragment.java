package com.ravg95.tuner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rafal on 16/05/2018.
 */

public class MainFragment extends Fragment implements CanvasController{
    private TunerView customCanvas;
    CollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;
    private ListeningController listeningController;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.main_fragment, container, false);
        customCanvas = (TunerView) rootView.findViewById(R.id.my_canvas);

        collectionPagerAdapter =
                new CollectionPagerAdapter(
                        getChildFragmentManager());
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.setAdapter(collectionPagerAdapter);

        FloatingActionButton settingsFab = (FloatingActionButton) rootView.findViewById(R.id.settings);
        settingsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new SettingsFragment(), "SETTINGS")
                        .addToBackStack("SETTINGS")
                        .commit();
            }
        });


        return rootView;
    }

    public void refreshCanvas() {
        customCanvas = (TunerView) getView().findViewById(R.id.my_canvas);
    }

    public void setPitchProperties(String note, String format, double value) {
        customCanvas.setPitchProperties(note, format, value);
    }

    public void invalidateCanvas() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(customCanvas == null)
                    refreshCanvas();
                customCanvas.invalidate();
            }
        });
    }

    public void setListeningController(ListeningController listeningController) {
        this.listeningController = listeningController;
    }
    @Override
    public void onResume() {
        super.onResume();
        listeningController.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        listeningController.pause();
    }
}
