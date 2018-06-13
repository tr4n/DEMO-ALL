package com.example.mypc.officaligif.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.adapters.RelatedAdapter;
import com.example.mypc.officaligif.database_dir.TopicDatabaseManager;
import com.example.mypc.officaligif.messages.BackSticky;
import com.example.mypc.officaligif.messages.DataListSticky;
import com.example.mypc.officaligif.messages.MediaSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFragment extends Fragment {

    private final int WIDTH_SCREEN = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int HEIGHT_SCREEN = Resources.getSystem().getDisplayMetrics().heightPixels;


    @BindView(R.id.tv_title_media)
    TextView tvTitleMedia;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_loading_sharing_media)
    ImageView ivLoadingSharingMedia;
    @BindView(R.id.iv_sharing_media)
    ImageView ivSharingMedia;
    @BindView(R.id.tv_source)
    TextView tvSource;
    @BindView(R.id.iv_copy_link)
    ImageView ivCopyLink;
    @BindView(R.id.iv_facebook)
    ImageView ivFacebook;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.iv_download)
    ImageView ivDownload;

    Unbinder unbinder;


    @BindView(R.id.rl_related_title)
    RelativeLayout rlRelatedTitle;
    @BindView(R.id.iv_expanded)
    ImageView ivExpanded;
    @BindView(R.id.rv_related_items)
    RecyclerView rvRelatedItems;
    boolean isExpanded = false;
    int classID = 0;
    String titleFragment;
    MediaModel mediaModel;
    DataListSticky dataListSticky;
    View infalteView;
    Boolean favoriteMedia ;


    public ShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        infalteView = inflater.inflate(R.layout.fragment_share, container, false);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, infalteView);
        Definition();
        Initialization();
        setupUI();


        return infalteView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMediaSticky(MediaSticky mediaSticky) {
        mediaModel = mediaSticky.mediaModel;
        dataListSticky = mediaSticky.dataListSticky;
        dataListSticky.initializeRelatedList();
        classID = mediaSticky.classID;
        titleFragment = mediaSticky.title;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_back, R.id.iv_copy_link, R.id.iv_facebook, R.id.iv_favorite, R.id.iv_download, R.id.rl_related_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                Utils.backFragment(getFragmentManager(), classID);
                break;
            case R.id.iv_copy_link:
                Utils.copyClipBoard(mediaModel.original_url, getContext());
                ImageView temporaryView = new ImageView(getContext());
                temporaryView.setImageResource(R.drawable.ic_content_copy_white_24dp);
                Toasty.normal(getContext(),"Copied link", temporaryView.getDrawable()).show();
                break;
            case R.id.iv_facebook:
                break;
            case R.id.iv_favorite:
                if(TopicDatabaseManager.getInstance(getContext()).addFavoriteItem(mediaModel)){
                    ivFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                    Toasty.normal(getContext(), "^^").show();
                }else{

                }
                break;
            case R.id.iv_download:
                break;
            case R.id.rl_related_title:
                setExpanedRelatedList(!isExpanded);
                break;
        }
    }

    private void Definition() {
        EventBus.getDefault().postSticky(new BackSticky(classID));

    }

    private void Initialization() {


        setExpanedRelatedList(false);
        if(titleFragment.contains(" GIF")){
            titleFragment = titleFragment.split("GIF")[0].trim();
        }

        tvTitleMedia.setText(titleFragment);
        tvTitleMedia.setSelected(true);
        tvSource.setText("SOURCE: " + mediaModel.source_tld);

        int width = Integer.parseInt(mediaModel.original_width);
        int height = Integer.parseInt(mediaModel.original_height);
        int fixedWidth = WIDTH_SCREEN;
        int fixedHeight = (fixedWidth * height) / width;
        Utils.loadImageUrl(ivLoadingSharingMedia, ivSharingMedia, fixedWidth, fixedHeight, mediaModel.fixed_width_url, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RelatedAdapter relatedAdapter = new RelatedAdapter(dataListSticky,classID, getContext());
        rvRelatedItems.setLayoutManager(linearLayoutManager);
        rvRelatedItems.setAdapter(relatedAdapter);
    }

    private void setupUI() {
        favoriteMedia = TopicDatabaseManager.getInstance(getContext()).inFavoriteList(mediaModel);
        if(favoriteMedia){
            ivFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
        }else{
            ivFavorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }

    }

    private void setExpanedRelatedList(boolean expanded){
        isExpanded = expanded;
        if(expanded){
            rvRelatedItems.setVisibility(View.VISIBLE);
            ivExpanded.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
        }else{
            rvRelatedItems.setVisibility(View.GONE);
            ivExpanded.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                   Utils.backFragment(getFragmentManager(), classID);
                    return true;
                }
                return false;
            }
        });
    }
}
