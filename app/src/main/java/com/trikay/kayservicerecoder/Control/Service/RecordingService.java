package com.trikay.kayservicerecoder.Control.Service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.trikay.kayservicerecoder.Control.VideoRecoder.VideoRecorderControl;
import com.trikay.kayservicerecoder.Model.VideoRecoder.VideoRecorder;

import java.util.concurrent.TimeUnit;

public class RecordingService extends Service {
    private VideoRecorderControl videoRecorderControl;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private boolean isPaused = false;
    private long pausedTime;

    private long startTime;
    private static final String CHANNEL_ID = "RecordingServiceChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        videoRecorderControl = new VideoRecorderControl(new VideoRecorder());
        videoRecorderControl.startRecording();
        createNotificationChannel();
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Recording video")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setAutoCancel(true);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        startForeground(1, notificationBuilder.build());
        startTimer();
        return START_STICKY;
    }

    private void startTimer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startTime = System.currentTimeMillis();
                while (videoRecorderControl.isRecording()) {
                    long currentTime = System.currentTimeMillis();
                    long duration = currentTime - startTime;
                    if (isPaused) {
                        duration = pausedTime - startTime;
                    }
                    @SuppressLint("DefaultLocale") String durationString = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration), TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));
                    notificationBuilder.setContentText("Duration: " + durationString);
                    notificationManager.notify(1, notificationBuilder.build());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        videoRecorderControl.stopRecording();
        super.onDestroy();
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Recording Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}

