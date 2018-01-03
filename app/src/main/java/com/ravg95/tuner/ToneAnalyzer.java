package com.ravg95.tuner;

import android.util.Log;

import java.util.TreeMap;

/**
 * Created by rafal on 17/11/2017.
 * source of maths : https://pages.mtu.edu/~suits/NoteFreqCalcs.html
 */

public class ToneAnalyzer {
    private static final double CONSTANT = 1.059463; // 2^-12
    private double BASE_FREQ = Settings.getBaseFreq();
    ;
    private TreeMap<Double, String> tones;
    //It is important that C is fisrt in this array as i will assume that in further calculations.
    public final String[] toneNames = {"C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A", "Bb", "B"};
    private static boolean initialized = false;
    private final int SEMITONES_ABOVE_A4 = 50;
    private final int SEMITONES_BELOW_A4 = 57;


    String getNearestNoteAndDistance(double freq, DoublePointer distance) throws NoteOutOfBoundsException {
        double semitones = Math.log(freq / BASE_FREQ) / Math.log(CONSTANT);
        double roundSemitones = Math.round(semitones);
        double cents = semitones - roundSemitones;
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

    private String getNameForSemitones(int semitones) throws NoteOutOfBoundsException {
        if (semitones < -SEMITONES_BELOW_A4 || semitones > SEMITONES_ABOVE_A4)
            throw new NoteOutOfBoundsException();
        int indexOfA = 9;
        int toneIndex = (indexOfA + semitones) % toneNames.length;
        int octaveIndex = (indexOfA + 4 * toneNames.length + semitones) / 12;
        if(toneIndex < 0 ) toneIndex += 12;
        return toneNames[toneIndex]+""+octaveIndex;
    }

    class NoteOutOfBoundsException extends Throwable {
    }
}
