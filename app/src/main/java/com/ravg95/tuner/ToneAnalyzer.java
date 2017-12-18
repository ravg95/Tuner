package com.ravg95.tuner;

import android.util.Log;

import java.util.TreeMap;

/**
 * Created by rafal on 17/11/2017.
 * source of maths : https://pages.mtu.edu/~suits/NoteFreqCalcs.html
 */

public class ToneAnalyzer {
    private static final double CONSTANT = 1.059463; // 2^-12
    private double BASE_FREQ;
    private TreeMap<Double, String> tones;
    //It is important that C is fisrt in this array as i will assume that in further calculations.
    public final String[] toneNames = {"C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A", "Bb", "B"};
    private static boolean initialized = false;
    private final int SEMITONES_ABOVE_A4 = 50;
    private final int SEMITONES_BELOW_A4 = 57;


    public void init(){
        BASE_FREQ = Settings.getBaseFreq();
        tones = new TreeMap<>();
        generateToneTree();
        initialized = true;
    }

    private void generateToneTree(){ //do poprawy
        int octaveIndex = 0;
        int toneIndex  = 0;
        for(int i = -SEMITONES_BELOW_A4 ; i <=SEMITONES_ABOVE_A4 ; i++){
            String name = toneNames[toneIndex] + octaveIndex;
            double freq = BASE_FREQ * Math.pow(CONSTANT, i);
            Log.d("new sound: ", name +" "+ freq);
            tones.put(freq, name);
            toneIndex++;
            if(toneIndex == toneNames.length){
                toneIndex = 0;
                octaveIndex++;
            }
        }
    }
    String getNearestNoteAndDistance(double freq, DoublePointer distance) throws NullPointerException{
        double ceilKey=0, floorKey=0;
        try {
            ceilKey = tones.ceilingKey(freq);
            floorKey = tones.floorKey(freq);
        } catch (NullPointerException e) {
            Log.e("getNearestNoteAndDi", "null ptr exception");
            return "";
        }
        if(freq - floorKey < ceilKey - freq){
            distance.setValue(floorKey - freq);
            return String.valueOf(tones.ceilingEntry(freq).getValue());
        }  else {
            distance.setValue(ceilKey - freq);
            return String.valueOf(tones.floorEntry(freq).getValue());
        }
    }
}
