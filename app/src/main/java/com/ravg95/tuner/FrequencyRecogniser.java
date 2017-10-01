package com.ravg95.tuner;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
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
        record.startRecording();

        AudioTrack audioPlayer = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, AudioFormat.CHANNEL_OUT_MONO,
                audioFormat, bufferSizeInBytes, AudioTrack.MODE_STREAM);

        if(audioPlayer.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
            audioPlayer.play();

        do {
            short[] buffer = new short[bufferSizeInBytes];
            int msg = record.read(buffer, 0, bufferSizeInBytes);
            if (msg >= 0) {
                audioPlayer.write(buffer, 0, msg);
                Log.d("FrequencyRecogniser", "going for fft");
                String bufferContent = "";
            }
            Log.e("FrequencyRecogniser", "invalid operation!");

        } while (record.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING);
    }

}
