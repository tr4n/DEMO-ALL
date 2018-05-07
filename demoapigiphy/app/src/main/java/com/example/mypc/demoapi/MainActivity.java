package com.example.mypc.demoapi;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mypc.demoapi.models.GifModel;
import com.example.mypc.demoapi.networks.GIPHYService;
import com.example.mypc.demoapi.networks.GifResponse;
import com.example.mypc.demoapi.networks.RetrofitInstance;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.gv_items)
    GridView gvItems;
    @BindView(R.id.cl_view_items)
    ConstraintLayout clViewItems;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;


    private List<GifModel> gifModelList = new ArrayList<>();

    private GridViewAdapter gridViewAdapter;
    private final String[] key_words = {
            " ",
            " facebook",
            " instagram",
            " zalo",
            " a",
            " b",
            " c",
            " d",
            " e",
            " f",
            " g",
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        gridViewAdapter = new GridViewAdapter(gifModelList);

        gvItems.setAdapter(gridViewAdapter);
        searchKey("beautiful girl idols");




        Log.d(TAG, "onCreate: " + gifModelList);

    }

    private void loadData(String keySearch) {
        avi.show();
        GIPHYService giphyService = RetrofitInstance.getRetrofitGifInstance().create(GIPHYService.class);

        giphyService.getGifResponse(keySearch, "nHbzILQGgqGdO3kjWE8t6mbbV8AExd6N")
                .enqueue(new Callback<GifResponse>() {
                    @Override
                    public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {
                        avi.hide();
                        if (response.body() == null) {
                            return;
                        } else {
                            if (response.body().data.isEmpty()) return;

                            List<GifResponse.Data> dataList = response.body().data;


                            for (GifResponse.Data data : dataList) {
                                GifModel gifModel =
                                        new GifModel(
                                                data.title,
                                                data.images.fixed_width.url,
                                                data.images.fixed_width.width,
                                                data.images.fixed_width.height
                                        );
                                gifModelList.add(gifModel);
                                /*
                                Glide.with(MainActivity.this)
                                        .asGif()
                                        .load(gifModel.url)
                                        .preload();
                                 */
                                gridViewAdapter.notifyDataSetChanged();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<GifResponse> call, Throwable t) {

                    }
                });


    }

    @OnClick({R.id.iv_search, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                gifModelList.clear();
                searchKey(etSearch.getText().toString());



                break;
            case R.id.iv_back:
                break;

        }
    }

    private void searchKey(String key){
        gifModelList.clear();
        for(int i = 0 ;i < key_words.length ;i ++)
            loadData(key + key_words[i]);
    }
}
