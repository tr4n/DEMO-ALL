package com.example.mypc.demoapi.models;

import android.support.v7.widget.RecyclerView;

public class GifModel  {
    public String title;
    public String url;
    public String width;
    public String height;

    public GifModel(String title, String url, String width, String height) {
        this.title = title;
        this.url = url;
        this.width = width;
        this.height = height;
    }
}
