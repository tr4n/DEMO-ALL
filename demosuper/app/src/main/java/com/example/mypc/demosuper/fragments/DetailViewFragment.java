package com.example.mypc.demosuper.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypc.demosuper.R;
import com.example.mypc.demosuper.adapters.HorizontalRecyclerViewAdapter;
import com.example.mypc.demosuper.models.DataPassing;
import com.example.mypc.demosuper.models.FixedHeightGIFModel;
import com.example.mypc.demosuper.models.GifModel;
import com.example.mypc.demosuper.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailViewFragment extends Fragment {


    @BindView(R.id.iv_original_gif)
    ImageView ivOriginalGif;
    @BindView(R.id.iv_copy_link)
    ImageView ivCopyLink;
    @BindView(R.id.iv_facebook)
    ImageView ivFacebook;
    @BindView(R.id.iv_messenger)
    ImageView ivMessenger;
    @BindView(R.id.iv_tym)
    ImageView ivTym;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.cl_sharing_items)
    ConstraintLayout clSharingItems;
    @BindView(R.id.rv_relative_items)
    RecyclerView rvRelativeItems;
    Unbinder unbinder;
    @BindView(R.id.iv_pre_original_gif)
    ImageView ivPreOriginalGif;

    ImageView ivOpenRelativeList;

    GifModel gifModel;
    DataPassing dataPassing;
    FixedHeightGIFModel fixedHeightGIFModel;
    List<FixedHeightGIFModel> fixedHeightGIFModelList;
    @BindView(R.id.tv_source)
    TextView tvSource;


    public DetailViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_view, container, false);
        unbinder = ButterKnife.bind(this, view);

        Definition(view);

        Initialization();
        setupUI();


        return view;
    }

    private void Definition(View view) {
        ivOpenRelativeList = view.findViewById(R.id.iv_open_relative_list);
    }

    private void setupUI() {
        dataPassing = (DataPassing) getArguments().getSerializable("data_passing");
        int widthScreen = Resources.getSystem().getDisplayMetrics().widthPixels;
        if (dataPassing.isVertical) {
            gifModel = dataPassing.gifModel;
            int height = Integer.parseInt(gifModel.height);
            int width = Integer.parseInt(gifModel.width);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    widthScreen,
                    (widthScreen * height) / width
            );

            ivOriginalGif.setLayoutParams(layoutParams);
            ivPreOriginalGif.setLayoutParams(layoutParams);
            Utils.loadGifUrl(getContext(), gifModel.originalUrl, ivOriginalGif, ivPreOriginalGif);

        } else {
            fixedHeightGIFModel = dataPassing.fixedHeightGIFModel;
            int height = Integer.parseInt(fixedHeightGIFModel.height);
            int width = Integer.parseInt(fixedHeightGIFModel.width);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    widthScreen,
                    (widthScreen * height) / width
            );

            ivOriginalGif.setLayoutParams(layoutParams);
            ivPreOriginalGif.setLayoutParams(layoutParams);
            Utils.loadGifUrl(getContext(), fixedHeightGIFModel.originalUrl, ivOriginalGif, ivPreOriginalGif);
            tvSource.setText(fixedHeightGIFModel.source_tld.toString().toUpperCase());
        }



        ivOpenRelativeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rvRelativeItems.getVisibility() == View.GONE) {
                    rvRelativeItems.setVisibility(View.VISIBLE);
                    ivOpenRelativeList.setImageResource(R.drawable.ic_expand_more_white_24dp);

                } else {
                    rvRelativeItems.setVisibility(GONE);
                    ivOpenRelativeList.setImageResource(R.drawable.ic_expand_less_white_24dp);

                }

            }
        });

    }

    private void Initialization() {
        dataPassing = (DataPassing) getArguments().getSerializable("data_passing");
        int widthScreen = Resources.getSystem().getDisplayMetrics().widthPixels;
        if (dataPassing.isVertical) {
            gifModel = dataPassing.gifModel;
            int height = Integer.parseInt(gifModel.height);
            int width = Integer.parseInt(gifModel.width);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    widthScreen,
                    (widthScreen * height) / width
            );

            ivOriginalGif.setLayoutParams(layoutParams);
            ivPreOriginalGif.setLayoutParams(layoutParams);
            Utils.loadGifUrl(getContext(), gifModel.originalUrl, ivOriginalGif, ivPreOriginalGif);
        } else {
            fixedHeightGIFModel = dataPassing.fixedHeightGIFModel;
            int height = Integer.parseInt(fixedHeightGIFModel.height);
            int width = Integer.parseInt(fixedHeightGIFModel.width);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    widthScreen,
                    (widthScreen * height) / width
            );

            ivOriginalGif.setLayoutParams(layoutParams);
            ivPreOriginalGif.setLayoutParams(layoutParams);
            Utils.loadGifUrl(getContext(), fixedHeightGIFModel.originalUrl, ivOriginalGif, ivPreOriginalGif);

        }

        fixedHeightGIFModelList = dataPassing.fixedHeightGIFModelList;
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rvRelativeItems.setLayoutManager(staggeredGridLayoutManager);
        rvRelativeItems.setAdapter(new HorizontalRecyclerViewAdapter(fixedHeightGIFModelList, getContext()));
        rvRelativeItems.setHasFixedSize(true);
        rvRelativeItems.setNestedScrollingEnabled(false);
        if (!fixedHeightGIFModelList.isEmpty()) {
            Log.d(TAG, "Initialization:  " + fixedHeightGIFModelList.size());

        } else {
            Toast.makeText(getContext(), "No results ", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_copy_link, R.id.iv_facebook, R.id.iv_messenger, R.id.iv_tym, R.id.iv_download, R.id.cl_sharing_items})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_copy_link:
                String link = gifModel != null ? gifModel.originalUrl : fixedHeightGIFModel.originalUrl;
                if(Utils.copyText(getContext(), link)){
                    ImageView temporaryImageView = new ImageView(getContext());
                    temporaryImageView.setImageResource(R.drawable.ic_content_copy_white_24dp);
                    Toasty.normal(getContext(), "Link is copied", temporaryImageView.getDrawable()).show();
                }else{
                    Toasty.error(getContext(), "Link is empty!").show();
                }



            case R.id.iv_facebook:
                break;
            case R.id.iv_messenger:
                break;
            case R.id.iv_tym:
                break;
            case R.id.iv_download:
                break;
            case R.id.cl_sharing_items:
                break;
        }
    }


}
