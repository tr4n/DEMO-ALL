package com.example.mypc.officaligif.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.Utils;

import java.util.List;

public class FavoriteGridViewAdapter extends BaseAdapter {
    public List<MediaModel> favoriteList;
    public Context context;
    private final int WIDTH_SCREEN = Resources.getSystem().getDisplayMetrics().widthPixels;
    public FavoriteGridViewAdapter(List<MediaModel> favoriteList, Context context) {
        this.favoriteList = favoriteList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return favoriteList.size();
    }

    @Override
    public Object getItem(int position) {
        return favoriteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.layout_favorite_item, null);
        ImageView ivMedia = convertView.findViewById(R.id.iv_favorite_media);
        ImageView ivLoadingMedia = convertView.findViewById(R.id.iv_loading_favorite_media);
        TextView tvTitle = convertView.findViewById(R.id.tv_title_favorite_media);

        MediaModel mediaModel = favoriteList.get(position);
        int width = Integer.parseInt(mediaModel.fixed_width_downsampled_width);
        int height = Integer.parseInt(mediaModel.fixed_width_downsampled_height);
        int fixedWidth = WIDTH_SCREEN >> 1;
        int fixedHeight = (fixedWidth * height) / width;
        ivMedia.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Utils.loadImageUrl(ivLoadingMedia, ivMedia, fixedWidth, fixedWidth, mediaModel.fixed_width_downsampled_url, context);
        tvTitle.setText(mediaModel.title);
        tvTitle.setSelected(true);



        return convertView;
    }
}
