package com.example.servicestest1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.servicestest1.mediaplayer.AudioService;

public class LauncherActivity extends AppCompatActivity {
    Button btnMediaPlayer;
    private static final int REQUEST_PICK_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        setupView();
    }

    /**
     * run after setContentView()
     */
    private void setupView() {
        btnMediaPlayer = findViewById(R.id.btn_media_player_service);
        btnMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, REQUEST_PICK_AUDIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_AUDIO && resultCode == RESULT_OK && data != null) {
            Uri audioUri = data.getData();
            startAudioService(audioUri);
        }
    }

    private void startAudioService(Uri audioUri) {
        Intent serviceIntent = new Intent(this, AudioService.class);
        serviceIntent.setAction(AudioService.ACTION_PLAY);
        serviceIntent.setData(audioUri);
        startService(serviceIntent);
    }
}