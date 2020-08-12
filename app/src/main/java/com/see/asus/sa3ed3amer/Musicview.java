package com.see.asus.sa3ed3amer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Musicview extends AppCompatActivity {
    Button back, stop, next;

    com.gauravk.audiovisualizer.visualizer.BarVisualizer visualizer;

    TextView soundname, current, total;
    MediaPlayer sound = new MediaPlayer();
    SeekBar seekBar;
    Boolean play = true;
    ArrayList<String> paths_arr;
    ArrayList<String> names_arr;
    int current_position;
    int audioSessionId;
    Getwavfiles wav;
    Getmp3files mp3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicview);
        visualizer = findViewById(R.id.Bar);
        checkAudioPermission();


        back = (Button) findViewById(R.id.back);
        stop = (Button) findViewById(R.id.resume);
        next = (Button) findViewById(R.id.next);
        soundname = (TextView) findViewById(R.id.soundname);
        current = (TextView) findViewById(R.id.currenttime);
        total = (TextView) findViewById(R.id.totaltime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        sound.stop();
        sound.reset();
        when_choose_sound();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) sound.seekTo(progress);
                soundtime();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        boolean supports_equalizer=false;
        AudioEffect.Descriptor [] effects = Equalizer.queryEffects();
        for (AudioEffect.Descriptor lDescriptor:effects){
            if (Build.VERSION.SDK_INT>=18) { //Equalizer present only starting with API 18. Cam try to hardcode its UUID
                if (AudioEffect.EFFECT_TYPE_EQUALIZER.equals(lDescriptor.uuid)){
                    supports_equalizer=true;
    }}}}

    public void when_choose_sound() {
        Intent intent1 = getIntent();
        Bundle bundle = intent1.getExtras();
        String sound_path = bundle.getString("songpath");
        String sound_name = bundle.getString("soundname");
        paths_arr = bundle.getStringArrayList("paths_arr");
        names_arr = bundle.getStringArrayList("names_arr");
        current_position = bundle.getInt("current");
        sound.reset();
        sound = MediaPlayer.create(getApplicationContext(), Uri.parse(sound_path));

        audioSessionId = sound.getAudioSessionId();
        if (audioSessionId != -1)
            visualizer.setAudioSessionId(audioSessionId);
        soundname.setText(sound_name);
        soundtime();
        upadateseekbar();

    }

    void upadateseekbar() {
        if (!sound.isPlaying()) {
            Thread update_seekBar = new Thread() {
                @Override
                public void run() {
                    int SoundDuration = sound.getDuration();
                    int Currentposition = 0;
                    seekBar.setMax(SoundDuration);
                    while (Currentposition < SoundDuration)

                    {
                        try {
                            sleep(50);
                            Currentposition = sound.getCurrentPosition();
                            seekBar.setProgress(Currentposition);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                }
            };
            sound.start();
            update_seekBar.start();

        }
    }


    private void soundtime() {
        seekBar.setMax(sound.getDuration());
        int time = (seekBar.getMax() / 1000);
        int m = time / 60;
        int s = time % 60;

        int time0 = (seekBar.getProgress() / 1000);
        int m0 = time0 / 60;
        int s0 = time0 % 60;


        total.setText(m + " : " + s);
        current.setText(m0 + " : " + s0);

    }


    public void btn_pause(View view) {
        if (play == true) {
            sound.pause();
            stop.setBackgroundResource(R.drawable.pause);
            play = false;


        } else if (play == false) {
            sound.start();
            stop.setBackgroundResource(R.drawable.play);
            play = true;
        } else {
        }

    }


    @Override
    public void onBackPressed() {
        sound.stop();
        super.onBackPressed();

    }

    public void btn_next(View view) {
        sound.stop();
        sound.reset();
        if (current_position == paths_arr.size() - 1) {
            current_position = 0;
            sound = MediaPlayer.create(getApplicationContext(), Uri.parse(paths_arr.get(0)));
            audioSessionId = sound.getAudioSessionId();
            if (audioSessionId != -1)
                visualizer.setAudioSessionId(audioSessionId);
            soundname.setText(names_arr.get(0));
            upadateseekbar();
            stop.setBackgroundResource(R.drawable.play);
            play = true;
        } else {
            current_position++;
            sound = MediaPlayer.create(getApplicationContext(), Uri.parse(paths_arr.get(current_position)));
            audioSessionId = sound.getAudioSessionId();
            if (audioSessionId != -1)
                visualizer.setAudioSessionId(audioSessionId);
            soundname.setText(names_arr.get(current_position));
            upadateseekbar();
            stop.setBackgroundResource(R.drawable.play);
            play = true;
        }

    }

    public void btn_back(View view) {
        sound.stop();
        sound.reset();
        if (current_position == 0) {
            current_position = paths_arr.size() - 1;
            sound = MediaPlayer.create(getApplicationContext(), Uri.parse(paths_arr.get(paths_arr.size() - 1)));
            audioSessionId = sound.getAudioSessionId();
            if (audioSessionId != -1)
                visualizer.setAudioSessionId(audioSessionId);
            soundname.setText(names_arr.get(names_arr.size() - 1));
          //  upadateseekbar();
            stop.setBackgroundResource(R.drawable.play);
            play = true;


        } else {
            current_position--;
            sound = MediaPlayer.create(getApplicationContext(), Uri.parse(paths_arr.get(current_position)));
            audioSessionId = sound.getAudioSessionId();
            if (audioSessionId != -1)
                visualizer.setAudioSessionId(audioSessionId);
            soundname.setText(names_arr.get(current_position));
           // upadateseekbar();
            stop.setBackgroundResource(R.drawable.play);
            play = true;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (visualizer != null)
            visualizer.release();
    }

    private void checkAudioPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 100);
                return;

            }
            else {}

        }}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
        else {checkAudioPermission();}
    }
}