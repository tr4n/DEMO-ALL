package com.example.mypc.demoapi.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mypc.demoapi.R;

import java.util.Calendar;

import butterknife.OnClick;

public class FloatingService extends Service implements View.OnClickListener {

    private WindowManager windowManager;
    private View floatingLayout, exitingLayout;
    private LinearLayout llExpandedWindow;
    private ConstraintLayout clParent;
    private boolean isExpanded = false;
    private WindowManager.LayoutParams lpFloating, lpExiting;

    public FloatingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Definition();
        Initialization();
        setupUI();
    }

    private void setupUI() {
        // set On Click for Icon

        floatingLayout.findViewById(R.id.cl_parent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private long startClick = 0;
            private final long LIMIT_TIME_CLICK = 150;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = lpFloating.x;
                        initialY = lpFloating.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        startClick = Calendar.getInstance().getTimeInMillis();
                        windowManager.addView(exitingLayout, lpExiting);
                        return true;
                    case MotionEvent.ACTION_UP:

                        long timeClick = Calendar.getInstance().getTimeInMillis() - startClick;
                        if (timeClick < LIMIT_TIME_CLICK) {
                            CustomClicking();
                        }else {
                            Toast.makeText(FloatingService.this, "" + lpFloating.x + " " + lpFloating.y + " | " + lpExiting.x + " " + lpExiting.y, Toast.LENGTH_SHORT).show();
                            if (isNearing(lpFloating)) {
                                stopSelf();
                            }
                        }

                        windowManager.removeViewImmediate(exitingLayout);

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        lpFloating.x = initialX + (int) (event.getRawX() - initialTouchX);
                        lpFloating.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingLayout, lpFloating);
                        break;
                }

                return false;
            }
        });

    }

    private boolean isNearing(WindowManager.LayoutParams layoutParams) {
        int x = layoutParams.x;
        int y = layoutParams.y;
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();


        return (Math.abs(x) <= 200 && Math.abs(y) >= (height - 300));

    }

    private void CustomClicking() {
        if (isExpanded) {
            lpFloating = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );

            llExpandedWindow.setVisibility(View.GONE);
            clParent.setLayoutParams(new Constraints.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            );

            windowManager.updateViewLayout(floatingLayout, lpFloating);

        } else {
            lpFloating = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
            lpFloating.gravity = Gravity.TOP | Gravity.RIGHT;
            llExpandedWindow.setVisibility(View.VISIBLE);
            clParent.setLayoutParams(new Constraints.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    )
            );

            windowManager.updateViewLayout(floatingLayout, lpFloating);
        }

        isExpanded = !isExpanded;

    }

    private void Initialization() {
        isExpanded = false;

        //Init floating layout
        lpFloating = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        llExpandedWindow.setBackgroundColor(Color.BLACK);
        llExpandedWindow.setVisibility(View.GONE);
        ((ImageView) floatingLayout.findViewById(R.id.iv_floating_icon)).setImageResource(R.drawable.fgificon);

        // Init Exiting layout
        lpExiting = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        lpExiting.gravity = Gravity.BOTTOM;
        ((ImageView) exitingLayout.findViewById(R.id.iv_exit)).setImageResource(R.drawable.ic_close_black_24dp);

        // add layout in window
        windowManager.addView(floatingLayout, lpFloating);



    }

    private void Definition() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        floatingLayout = LayoutInflater.from(this).inflate(R.layout.floating_layout, null);
        exitingLayout = LayoutInflater.from(this).inflate(R.layout.exiting_layout, null);
        llExpandedWindow = floatingLayout.findViewById(R.id.ll_expanded_window);
        clParent = floatingLayout.findViewById(R.id.cl_parent);


    }
}
