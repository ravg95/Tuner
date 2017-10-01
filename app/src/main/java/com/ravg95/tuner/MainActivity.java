package com.ravg95.tuner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Timer t = new Timer();
        TimerTask tt = new BRh();
        t.schedule(tt,5000);

    }
    private class BRh extends TimerTask {
        @Override
        public void run(){
            FrequencyRecogniser.listen(new DoublePointer(0));
        }
    }
}
