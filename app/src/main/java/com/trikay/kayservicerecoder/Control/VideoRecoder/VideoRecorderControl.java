package com.trikay.kayservicerecoder.Control.VideoRecoder;

import com.trikay.kayservicerecoder.Model.VideoRecoder.VideoRecorder;

public class VideoRecorderControl {
    private VideoRecorder videoRecorder;
    private boolean isRecording = false;

    public VideoRecorderControl(VideoRecorder videoRecorder) {
        this.videoRecorder = videoRecorder;
    }

    public void startRecording() {
        videoRecorder.startRecording();
        isRecording = true;
    }

    public void pauseRecording() {
        videoRecorder.pauseRecording();
        isRecording = false;
    }

    public void stopRecording() {
        videoRecorder.stopRecording();
        isRecording = false;
    }

    public boolean isRecording() {
        return isRecording;
    }
}
