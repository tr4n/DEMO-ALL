package com.example.mypc.demosuper.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Constraints;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mypc.demosuper.models.GifModel;
import com.example.mypc.demosuper.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import javax.xml.transform.Transformer;

public class GifRecyclerViewAdapter extends RecyclerView.Adapter<GifRecyclerViewAdapter.GifViewHolder> {

    public List<GifModel> gifModelList;
    public Context context;
    private int[] colors = {Color.RED, Color.BLACK, Color.GREEN, Color.GRAY, Color.YELLOW, Color.CYAN, Color.WHITE, Color.MAGENTA, Color.DKGRAY, Color.LTGRAY};
    private int[] idColorResource = {R.drawable.black, R.drawable.blue, R.drawable.bluetwo, R.drawable.bluethree, R.drawable.brown, R.drawable.gray, R.drawable.green, R.drawable.greentwo, R.drawable.pink, R.drawable.purple, R.drawable.red, R.drawable.white, R.drawable.yellow};

    public GifRecyclerViewAdapter(List<GifModel> gifModelList, Context context) {
        this.gifModelList = gifModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public GifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_view, null);
        return new GifViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull GifViewHolder holder, int position) {
        gifModelList.get(position).position = position;
        holder.setData(gifModelList.get(position));

    }

    @Override
    public int getItemCount() {
        return gifModelList.size();
    }

    public class GifViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;
        private View itemView;
        public ImageView ivGif;
        private TextView tvTitle;
        private GifDrawable gifDrawable;

        public GifViewHolder(View itemView, Context context) {
            super(itemView);
            this.itemView = itemView;
            this.context = context;
            this.ivGif = itemView.findViewById(R.id.iv_gif);
            this.tvTitle = itemView.findViewById(R.id.tv_title);

        }

        public void setData(final GifModel gifModel) {

            int width = Integer.parseInt(gifModel.width);
            int height = Integer.parseInt(gifModel.height);
            final int fixedWidth = Resources.getSystem().getDisplayMetrics().widthPixels >> 1;
            final int fixedHeight = (fixedWidth * height) / width;
            ivGif.setLayoutParams(new Constraints.LayoutParams(fixedWidth, fixedHeight));
            ivGif.setScaleType(ImageView.ScaleType.CENTER_CROP);
          //  ivGif.setPadding(8,8,8,8);

            ivGif.setImageResource(idColorResource[(new Random()).nextInt(idColorResource.length)]);
            CountDownTimer countDownTimer = new CountDownTimer(500, 250) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (gifModel.url != null) {
                        Glide.with(context)
                                .asGif()
                                .load(gifModel.url)
                                .into(ivGif);

                    }

                }
            }.start();


            tvTitle.setText("" + gifModel.position);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Clicked " + gifModel.position, Toast.LENGTH_SHORT).show();
                }
            });
        }


        @Override
        public void onClick(View v) {

        }

        private void loadGif( GifModel gifModel, ImageView imageView){



        }
    }
}


