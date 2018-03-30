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
    private static final double AMPLITUDE_QUOTIENT_THRESHOLD = 0.9;
    private AudioRecord record;
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int SAMPLE_RATE = 44100;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSizeInBytes = findFloorPowerOf2(AudioRecord.getMinBufferSize(SAMPLE_RATE, channelConfig, audioFormat)*2);
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
                    checkDataQuality(buffer, readBytes);
                    frequency.setValue(analyze(buffer, readBytes));
                } catch (LowPeakException e) {
                    Log.d("listen", "Low Peak exception caught");
                } catch (FrequencyOutOfExpectedRangeException e) {
                    Log.d("listen", "Frequency out of expected range exception caught");
                } catch (BadDataQualityException e) {
                    Log.d("listen", "Bad signal quality");
                }

            }

        }
        while (record.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING);
    }

    private void checkDataQuality(short[] buffer, int readBytes) throws BadDataQualityException {
        short a1 = 0, a2 = 0;
        Log.d("read bytes: ", "" + readBytes);
        for (int i = 0; i < readBytes; i++) {
            if (i < readBytes / 2 && (short) Math.abs(buffer[i]) > a1)
                a1 = (short) Math.abs(buffer[i]);
            else if (i >= readBytes / 2 && (short) Math.abs(buffer[i]) > a2)
                a2 = (short) Math.abs(buffer[i]);

        }
      //  if ((double) (Math.min(a1, a2) / Math.max(a1, a2)) < AMPLITUDE_QUOTIENT_THRESHOLD)
      //      throw new BadDataQualityException();
        //TODO:: zero crossing and magnitude check


    }

    public void stopListening() {
        if (record != null)
            record.stop();
        //need to init befroe listening again
    }

    private double analyze(short[] buffer, int readBytes) throws LowPeakException, FrequencyOutOfExpectedRangeException {
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
        double[] re4 = new double[readBytes / 4];
        double[] re5 = new double[readBytes / 5];
        double[] im2 = new double[readBytes / 2];
        double[] im3 = new double[readBytes / 3];
        double[] im4 = new double[readBytes / 4];
        double[] im5 = new double[readBytes / 5];
        for (int i = 0; i < readBytes / 2; i++) {
            re2[i] = re[2 * i];
            im2[i] = im[2 * i];
        }
        for (int i = 0; i < readBytes / 3; i++) {

            re3[i] = re[3 * i];
            im3[i] = im[3 * i];

        }
        for (int i = 0; i < readBytes / 4; i++) {
            re4[i] = re[4 * i];
            im4[i] = im[4 * i];
        }
        for (int i = 0; i < readBytes / 5; i++) {
            re5[i] = re[5 * i];
            im5[i] = im[5 * i];
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
        if (highestPeak < LOWEST_ACCEPTABLE_PEAK) throw new LowPeakException();
        Log.d("analysis", "highest peak: " + highestPeak);
        double frequency = indexOfHihgestPeak * SAMPLE_RATE / readBytes;
        if (frequency < LOWER_BOUND_FREQUENCY || frequency > UPPER_BOUND_FREQUENCY)
            throw new FrequencyOutOfExpectedRangeException();
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

    private class LowPeakException extends Exception {
    }

    private class FrequencyOutOfExpectedRangeException extends Exception {
    }

    private class BadDataQualityException extends Exception {
    }

}