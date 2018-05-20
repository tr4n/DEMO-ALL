package com.example.mypc.demosuper.adapters;

import android.annotation.SuppressLint;
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

import com.example.mypc.demosuper.R;
import com.example.mypc.demosuper.activities.MainActivity;
import com.example.mypc.demosuper.fragments.DetailViewFragment;
import com.example.mypc.demosuper.models.DataPassing;
import com.example.mypc.demosuper.models.FixedHeightGIFModel;
import com.example.mypc.demosuper.models.GifModel;
import com.example.mypc.demosuper.utils.Utils;

import java.util.List;

import butterknife.BindView;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.FixedHeightViewHolder> {

    public List<FixedHeightGIFModel> fixedHeightGIFModelList;
    public Context context;

    public HorizontalRecyclerViewAdapter(List<FixedHeightGIFModel> fixedHeightGIFModelList, Context context) {
        this.fixedHeightGIFModelList = fixedHeightGIFModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public FixedHeightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_view, null);
        return new FixedHeightViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FixedHeightViewHolder holder, int position) {
        fixedHeightGIFModelList.get(position).position = position;
        holder.setData(fixedHeightGIFModelList, position, context);

    }

    @Override
    public int getItemCount() {
        return fixedHeightGIFModelList.size();
    }

    public class FixedHeightViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivGif, ivPreGif;
        private TextView tvTitle;
        private View itemView;
        Context context;

        public FixedHeightViewHolder(View itemView, Context context) {

            super(itemView);
            this.context = context;
            this.itemView = itemView;
            this.ivGif = itemView.findViewById(R.id.iv_gif);
            this.tvTitle = itemView.findViewById(R.id.tv_title);
            this.ivPreGif = itemView.findViewById(R.id.iv_pre_gif);

        }


        public void setData(final List<FixedHeightGIFModel> fixedHeightGIFModelList, final int position , final Context context){
            final FixedHeightGIFModel fixedHeightGIFModel= fixedHeightGIFModelList.get(position);

            int width = Integer.parseInt(fixedHeightGIFModel.width);
            int height = Integer.parseInt(fixedHeightGIFModel.height);

            final int fixedHeight = (int)(Resources.getSystem().getDisplayMetrics().heightPixels*0.2);
            final int fixedWidth = (int)(0.2 * (Resources.getSystem().getDisplayMetrics().widthPixels << 1));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    fixedHeight,
                    fixedWidth
            );
            ivPreGif.setLayoutParams(layoutParams);
            ivGif.setLayoutParams(layoutParams);
            ivGif.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Utils.loadGifUrl(context, fixedHeightGIFModel.urlSmall, ivGif, ivPreGif);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<FixedHeightGIFModel> fixedHeightGIFModels = fixedHeightGIFModelList.subList(position + 1, fixedHeightGIFModelList.size());
                    fixedHeightGIFModels.addAll(fixedHeightGIFModelList.subList(0, position));

                    DataPassing dataPassing = new DataPassing(fixedHeightGIFModel, fixedHeightGIFModels);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data_passing", dataPassing);
                    DetailViewFragment detailViewFragment = new DetailViewFragment();
                    detailViewFragment.setArguments(bundle);

                    Utils.openFragment(
                            ( (MainActivity) context).getSupportFragmentManager(),
                            R.id.cl_fragment,
                            detailViewFragment,
                            "name_fragment"
                    );



                }
            });

        }

    }
}
