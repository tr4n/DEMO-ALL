package com.example.mypc.officaligif.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.fragments.HomeFragment;
import com.example.mypc.officaligif.messages.DataListSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.models.ResponseModel;
import com.example.mypc.officaligif.networks.MediaResponse;
import com.example.mypc.officaligif.networks.RetrofitInstance;
import com.example.mypc.officaligif.networks.iGIPHYService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Utils {

    public static int[] idColors = {R.drawable.blue, R.drawable.bluetwo, R.drawable.bluethree, R.drawable.brown, R.drawable.gray, R.drawable.green, R.drawable.greentwo, R.drawable.pink, R.drawable.pink, R.drawable.red};


    public static void openFragment(android.support.v4.app.FragmentManager fragmentManager, int layoutID, Fragment fragment) {

        fragmentManager.beginTransaction()
                .add(layoutID, fragment)
                .addToBackStack(null)
                .commit();

    }



    public static void backFragment(FragmentManager fragmentManager){

        if(fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    public static void refreshFragment(FragmentManager fragmentManager){

    }

    public static int gerRandomResourceColor(){
        int position = (new Random()).nextInt(idColors.length);
        return idColors[position];
    }

    public static void exitScreen(FragmentManager fragmentManager){
       while(fragmentManager.getBackStackEntryCount() > 0){
           fragmentManager.popBackStack();
       }

       openFragment(fragmentManager, R.id.cl_home_fragment, new HomeFragment());

    }

    public static void loadImageUrl(final ImageView loadingView, final ImageView view, int width, int height, String url, Context context){

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        loadingView.setLayoutParams(layoutParams);
        view.setLayoutParams(layoutParams);
        loadingView.setImageResource(gerRandomResourceColor());
        loadingView.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);

        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loadingView.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(view);

    }






}
