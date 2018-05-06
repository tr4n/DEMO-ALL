package com.example.mypc.demofloatingwidget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;

import static android.widget.Toast.makeText;

public class FloatingWidgetService extends Service implements OnClickListener {

    private WindowManager windowManager;
    private View layoutFloating;
    private View layoutClosing;

    private WindowManager.LayoutParams lpFloating;
    private WindowManager.LayoutParams lpClosing;

    private RelativeLayout rlOpenWindow;
    private LinearLayout llParent;
    private boolean isOpening;


    public FloatingWidgetService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Definition();
        Initialization();
        SetupUI();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(layoutFloating != null) windowManager.removeView(layoutFloating);
    }

    private void Definition() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        layoutFloating = LayoutInflater.from(this).inflate(R.layout.floating_layout, null);
        layoutClosing = LayoutInflater.from(this).inflate(R.layout.closing_layout, null);
        rlOpenWindow = layoutFloating.findViewById(R.id.rl_open_window);
        llParent = layoutFloating.findViewById(R.id.ll_parent);


    }


    private void Initialization() {

        isOpening = false;
        // Init Floating View
        lpFloating = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );


        rlOpenWindow.setBackgroundColor(Color.GREEN);
        rlOpenWindow.setVisibility(View.GONE);
        ((ImageView) layoutFloating.findViewById(R.id.iv_floating_icon)).setImageResource(R.drawable.ic_do_not_disturb_on_black_24dp);


        //Init Closing View

        lpClosing = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        lpClosing.gravity = Gravity.BOTTOM;

        ((ImageView) layoutClosing.findViewById(R.id.iv_close)).setImageResource(R.drawable.ic_close_black_24dp);

        //add view on Window
        windowManager.addView(layoutFloating, lpFloating);
        // windowManager.addView(layoutClosingView, lpClosingView);
    }

    private void SetupUI() {

        layoutFloating.findViewById(R.id.ll_parent).setOnTouchListener(new View.OnTouchListener() {

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
                        windowManager.addView(layoutClosing, lpClosing);
                        return true;
                    case MotionEvent.ACTION_UP:

                        long timeClick = Calendar.getInstance().getTimeInMillis() - startClick;
                        if (timeClick < LIMIT_TIME_CLICK) {
                            CustomClicking();
                        }else {
                            if (isNearing(lpFloating.x, lpFloating.y)) {
                                stopSelf();
                            }
                        }

                        windowManager.removeViewImmediate(layoutClosing);

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        lpFloating.x = initialX + (int) (event.getRawX() - initialTouchX);
                        lpFloating.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(layoutFloating, lpFloating);
                        break;
                }

                return false;
            }
        });


    }

    private void CustomClicking() {
        if (isOpening) {
            lpFloating = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );

            rlOpenWindow.setVisibility(View.GONE);
            llParent.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            windowManager.updateViewLayout(layoutFloating, lpFloating);

        } else {
            lpFloating = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
            lpFloating.gravity = Gravity.TOP | Gravity.RIGHT;
            rlOpenWindow.setVisibility(View.VISIBLE);
            llParent.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));

            windowManager.updateViewLayout(layoutFloating, lpFloating);
        }

        isOpening = !isOpening;
    }

    private boolean isNearing(int first, int second) {

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();



        Toast.makeText(FloatingWidgetService.this,width + " " + height + " | " + first+ " " + second  , Toast.LENGTH_SHORT).show();


        return (Math.abs(first) <= 200 && 2* second >= (height - 500));


    }



}



