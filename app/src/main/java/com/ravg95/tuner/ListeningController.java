package com.ravg95.tuner;

import android.util.Log;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rafal on 16/05/2018.
 */

public class ListeningController  implements DoublePointer.OnValueChangedListener {
    private FrequencyRecognizer frequencyRecognizer;
    private ToneAnalyzer toneAnalyzer;
    private boolean isListeningPaused = true;
    private CanvasController canvasController;
    private AnimationThread animationThread;
    public ListeningController(CanvasController canvasController){
        frequencyRecognizer = new FrequencyRecognizer(new DoublePointer(0, this));
        toneAnalyzer = new ToneAnalyzer();
        animationThread = new AnimationThread();
        this.canvasController = canvasController;
    }

    @Override
    public void valueChanged(final double newValue) {
        Log.d("value changed frequency","freq: " + newValue + "Hz");
        if(isListeningPaused) return;

                DoublePointer distance = new DoublePointer(0, null);
                String note;
                note = toneAnalyzer.getNearestNoteAndDistance(newValue, distance, canvasController.getContext());

                Log.d("value changed sound","Note: " + note + "\ndistance: " + distance.getValue());
                canvasController.refreshCanvas();
                canvasController.setPitchProperties(note, String.format(Locale.getDefault(), " %.1f Hz",newValue), distance.getValue());
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
            canvasController.invalidateCanvas();
        }
    }
}
