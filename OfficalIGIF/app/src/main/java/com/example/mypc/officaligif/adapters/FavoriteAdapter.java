package com.example.mypc.officaligif.adapters;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.activities.MainActivity;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.Utils;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    List<MediaModel> favoriteList;
    Context context;

    public FavoriteAdapter(List<MediaModel> favoriteList, Context context) {
        this.favoriteList = favoriteList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_favorite_item, null);
        return new FavoriteViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.setData(favoriteList, position);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMedia;
        ImageView ivLoadingMedia;
        TextView tvTitle;
        View itemView;
        Context context;
        int WIDTH_SCREEN = Resources.getSystem().getDisplayMetrics().widthPixels;

        public FavoriteViewHolder(View itemView, Context context) {
            super(itemView);
            this.itemView = itemView;
            this.context = context;
            ivMedia = itemView.findViewById(R.id.iv_favorite_media);
            ivLoadingMedia = itemView.findViewById(R.id.iv_loading_favorite_media);
            tvTitle = itemView.findViewById(R.id.tv_title_favorite_media);
        }

        public void setData(List<MediaModel> favoriteList, int position) {
            MediaModel mediaModel = favoriteList.get(position);
            int width = Integer.parseInt(mediaModel.fixed_width_downsampled_width);
            int height = Integer.parseInt(mediaModel.fixed_width_downsampled_height);
            int fixedWidth = WIDTH_SCREEN >> 1;
            int fixedHeight = (fixedWidth * height) / width;


            Utils.loadImageUrl(ivLoadingMedia, ivMedia, fixedWidth, fixedHeight, mediaModel.fixed_width_downsampled_url, context);
            tvTitle.setText(mediaModel.title);
            tvTitle.setSelected(true);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    ImageView temporaryIcon = new ImageView(context);
                    temporaryIcon.setImageResource(R.drawable.bubbletrash);
                    builder.setIcon(temporaryIcon.getDrawable())
                            .setTitle("Remove from favorites")
                            .setMessage("Do you want to remove this item from your favorites?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(context, "removed! ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                    return true;
                }
            });
        }
    }

}
