package com.ravg95.tuner.data;

import android.content.Context;

import com.ravg95.tuner.data.SettingsManager;
import com.ravg95.tuner.util.DoublePointer;

import java.util.Arrays;
import java.util.TreeMap;

/**
 * Created by rafal on 17/11/2017.
 * source of maths : https://pages.mtu.edu/~suits/NoteFreqCalcs.html
 */

public class ToneAnalyzer {
    private static final double CONSTANT = 1.059463; // 2^(-12)

    private TreeMap<Double, String> tones;
    //It is important that C is fisrt in this array as i will assume that in further calculations.
    public static final String[] toneNames = {"C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A", "Bb", "B"};
    private static final int indexOfA = 9;

    public String getNearestNoteAndDistance(double freq, DoublePointer distance, Context context) {
        double semitones = Math.log(freq / SettingsManager.getBaseFreq(context)) / Math.log(CONSTANT);
        double roundSemitones = Math.round(semitones);
        double cents = (semitones - roundSemitones)*100;
        if (cents > 50) {
            cents = cents - 100;
            roundSemitones++;
        } else if (cents < -50) {
            cents = cents + 100;
            roundSemitones--;
        }
        distance.setValue(cents);
        return getNameForSemitones((int) roundSemitones);
    }

    private String getNameForSemitones(int semitones) {

        int toneIndex = (indexOfA + semitones) % toneNames.length;
        int octaveIndex = (indexOfA + 4 * toneNames.length + semitones) / 12;
        if(toneIndex < 0 ) toneIndex += 12;
        return toneNames[toneIndex]+""+octaveIndex;
    }

    public static int getSemitonesFromNoteName(String name){
        String tone = name.charAt(0)+"";
        char sign = name.charAt(1);
        if(sign == '#' || sign == 'b')
            tone = tone.concat(sign+"");
        int octave = name.charAt(name.length()-1) - '0';
        int toneIndex = Arrays.asList(toneNames).indexOf(tone);
        //TODO:: make work for all kinds of tone naming eg. G# = Ab
        int semitones = (octave - 4) * 12 + toneIndex - indexOfA;
        return semitones;
    }
}
