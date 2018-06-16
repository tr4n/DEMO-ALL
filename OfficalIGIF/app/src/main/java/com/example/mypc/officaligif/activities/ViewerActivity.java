package com.example.mypc.officaligif.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.database_dir.TopicDatabaseManager;
import com.example.mypc.officaligif.messages.ViewSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.DownloadUtils;
import com.example.mypc.officaligif.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class ViewerActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title_media)
    TextView tvTitleMedia;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.iv_copy_link)
    ImageView ivCopyLink;
    @BindView(R.id.iv_facebook)
    ImageView ivFacebook;
    @BindView(R.id.iv_messenger)
    ImageView ivMessenger;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.giv_media)
    GifImageView givMedia;
    @BindView(R.id.ll_share_buttons)
    LinearLayout llShareButtons;

    ViewSticky viewSticky;
    @BindView(R.id.iv_loading_media)
    ImageView ivLoadingMedia;

    MediaModel mediaModel;
    @BindView(R.id.iv_zoom)
    ImageView ivZoom;
    ScaleGestureDetector scaleGestureDetector;

    boolean isZoomed = false;
    int width =200;
    int height = 200;
    final int widthScreen = Resources.getSystem().getDisplayMetrics().widthPixels;
    final int heightScreen = Resources.getSystem().getDisplayMetrics().heightPixels;
    @BindView(R.id.ll_viewer)
    LinearLayout llViewer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viewer);
        getSupportActionBar().hide();
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        Definition();
        Initialization();
        setupUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getViewSticky(ViewSticky viewSticky1) {
        viewSticky = viewSticky1;
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void Definition() {
        isZoomed = false;
        scaleGestureDetector = new ScaleGestureDetector(this, new MyGesture());
    }

    public void Initialization() {
        if (viewSticky != null) {
            if (viewSticky.TAG == 1) {
                llShareButtons.setVisibility(View.GONE);
                File file = viewSticky.file;
                String path = file.getAbsolutePath().toString();
                width = Integer.parseInt(file.getName().split("---")[0]);
                height = Integer.parseInt(file.getName().split("---")[1]);

                try {

                    givMedia.setImageDrawable(new GifDrawable(path));
                    givMedia.setLayoutParams(getZoomLayoutParams(width, height, false));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                llShareButtons.setVisibility(View.VISIBLE);
                mediaModel = viewSticky.mediaModel;
                width = Integer.parseInt(mediaModel.original_width);
                height = Integer.parseInt(mediaModel.original_height);
                LinearLayout.LayoutParams layoutParams = getZoomLayoutParams(width, height, false);
                tvTitleMedia.setText(Utils.getBaseString(mediaModel.title));
                DownloadUtils.getInstance(this).load(givMedia, ivLoadingMedia, layoutParams.width, layoutParams.height, mediaModel, this);
            }
        }

    }

    public void setupUI() {
        llViewer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }


    @OnClick({R.id.iv_copy_link, R.id.iv_facebook, R.id.iv_messenger, R.id.iv_download, R.id.iv_favorite, R.id.iv_zoom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_copy_link:
                Utils.copyClipBoard(mediaModel.original_url, this);
                Toasty.normal(ViewerActivity.this, "Copied link", Utils.getDrawableResource(R.drawable.ic_content_copy_white_24dp, this)).show();
                break;
            case R.id.iv_facebook:
                Utils.shareFacebook(mediaModel, ViewerActivity.this);
            case R.id.iv_messenger:
                Utils.shareMessenger(mediaModel, ViewerActivity.this);
                break;
            case R.id.iv_download:
                DownloadUtils.getInstance(this).downloadMedia(mediaModel, this);
                break;
            case R.id.iv_favorite:
                if (TopicDatabaseManager.getInstance(this).inFavoriteList(mediaModel)) {
                    ivFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                    break;
                } else if (viewSticky.TAG == 3) {
                    if (TopicDatabaseManager.getInstance((this)).addFavoriteItem(mediaModel)) {
                        ivFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                        Toasty.normal(ViewerActivity.this, "", Utils.getDrawableResource(R.drawable.ic_favorite_white_24dp, this)).show();
                    }
                }
                break;
            case R.id.iv_zoom:
                if (givMedia.getVisibility() == View.GONE) break;
                if (isZoomed) {

                    givMedia.setLayoutParams(getZoomLayoutParams(width, height, false));
                    isZoomed = false;
                    ivZoom.setImageResource(R.drawable.ic_zoom_in_white_24dp);
                } else {
                    givMedia.setLayoutParams(getZoomLayoutParams(width, height, true));
                    isZoomed = true;
                    ivZoom.setImageResource(R.drawable.ic_zoom_out_white_24dp);
                }
                break;

        }
    }


    public LinearLayout.LayoutParams getZoomLayoutParams(int widthZ, int heightZ, boolean zoom) {
        int fixedWidth = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.8);
        int fixedHeight = (heightZ * fixedWidth) / widthZ;
        return (!zoom && widthZ < fixedWidth) ? new LinearLayout.LayoutParams(widthZ, heightZ) : new LinearLayout.LayoutParams(fixedWidth, fixedHeight);

    }


    class MyGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        float scale = 1.0F;


        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
           if(width*scale > widthScreen || height * heightScreen > heightScreen) return false;
            givMedia.setLayoutParams(new LinearLayout.LayoutParams((int)(width*scale), (int)(height*scale)));

            return true;
           // return super.onScale(detector);
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
           // givMedia.setLayoutParams(layoutParams);
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
                  super.onScaleEnd(detector);
        }
    }
}
