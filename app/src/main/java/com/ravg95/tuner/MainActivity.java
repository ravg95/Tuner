package com.ravg95.tuner;

import android.media.audiofx.Visualizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DoublePointer.OnValueChangedListener {

    private TextView textView;
    private FrequencyRecogniser frequencyRecogniser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        frequencyRecogniser = new FrequencyRecogniser(new DoublePointer(0, this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timer t = new Timer();
        TimerTask tt = new DelayedStart();
        t.schedule(tt, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        frequencyRecogniser.stopListening();
    }


    @Override
    public void valueChanged(double newValue) {
        textView.setText(new String(newValue+"Hz"));
    }

    private class DelayedStart extends TimerTask {

        @Override
        public void run() {
            frequencyRecogniser.init();
            frequencyRecogniser.listen();
        }
    }

}

