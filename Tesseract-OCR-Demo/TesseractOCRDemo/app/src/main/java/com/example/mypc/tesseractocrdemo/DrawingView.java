package com.example.mypc.tesseractocrdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.nio.file.Path;

import static android.content.ContentValues.TAG;

public class DrawingView extends View {

    private android.graphics.Path path;
    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;

    public DrawingView(Context context, Bitmap bitmap) {
        super(context);

        canvas = new Canvas();
        path = new android.graphics.Path();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(15);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        this.bitmap = bitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.WHITE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                path.moveTo(event.getX(), event.getY());

                paint.setColor(Color.RED);
                paint.setStrokeWidth(15);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                path.lineTo(event.getX(), event.getY());
                break;
            }
            case MotionEvent.ACTION_UP: {
                canvas.drawPath(path, paint);

               //  path.reset();
                break;
            }
        }
        invalidate();

        return true;

    }

    public void resetBitmap(){
        path.reset();
    }
}
