package com.ravg95.tuner;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;

import android.media.MediaRecorder;
import android.util.Log;


public final class FrequencyRecogniser {
    public static void listen(DoublePointer frequency) {
        int audioSource = MediaRecorder.AudioSource.MIC;
        int sampleRateInHz = 44100;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);


        AudioRecord record = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);

        if (record.getState() == AudioRecord.STATE_INITIALIZED) {
            record.startRecording();
        } else {
            return;
        }

        do {
            short[] buffer = new short[bufferSizeInBytes / 2];
            int msg = record.read(buffer, 0, bufferSizeInBytes / 2);
            if (msg >= 0) { //if no error read returns a non-negative number of read bytes
                //do FFT with buffer and msg read bytes
            }

        } while (record.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING);
    }

}
