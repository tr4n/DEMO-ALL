package com.example.mypc.demosuper.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mypc.demosuper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StickerSearchingFragment extends Fragment {


    public StickerSearchingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sticker_searching, container, false);
    }

}