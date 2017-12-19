package com.ravg95.tuner;


import android.media.AudioFormat;
import android.media.AudioRecord;

import android.media.MediaRecorder;
import android.util.Log;

import lombok.NonNull;


public class FrequencyRecogniser {
    private static final double LOWEST_ACCEPTABLE_PEAK = 10000;
    private static final double UPPER_BOUND_FREQUENCY = 7905;
    private static final double LOWER_BOUND_FREQUENCY = 15;
    private AudioRecord record;
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int SAMPLE_RATE = 44100;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSizeInBytes = findFloorPowerOf2(AudioRecord.getMinBufferSize(SAMPLE_RATE, channelConfig, audioFormat) * 2);
    @NonNull
    private DoublePointer frequency;


    public FrequencyRecogniser(DoublePointer frequency) {
        this.frequency = frequency;
    }

    public void init() {
        record = new AudioRecord(audioSource, SAMPLE_RATE, channelConfig, audioFormat, bufferSizeInBytes);
    }

    public void listen() {

        if (record.getState() == AudioRecord.STATE_INITIALIZED) {
            record.startRecording();
        } else {
            return;
        }

        do {

            short[] buffer = new short[bufferSizeInBytes / 2];
            int readBytes = record.read(buffer, 0, bufferSizeInBytes / 2);
            if (readBytes >= 0) {           // if readBytes < 0, an error has occured while reading
                try {
                    frequency.setValue(analyze(buffer, readBytes));
                } catch (LowPeakException e){
                    Log.d("listen", "Low Peak exception caught");
                } catch (FrequencyOutOfExpectedRangeException e) {
                    Log.d("listen", "Frequency out of expected range exception caught");
                }
            }

        }
        while (record.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING);
    }

    public void stopListening() {
        if(record != null)
            record.stop();
        //need to init befroe listening again
    }

    private double analyze(short[] buffer, int readBytes) throws LowPeakException, FrequencyOutOfExpectedRangeException{
        readBytes = findFloorPowerOf2(readBytes);
        FFT fft = new FFT(readBytes);
        double[] re = new double[readBytes];
        double[] im = new double[readBytes];
        for(int i=0; i<readBytes; i++){  // fill re and im arrays and apply window.
            re[i] = (double) buffer[i] * window(i, readBytes);
            im[i] = 0;
        }
        fft.fft(re, im);
        double highestPeak = 0;
        int indexOfHihgestPeak=0;
        for(int i=0; i<readBytes; i++) {
            double magnitude = Math.sqrt(re[i]*re[i]+im[i]*im[i]);
            if(magnitude > highestPeak) {
                highestPeak = magnitude;
                indexOfHihgestPeak = i;
            }
        }
        if(highestPeak < LOWEST_ACCEPTABLE_PEAK) throw new LowPeakException();
        Log.d("analysis", "highest peak: "+highestPeak);
        double frequency = indexOfHihgestPeak * SAMPLE_RATE / readBytes;
        if(frequency < LOWER_BOUND_FREQUENCY || frequency > UPPER_BOUND_FREQUENCY) throw new FrequencyOutOfExpectedRangeException();
        return frequency;
    }

    private int findFloorPowerOf2(int n) {
        int exponent = (int) Math.floor (Math.log(n)/Math.log(2));
        return (int) Math.pow(2,exponent);
    }

    public double window(double n, int N){
        return 1; //square window
        // Hann window
       // return (1 - Math.cos((2*Math.PI*n)/(N-1)))/2.0;
    }
    private class LowPeakException extends Exception{}
    private class FrequencyOutOfExpectedRangeException extends  Exception {}
}
