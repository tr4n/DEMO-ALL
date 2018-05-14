package com.example.mypc.demosuper.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mypc.demosuper.R;
import com.example.mypc.demosuper.adapters.GifRecyclerViewAdapter;
import com.example.mypc.demosuper.models.GifModel;
import com.example.mypc.demosuper.networks.GIPHYService;
import com.example.mypc.demosuper.networks.GifResponse;
import com.example.mypc.demosuper.networks.RetrofitInstance;
import com.example.mypc.demosuper.recyclerview.EndlessRecyclerViewScrollListener;
import com.example.mypc.demosuper.services.FloatingWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class GifSearchingFragment extends Fragment implements TextView.OnEditorActionListener {

    @BindView(R.id.rv_items)
    RecyclerView rvItems;
    Unbinder unbinder;


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.iv_seraching)
    ImageView ivSeraching;
    @BindView(R.id.cl_title_unsearching)
    ConstraintLayout clTitleUnsearching;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.cl_searching)
    ConstraintLayout clSearching;
    @BindView(R.id.cl_title)
    ConstraintLayout clTitle;
    @BindView(R.id.ll_loading_more)
    LinearLayout llLoadingMore;

    private List<GifModel> gifModelList = new ArrayList<>();
    private List<GifModel> gifModelPageList = new ArrayList<>();
    private GifRecyclerViewAdapter gifRecyclerViewAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private final int VERTICAL_ITEM_SPACE = 10;
    private final int HORIZONTAL_ITEM_SPACE = 10;


    public GifSearchingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_searching, container, false);
        unbinder = ButterKnife.bind(this, view);

        Definition();
        Initialization();
        setupUI();


        return view;
    }

    private void Definition() {

    }


    private void setupUI() {
        loadSearching("naruto");
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //    Toast.makeText(getContext(), etSearch.getText().toString(), Toast.LENGTH_SHORT).show();
                    etSearch.clearFocus();
                    InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    clSearching.setVisibility(View.GONE);

                    loadSearching(etSearch.getText().toString());

                    etSearch.setText("");


                    return true;
                }
                return false;
            }
        });

    }

    private void Initialization() {
        llLoadingMore.setVisibility(View.GONE);

        gifRecyclerViewAdapter = new GifRecyclerViewAdapter(gifModelPageList, getContext());
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvItems.setLayoutManager(staggeredGridLayoutManager);
        rvItems.setAdapter(gifRecyclerViewAdapter);
        rvItems.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, final int totalItemsCount) {
                llLoadingMore.setVisibility(View.VISIBLE);

                CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //    gifRecyclerViewAdapter.notifyDataSetChanged();
                        //    Toast.makeText(getContext(), "Loading... ", Toast.LENGTH_SHORT).show();




                    }

                    @Override
                    public void onFinish() {
                        llLoadingMore.setVisibility(View.GONE);
                        int firstIndex = gifModelPageList.size();
                        Log.d(TAG, "onTick: " + firstIndex);
                        int secondIndex = Math.min(gifModelPageList.size() + 15, gifModelList.size());
                        gifModelPageList.addAll(gifModelList.subList(firstIndex, secondIndex));
                        gifRecyclerViewAdapter.notifyItemRangeInserted(firstIndex, gifModelPageList.size() - 1);

                    }
                }.start();

            }
        });
    }

    private void loadSearching(final String searchingKey) {
        loadGifModelList(searchingKey, 200);


    }

    public void loadGifModelList(String key, final int limit) {
        gifModelList.clear();
        gifModelPageList.clear();
        llLoadingMore.setVisibility(View.VISIBLE);


        RetrofitInstance.getRetrofitGifInstance().create(GIPHYService.class)
                .getGifResponse(key, limit, "WNHN34giS02m5C8EDvufq4E8CjKDIk02")
                .enqueue(new Callback<GifResponse>() {
                    @Override
                    public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {

                        if (response.body() == null || response.body().pagination.count == 0) {
                            return;
                        } else {
                            if (response.body().data.isEmpty()) return;

                            int numberResults = response.body().pagination.count;

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
                                    if (gifModelPageList.size() < 1) {
                                        gifModelPageList.add(gifModel);
                                    }
                                    Log.d(TAG, "onResponse: " + gifModel.url);
                                }

                            }
                            gifRecyclerViewAdapter.notifyDataSetChanged();
                            llLoadingMore.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<GifResponse> call, Throwable t) {


                    }
                });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_icon, R.id.iv_seraching, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_icon:
                Intent intent = new Intent(getActivity(), FloatingWidgetService.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startService(intent);
                //  android.os.Process.killProcess(android.os.Process.myPid());
                //System.exit(0);
                getActivity().finish();

                break;
            case R.id.iv_seraching:

                CountDownTimer countDownTimer = new CountDownTimer(300, 150) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //  clTitleUnsearching.setVisibility(View.GONE);
                        clSearching.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        // Show soft keyboard automaticly
                        etSearch.dispatchTouchEvent(
                                MotionEvent.obtain(SystemClock.uptimeMillis(),
                                        SystemClock.uptimeMillis(),
                                        MotionEvent.ACTION_DOWN,
                                        0,
                                        0,
                                        0)
                        );
                        etSearch.dispatchTouchEvent(
                                MotionEvent.obtain(SystemClock.uptimeMillis(),
                                        SystemClock.uptimeMillis(),
                                        MotionEvent.ACTION_UP,
                                        0,
                                        0,
                                        0)
                        );
                    }
                }.start();

                break;
            case R.id.iv_back:
                clSearching.setVisibility(View.GONE);
                clTitleUnsearching.setVisibility(View.VISIBLE);
                etSearch.clearFocus();
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
