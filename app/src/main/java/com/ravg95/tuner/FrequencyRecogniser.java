package com.ravg95.tuner;


import android.media.AudioFormat;
import android.media.AudioRecord;

import android.media.MediaRecorder;
import android.util.Log;

import lombok.NonNull;


public class FrequencyRecogniser {
    private static final double AMPLITUDE_THRESHOLD = 30000;
    private static final double QUALITY_THRESHOLD = 0.6;
    private AudioRecord record;
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int SAMPLE_RATE = 44100;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSizeInBytes = findFloorPowerOf2(AudioRecord.getMinBufferSize(SAMPLE_RATE, channelConfig, audioFormat)*16);
    @NonNull
    private DoublePointer frequency;


    public FrequencyRecogniser(DoublePointer frequency) {
        this.frequency = frequency;
    }

    public void init() {
        record = new AudioRecord(audioSource, SAMPLE_RATE, channelConfig, audioFormat, bufferSizeInBytes);
        Log.d("buff size",""+AudioRecord.getMinBufferSize(SAMPLE_RATE, channelConfig, audioFormat));
    }

    public void listen() {

        if (record.getState() == AudioRecord.STATE_INITIALIZED) {
            record.startRecording();
        } else {
            return;
        }

        do {

            short[] buffer = new short[bufferSizeInBytes];
            int readBytes = record.read(buffer, 0, bufferSizeInBytes);
            if (readBytes >= 0) {           // if readBytes < 0, an error has occured while reading
                try {
                    checkDataQuality(buffer, readBytes);
                    frequency.setValue(analyze(buffer, readBytes));
                } catch (BadDataQualityException e) {
                   Log.d("listen", "Bad signal quality");
                }

            }

        }
        while (record.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING);
    }

    private void checkDataQuality(short[] buffer, int readBytes) throws BadDataQualityException {
        short a1 = 0, a2 = 0;
        int z1 = 0, z2 = 0;
        int highestAmp = 0;
        Log.d("read bytes: ", "" + readBytes);
        for (int i = 0; i < readBytes; i++) {
            if (i < readBytes / 2 ) {
                if( (short) Math.abs(buffer[i]) > a1)
                    a1 = (short) Math.abs(buffer[i]);
                if(i+1 < readBytes/2 && buffer[i] * buffer[i+1]<=0) {
                    z1++;
                    if(buffer[i] * buffer[i+1]==0)
                        i++;
                }

            }else{
                if((short) Math.abs(buffer[i]) > a2 )
                    a2 = (short) Math.abs(buffer[i]);
                if(i+1 < readBytes && buffer[i] * buffer[i+1]<=0) {
                    z2++;
                    if(buffer[i] * buffer[i+1]==0)
                        i++;
                }
            }
            if(Math.abs(buffer[i])>highestAmp) {
                highestAmp = Math.abs(buffer[i]);
            }

        }
        double pa1 = (double) ((double)Math.min(a1, a2) / (double)Math.max(a1, a2));
        double pa2 = (double) ((double)Math.min(z1, z2) / (double)Math.max(z1, z2));
        double pa3 = 0;
       if (highestAmp <= AMPLITUDE_THRESHOLD)pa3 = (1-Math.pow((highestAmp/ AMPLITUDE_THRESHOLD),2));
        else pa3 = 0;

        double quality = pa1 * pa2 * pa3;
        Log.d("pa1: ", ""+pa1);
        Log.d("pa2: ", ""+pa2);
        Log.d("pa3: ", ""+pa3);
        Log.d("quality: ", ""+quality);
        if(quality < QUALITY_THRESHOLD) throw new BadDataQualityException();


    }

    public void stopListening() {
        if (record != null)
            record.stop();
        //need to init befroe listening again
    }

    private double analyze(short[] buffer, int readBytes) {
        readBytes = findFloorPowerOf2(readBytes);
        FFT fft = new FFT(readBytes);
        double[] re = new double[readBytes];
        double[] im = new double[readBytes];
        for (int i = 0; i < readBytes; i++) {  // fill re and im arrays and apply window.
            re[i] = (double) buffer[i] * window(i, readBytes);
            im[i] = 0;
        }
        fft.fft(re, im);
        //Downsampling for HSS
        double[] re2 = new double[readBytes / 2];
        double[] re3 = new double[readBytes / 3];
        double[] im2 = new double[readBytes / 2];
        double[] im3 = new double[readBytes / 3];
        for (int i = 0; 2*i+1 < readBytes ; i++) {
            re2[i] = (re[2 * i] + re[2 * i +1] )/2;
            im2[i] = (im[2 * i] + im[2 * i +1] )/2;
        }
        for (int i = 0; 3*i+2 < readBytes ; i++) {

            re3[i] = (re[3 * i] + re[3 * i + 1] + re[3 * i + 2] )/3;
            im3[i] = (im[3 * i] + im[3 * i + 1] + im[3 * i + 2] )/3;;

        }
        //HSS

        for (int i = 0; i < readBytes; i++) {
            if (i < readBytes / 2) {
                re[i] += re2[i];
                im[i] += im2[i];
            }
            if (i < readBytes / 3) {
                re[i] += re3[i];
                im[i] += im3[i];
            }
        }
        double highestPeak = 0;
        int indexOfHihgestPeak = 0;
        for (int i = 0; i < readBytes; i++) {
            double magnitude = Math.sqrt(re[i] * re[i] + im[i] * im[i]);
            if (magnitude > highestPeak) {
                highestPeak = magnitude;
                indexOfHihgestPeak = i;
            }
        }

        double frequency = (double) (indexOfHihgestPeak * SAMPLE_RATE / (double) readBytes);

        return frequency;
    }

    private int findFloorPowerOf2(int n) {
        int exponent = (int) Math.floor(Math.log(n) / Math.log(2));
        return (int) Math.pow(2, exponent);
    }

    public double window(double n, int N) {
        return 1; //square window
        // Hanning window
        //return 0.5 + 0.5 * Math.cos((2 * n * Math.PI) / N);
    }

    private class BadDataQualityException extends Exception {
    }

}