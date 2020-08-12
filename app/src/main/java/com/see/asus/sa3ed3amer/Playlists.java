package com.see.asus.sa3ed3amer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Playlists extends AppCompatActivity {
    LinearLayout linearLayout;
    Button btn_add, cancel, create;
    ListView listView, playlist;
    EditText editname;
    ArrayList<mediainfo> arrayList = new ArrayList<mediainfo>();
    ArrayList<Integer> playlistitems = new ArrayList<Integer>();
    ArrayList<playlistitem> play_list = null;
    Boolean[] checked = new Boolean[1000];
    String PLname;




    final File file = new File(Environment.getExternalStorageDirectory().getPath() + "/PLnames");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);


        btn_add = (Button) findViewById(R.id.btn_add);
        cancel = (Button) findViewById(R.id.bucancel);
        create = (Button) findViewById(R.id.bucreate);
        create.setEnabled(false);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        listView = (ListView) findViewById(R.id.listview);
        playlist = (ListView) findViewById(R.id.playlist);
        editname = (EditText) findViewById(R.id.editname);


        getmusic();
        fillcheckboxarray();
        listView.setAdapter(new Playlists.mycustomAdapter());
        play_list= SavedPL.ReadFile(getApplicationContext());
        playlist.setAdapter(new mycustomAdapter2(play_list));
        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Musicplaylist.class);
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("songsplaces", play_list.get(position).PLitems);
                bundle.putString("PLname", play_list.get(position).name);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });
        playlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                play_list.remove(position);
                SavedPL.saveToFile(play_list,getApplicationContext());
                playlist.setAdapter(new mycustomAdapter2(play_list));
                Toast.makeText(getApplicationContext(),"playlist deleted",Toast.LENGTH_SHORT).show();
                return false;
            }
        });




    }


    public void clk_add(View view) {

        linearLayout.setVisibility(view.VISIBLE);
        playlist.setVisibility(View.INVISIBLE);
        CheckPermission();
    }

    public void fillcheckboxarray() {
        for (int i = 0; i < arrayList.size(); i++) {
            checked[i] = false;
        }
    }

    public void clk_create(View view) throws IOException {

        PLname = String.valueOf(editname.getText());
        play_list.add(new playlistitem(PLname,playlistitems));
        playlist.setAdapter(new mycustomAdapter2(play_list));
        playlist.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Musicplaylist.class);
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("songsplaces", play_list.get(position).PLitems);
                bundle.putString("PLname", play_list.get(position).name);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });


    }


    public void clk_cancel(View view) {
        linearLayout.setVisibility(view.INVISIBLE);
        playlist.setVisibility(View.VISIBLE);

    }


    private class mycustomAdapter extends BaseAdapter {


        public mycustomAdapter() {

        }

        @Override
        public int getCount() {
            return arrayList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.rowitemplay, null);
            mediainfo mediainfo = arrayList.get(position);
            TextView txt1 = (TextView) view.findViewById(R.id.txt1);
            TextView txt2 = (TextView) view.findViewById(R.id.txt2);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            txt1.setText(mediainfo.Name);
            txt2.setText(mediainfo.Artist);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    editname.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            if(playlistitems.isEmpty()|editname.getText().toString().isEmpty())
                                create.setEnabled(false);
                            else
                                create.setEnabled(true);
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(playlistitems.isEmpty()|editname.getText().toString().isEmpty())
                                create.setEnabled(false);
                            else
                                create.setEnabled(true);

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if(playlistitems.isEmpty()|editname.getText().toString().isEmpty())
                                create.setEnabled(false);
                            else
                                create.setEnabled(true);
                        }
                    });
                    if (isChecked) {
                        checked[position] = true;

                        if (!playlistitems.contains(position)) {
                            playlistitems.add(position);
                            if(playlistitems.isEmpty()|editname.getText().toString().isEmpty())
                                create.setEnabled(false);
                            else
                                create.setEnabled(true);


                        } else {
                        }


                    } else {
                        checked[position] = false;

                        try {

                            if (playlistitems.contains(position)) {
                                playlistitems.remove((Integer) position);

                                if (playlistitems.size() == 0) {
                                            create.setEnabled(false);
                                } else {create.setEnabled(true);
                                }
                            } else {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            });
            checkBox.setChecked(checked[position]);


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
                    arrayList.add(new mediainfo(fullpath, videoname, artistname, albumname));


                } while (cursor.moveToNext());


            }
            cursor.close();


        }
    }

    private class mycustomAdapter2 extends BaseAdapter {
        ArrayList<playlistitem> lista;

        public mycustomAdapter2(ArrayList<playlistitem> lista) {
            this.lista = lista;
        }

        @Override
        public int getCount() {
            return lista.size();
        }

        @Override
        public Object getItem(int position) {
            return lista.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view1 = layoutInflater.inflate(R.layout.playlistrow, null);
            TextView txtname = (TextView) view1.findViewById(R.id.txtname);
            ImageView playimage = (ImageView) view1.findViewById(R.id.playimage);
            txtname.setText(lista.get(position).name);
            playimage.setImageResource(R.drawable.playlistimg);
            return view1;
        }
    }



    @Override
    public void onBackPressed() {
        SavedPL.saveToFile(play_list,getApplicationContext());
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        SavedPL.saveToFile(play_list,getApplicationContext());
        super.onStop();
    }

    public void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                return;

            }

        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            CheckPermission();
        }
    }

}
