package com.example.mypc.demosuper.utils;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.example.mypc.demosuper.adapters.GifRecyclerViewAdapter;
import com.example.mypc.demosuper.models.GifModel;
import com.example.mypc.demosuper.networks.GIPHYService;
import com.example.mypc.demosuper.networks.GifResponse;
import com.example.mypc.demosuper.networks.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Utils {

    private static final String TAG = "Utils";

    public static void openFragment(android.support.v4.app.FragmentManager fragmentManager, int layoutID, Fragment fragment) {
        fragmentManager.beginTransaction()
                .add(layoutID, fragment)
                .addToBackStack(null)
                .commit();

    }
    public static void replaceFragment(android.support.v4.app.FragmentManager fragmentManager, int layoutID, Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(layoutID, fragment)
                .addToBackStack(null)
                .commit();

    }

    public static void loadGifModelList(
            String key,
            final int limit,
            final List<GifModel> gifModels,
            final List<GifModel> gifModelPages,
            final GifRecyclerViewAdapter gifRecyclerViewAdapter
    ) {
        gifModels.clear();
        gifModelPages.clear();


        RetrofitInstance.getRetrofitGifInstance().create(GIPHYService.class)
                .getGifResponse(key, limit, "nHbzILQGgqGdO3kjWE8t6mbbV8AExd6N")
                .enqueue(new Callback<GifResponse>() {
                    @Override
                    public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {

                        if (response.body() == null || response.body().pagination.count == 0) {
                            return;
                        } else {
                            if (response.body().data.isEmpty()) return;

                            int numberResults = response.body().pagination.count;

                            List<GifResponse.DataJSON> dataJSONList = response.body().data;

                            for (GifResponse.DataJSON dataJSON : dataJSONList) {
                                {
                                    GifModel gifModel =
                                            new GifModel(
                                                    dataJSON.id,
                                                    dataJSON.title,
                                                    dataJSON.images.fixed_width_small.url,
                                                    dataJSON.images.fixed_width_small.width,
                                                    dataJSON.images.fixed_width_small.height,
                                                    dataJSON.images.original.url
                                            );
                                    gifModels.add(gifModel);
                                    if(gifModels.size() < 12)
                                    {
                                        gifModelPages.add(gifModel);
                                    }
                                    Log.d(TAG, "onResponse: " + gifModel.url );
                                }

                            }
                            gifRecyclerViewAdapter.notifyDataSetChanged();
                        }



                    }

                    @Override
                    public void onFailure(Call<GifResponse> call, Throwable t) {


                    }
                });


    }


}
