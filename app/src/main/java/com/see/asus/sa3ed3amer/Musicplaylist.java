package com.see.asus.sa3ed3amer;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Musicplaylist extends AppCompatActivity {
    TextView PLname;
    ListView songslist;
    ArrayList<mediainfo> allsongs = new ArrayList<mediainfo>();
    ArrayList<mediainfo> songs = new ArrayList<mediainfo>();
    ArrayList<Integer> songs_places = new ArrayList<Integer>();
    ArrayList<String>paths_arr=new ArrayList<String>();
    ArrayList<String>names_arr=new ArrayList<String>();
    int current_song_posintion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplaylist);
        PLname = (TextView) findViewById(R.id.PLname);
        songslist = (ListView) findViewById(R.id.songslist);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs_places = bundle.getIntegerArrayList("songsplaces");
        String name=bundle.getString("PLname");
        PLname.setText(name);
        getmusic();
        for( int i=0;i<songs_places.size();i++)
        {
            songs.add(i,allsongs.get(songs_places.get(i)));

        }
        //for bundle to musicview button next/back
        getpaths();
        getnames();
        songslist.setAdapter(new mycustomAdapter(R.drawable.music));
        songslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String path = songs.get(position).Path;
                String name = songs.get(position).Name;
                current_song_posintion=position;
                Bundle bundle = new Bundle();
                bundle.putString("songpath", path);
                bundle.putString("soundname", name);
                bundle.putStringArrayList("paths_arr",paths_arr);
                bundle.putStringArrayList("names_arr",names_arr);
                bundle.putInt("current",current_song_posintion);

                Intent intent1 = new Intent(getApplicationContext(), Musicview.class);
                intent1.putExtras(bundle);
                startActivity(intent1);

            }
        });


    }
    void getpaths(){
         paths_arr.clear();
        for(int i=0;i<songs.size();i++)
        {
            paths_arr.add(i,songs.get(i).Path);
        }
    }
    void getnames()
    {    names_arr.clear();
        for(int i=0;i<songs.size();i++)
        {
            names_arr.add(i,songs.get(i).Name);
        }
    }

    private class mycustomAdapter extends BaseAdapter {
        int img = R.drawable.video;

        public mycustomAdapter(int img) {
            this.img = img;
        }

        @Override
        public int getCount() {
            return songs.size();
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
            mediainfo mediainfo = songs.get(position);
            ImageView img = (ImageView) view.findViewById(R.id.imageView);
            TextView txt1 = (TextView) view.findViewById(R.id.txt1);
            TextView txt2 = (TextView) view.findViewById(R.id.txt2);
            img.setImageResource(this.img);
            txt1.setText(mediainfo.Name);
            txt2.setText(mediainfo.Artist);
            return view;
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
                    allsongs.add(new mediainfo(fullpath, videoname, artistname, albumname));


                } while (cursor.moveToNext());


            }
            cursor.close();


        }
    }
}
