package com.see.asus.sa3ed3amer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplachScreen extends AppCompatActivity {
ImageView imgSplach;
Animation anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);



        imgSplach=(ImageView) findViewById(R.id.imgsplach);

        anim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);
        imgSplach.setAnimation(anim);
        Thread thread=new Thread(){

            @Override
            public void run() {

                try {
                    sleep(3000);

                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }
}
