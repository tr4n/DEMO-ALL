package com.example.mypc.demosuper.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mypc.demosuper.R;
import com.example.mypc.demosuper.activities.ExpandedActivity;
import com.example.mypc.demosuper.activities.MainActivity;
import com.example.mypc.demosuper.fragments.MainFragment;
import com.example.mypc.demosuper.utils.Utils;

import java.util.Calendar;

public class FloatingWidgetService extends Service implements View.OnClickListener {

    private WindowManager windowManager;
    private View layoutFloatingWidget;
    private View layoutExitingWidget;
    private WindowManager.LayoutParams lpFloatingWidget, lpExitingWidget;
    private LinearLayout llExpandedView;
    private ImageView ivClosingExpandedView;
    private boolean isExpanded = false;


    public FloatingWidgetService() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(layoutFloatingWidget != null) windowManager.removeView(layoutFloatingWidget);
    }

    private void setupUI() {



        layoutFloatingWidget.findViewById(R.id.iv_floating_icon).setOnTouchListener(new View.OnTouchListener() {

            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private long startClick = 0;
            private final long LIMIT_TIME_CLICK = 125;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = lpFloatingWidget.x;
                        initialY = lpFloatingWidget.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        startClick = Calendar.getInstance().getTimeInMillis();
                        windowManager.addView(layoutExitingWidget, lpExitingWidget);
                        return true;
                    case MotionEvent.ACTION_UP:

                        long timeClick = Calendar.getInstance().getTimeInMillis() - startClick;
                        if (timeClick < LIMIT_TIME_CLICK) {
                            CustomClicking();
                        } else {
                            if (isNearing(lpFloatingWidget.x, lpFloatingWidget.y)) {
                               // Toast.makeText(FloatingWidgetService.this, "true", Toast.LENGTH_SHORT).show();
                                onDestroy();
                            }

                        }

                        windowManager.removeViewImmediate(layoutExitingWidget);

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        lpFloatingWidget.x = initialX + (int) (event.getRawX() - initialTouchX);
                        lpFloatingWidget.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(layoutFloatingWidget, lpFloatingWidget);
                        break;
                }

                return false;
            }
        });

    }

    private boolean isNearing(float first, float second) {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

    //    Toast.makeText(FloatingWidgetService.this, width + " " + height + " | " + first + " " + second, Toast.LENGTH_SHORT).show();


        return (Math.abs(first) <= 200 && 2 * second >= (height - 400));
    }

    private void CustomClicking() {
        if (isExpanded) {
            lpFloatingWidget = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );

            llExpandedView.setVisibility(View.GONE);
            ivClosingExpandedView.setVisibility(View.GONE);

            layoutFloatingWidget.findViewById(R.id.ll_parent).setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            windowManager.updateViewLayout(layoutFloatingWidget, lpFloatingWidget);

        } else {
            /*
            lpFloatingWidget = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
            lpFloatingWidget.gravity = Gravity.TOP | Gravity.RIGHT;
            llExpandedView.setVisibility(View.VISIBLE);



            ivClosingExpandedView.setVisibility(View.VISIBLE);
            layoutFloatingWidget.findViewById(R.id.ll_parent).setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));

            windowManager.updateViewLayout(layoutFloatingWidget, lpFloatingWidget);

*/


            Intent intent = new Intent(FloatingWidgetService.this, ExpandedActivity.class);
            startActivity(intent);
            stopSelf();

        }

        isExpanded = !isExpanded;
    }

    private void Initialization() {
        isExpanded = false;

        // Init Floating Widget View

        lpFloatingWidget = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        ((ImageView) layoutFloatingWidget.findViewById(R.id.iv_floating_icon)).setImageResource(R.drawable.igif);
        ((ImageView) layoutFloatingWidget.findViewById(R.id.iv_close_expanded_view)).setImageResource(R.drawable.ic_close_red_24dp);
        llExpandedView.setVisibility(View.GONE);

        //Init Exiting Widget View

        lpExitingWidget = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        lpExitingWidget.gravity = Gravity.BOTTOM;

        ((ImageView) layoutExitingWidget.findViewById(R.id.iv_exit)).setImageResource(R.drawable.ic_close_red_24dp);

        // add view on Window (only add FloatinWidgetView)

        windowManager.addView(layoutFloatingWidget, lpFloatingWidget);


    }

    private void Definition() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutFloatingWidget = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        layoutExitingWidget = LayoutInflater.from(this).inflate(R.layout.layout_exiting_widget, null);
        llExpandedView = layoutFloatingWidget.findViewById(R.id.ll_expanded_view);
        ivClosingExpandedView = layoutFloatingWidget.findViewById(R.id.iv_close_expanded_view);
    }
}
