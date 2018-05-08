package com.example.mypc.demoapi;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mypc.demoapi.models.GifModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static android.support.constraint.Constraints.TAG;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    public List<GifModel> gifModelList = new ArrayList<>();
    public Context context;
    private int[] colors = {Color.RED, Color.BLACK, Color.GREEN, Color.GRAY, Color.YELLOW, Color.CYAN, Color.WHITE, Color.MAGENTA, Color.DKGRAY, Color.LTGRAY};

    public RecyclerAdapter(List<GifModel> gifModelList, Context context) {
        this.gifModelList = gifModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_gif_list, parent, false);

        return new RecyclerViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.setData(gifModelList.get(position), this.context);
    }

    @Override
    public int getItemCount() {
        return gifModelList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivGif;
        private Context context;

        public RecyclerViewHolder(View itemView, final Context context) {
            super(itemView);
            ivGif = (ImageView) itemView.findViewById(R.id.iv_gif);
            this.context = context;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: recycler view item" );
                    Utils.openFragment(
                            ( (MainActivity) context).getSupportFragmentManager(),
                            R.id.rv_items,
                            new GifFragment()
                    );

                    Toast.makeText(context, "abcxyz", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick:after open" );
                }
            });
        }


        public void setData(final GifModel gifModel, final Context context) {
            int height = Integer.parseInt(gifModel.height);
            int width = Integer.parseInt(gifModel.width);

            int widthPixels = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.5);

            ivGif.setLayoutParams(new RelativeLayout.LayoutParams(
                    widthPixels,
                    (widthPixels * height) / width
            ));

            ivGif.setBackgroundColor(colors[(new Random()).nextInt(colors.length)]);

            new CountDownTimer(500, 250) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Glide.with(context)
                            .asGif()
                            .load(gifModel.url)
                            .into(ivGif);
                }

                @Override
                public void onFinish() {
                    ivGif.setBackgroundColor(Color.BLACK);
                }
            }.start();


        }

        @Override
        public void onClick(View v) {

        }
    }
}
