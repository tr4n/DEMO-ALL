package com.example.mypc.officaligif.databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.mypc.officaligif.models.SuggestTopicModel;
import com.example.mypc.officaligif.models.TopicModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TopicDatabaseManager {
    private static final String TAG = "TopicDatabaseManager";

    private static final String TABLE_MAIN = "main_topics";
    private static final String TABLE_TOPICS = "topic_list";
    private static final String SEARCHED_TOPICS = "searched_topics";
    private static final String RECENT_TOPICS = "recent_search";


    private SQLiteDatabase sqLiteDatabase;
    private TopicDatabase topicDatabase;

    private static TopicDatabaseManager TopicDatabaseManager;

    public static TopicDatabaseManager getInstance(Context context) {
        if (TopicDatabaseManager == null) {
            TopicDatabaseManager = new TopicDatabaseManager(context);
        }
        return TopicDatabaseManager;
    }

    public TopicDatabaseManager(Context context) {
        topicDatabase = new TopicDatabase(context);
        sqLiteDatabase = topicDatabase.getWritableDatabase();
    }

    @SuppressLint("LongLogTag")
    public List<SuggestTopicModel> getSuggestTopicModelList() {


        List<SuggestTopicModel> suggestTopicModelList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_MAIN, null);
        int NUMBER_TOPICS = cursor.getCount();

        for (int topic = 1; topic <= NUMBER_TOPICS; topic++) {
            cursor = sqLiteDatabase.rawQuery(
                    "select * from " +
                            TABLE_MAIN + " , " + TABLE_TOPICS +
                            " where " + TABLE_MAIN + ".id = " +
                            TABLE_TOPICS + ".parent_id and " +
                            TABLE_TOPICS + ".parent_id = " + topic,
                    null);
            cursor.moveToFirst();


            int parent_id = cursor.getInt(0);
            String parent_key = cursor.getString(1);
            int count = cursor.getInt(2);
            String color = cursor.getString(3);

            List<TopicModel> topicModelList = new ArrayList<>();


            while (cursor.moveToNext()) {
                //read data
                int id = cursor.getInt(4);
                String key = cursor.getString(5);
                String url = cursor.getString(6);

                int parentid = cursor.getInt(7);

                topicModelList.add(new TopicModel(id, key, url, parentid));
                //next
                ;
            }

            suggestTopicModelList.add(new SuggestTopicModel(
                    parent_id,
                    parent_key,
                    count,
                    color,
                    topicModelList
            ));
        }


        return suggestTopicModelList;


    }

    @SuppressLint("LongLogTag")
    public void saveSearchedTopic(String topic) {
        updateRecentTopic(topic);


        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + SEARCHED_TOPICS + " where " + SEARCHED_TOPICS + ".topic LIKE \"" + topic + "\"", null);

        if (cursor.getCount() == 0) {
            int id = sqLiteDatabase.rawQuery("select * from " + SEARCHED_TOPICS, null).getCount();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", id);
            contentValues.put("topic", topic);
            contentValues.put("searching_times", 1);

            sqLiteDatabase.insert(SEARCHED_TOPICS, null, contentValues);
        } else {
            cursor.moveToFirst();
            Log.d(TAG, "saveSearchedTopic: " + cursor.getInt(0) + " " +
                    cursor.getString(1) + " " +
                    cursor.getInt(2) + " "

            );
            int searchTimes = cursor.getInt(2);
            if (searchTimes < 20) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("searching_times", (++searchTimes));
                sqLiteDatabase.update(SEARCHED_TOPICS, contentValues, "topic LIKE \"" + topic + "\"", null);

            }
            Log.d(TAG, "saveSearchedTopic: " + searchTimes);
        }


    }

    private void updateRecentTopic(String topic) {


        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + RECENT_TOPICS, null);

        if (cursor.getCount() == 0) { // if table is empty
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", 0);
            contentValues.put("topic", topic);
            sqLiteDatabase.insert(RECENT_TOPICS, null, contentValues);
            Log.d(TAG, "updateRecentTopic: " + "add First <" + topic + ">");
            Log.d(TAG, "updateRecentTopic: " + cursor.getCount());
        } else { // if table is not empty

            List<String> recentSearchList = new ArrayList<String>();
            HashSet<String> recentSearchHashSet = new HashSet<>();
            recentSearchHashSet.clear();
            recentSearchList.clear();
            cursor.moveToFirst();

            recentSearchHashSet.add(topic);
            recentSearchList.add(topic);
            do {
                String recentTopic = cursor.getString(1);
                {
                    if (recentSearchHashSet.add(recentTopic))
                        recentSearchList.add(recentTopic);
                }
            } while (cursor.moveToNext());

            Log.d(TAG, "updateRecentTopic: after while" + recentSearchList.size());
            Log.d(TAG, "updateRecentTopic: " + "add <" + topic + ">");
            sqLiteDatabase.execSQL("delete from " + RECENT_TOPICS);
            for (int id = 0; id < Math.min(10, recentSearchList.size()); id++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", id);
                contentValues.put("topic", recentSearchList.get(id));
                sqLiteDatabase.insert(RECENT_TOPICS, null, contentValues);
            }


            cursor = sqLiteDatabase.rawQuery("select * from " + RECENT_TOPICS, null);

            Log.d(TAG, "updateRecentTopic: after delete " + cursor.getCount());
            cursor.moveToFirst();
            do{
                Log.d(TAG, "updateRecentTopic: list after add " + cursor.getString(1) );
            }while(cursor.moveToNext());


        }
    }

    public List<String> getRecentSearchList() {
        List<String> topicList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + RECENT_TOPICS, null);
        int count = cursor.getCount();
        if (count != 0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                topicList.add(cursor.getString(1));
            }
        }

        Log.d(TAG, "getRecentSearchList: " + topicList.size());
        return topicList;
    }


}
