package com.ravg95.tuner;


import java.util.HashMap;

/**
 * Created by rafal on 17/11/2017.
 */

public class Settings {

    static double getBaseFreq(){
        double readFreq = 440;
        return readFreq;
    }


    public static int getToleranceInCents() {
        return 5;
    }

    public static Preset getCurrentPreset(){
        return new Preset(6, "Guitar Standard", new String[]{"E4","B3", "G3", "D3", "A2", "E2"});
    }
}
