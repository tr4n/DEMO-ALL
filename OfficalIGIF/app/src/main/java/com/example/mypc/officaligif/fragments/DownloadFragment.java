package com.example.mypc.officaligif.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.adapters.DownloadGridViewAdapter;
import com.example.mypc.officaligif.utils.DownloadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.facebook.internal.FacebookDialogFragment.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {


    @BindView(R.id.gv_downloads)
    GridView gvDownloads;
    Unbinder unbinder;

    List<File> downloadList = new ArrayList<>();

    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        unbinder = ButterKnife.bind(this, view);
        Definition();
        Initialization();
        setupUI();



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void Definition(){

    }

    public void Initialization(){
        downloadList = DownloadUtils.getInstance(getContext()).getDownloadedFile(getContext());
        gvDownloads.setAdapter(new DownloadGridViewAdapter(downloadList, getContext()));
    }
    public void setupUI(){

    }
}
