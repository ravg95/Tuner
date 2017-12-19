package com.ravg95.tuner;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DoublePointer.OnValueChangedListener {

    private TextView freqView;
    private TextView toneView;
    private TextView distView;
    private FrequencyRecogniser frequencyRecogniser;
    private ToneAnalyzer toneAnalyzer;
    ListenThread listenThread;

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
        listenThread = new ListenThread();
        t.schedule(listenThread, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        frequencyRecogniser.stopListening();
        listenThread.cancel();
    }


    @Override
    public void valueChanged(final double newValue) {
        Log.d("value changed frequency","freq: " + newValue + "Hz");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                freqView.setText(String.format(Locale.getDefault(), " %.1f Hz",newValue));
                DoublePointer distance = new DoublePointer(0, null);
                String note = toneAnalyzer.getNearestNoteAndDistance(newValue, distance);
                toneView.setText(note);
                distView.setText(String.format(Locale.getDefault(), " %.1f Hz", distance.getValue()));
                Log.d("value changed sound","Note: " + note + "\ndistance: " + distance.getValue());

            }
        });

    }

    private class ListenThread extends TimerTask {

        @Override
        public void run() {
            frequencyRecogniser.init();
            frequencyRecogniser.listen();
        }
    }

}

