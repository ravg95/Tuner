package com.ravg95.tuner;


import android.media.AudioFormat;
import android.media.AudioRecord;

import android.media.MediaRecorder;


public final class FrequencyRecogniser {

    private final static int SAMPLE_RATE = 44100;
    public static void listen(DoublePointer frequency) {
        int audioSource = MediaRecorder.AudioSource.MIC;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int bufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE, channelConfig, audioFormat);


        AudioRecord record = new AudioRecord(audioSource, SAMPLE_RATE, channelConfig, audioFormat, bufferSizeInBytes);

        if (record.getState() == AudioRecord.STATE_INITIALIZED) {
            record.startRecording();
        } else {
            return;
        }


        do {

            short[] buffer = new short[bufferSizeInBytes / 2];
            int readBytes = record.read(buffer, 0, bufferSizeInBytes / 2);
            if (readBytes >= 0) {           // if readBytes < 0, an error has occured while reading
               frequency.setValue(analyze(buffer, readBytes));
            }

        }
        while (record.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING );
    }

    private static double analyze(short[] buffer, int readBytes) {
        FFT fft = new FFT(readBytes);
        double[] re = new double[readBytes];
        double[] im = new double[readBytes];
        for(int i=0; i<readBytes; i++){
            re[i] = (double) buffer[i];
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
        return indexOfHihgestPeak * SAMPLE_RATE / readBytes;
    }

}
