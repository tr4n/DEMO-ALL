package com.example.mypc.officaligif.database_dir;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class TopicDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "igif_topics.db";
    private static final int DATABASE_VERSION = 1;


    public TopicDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




}
