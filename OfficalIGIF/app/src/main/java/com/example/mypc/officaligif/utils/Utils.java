package com.example.mypc.officaligif.utils;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.models.ResponseModel;
import com.example.mypc.officaligif.models.ResultResponseListModel;
import com.example.mypc.officaligif.networks.MediaResponse;
import com.example.mypc.officaligif.networks.RetrofitInstance;
import com.example.mypc.officaligif.networks.iGIPHYService;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {

    public static void openFragment(android.support.v4.app.FragmentManager fragmentManager, int layoutID, Fragment fragment, String nameInStack) {

        fragmentManager.beginTransaction()
                .add(layoutID, fragment)
                .addToBackStack(nameInStack)
                .commit();


    }

    public static ResultResponseListModel getResultResponseList(ResponseModel responseModel, final ResultResponseListModel resultResponseListModel, final Context context) {


        RetrofitInstance.getRetrofitGifInstance().create(iGIPHYService.class)
                .getMediaResponses(responseModel.key, responseModel.lang, responseModel.limit, responseModel.api_key)
                .enqueue(new Callback<MediaResponse>() {
                    @Override
                    public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                        if (response.body() == null || response.body().pagination.count == 0 || response.body().data.isEmpty())
                            return;

                        List<MediaResponse.DataJSON> dataJSONList = response.body().data;

                        List<MediaModel> mediaModelList = new ArrayList<>();

                        for (MediaResponse.DataJSON dataJSON : dataJSONList) {
                            MediaModel mediaModel = new MediaModel(
                                    dataJSON.id,
                                    dataJSON.images.original.url,
                                    dataJSON.images.original.width,
                                    dataJSON.images.original.height,
                                    dataJSON.source_tld,
                                    dataJSON.title,
                                    dataJSON.caption,
                                    dataJSON.images.fixed_height.url,
                                    dataJSON.images.fixed_height.width,
                                    dataJSON.images.fixed_height.height,
                                    dataJSON.images.fixed_height_small.url,
                                    dataJSON.images.fixed_height_small.width,
                                    dataJSON.images.fixed_height_small.height,
                                    dataJSON.images.fixed_width.url,
                                    dataJSON.images.fixed_width.width,
                                    dataJSON.images.fixed_width.width,
                                    dataJSON.images.fixed_width_small.url,
                                    dataJSON.images.fixed_width_small.width,
                                    dataJSON.images.fixed_width_small.width,
                                    0
                            );

                            resultResponseListModel.add(mediaModel);

                        }
                        return;

                    }

                    @Override
                    public void onFailure(Call<MediaResponse> call, Throwable t) {
                        Toasty.normal(context, "No Internet!").show();

                    }
                });

        return resultResponseListModel;


    }

}
