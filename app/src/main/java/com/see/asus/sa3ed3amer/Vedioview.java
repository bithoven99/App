package com.see.asus.sa3ed3amer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class Vedioview extends AppCompatActivity {
    VideoView videoplayer;
    TextView textView;
     Getmp4files mp4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedioview);
        videoplayer = (VideoView) findViewById(R.id.videoView);
        textView = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();
        Bundle bundle1 = intent.getExtras();

        String key1 = bundle1.getString("key1");
        String key2 = bundle1.getString("key2");
        textView.setText(key1);
        videoplayer.setVideoPath(key1);
        videoplayer.setMediaController(new MediaController(this));
        videoplayer.canPause();
        videoplayer.canSeekBackward();
        videoplayer.canSeekForward();

        videoplayer.start();


    }



}