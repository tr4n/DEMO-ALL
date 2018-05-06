package com.example.mypc.tesseractocrdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrawingActivity extends AppCompatActivity {

    private static final String TAG = "DrawingActivity";
    private final int COLOR_PEN = Color.RED;
    private final int SIZE_PEN = 15;
    DrawingView drawingView;

    @BindView(R.id.bt_back)
    ImageButton btBack;
    @BindView(R.id.iv_bitmap)
    ImageView ivBitmap;
    @BindView(R.id.bt_get_text)
    ImageButton btGetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        ButterKnife.bind(this);

        setupUI();
        addDrawingView(null);
    }

    private void setupUI() {


    }

    private void addDrawingView(Bitmap bitmap) {
        LinearLayout linearLayout = findViewById(R.id.ll_draw);

        if (bitmap == null) {
            drawingView = new DrawingView(this, null);

            drawingView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
        } else {
            drawingView = new DrawingView(this, bitmap);

            drawingView.setLayoutParams(new LinearLayout.LayoutParams(
                    bitmap.getWidth(),
                    bitmap.getHeight()));
        }

        linearLayout.addView(drawingView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Bitmap bitmap = ImageUtils.getBitmap(this);
            addDrawingView(bitmap);
        }
    }

    private Bitmap getBitmapView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }


    @OnClick({R.id.bt_back, R.id.iv_bitmap, R.id.bt_get_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                this.finish();
                break;
            case R.id.iv_bitmap:
                break;
            case R.id.bt_get_text:
                Bitmap bitmap = getBitmapView(drawingView);

                if (bitmap == null)
                    Toast.makeText(DrawingActivity.this, "Null", Toast.LENGTH_SHORT).show();
                else {
                    ivBitmap.setImageBitmap(bitmap);
                    Log.d(TAG, "onClick: " + Environment.getExternalStorageDirectory().toString());

                    TessOCR tessOCR = new TessOCR();
                    Toast.makeText(DrawingActivity.this, "" + tessOCR.getOCRResult(bitmap), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
