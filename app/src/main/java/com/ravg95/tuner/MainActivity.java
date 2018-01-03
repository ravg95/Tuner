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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timer t = new Timer();
        listenThread = new ListenThread(new DoublePointer(0, this)); // TODO: for testing purpose. remove argument and constructor afterwards
        t.schedule(listenThread, 2000, 7000);
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
                String note;
                try {
                    note = toneAnalyzer.getNearestNoteAndDistance(newValue, distance);
                } catch (ToneAnalyzer.NoteOutOfBoundsException e){
                    note = "N/A";
                }
                toneView.setText(note);
                distView.setText(String.format(Locale.getDefault(), " %.1f Hz", distance.getValue()));
                Log.d("value changed sound","Note: " + note + "\ndistance: " + distance.getValue());

            }
        });

    }

    private class ListenThread extends TimerTask {
        DoublePointer b;
        int w = 0;
        public ListenThread(DoublePointer dp){
            b = dp;
        }
        double[] freqs = {27.5, 28, 261.63, 261, 440, 432.756, 882, 880};
        @Override
        public void run() {
            if(w >= freqs.length) return;
            b.setValue(freqs[w]);
            w++;

        }
    }

}

