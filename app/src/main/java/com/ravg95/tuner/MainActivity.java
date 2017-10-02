package com.ravg95.tuner;

import android.media.audiofx.Visualizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DoublePointer.OnValueChangedListener{

    private TextView textView;

    private DoublePointer freq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        freq = new DoublePointer(0, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timer t = new Timer();
        TimerTask tt = new DelayedStart(this);
        t.schedule(tt, 2000);
    }


    @Override
    public void valueChanged(double newValue) {
        textView.setText(newValue+"Hz");
    }

    private class DelayedStart extends TimerTask {
        DoublePointer.OnValueChangedListener listener;
        public DelayedStart(DoublePointer.OnValueChangedListener listener) {
            this.listener=listener;
        }

        @Override
        public void run() {
            FrequencyRecogniser.listen(new DoublePointer(0, listener));
        }
    }

}

