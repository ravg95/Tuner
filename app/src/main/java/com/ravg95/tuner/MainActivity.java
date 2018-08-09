package com.ravg95.tuner;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ravg95.tuner.fragment.MainFragment;
import com.ravg95.tuner.presenter.MainFragmentPresenter;

public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainFragment mainFragment = new MainFragment();
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,mainFragment,"MAIN_FRAGMENT")
                .commit();
    }


}

