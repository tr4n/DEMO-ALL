package com.example.mypc.officaligif.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.mypc.officaligif.R;
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

    public static int gerRandomResourceColor(){
        int position = (new Random()).nextInt(idColors.length);
        return idColors[position];
    }






}
