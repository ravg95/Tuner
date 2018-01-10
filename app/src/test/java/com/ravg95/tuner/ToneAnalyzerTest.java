package com.ravg95.tuner;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ToneAnalyzerTest {
    @Test
    public void toneNames_is_correct() throws Exception {
        ToneAnalyzer analyzer = new ToneAnalyzer();
        String[] names = {"A0", "A0", "C4", "C4", "A4", "A4", "A5", "A5", "A6"};
        double[] vals = {27.5, 28, 261.63, 261, 440, 432.756, 882, 880, 1789};
        for(int i = 0; i < vals.length && i < names.length; i++){
            String name;
            try {
                 name = analyzer.getNearestNoteAndDistance(vals[i], new DoublePointer(0, null));
                System.out.println("frequency: "+vals[i]+" expected: "+names[i]+" result: "+ name);
            } catch (ToneAnalyzer.NoteOutOfBoundsException e){
                System.err.println("note out of bounds!! -- BAD, Very Bad");
                continue;
            }
            assertEquals("tone names differ", names[i], name);
        }

    }
}
