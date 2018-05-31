package com.example.mypc.officaligif.models;

import java.util.ArrayList;
import java.util.List;

public class ResultResponseListModel {
    public List<MediaModel> mediaModelList;
    public List<MediaModel> pageMediaModelList;

    public ResultResponseListModel() {
        this.mediaModelList = new ArrayList<>();
        this.pageMediaModelList = new ArrayList<>();
    }

    public ResultResponseListModel(List<MediaModel> mediaModelList) {
        this.mediaModelList = mediaModelList;
        if (!mediaModelList.isEmpty())
            for (int position = 0; position < Math.min(10, mediaModelList.size()); position++) {
                pageMediaModelList.add(mediaModelList.get(position));
            }
    }

    public void add(MediaModel mediaModel){
        if(mediaModelList == null)
            mediaModelList = new ArrayList<>();
        if(pageMediaModelList == null)
            pageMediaModelList = new ArrayList<>();

        mediaModelList.add(mediaModel);
        if(pageMediaModelList.size() < 10)
            pageMediaModelList.add(mediaModel);
    }
}
