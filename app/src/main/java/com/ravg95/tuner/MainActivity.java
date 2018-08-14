package com.ravg95.tuner;


import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ravg95.tuner.fragment.MainFragment;
import com.ravg95.tuner.presenter.MainFragmentPresenter;

public class MainActivity extends AppCompatActivity{

    MainFragment mainFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainFragment = new MainFragment();
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,mainFragment,"MAIN_FRAGMENT")
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MainFragmentPresenter.PERMISSION_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mainFragment.onResume();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}

