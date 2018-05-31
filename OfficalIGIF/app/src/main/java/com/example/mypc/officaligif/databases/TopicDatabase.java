package com.example.mypc.officaligif.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class TopicDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "igif_topics.db";
    private static final int DATABASE_VERSION = 1;


    public TopicDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




}
