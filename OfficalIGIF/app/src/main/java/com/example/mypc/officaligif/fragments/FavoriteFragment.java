package com.example.mypc.officaligif.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.messages.BackSticky;

import org.greenrobot.eventbus.EventBus;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    View fragmentView ;


    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         fragmentView = inflater.inflate(R.layout.fragment_favorite, container, false);

        Definition();
        Initialization();
        setupUI();

        return fragmentView;
    }



    private void Definition(){
        EventBus.getDefault().postSticky(new BackSticky(0));
    };

    private void Initialization(){

    }

    private void setupUI(){

    }



}
