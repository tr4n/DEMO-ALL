package com.example.mypc.officaligif.databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;

import com.example.mypc.officaligif.adapters.SearchedAdapter;
import com.example.mypc.officaligif.messages.DataListSticky;
import com.example.mypc.officaligif.models.DataPair;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.models.PairModel;
import com.example.mypc.officaligif.models.ResponseModel;
import com.example.mypc.officaligif.models.SuggestTopicModel;
import com.example.mypc.officaligif.models.TopicModel;
import com.example.mypc.officaligif.networks.MediaResponse;
import com.example.mypc.officaligif.networks.RetrofitInstance;
import com.example.mypc.officaligif.networks.iGIPHYService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                String url = cursor.getString(6).trim();
                 int parentid = cursor.getInt(7);

                topicModelList.add(new TopicModel(id, key, url, parentid));


                final String keySearch = key;
                /*
                ResponseModel responseModel = new ResponseModel(keySearch + " random", "eng", 1);
                RetrofitInstance.getRetrofitGifInstance().create(iGIPHYService.class)
                        .getMediaResponses(responseModel.key, responseModel.lang, responseModel.limit, responseModel.api_key)
                        .enqueue(new Callback<MediaResponse>() {
                            @Override
                            public void onResponse(Call<MediaResponse> call, final Response<MediaResponse> response) {
                                if (response.body() == null || response.body().pagination.count == 0 || response.body().data.isEmpty()) {
                                    return;
                                }


                                final List<MediaModel> mediaModelList = new ArrayList<>();
                                List<MediaResponse.DataJSON> dataJSONList = response.body().data;
                                String fixedUrl = response.body().data.get(0).images.preview_gif.url;
                                Log.d(TAG, "onResponse: " + keySearch + ": " + fixedUrl);


                            }

                            @Override
                            public void onFailure(Call<MediaResponse> call, Throwable t) {

                            }
                        });

*/



              //  Log.d(TAG, "getSuggestTopicModelList: \n" + url + "\n- " + fixedUrl[0] );
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
            do {
                Log.d(TAG, "updateRecentTopic: list after add " + cursor.getString(1));
            } while (cursor.moveToNext());


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

    private String getRandomUrlTopic(final DataPair<String, String> dataPair) {
        ResponseModel responseModel = new ResponseModel(dataPair.first, "eng", 1);
        RetrofitInstance.getRetrofitGifInstance().create(iGIPHYService.class)
                .getMediaResponses(responseModel.key, responseModel.lang, responseModel.limit, responseModel.api_key)
                .enqueue(new Callback<MediaResponse>() {
                    @Override
                    public void onResponse(Call<MediaResponse> call, final Response<MediaResponse> response) {
                        if (response.body() == null || response.body().pagination.count == 0 || response.body().data.isEmpty()) {
                            return;
                        }


                        final List<MediaModel> mediaModelList = new ArrayList<>();
                        List<MediaResponse.DataJSON> dataJSONList = response.body().data;
                        Log.d(TAG, "onResponse: dataPair.second before = " + dataPair.second);
                        dataPair.second = response.body().data.get(0).images.preview_gif.url;
                        Log.d(TAG, "onResponse: dataPair.second = \n" + dataPair.second + "\n" + response.body().data.get(0).images.preview_gif.url );

                    }

                    @Override
                    public void onFailure(Call<MediaResponse> call, Throwable t) {

                    }
                });
        return dataPair.second;
    }




}
