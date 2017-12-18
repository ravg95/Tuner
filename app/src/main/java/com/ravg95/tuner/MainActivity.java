package com.ravg95.tuner;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DoublePointer.OnValueChangedListener {

    private TextView freqView;
    private TextView toneView;
    private TextView distView;
    private FrequencyRecogniser frequencyRecogniser;
    private ToneAnalyzer toneAnalyzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        freqView = (TextView) findViewById(R.id.freq);
        toneView = (TextView) findViewById(R.id.tone);
        distView = (TextView) findViewById(R.id.dist);
        frequencyRecogniser = new FrequencyRecogniser(new DoublePointer(0, this));
        toneAnalyzer = new ToneAnalyzer();
        toneAnalyzer.init();
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
    public void valueChanged(final double newValue) {
        Log.d("value changed frequency","freq: " + newValue + "Hz");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                freqView.setText(newValue + "Hz");
                DoublePointer distance = new DoublePointer(0, null);
                String note = toneAnalyzer.getNearestNoteAndDistance(newValue, distance);
                toneView.setText(note);
                distView.setText(distance.getValue()+ "Hz");
                Log.d("value changed sound","Note: " + note + "\ndistance: " + distance.getValue());

            }
        });

    }

    private class DelayedStart extends TimerTask {

        @Override
        public void run() {
            frequencyRecogniser.init();
            frequencyRecogniser.listen();
        }
    }

}

