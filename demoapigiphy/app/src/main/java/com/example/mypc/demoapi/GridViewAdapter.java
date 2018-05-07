package com.example.mypc.demoapi;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mypc.demoapi.models.GifModel;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    public List<GifModel> gifModelList;


    public GridViewAdapter(List<GifModel> gifModelList) {
        this.gifModelList = gifModelList;

    }

    @Override
    public int getCount() {
        return gifModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return gifModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GifModel gifModel = (GifModel) getItem(position);
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gif_list, parent, false);
        ImageView ivGif = convertView.findViewById(R.id.iv_gif);





        Glide.with(parent.getContext())
                .asGif()
                .load(gifModel.url)
                .into(ivGif);


        return convertView;


    }

}
