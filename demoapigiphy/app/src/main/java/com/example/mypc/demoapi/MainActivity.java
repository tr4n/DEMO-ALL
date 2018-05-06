package com.example.mypc.demoapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mypc.demoapi.networks.GIPHYService;
import com.example.mypc.demoapi.networks.GifResponse;
import com.example.mypc.demoapi.networks.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ImageView ivGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivGif = findViewById(R.id.iv_gif);

        Glide.with(this).asGif().load("https://media.giphy.com/media/JOXHRcd3Llz5m/giphy.gif").into(ivGif);


        GIPHYService giphyService = RetrofitInstance.getRetrofitGifInstance().create(GIPHYService.class);

        giphyService.getGifResponse("hanoi Vietnam","nHbzILQGgqGdO3kjWE8t6mbbV8AExd6N")
                .enqueue(new Callback<GifResponse>() {
                    @Override
                    public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {
                        String url = response.body().data.get(0).images.original.url;
                        Log.d(TAG, "onResponse: " + url);
                        Glide.with(MainActivity.this).asGif().load(url).into(ivGif);
                    }

                    @Override
                    public void onFailure(Call<GifResponse> call, Throwable t) {

                    }
                });


    }
}
