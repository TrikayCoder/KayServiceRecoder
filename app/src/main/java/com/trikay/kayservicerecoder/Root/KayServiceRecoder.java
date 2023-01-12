package com.trikay.kayservicerecoder.Root;

import android.content.Context;
import android.content.Intent;

import com.trikay.kayservicerecoder.Control.Service.RecordingService;
import com.trikay.kayservicerecoder.Control.VideoRecoder.VideoRecorderControl;
import com.trikay.kayservicerecoder.Model.VideoRecoder.VideoRecorder;

public class KayServiceRecoder {
    private VideoRecorderControl videoRecorderControl;
    private RecordingService recordingService;
    private Context context;
    private boolean isPaused = false;
    private long pausedTime;

    private  long startTime;

    public KayServiceRecoder(Context context) {
        this.context = context;
        startTime = 0;
    }

    public void startRecording() {
        recordingService = new RecordingService();
        videoRecorderControl = new VideoRecorderControl(new VideoRecorder());
        Intent serviceIntent = new Intent(context, recordingService.getClass());
        context.startService(serviceIntent);
        videoRecorderControl.startRecording();
    }

    public void pauseRecording() {
        if (videoRecorderControl.isRecording() && !isPaused) {
            videoRecorderControl.pauseRecording();
            isPaused = true;
            pausedTime = System.currentTimeMillis();
        }
    }

    public void resumeRecording() {
        if (isPaused) {
            videoRecorderControl.startRecording();
            isPaused = false;
            startTime += System.currentTimeMillis() - pausedTime;
        }
    }

    public void stopRecording() {
        videoRecorderControl.stopRecording();
        Intent serviceIntent = new Intent(context, recordingService.getClass());
        context.stopService(serviceIntent);
    }

    public boolean isRecording() {
        return videoRecorderControl.isRecording();
    }
}
