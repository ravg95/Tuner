package com.ravg95.tuner;

import android.media.audiofx.Visualizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DoublePointer.OnValueChangedListener {

    //private TextView textView;
    //private Visualizer visualizer;
    //private DoublePointer freq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //    textView = (TextView) findViewById(R.id.textView);
       // freq = new DoublePointer(0, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FrequencyRecogniser.listen(new DoublePointer(0));
       // visualizer = new Visualizer(0);
        //analyze();
    }

    @Override
    public void valueChanged(double newValue) {
    //    textView.setText(newValue+"Hz");
    }

    public void analyze(){
        do {
            byte[] fft = new byte[3000];
       //     visualizer.setEnabled(true);
        //    visualizer.getFft(fft);
            double frequency = 0;
            for (int i = 0; i < fft.length; i++) {
                if (fft[i] > frequency) frequency = fft[i];
            }
           // freq.setValue(frequency);
        }while(true);
    }

}
