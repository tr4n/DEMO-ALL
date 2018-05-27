package com.example.mypc.demosuper.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.mypc.demosuper.R;
import com.example.mypc.demosuper.utils.AccountUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class CustomLoginActivity extends AppCompatActivity {

    public static final String TAG = "CustomLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_login);
        Definition();
        Initialization();
    }

    private void Initialization() {
        // Initialize Facebook Login button
        AccountUtils.getKeyHash(getApplicationContext());
    }

    private void Definition() {

    }
}
