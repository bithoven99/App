package com.see.asus.sa3ed3amer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

public class SavedPL {
    final static String fileName = "savedPLnames.txt";
    final static String path = Environment.getExternalStorageDirectory().getPath()+"/MyAppFile/";
    final static String TAG = SavedPL.class.getName();

    public static ArrayList<playlistitem> ReadFile(Context context) {
        ArrayList<playlistitem> A = new ArrayList<playlistitem>();
        try {
            FileInputStream fis = new FileInputStream(path+fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            A = (ArrayList<playlistitem>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return A;
    }

    public static boolean saveToFile(ArrayList<playlistitem> A,Context context) {
        try {
            new File(path).mkdir();
            File file = new File(path+fileName );
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(path+fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.reset();
            oos.writeObject(A);
            oos.close();
            fos.close();
            return true;

        } catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return false;


    }



}
