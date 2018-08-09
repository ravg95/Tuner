package com.ravg95.tuner.presenter;

import android.util.Log;

import com.ravg95.tuner.data.FrequencyRecognizer;
import com.ravg95.tuner.data.ToneAnalyzer;
import com.ravg95.tuner.fragment.MainFragment;
import com.ravg95.tuner.util.DoublePointer;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rafal on 16/05/2018.
 */

public class MainFragmentPresenter implements DoublePointer.OnValueChangedListener {
    private FrequencyRecognizer frequencyRecognizer;
    private ToneAnalyzer toneAnalyzer;
    private boolean isListeningPaused = true;
    private MainFragment mainFragment;
    private AnimationThread animationThread;
    public MainFragmentPresenter(MainFragment mainFragment){
        frequencyRecognizer = new FrequencyRecognizer(new DoublePointer(0, this));
        toneAnalyzer = new ToneAnalyzer();
        animationThread = new AnimationThread();
        this.mainFragment = mainFragment;
    }

    @Override
    public void valueChanged(final double newValue) {
        Log.d("value changed frequency","freq: " + newValue + "Hz");
        if(isListeningPaused) return;

                DoublePointer distance = new DoublePointer(0, null);
                String note;
                note = toneAnalyzer.getNearestNoteAndDistance(newValue, distance, mainFragment.getContext());

                Log.d("value changed sound","Note: " + note + "\ndistance: " + distance.getValue());
                mainFragment.getTunerView()
                        .getTunerViewPresenter()
                        .setPitchProperties(note, String.format(Locale.getDefault(), " %.1f Hz",newValue), distance.getValue());
                if(animationThread != null)
                    animationThread.cancel();
                animationThread = new AnimationThread();
                Timer t = new Timer();
                t.schedule(animationThread,0, 50);

    }




    public void pause(){
        frequencyRecognizer.stopListening();
        animationThread.cancel();
        isListeningPaused = true;
    }

    public void resume() {
        frequencyRecognizer.startListening();
        isListeningPaused = false;

    }

    private class AnimationThread extends TimerTask {
        @Override
        public void run() {
            mainFragment.invalidateCanvas();
        }
    }
}
