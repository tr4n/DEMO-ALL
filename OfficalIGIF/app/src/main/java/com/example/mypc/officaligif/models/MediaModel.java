package com.example.mypc.officaligif.models;

import java.util.List;

public class MediaModel {
    public String id;
    public String original_url ;
    public String original_width;
    public String original_height;
    public String source_tld;
    public String title;
    public String caption;
    public String fixed_height_url;
    public String fixed_height_width;
    public String fixed_height_height;
    public String fixed_height_small_url;
    public String fixed_height_small_width;
    public String fixed_height_small_height;
    public String fixed_width_url;
    public String fixed_width_width;
    public String fixed_width_height;
    public String fixed_width_small_url;
    public String fixed_width_small_width;
    public String fixed_width_small_height;
    public int position;

    public MediaModel(String id, String original_url, String original_width, String original_height, String source_tld, String title, String caption, String fixed_height_url, String fixed_height_width, String fixed_height_height, String fixed_height_small_url, String fixed_height_small_width, String fixed_height_small_height, String fixed_width_url, String fixed_width_width, String fixed_width_height, String fixed_width_small_url, String fixed_width_small_width, String fixed_width_small_height, int position) {
        this.id = id;
        this.original_url = original_url;
        this.original_width = original_width;
        this.original_height = original_height;
        this.source_tld = source_tld;
        this.title = title;
        this.caption = caption;
        this.fixed_height_url = fixed_height_url;
        this.fixed_height_width = fixed_height_width;
        this.fixed_height_height = fixed_height_height;
        this.fixed_height_small_url = fixed_height_small_url;
        this.fixed_height_small_width = fixed_height_small_width;
        this.fixed_height_small_height = fixed_height_small_height;
        this.fixed_width_url = fixed_width_url;
        this.fixed_width_width = fixed_width_width;
        this.fixed_width_height = fixed_width_height;
        this.fixed_width_small_url = fixed_width_small_url;
        this.fixed_width_small_width = fixed_width_small_width;
        this.fixed_width_small_height = fixed_width_small_height;
        this.position = position;
    }
}
