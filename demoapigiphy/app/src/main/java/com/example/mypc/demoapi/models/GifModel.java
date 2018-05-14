package com.example.mypc.demoapi.models;

import android.support.v7.widget.RecyclerView;

import java.io.Serializable;

public class GifModel implements Serializable {
    public String id;
    public String title;
    public String url;
    public String width;
    public String height;
    public String linkCopy;

    public GifModel(String id, String title, String url, String width, String height, String linkCopy) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.width = width;
        this.height = height;
        this.linkCopy = linkCopy;
    }
}
