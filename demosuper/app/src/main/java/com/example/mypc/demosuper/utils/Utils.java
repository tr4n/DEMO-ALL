package com.example.mypc.demosuper.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mypc.demosuper.R;
import com.example.mypc.demosuper.adapters.VerticalRecyclerViewAdapter;
import com.example.mypc.demosuper.models.GifModel;
import com.example.mypc.demosuper.networks.GIPHYService;
import com.example.mypc.demosuper.networks.GifResponse;
import com.example.mypc.demosuper.networks.RetrofitInstance;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Utils {

    private static final String TAG = "Utils";
    private static int[] idColorResource = {R.drawable.blue, R.drawable.bluetwo, R.drawable.bluethree, R.drawable.brown, R.drawable.gray, R.drawable.green, R.drawable.greentwo, R.drawable.pink, R.drawable.purple, R.drawable.red, R.drawable.white, R.drawable.yellow};

    public static void openFragment(android.support.v4.app.FragmentManager fragmentManager, int layoutID, Fragment fragment, String nameInStack) {

            fragmentManager.beginTransaction()
                    .add(layoutID, fragment)
                    .addToBackStack(nameInStack)
                    .commit();


    }

    public static void replaceFragment(android.support.v4.app.FragmentManager fragmentManager, int layoutID, Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(layoutID, fragment)
                .addToBackStack(null)
                .commit();

    }
    public static boolean copyText(Context context, String text) {
        if(text == null) return false;
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
        return true;
    }

    public static void loadGifModelList(
            String key,
            final int limit,
            final List<GifModel> gifModels,
            final List<GifModel> gifModelPages,
            final VerticalRecyclerViewAdapter verticalRecyclerViewAdapter
    ) {
        gifModels.clear();
        gifModelPages.clear();


        RetrofitInstance.getRetrofitGifInstance().create(GIPHYService.class)
                .searchGifResponse(key, limit, "nHbzILQGgqGdO3kjWE8t6mbbV8AExd6N")
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
                                    if (gifModels.size() < 12) {
                                        gifModelPages.add(gifModel);
                                    }
                                    Log.d(TAG, "onResponse: " + gifModel.url);
                                }

                            }
                            verticalRecyclerViewAdapter.notifyDataSetChanged();
                        }


                    }

                    @Override
                    public void onFailure(Call<GifResponse> call, Throwable t) {


                    }
                });


    }

    //auxiliary
    public static void loadGifUrl(Context context, String url, final ImageView targetView, final ImageView temporaryView) {
        if (url == null) return;


        targetView.setVisibility(View.GONE);
        temporaryView.setVisibility(View.VISIBLE);
        temporaryView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        temporaryView.setImageResource(idColorResource[(new Random()).nextInt(idColorResource.length)]);
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        targetView.setVisibility(View.VISIBLE);
                        temporaryView.setVisibility(View.GONE);

                        return false;
                    }
                })
                .into(targetView);
    }


}
