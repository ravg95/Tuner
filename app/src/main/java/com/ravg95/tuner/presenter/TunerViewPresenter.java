package com.ravg95.tuner.presenter;

import android.content.Context;

import com.ravg95.tuner.tools.SettingsManager;
import com.ravg95.tuner.tools.ToneAnalyzer;
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


    public static int findNearestString(String note, Preset currentPreset, DoublePointer distance) {
        int semiNote = ToneAnalyzer.getSemitonesFromNoteName(note);
        double distNote = distance.getValue();
        int minDist = Integer.MAX_VALUE;
        int retIndex = -1;
        for(int i = 0; i < currentPreset.getNumOfStrings(); i++ ) {
            int semiString = ToneAnalyzer.getSemitonesFromNoteName(currentPreset.getStrings()[i]);
            int dist = (int) (distNote + 100 * (semiNote - semiString));
            if(Math.abs(dist) < Math.abs(minDist)) {
                minDist = dist;
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
