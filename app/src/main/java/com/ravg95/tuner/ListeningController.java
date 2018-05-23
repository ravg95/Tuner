package com.ravg95.tuner;

import android.util.Log;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rafal on 16/05/2018.
 */

public class ListeningController  implements DoublePointer.OnValueChangedListener {
    private FrequencyRecogniser frequencyRecogniser;
    private ToneAnalyzer toneAnalyzer;
    private ListenThread listenThread;
    private CanvasController canvasController;
    private AnimationThread animationThread;
    public ListeningController(CanvasController canvasController){
        frequencyRecogniser = new FrequencyRecogniser(new DoublePointer(0, this));
        toneAnalyzer = new ToneAnalyzer();
        this.canvasController = canvasController;
    }

    @Override
    public void valueChanged(final double newValue) {
        Log.d("value changed frequency","freq: " + newValue + "Hz");

                DoublePointer distance = new DoublePointer(0, null);
                String note;
                try {
                    note = toneAnalyzer.getNearestNoteAndDistance(newValue, distance);
                } catch (ToneAnalyzer.NoteOutOfBoundsException e){
                    note = "N/A";
                }

                Log.d("value changed sound","Note: " + note + "\ndistance: " + distance.getValue());
                canvasController.refreshCanvas();
                canvasController.setPitchProperties(note, String.format(Locale.getDefault(), " %.1f Hz",newValue), distance.getValue());
            }




    public void pause(){

        frequencyRecogniser.stopListening();
        animationThread.cancel();
    }

    public void resume() {
        Timer t = new Timer();
        listenThread = new ListenThread();
        t.schedule(listenThread, 2000);
        animationThread = new AnimationThread();
        t.schedule(animationThread, 2010, 70);
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
            canvasController.invalidateCanvas();
        }
    }
}
