package com.example.mypc.demoapi;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.Constraints;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mypc.demoapi.models.GifModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class GifFragment extends Fragment {


    @BindView(R.id.tv_link_copy)
    TextView tvLinkCopy;
    @BindView(R.id.iv_gif_view)
    ImageView ivGifView;
    Unbinder unbinder;

    public GifFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gif, container, false);
        unbinder = ButterKnife.bind(this, view);

        GifModel gifModel = (GifModel) getArguments().getSerializable("gif_model");

        Log.d(TAG, "onCreateView: " + gifModel.linkCopy);
        tvLinkCopy.setText(gifModel.linkCopy);
        int height = Integer.parseInt(gifModel.height);
        int width = Integer.parseInt(gifModel.width);

        int widthPixels = (int) (Resources.getSystem().getDisplayMetrics().widthPixels);

        ivGifView.setLayoutParams(new Constraints.LayoutParams(
                widthPixels,
                (widthPixels * height) / width
        ));

        Glide.with(getContext())
                .asGif()
                .load(gifModel.linkCopy)
                .into(ivGifView);



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
