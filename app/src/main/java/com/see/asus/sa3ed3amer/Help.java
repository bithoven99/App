package com.see.asus.sa3ed3amer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class Help extends AppCompatActivity {
    TextView helptxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        helptxt = (TextView) findViewById(R.id.helptxt);

        InputStream input = null;
        try {
            input = getAssets().open("help.txt");

            int size = input.available();
            byte[] text = new byte[size];
            input.read(text);
            input.close();

            String text_help = new String(text);
            helptxt.setText(text_help);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
