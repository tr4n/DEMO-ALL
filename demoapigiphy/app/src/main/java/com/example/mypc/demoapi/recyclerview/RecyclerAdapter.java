package com.example.mypc.demoapi.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mypc.demoapi.MainActivity;
import com.example.mypc.demoapi.R;
import com.example.mypc.demoapi.models.GifModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<GifModel> gifModelList = new ArrayList<>();
    Context context;

    public RecyclerAdapter(List<GifModel> gifModelList, Context context) {
        this.gifModelList = gifModelList;
        this.context = context;
    }

    //tao itemView
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_gif_list, parent, false);
        return new ViewHolder(itemView);
    }

    //load data
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(gifModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return gifModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_gif)
        ImageView ivGif;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        //load data ntn?
        public void setData(final GifModel gifModel) {
            Glide.with(context)
                    .asGif()
                    .load(gifModel.url)
                    .into(ivGif);


        }
    }
}