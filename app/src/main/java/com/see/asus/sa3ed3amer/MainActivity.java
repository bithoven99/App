package com.see.asus.sa3ed3amer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FileConversionTool fct;
    Getmp4files mp4;
    Getmp3files mp3;
    Getwavfiles wav;
    ListView listview;
    Button vedios, musics, playlists, btnadd, btnhelp, btnshare;
    Animation buttons_in, buttons_out, btn_add_anim, btn_add_anim_inv;
    long Time;
    ArrayList<mediainfo> fullmediapath = new ArrayList<mediainfo>();
    ArrayList<String> paths_arr = new ArrayList<String>();
    ArrayList<String> names_arr = new ArrayList<String>();
    int current_song_posintion;
    Boolean onbuttonclick = true;
    Boolean open = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        vedios = (Button) findViewById(R.id.btn_vidoes);
        musics = (Button) findViewById(R.id.btn_music);
        playlists = (Button) findViewById(R.id.btn_playlist);
        btnadd = (Button) findViewById(R.id.btn_add);
        btnshare = (Button) findViewById(R.id.btn_share);
        btnhelp = (Button) findViewById(R.id.btn_help);
        buttons_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.buttons_in);
        buttons_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.buttons_out);
        btn_add_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.btn_add_anim);
        btn_add_anim_inv = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.btn_add_anim_inverse);
        btnadd.bringToFront();
        listview = findViewById(R.id.listview);
        CheckPermission();

    }

    public void fillListAndActions() {

        listview.setAdapter(new mycustomAdapter(R.drawable.video));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("key1", fullmediapath.get(position).Path);
                bundle.putString("key2", fullmediapath.get(position).Name);
                Intent intent = new Intent(getApplicationContext(), Vedioview.class);
                intent.putExtras(bundle);
                startActivity(intent); 

            }
        });

    }


    public void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;

            }

        }
        getvedios();
        fillListAndActions();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getvedios();
            fillListAndActions();
        } else {
            finish();
        }
    }

    public void getvedios() {
        Cursor cursor;
        Uri allsonguri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media.DATA + "!=0";
        cursor = getContentResolver().query(allsonguri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String videoname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String albumname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String artistname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    fullmediapath.add(new mediainfo(fullpath, videoname, artistname, albumname));


                } while (cursor.moveToNext());


            }
            cursor.close();


        }
    }

    public void getmusic() {
        Cursor cursor;
        Uri allsonguri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        cursor = getContentResolver().query(allsonguri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String videoname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String albumname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String artistname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    fullmediapath.add(new mediainfo(fullpath, videoname, artistname, albumname));


                } while (cursor.moveToNext());


            }
            cursor.close();


        }
    }

    public void btn_music(View view) {
        if (onbuttonclick == true) {
            fullmediapath.clear();
            getmusic();
            getpaths();
            getnames();
            listview.setAdapter(new mycustomAdapter(R.drawable.music));
            onbuttonclick = false;
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String path = fullmediapath.get(position).Path;
                    String name = fullmediapath.get(position).Name;
                    current_song_posintion = position;
                    Bundle bundle = new Bundle();
                    bundle.putString("songpath", path);
                    bundle.putString("soundname", name);
                    bundle.putStringArrayList("paths_arr", paths_arr);
                    bundle.putStringArrayList("names_arr", names_arr);
                    bundle.putInt("current", current_song_posintion);
                    Intent intent1 = new Intent(getApplicationContext(), Musicview.class);
                    intent1.putExtras(bundle);
                    startActivity(intent1);

                }
            });


        } else {
        }
    }

    void getpaths() {
        paths_arr.clear();
        for (int i = 0; i < fullmediapath.size(); i++) {
            paths_arr.add(i, fullmediapath.get(i).Path);
        }
    }

    void getnames() {
        names_arr.clear();

        for (int i = 0; i < fullmediapath.size(); i++) {
            names_arr.add(i, fullmediapath.get(i).Name);
        }
    }

    public void btn_video(View view) {
        if (onbuttonclick == false) {

            fullmediapath.clear();
            getvedios();
            listview.setAdapter(new mycustomAdapter(R.drawable.video));
            onbuttonclick = true;
            fillListAndActions();


        } else {
        }

    }

    public void btnplaylist(View view) {
        Intent intent = new Intent(getApplicationContext(), Playlists.class);
        startActivity(intent);
    }

    public void clk_add(View view) {
        if (open == true) {
            open = false;
            btnadd.startAnimation(btn_add_anim);
            btnshare.startAnimation(buttons_in);
            btnhelp.startAnimation(buttons_in);

            btnshare.setVisibility(view.VISIBLE);
            btnhelp.setVisibility(view.VISIBLE);
        } else {
            open = true;
            btnadd.startAnimation(btn_add_anim_inv);
            btnshare.startAnimation(buttons_out);
            btnhelp.startAnimation(buttons_out);

            btnshare.setVisibility(view.INVISIBLE);
            btnhelp.setVisibility(view.INVISIBLE);


        }
    }

    public void clk_help(View view) throws InterruptedException {

        open = true;
        Intent intent = new Intent(getApplicationContext(), Help.class);
        startActivity(intent);

        btnshare.setVisibility(view.INVISIBLE);
        btnhelp.setVisibility(view.INVISIBLE);

    }

    public void clk_share(View view) {
        open = true;
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("mailto:"+"*"));
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, "saeed.aamer.16@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Download this app and Enjoy...");
            intent.putExtra(Intent.EXTRA_TEXT, "https://drive.google.com/file/d/1frvMjV7RKCsCXVVyUlruVuPWf82eMkFy/view?usp=sharing");
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Sorry..You Have Not Email App", Toast.LENGTH_SHORT).show();
        }
        btnshare.setVisibility(view.INVISIBLE);
        btnhelp.setVisibility(view.INVISIBLE);
    }

    private class mycustomAdapter extends BaseAdapter {
        int img = R.drawable.video;

        public mycustomAdapter(int img) {
            this.img = img;
        }

        @Override
        public int getCount() {
            return fullmediapath.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.rowitem, null);
            mediainfo mediainfo = fullmediapath.get(position);
            ImageView img = (ImageView) view.findViewById(R.id.imageView);
            TextView txt1 = (TextView) view.findViewById(R.id.txt1);
            TextView txt2 = (TextView) view.findViewById(R.id.txt2);
            img.setImageResource(this.img);
            txt1.setText(mediainfo.Name);
            txt2.setText(mediainfo.Artist);
            return view;


        }

    }

    @Override
    public void onBackPressed() {
        if (Time + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Click again to exit", Toast.LENGTH_SHORT).show();
        }
        Time = System.currentTimeMillis();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.onResume();
    }


}
