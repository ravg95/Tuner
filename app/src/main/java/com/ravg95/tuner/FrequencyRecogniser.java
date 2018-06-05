package com.ravg95.tuner;


import android.util.Log;

import be.tarsos.dsp.AudioDispatcher;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import lombok.NonNull;


public class FrequencyRecogniser {
    private int SAMPLE_RATE = 22050;
    @NonNull
    private DoublePointer frequency;


    public FrequencyRecogniser(DoublePointer frequency) {
        this.frequency = frequency;
    }


    Thread listenThread;

    public void init() {

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(SAMPLE_RATE,1024,0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                final float pitchInHz = result.getPitch();
                Log.d("Pitch event", "pitch received");
                frequency.setValue(pitchInHz);

            }
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, SAMPLE_RATE, 1024, pdh);
        dispatcher.addAudioProcessor(p);
        Log.d("Pitch init", "pitch initialised");
        listenThread = new Thread(dispatcher,"Audio Dispatcher");
        Log.d("Pitch listen", "pitch started");
        listenThread.start();
    }


    public void stopListening() {
        if (listenThread != null)
            listenThread.interrupt();
        Log.d("Pitch stop", "pitch stopped");
    }

}