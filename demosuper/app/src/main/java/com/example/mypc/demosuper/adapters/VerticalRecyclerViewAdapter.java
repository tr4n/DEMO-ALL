package com.example.mypc.demosuper.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.example.mypc.demosuper.activities.MainActivity;
import com.example.mypc.demosuper.fragments.DetailViewFragment;
import com.example.mypc.demosuper.models.DataPassing;
import com.example.mypc.demosuper.models.FixedHeightGIFModel;
import com.example.mypc.demosuper.models.GifModel;
import com.example.mypc.demosuper.R;
import com.example.mypc.demosuper.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VerticalRecyclerViewAdapter extends RecyclerView.Adapter<VerticalRecyclerViewAdapter.FixedWidthViewHolder> {

    public List<GifModel> gifModelList;
    public List<FixedHeightGIFModel> fixedHeightGIFModelList;
    public Context context;

    public VerticalRecyclerViewAdapter(List<GifModel> gifModelList,List<FixedHeightGIFModel> fixedHeightGIFModelList, Context context) {
        this.gifModelList = gifModelList;
        this.context = context;
        this.fixedHeightGIFModelList = fixedHeightGIFModelList;
    }

    @NonNull
    @Override
    public FixedWidthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_view, null);
        return new FixedWidthViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FixedWidthViewHolder holder, int position) {
        gifModelList.get(position).position = position;
        holder.setData(gifModelList,position, fixedHeightGIFModelList);

    }

    @Override
    public int getItemCount() {
        return gifModelList.size();
    }

    public class FixedWidthViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;
        private View itemView;
        public ImageView ivGif;
        private ImageView ivPreGif;
        private TextView tvTitle;

        public FixedWidthViewHolder(View itemView, Context context) {
            super(itemView);
            this.itemView = itemView;
            this.context = context;
            this.ivGif = itemView.findViewById(R.id.iv_gif);
            this.tvTitle = itemView.findViewById(R.id.tv_title);
            this.ivPreGif = itemView.findViewById(R.id.iv_pre_gif);


        }

        public void setData(List<GifModel> gifModelList, int position, final List<FixedHeightGIFModel> fixedHeightGIFModelList) {

            final GifModel gifModel = gifModelList.get(position);


            int width = Integer.parseInt(gifModel.width);
            int height = Integer.parseInt(gifModel.height);
            final int fixedWidth = Resources.getSystem().getDisplayMetrics().widthPixels >> 1;
            final int fixedHeight = (fixedWidth * height) / width;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    fixedWidth,
                    fixedHeight
            );
            ivPreGif.setLayoutParams(layoutParams);
            ivGif.setLayoutParams(layoutParams);

           Utils.loadGifUrl(context, gifModel.url, ivGif, ivPreGif);

            tvTitle.setText("" + gifModel.position);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DataPassing dataPassing = new DataPassing(gifModel, fixedHeightGIFModelList);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data_passing", dataPassing);
                    DetailViewFragment detailViewFragment = new DetailViewFragment();
                    detailViewFragment.setArguments(bundle);

                    Utils.openFragment(
                            ( (MainActivity) context).getSupportFragmentManager(),
                            R.id.cl_fragment,
                            detailViewFragment,
                            "main_fragment"
                    );



                }
            });
        }


        @Override
        public void onClick(View v) {

        }


    }


}


