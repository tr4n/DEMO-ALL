package com.example.mypc.demosuper.models;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class GifModel implements Serializable, Comparable<GifModel> {

    public String id;
    public String title;
    public String url;
    public String width;
    public String height;

    public String originalUrl;
    public String relativeUrl;

    public GifModel(String id, String title, String url, String width, String height, String originalUrl, String relativeUrl, Drawable drawable, int position) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.width = width;
        this.height = height;
        this.originalUrl = originalUrl;
        this.relativeUrl = relativeUrl;
        this.drawable = drawable;
        this.position = position;
    }

    public GifModel(String width, String height, int position) {
        this.width = width;

        this.height = height;
        this.position = position;
    }

    public GifModel(String id, String title, String url, String width, String height, String originalUrl, Drawable drawable, int position) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.width = width;
        this.height = height;

        this.originalUrl = originalUrl;
        this.drawable = drawable;
        this.position = position;
    }

    public Drawable drawable;
    public int position;

    public GifModel(String width, String height, Drawable drawable) {
        this.width = width;
        this.height = height;
        this.drawable = drawable;
    }

    public GifModel(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public GifModel(String id, String title, String url, String width, String height, String originalUrl) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.width = width;
        this.height = height;
        this.originalUrl = originalUrl;
    }

    public GifModel(String id, String title, String url, String width, String height, String originalUrl, Drawable drawable) {
        this.id = id;
        this.title = title;

        this.url = url;
        this.width = width;
        this.height = height;
        this.originalUrl = originalUrl;
        this.drawable = drawable;

    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int compareTo(@NonNull GifModel o) {
        return  (this.id == o.id ) ? 1 : 0;
    }
}
