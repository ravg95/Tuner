package com.ravg95.tuner.presenter;

import android.content.Context;

import com.ravg95.tuner.data.SettingsManager;
import com.ravg95.tuner.data.ToneAnalyzer;
import com.ravg95.tuner.util.DoublePointer;
import com.ravg95.tuner.util.Preset;

import lombok.Getter;
import lombok.Setter;

public class TunerViewPresenter {

    @Getter
    protected String freq = "440Hz";
    @Getter
    protected double dist = 0;
    @Getter
    protected String note = "A4";
    @Getter
    @Setter
    protected String lastNote;
    @Getter
    @Setter
    protected double angle;


    public int findNearestString(String note, Preset currentPreset, DoublePointer distance) {
        double[] distances = new double[currentPreset.getNumOfStrings()];
        int semiNote = ToneAnalyzer.getSemitonesFromNoteName(note);
        double minDist = Double.MAX_VALUE;
        int retIndex = 0;
        for (int i = 0; i < currentPreset.getNumOfStrings(); i++) {
            int semiString = ToneAnalyzer.getSemitonesFromNoteName(currentPreset.getStrings()[i]);
            distances[i] = -100 * (semiNote - semiString) + (distance.getValue());
            if (Math.abs(distances[i]) < Math.abs(minDist)) {
                minDist = distances[i];
                retIndex = i;
            }
        }
        distance.setValue(minDist);
        return retIndex;
    }

    public void setPitchProperties(String note, String freq, double dist) {
        this.note = note;
        this.freq = freq;
        this.dist = dist;
    }
    public Preset getCurrentPreset(Context context){
        return SettingsManager.getCurrentPreset(context);
    }
}
