package com.example.servicestest1.mediaplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Button;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.servicestest1.R;

import java.io.IOException;

public class AudioService extends Service {
    public static final String ACTION_PLAY = "com.example.action.PLAY";
    public static final String ACTION_STOP = "com.example.action.STOP";
    private MediaPlayer mediaPlayer;
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_PLAY.equals(action)) {
                Uri audioUri = intent.getData();
                playAudio(audioUri);
            } else if (ACTION_STOP.equals(action)) {
                stopAudio();
            }
        }
        return START_NOT_STICKY;
    }

    private void playAudio(Uri audioUri) {
        try {
            mediaPlayer.setDataSource(getApplicationContext(), audioUri);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    startForeground(NOTIFICATION_ID, buildNotification());
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            stopForeground(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification buildNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.service_media_player);
        remoteViews.setOnClickPendingIntent(R.id.iv_pause, getPendingIntentForAction("pause"));
        remoteViews.setOnClickPendingIntent(R.id.iv_stop, getPendingIntentForAction("stop"));

        Intent stopIntent = new Intent(this, AudioService.class);
        stopIntent.setAction(ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
//                .setContentTitle("Audio is playing")
                .setCustomContentView(remoteViews)
//                .setSmallIcon(R.drawable.ic_audio)
                .setContentIntent(stopPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
                ;

        return builder.build();
    }

    private PendingIntent getPendingIntentForAction(String action) {
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction(action);
        return PendingIntent.getService(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }
}
