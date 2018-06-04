package com.example.mypc.officaligif.messages;

import com.example.mypc.officaligif.models.MediaModel;

import java.util.List;

public class MediaSticky {
    public int classID;
    public String title;
    public MediaModel mediaModel;
    public List<MediaModel> relatedList ;

    public MediaSticky(int classID, String title, MediaModel mediaModel, List<MediaModel> relatedList) {
        this.classID = classID;
        this.mediaModel = mediaModel;
        this.relatedList = relatedList;
        this.title = mediaModel.title != null
                ? mediaModel.title
                : mediaModel.caption != null
                ? mediaModel.caption : title;

    }
}
