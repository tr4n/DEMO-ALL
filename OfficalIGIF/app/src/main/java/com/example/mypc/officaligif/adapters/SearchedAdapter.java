package com.example.mypc.officaligif.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.messages.DataListSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.Utils;

public class SearchedAdapter extends RecyclerView.Adapter<SearchedAdapter.SearchedViewHolder>{

    public DataListSticky dataListSticky;
    public Context context;

    public SearchedAdapter(DataListSticky dataListSticky, Context context) {
        this.dataListSticky = dataListSticky;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_searched_item, null);

        return new SearchedViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedViewHolder holder, int position) {
        holder.setData(dataListSticky, position);
    }

    @Override
    public int getItemCount() {
        return dataListSticky.paperList != null ? dataListSticky.paperList.size() : 0;
    }

    public class SearchedViewHolder extends RecyclerView.ViewHolder{

        Context context;
        View itemView;
        ImageView ivLoadingMedia, ivMedia;
        public SearchedViewHolder(View itemView, Context context) {
            super(itemView);
            this.itemView = itemView;
            this.context = context;
            this.ivLoadingMedia = itemView.findViewById(R.id.iv_loading_media);
            this.ivMedia = itemView.findViewById(R.id.iv_media);
        }

        public void setData(DataListSticky dataListSticky, int position){

            MediaModel mediaModel = dataListSticky.paperList.get(position);

            int width = Integer.parseInt(mediaModel.fixed_width_width);
            int height = Integer.parseInt(mediaModel.fixed_width_height);
            int fixedWidth = Resources.getSystem().getDisplayMetrics().widthPixels >> 1;
            int fixedHeight = (fixedWidth * height ) / (width);
          //  Log.d(TAG, "setData: " + width + " " + height + " " + fixedWidth + " " + fixedHeight);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(fixedWidth, fixedHeight);
            ivMedia.setLayoutParams(layoutParams);
            ivMedia.setVisibility(View.GONE);
            ivLoadingMedia.setLayoutParams(layoutParams);
            ivLoadingMedia.setVisibility(View.VISIBLE);
            ivLoadingMedia.setImageResource(Utils.gerRandomResourceColor());



            Glide.with(context)
                    .load(mediaModel.fixed_width_small_url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            ivLoadingMedia.setVisibility(View.GONE);
                            ivMedia.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(ivMedia);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}