package com.example.mypc.demosuper.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.mypc.demosuper.R;
import com.example.mypc.demosuper.fragments.GifSearchingFragment;
import com.example.mypc.demosuper.fragments.MainFragment;
import com.example.mypc.demosuper.services.FloatingWidgetService;
import com.example.mypc.demosuper.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExpandedActivity extends AppCompatActivity {


    @BindView(R.id.iv_expanded_icon_activity)
    ImageView ivExpandedIconActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_expanded);
        ButterKnife.bind(this);
         getSupportActionBar().hide();

        // getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Utils.openFragment(getSupportFragmentManager(), R.id.cl_fragment, new GifSearchingFragment());

    }


    @OnClick(R.id.iv_expanded_icon_activity)
    public void onViewClicked() {
        Intent intent = new Intent(ExpandedActivity.this, FloatingWidgetService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startService(intent);
        //  android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(0);
        finish();
    }
}
