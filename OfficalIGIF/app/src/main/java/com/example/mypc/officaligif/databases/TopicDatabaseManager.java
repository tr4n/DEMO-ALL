package com.example.mypc.officaligif.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mypc.officaligif.models.SuggestTopicModel;
import com.example.mypc.officaligif.models.TopicModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopicDatabaseManager {
    private static final String TAG = "TopicTopicDatabaseManager";

    private static final String TABLE_MAIN = "main_topics";
    private static final String TABLE_TOPICS = "topic_list";


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
    }

    public List<SuggestTopicModel> getSuggestTopicModelList() {
        sqLiteDatabase = topicDatabase.getReadableDatabase();

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


            while (!cursor.isAfterLast()) {
                //read data
                int id = cursor.getInt(4);
                String key = cursor.getString(5);
                String url = cursor.getString(6);
                int parentid = cursor.getInt(7);

                topicModelList.add(new TopicModel(id, key, url, parentid));
                //next
                cursor.moveToNext();
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

    /*
    public List<CategoryModel> getListCategory(List<TopicModel> topicModelList) {
        List<CategoryModel> categoryModelList = new ArrayList<>();
        for (int i = 0; i < topicModelList.size(); i = i + 5) {
            CategoryModel categoryModel = new CategoryModel(
                    topicModelList.get(i).category,
                    topicModelList.get(i).color);
            categoryModelList.add(categoryModel);
        }

        return categoryModelList;
    }

    public HashMap<String, List<TopicModel>> getHashMapTopic(
            List<TopicModel> topicModelList,
            List<CategoryModel> categoryModelList) {
        HashMap<String, List<TopicModel>> hashMap = new HashMap<>();
        for (int i = 0; i < categoryModelList.size(); i++) {
            int positionTopic = i * 5;

            hashMap.put(categoryModelList.get(i).name,
                    topicModelList.subList(positionTopic, positionTopic + 5));
        }
        return hashMap;
    }

    public WordModel getRandomWord(int topicId, int preId) {
        sqLiteDatabase = topicDatabase.getReadableDatabase();

        Cursor cursor;
        int level = 0;
        do {
            //1. level?
            double random = Math.random() * 100; // 0 <= random < 100
            if (random < 5) level = 4;
            else if (random < 15) level = 3;
            else if (random < 30) level = 2;
            else if (random < 60) level = 1;
            else level = 0;

            //2. word?
            cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_WORD +
                    " where topic_id = " + topicId +
                    " and level = " + level +
                    " and id <> " + preId +
                    " order by random() limit 1", null);
        } while (cursor.getCount() == 0);

        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String origin = cursor.getString(1);
        String explanation = cursor.getString(2);
        String type = cursor.getString(3);
        String pronunciation = cursor.getString(4);
        String imageUrl = cursor.getString(5);
        String example = cursor.getString(6);
        String exampleTrans = cursor.getString(7);

        WordModel wordModel = new WordModel(id, origin, explanation,
                type, pronunciation, imageUrl, example, exampleTrans, topicId, level);

        return wordModel;
    }

    public void updateWordLevel(WordModel wordModel, boolean isKnown) {
        sqLiteDatabase = topicDatabase.getWritableDatabase();

        int level = wordModel.level;
        if (isKnown && level < 4) {
            level++;
        } else if (!isKnown && level > 0) {
            level--;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("level", level);
        sqLiteDatabase.update(TABLE_WORD, contentValues,
                "id = " + wordModel.id, null);
    }

    public void updateLastTime(TopicModel topicModel, String lastTime) {
        sqLiteDatabase = topicDatabase.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("last_time", lastTime);
        sqLiteDatabase.update(TABLE_TOPIC, contentValues,
                "id = " + topicModel.id, null);
    }

    public int getNumOfMasterWordByTopicId(int topicId) {
        sqLiteDatabase = topicDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select level from " + TABLE_WORD
                        + " where level = 4 and topic_id = " + topicId,
                null);
        Log.d(TAG, "getNumOfMasterWordByTopicId: " + cursor.getCount());
        return cursor.getCount();
    }

    public int getNumOfNewWordByTopicId(int topicId) {
        sqLiteDatabase = topicDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select level from " + TABLE_WORD
                        + " where level = 0 and topic_id = " + topicId,
                null);
        Log.d(TAG, "getNumOfNewWordByTopicId: " + cursor.getCount());
        return cursor.getCount();
    }
    */
}
