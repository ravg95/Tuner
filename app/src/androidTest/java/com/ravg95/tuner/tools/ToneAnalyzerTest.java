package com.ravg95.tuner.tools;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ToneAnalyzerTest {
    @Test
    public void semitonesForNoteNameTest() {
      String[] names = new String[] {"A4", "A#4", "Ab4", "C3", "Eb2", "F4", "G#3", "Bb4", "C5", "D3"};
      int[] results = new int[] {0, 1, -1, -21, -30, -4, -13, 1, 3, -19};
      int[] temp = new int[results.length];
      for(int i =0 ; i < results.length; i++) {
          temp[i] = ToneAnalyzer.getSemitonesFromNoteName(names[i]);
      }
        Assert.assertArrayEquals(results, temp);
    }

}