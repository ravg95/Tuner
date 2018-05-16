package com.ravg95.tuner;


import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{



    private TunerView customCanvas;
    CollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;
    private ListeningController listeningController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listeningController = new ListeningController(this);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        refreshCanvas();

        collectionPagerAdapter =
                new CollectionPagerAdapter(
                        getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(collectionPagerAdapter);
        FloatingActionButton settingsFab = (FloatingActionButton) findViewById(R.id.settings);
        settingsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setVisibility(View.INVISIBLE);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new SettingsFragment(), "SETTINGS")
                        .addToBackStack("SETTINGS")
                        .commit();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        listeningController.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        listeningController.pause();
    }


    public void refreshCanvas() {
        customCanvas = (TunerView) findViewById(R.id.my_canvas);
    }

    public void setPitchProperties(String note, String format, double value) {
        customCanvas.setPitchProperties(note, format, value);
    }

    public void invalidateCanvas() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customCanvas.invalidate();
            }
        });
    }
}

