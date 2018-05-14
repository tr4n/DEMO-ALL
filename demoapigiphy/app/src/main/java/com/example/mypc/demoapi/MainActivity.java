package com.example.mypc.demoapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mypc.demoapi.models.GifModel;
import com.example.mypc.demoapi.networks.GIPHYService;
import com.example.mypc.demoapi.networks.GifResponse;
import com.example.mypc.demoapi.networks.RetrofitInstance;
import com.example.mypc.demoapi.services.FloatingService;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.GridLayout.VERTICAL;
import static com.example.mypc.demoapi.R.id.cl_view_items;
import static com.example.mypc.demoapi.R.id.tv_next;
import static com.example.mypc.demoapi.R.id.tv_number_results;
import static com.example.mypc.demoapi.R.id.tv_previous;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String LANGUAGE = "vie";
    private  int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;

    Fragment fragmentGif;

    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(cl_view_items)
    ConstraintLayout clViewItems;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    TextView tvCount;
    RecyclerView rvItems;
    RecyclerAdapter rvAdapter;
    @BindView(R.id.tv_number_results)
    TextView tvNumberResults;
    @BindView(R.id.tv_previous)
    TextView tvPrevious;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_page)
    TextView tvPage;
    private int currentPage = 0;
    private int maxPages = 0;
    private final int MAX_ITEMS_PAGE = 12;


    private List<GifModel> gifModelList = new ArrayList<>();
    private List<GifModel> subGifList = new ArrayList<>();
    private EndlessScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        askPermission();
        Definition();
        Initialization();
        setRecyclerView();
        changeFragment();
        OnFloatingIcon();

    }

    private void OnFloatingIcon() {

    }

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));

        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }


    private void Initialization() {
        currentPage = 0;
    }

    private void Definition() {
        rvItems = findViewById(R.id.rv_items);
        tvCount = (TextView) findViewById(tv_number_results);


    }

    private void loadData(final String keySearch) {
        final int LIMIT_RESULTS = 200;
        avi.show();
        tvCount.setText("loading . . .");
        GIPHYService giphyService = RetrofitInstance.getRetrofitGifInstance().create(GIPHYService.class);

        giphyService.getGifResponse(keySearch, LANGUAGE, LIMIT_RESULTS, "nHbzILQGgqGdO3kjWE8t6mbbV8AExd6N")
                .enqueue(new Callback<GifResponse>() {
                    @Override
                    public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {
                        avi.hide();

                        if (response.body() == null || response.body().pagination.count == 0) {
                            tvCount.setText("There are no any result for " + keySearch);
                            return;
                        } else {
                            if (response.body().data.isEmpty()) return;

                            int numberResults = response.body().pagination.count;
                            if (numberResults < LIMIT_RESULTS)
                                tvCount.setText(" There are " + numberResults + " results for " + keySearch);
                            else
                                tvCount.setText(" There are more " + numberResults + " results");

                            List<GifResponse.DataJSON> dataJSONList = response.body().data;

                            for (GifResponse.DataJSON dataJSON : dataJSONList) {
                                {
                                    GifModel gifModel =
                                            new GifModel(
                                                    dataJSON.id,
                                                    dataJSON.title,
                                                    dataJSON.images.fixed_width_small.url,
                                                    dataJSON.images.fixed_width_small.width,
                                                    dataJSON.images.fixed_width_small.height,
                                                    dataJSON.images.original.url
                                            );
                                    gifModelList.add(gifModel);
                                    if (gifModelList.size() <= MAX_ITEMS_PAGE) {
                                        subGifList.add(gifModel);
                                    }
                                }
                                rvAdapter.notifyDataSetChanged();

                                maxPages = gifModelList.isEmpty() ? 0
                                        : gifModelList.size() % MAX_ITEMS_PAGE == 0 ? gifModelList.size() / MAX_ITEMS_PAGE
                                        : gifModelList.size() / MAX_ITEMS_PAGE + 1;
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<GifResponse> call, Throwable t) {

                    }
                });


    }

    @OnClick({R.id.iv_search, R.id.iv_back, R.id.tv_number_results, tv_next, tv_previous})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                gifModelList.clear();
                subGifList.clear();
                searchKey(etSearch.getText().toString());
                currentPage = 0;
                loadMore(0);
                break;
            case R.id.iv_back:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    startService(new Intent(MainActivity.this, FloatingService.class));
                    finish();
                } else if (Settings.canDrawOverlays(MainActivity.this)) {
                    startService(new Intent(MainActivity.this, FloatingService.class));
                    finish();
                } else {
                    askPermission();
                    Toast.makeText(MainActivity.this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_number_results:

                break;
            case R.id.tv_next:
                loadMore(1);

                break;
            case R.id.tv_previous:
                loadMore(-1);
                break;


        }
    }

    private void loadMore(int dir) {
        if (currentPage + dir < 0 || currentPage + dir > maxPages) return;
        currentPage += dir;
        subGifList.clear();
        for (int position = currentPage * MAX_ITEMS_PAGE; position < Math.min(gifModelList.size(), (currentPage + 1) * MAX_ITEMS_PAGE); position++) {
            subGifList.add(gifModelList.get(position));
        }
        rvAdapter.notifyDataSetChanged();
        tvPage.setText(currentPage + "");

    }

    private void searchKey(String key) {
        loadData(key);

    }

    private void changeFragment() {


    }

    private void setRecyclerView() {
        rvAdapter = new RecyclerAdapter(subGifList, MainActivity.this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        );
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);


        rvItems.setLayoutManager(staggeredGridLayoutManager);


        rvItems.addItemDecoration(new DividerItemDecoration(rvItems.getContext(), staggeredGridLayoutManager.getOrientation()));

        rvItems.setAdapter(rvAdapter);

        searchKey("beautiful girl idols");
        Log.d(TAG, "onCreate: " + gifModelList);
    }


}
