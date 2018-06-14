package com.example.mypc.officaligif.utils;

import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mypc.officaligif.models.MediaModel;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static org.greenrobot.eventbus.EventBus.TAG;

public class DownloadUtils {

    public Context context;
    private static DownloadUtils downloadUtils;

    public static DownloadUtils getInstance(Context context) {
        if (downloadUtils == null) {
            downloadUtils = new DownloadUtils(context);
        }
        return downloadUtils;
    }

    public DownloadUtils(Context context) {
        this.context = context;
    }

    public void downloadMedia(MediaModel mediaModel, final Context context) {

        File folder = new File(context.getExternalCacheDir() + "/iGIF/downloads");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File downloadFile = new File(folder, mediaModel.id + ".gif");
        if (downloadFile.exists()) {
            Toast.makeText(context, "File have already downloaded", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri downloadUri = Uri.parse(mediaModel.original_url);
        Uri destinationUri = Uri.parse(context.getExternalCacheDir() + "/iGIF/downloads/" + mediaModel.id + ".gif");
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setDownloadContext(context)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        Toast.makeText(context, "Complete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {

                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {

                    }
                });
        Log.d(TAG, "downloadMedia: predownload");
        new ThinDownloadManager().add(downloadRequest);
        Log.d(TAG, "downloadMedia: " + "download ThinDownLoadManager");
    }

    public void load(final GifImageView gifImageView, final ImageView loadingView, final int width, final int height, final MediaModel mediaModel, final Context context) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        loadingView.setLayoutParams(layoutParams);
        gifImageView.setLayoutParams(layoutParams);
        loadingView.setImageResource(Utils.gerRandomResourceColor());
        loadingView.setVisibility(View.VISIBLE);
        gifImageView.setVisibility(View.GONE);


        File folder = new File(context.getExternalCacheDir() + "/iGIF/preLoading");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File downloadFile = new File(folder, mediaModel.id + ".gif");
        if (downloadFile.exists()) {
            try {
                gifImageView.setImageDrawable(new GifDrawable(context.getExternalCacheDir() + "/iGIF/preLoading/" + mediaModel.id + ".gif"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            gifImageView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
            return;
        }

        Uri downloadUri = Uri.parse(mediaModel.original_url);
        Uri destinationUri = Uri.parse(context.getExternalCacheDir() + "/iGIF/preLoading/" + mediaModel.id + ".gif");
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setDownloadContext(context)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        try {
                            gifImageView.setImageDrawable(new GifDrawable(context.getExternalCacheDir() + "/iGIF/preLoading/" + mediaModel.id + ".gif"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gifImageView.setVisibility(View.VISIBLE);
                        loadingView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {

                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {

                    }
                });
        new ThinDownloadManager().add(downloadRequest);
    }

    public List<File> getDownloadedFile(Context context) {
        List<File> fileList = new ArrayList<>();
        File folder = new File(context.getExternalCacheDir() + "/iGIF/downloads");
        if (!folder.exists()) folder.mkdirs();
        if (folder.listFiles() == null) return null;
        for (File file : folder.listFiles()) {
            Log.d(TAG, "getDownloadedFile: " + file.getName().toString());
            if (file.getName().toString().contains(".gif")) {
                fileList.add(file);
            }
        }
        return fileList;
    }
}