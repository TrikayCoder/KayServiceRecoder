package com.trikay.kayservicerecoder.Model.VideoRecoder;

import android.content.Context;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

public class VideoRecorder {
    private MediaRecorder recorder;
    private String outputFile;
    private boolean isRecording = false;



    public void startRecording() {
        if (recorder == null) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            recorder.setVideoSize(1920, 1080);
            recorder.setVideoFrameRate(30);
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/video.mp4";
            recorder.setOutputFile(outputFile);
            try {
                recorder.prepare();
                recorder.start();
                isRecording = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pauseRecording() {
        if (recorder != null && isRecording) {
            recorder.pause();
            isRecording = false;
        }
    }

    public void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
            isRecording = false;
        }
    }

    public boolean isRecording() {
        return isRecording;
    }
}

