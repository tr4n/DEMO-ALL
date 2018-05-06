package com.example.mypc.tesseractocrdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DrawingModel {

    private DrawingView drawingView;
    private Context context;


    private void addDrawingView(LinearLayout llDraw, Bitmap bitmap) {

        if (bitmap == null) {
            drawingView = new DrawingView(context, (Bitmap) null);
            drawingView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    )
            );
        } else {
            Toast.makeText(context, "add new one", Toast.LENGTH_SHORT).show();
            drawingView = new DrawingView(context, bitmap);
            drawingView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            bitmap.getWidth(),
                            bitmap.getHeight()
                    )
            );
        }

        llDraw.addView(drawingView);
        Toast.makeText(context, "added", Toast.LENGTH_SHORT).show();

    }
}
