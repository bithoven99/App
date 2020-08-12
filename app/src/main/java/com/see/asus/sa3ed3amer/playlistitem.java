package com.see.asus.sa3ed3amer;

import java.io.Serializable;
import java.util.ArrayList;

public class playlistitem implements Serializable{
    String name;
    ArrayList<Integer>PLitems;

    public playlistitem(String name, ArrayList<Integer> PLitems) {
        this.name = name;
        this.PLitems = PLitems;
    }
}
