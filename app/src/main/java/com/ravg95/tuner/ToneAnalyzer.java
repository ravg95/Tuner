package com.ravg95.tuner;

import android.util.Log;

import java.util.Arrays;
import java.util.TreeMap;

/**
 * Created by rafal on 17/11/2017.
 * source of maths : https://pages.mtu.edu/~suits/NoteFreqCalcs.html
 */

public class ToneAnalyzer {
    private static final double CONSTANT = 1.059463; // 2^-12
    private static final double BASE_FREQ = Settings.getBaseFreq();
    ;
    private TreeMap<Double, String> tones;
    //It is important that C is fisrt in this array as i will assume that in further calculations.
    public static final String[] toneNames = {"C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A", "Bb", "B"};
    private static final int indexOfA = 9;

    String getNearestNoteAndDistance(double freq, DoublePointer distance) throws NoteOutOfBoundsException {
        double semitones = Math.log(freq / BASE_FREQ) / Math.log(CONSTANT);
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
        int octave = name.charAt(name.length()-1) - '0';
        Log.d("note to freq:","tone: "+tone + " octave: "+ octave);
        int toneIndex = Arrays.binarySearch(toneNames,tone);
        int semitones = 12*octave - indexOfA - 4*toneNames.length;
        int distT = indexOfA - toneIndex;
        if(octave<4 || (octave == 4 && distT > 0)){
            semitones += distT;
            semitones = -semitones;
        } else if(octave>4 || (octave == 4 && distT < 0)) {
            semitones -= distT;
        }
        return semitones;
    }



    class NoteOutOfBoundsException extends Throwable {
    }
}
