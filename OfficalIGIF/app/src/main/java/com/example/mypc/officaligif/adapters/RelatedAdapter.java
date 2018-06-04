package com.example.mypc.officaligif.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.Utils;

import java.util.List;

public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.RelatedViewHolder> {

    public List<MediaModel> relatedMediaList;
    public Context context;

    public RelatedAdapter(List<MediaModel> relatedMediaList, Context context) {
        this.relatedMediaList = relatedMediaList;
        this.context = context;
    }

    @NonNull
    @Override
    public RelatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_searched_item, null);
        return new RelatedViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedViewHolder holder, int position) {
        holder.setData(relatedMediaList.get(position));
    }

    @Override
    public int getItemCount() {
        return relatedMediaList.size();
    }

    public class RelatedViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public View itemView;
        public ImageView ivLoading, ivMedia;


        public RelatedViewHolder(View itemView, Context context) {
            super(itemView);
            this.itemView = itemView ;
            this.context = context;
            this.ivLoading = itemView.findViewById(R.id.iv_loading_media);
            this.ivMedia = itemView.findViewById(R.id.iv_media);
        }

        public void setData(MediaModel mediaModel){
            int width = Integer.parseInt(mediaModel.original_width);
            int height = Integer.parseInt(mediaModel.original_height);
            int fixedWidth =  (int) (Resources.getSystem().getDisplayMetrics().widthPixels*0.4);
            int fixedHeight = (int) (Resources.getSystem().getDisplayMetrics().heightPixels*0.2);
            ivMedia.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Utils.loadImageUrl(ivLoading, ivMedia,fixedWidth, fixedHeight,mediaModel.fixed_height_url, context );



        }


    }

}
