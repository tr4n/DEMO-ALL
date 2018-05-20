package com.example.mypc.demosuper.models;

import java.io.Serializable;

public class FixedHeightGIFModel implements Serializable{

    public int position;

    public String id;
    public String source_tld;
    public String originalUrl;
    public String title;
    public String url;
    public String width;
    public String height;
    public String urlSmall;


    public FixedHeightGIFModel(String id, String source_tld, String originalUrl, String title, String url, String width, String height, String urlSmall) {
        this.id = id;
        this.source_tld = source_tld;
        this.originalUrl = originalUrl;
        this.title = title;
        this.url = url;
        this.width = width;
        this.height = height;
        this.urlSmall = urlSmall;
    }
}
