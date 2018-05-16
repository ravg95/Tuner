package com.ravg95.tuner;


import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DoublePointer.OnValueChangedListener {

  //  private TextView freqView;
   // private TextView toneView;
   // private TextView distView;
    private FrequencyRecogniser frequencyRecogniser;
    private ToneAnalyzer toneAnalyzer;
    ListenThread listenThread;
    private TunerView customCanvas;
    CollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        frequencyRecogniser = new FrequencyRecogniser(new DoublePointer(0, this));
        customCanvas = (TunerView) findViewById(R.id.my_canvas);
        toneAnalyzer = new ToneAnalyzer();
        collectionPagerAdapter =
                new CollectionPagerAdapter(
                        getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(collectionPagerAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Timer t = new Timer();
        listenThread = new ListenThread();
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


                DoublePointer distance = new DoublePointer(0, null);
                String note;
                try {
                    note = toneAnalyzer.getNearestNoteAndDistance(newValue, distance);
                } catch (ToneAnalyzer.NoteOutOfBoundsException e){
                    note = "N/A";
                }

                Log.d("value changed sound","Note: " + note + "\ndistance: " + distance.getValue());
                customCanvas = (TunerView) findViewById(R.id.my_canvas);
                customCanvas.setPitchProperties(note, String.format(Locale.getDefault(), " %.1f Hz",newValue), distance.getValue());

                Timer t = new Timer();
                AnimationThread animationThread = new AnimationThread();
                t.schedule(animationThread, 0, 700);

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

    private class AnimationThread extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customCanvas.invalidate();
                }
            });
            //TODO::stop condition
        }
    }

}

