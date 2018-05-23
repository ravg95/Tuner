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


    private ListeningController listeningController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainFragment mainFragment = new MainFragment();
        listeningController = new ListeningController(mainFragment);
        mainFragment.setListeningController(listeningController);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,mainFragment,"MAIN_FRAGMENT")
                .commit();
    }


}

