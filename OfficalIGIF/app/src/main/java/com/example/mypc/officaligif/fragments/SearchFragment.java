package com.example.mypc.officaligif.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.adapters.SearchedAdapter;
import com.example.mypc.officaligif.databases.TopicDatabaseManager;
import com.example.mypc.officaligif.messages.DataListSticky;
import com.example.mypc.officaligif.messages.SuggestTopicSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.models.PairModel;
import com.example.mypc.officaligif.models.ResponseModel;
import com.example.mypc.officaligif.networks.MediaResponse;
import com.example.mypc.officaligif.networks.RetrofitInstance;
import com.example.mypc.officaligif.networks.iGIPHYService;
import com.example.mypc.officaligif.recyclerview.EndlessRecyclerViewScrollListener;
import com.example.mypc.officaligif.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    @BindView(R.id.tv_key)
    TextView tvKey;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    Unbinder unbinder;
    @BindView(R.id.tv_number_results)
    TextView tvNumberResults;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.rv_searched_items)
    RecyclerView rvSearchedItems;

    @BindView(R.id.rl_avi)
    RelativeLayout rlAvi;


    DataListSticky dataListSticky;
    SearchedAdapter searchedAdapter;
    ResponseModel responseModel;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        Definition();
        Initialization();
        setupUI();

        return view;
    }

    private void setupUI() {

    }

    private void Initialization() {

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvSearchedItems.setItemAnimator(null);
        rvSearchedItems.setLayoutManager(staggeredGridLayoutManager);
        RetrofitInstance.getRetrofitGifInstance().create(iGIPHYService.class)
                .getMediaResponses(responseModel.key, responseModel.lang, responseModel.limit, responseModel.api_key)
                .enqueue(new Callback<MediaResponse>() {
                    @Override
                    public void onResponse(Call<MediaResponse> call, final Response<MediaResponse> response) {
                        if (response.body() == null || response.body().pagination.count == 0 || response.body().data.isEmpty()) {
                            if(response.body().pagination.count == 0){
                                tvNumberResults.setText("There isn't any results for " + responseModel.key);
                                if(rlAvi != null){
                                    rlAvi.setVisibility(View.GONE);
                                }
                            }
                            return;
                        }



                        final List<MediaModel> mediaModelList = new ArrayList<>();

                        List<MediaResponse.DataJSON> dataJSONList = response.body().data;
                        int position = 0;
                        for (MediaResponse.DataJSON dataJSON : dataJSONList) {
                            MediaModel mediaModel = new MediaModel(
                                    dataJSON.id,
                                    dataJSON.images.original.url,
                                    dataJSON.images.original.width,
                                    dataJSON.images.original.height,
                                    dataJSON.source_tld,
                                    dataJSON.title,
                                    dataJSON.caption,
                                    dataJSON.images.fixed_height.url,
                                    dataJSON.images.fixed_height.width,
                                    dataJSON.images.fixed_height.height,
                                    dataJSON.images.fixed_height_small.url,
                                    dataJSON.images.fixed_height_small.width,
                                    dataJSON.images.fixed_height_small.height,
                                    dataJSON.images.fixed_width.url,
                                    dataJSON.images.fixed_width.width,
                                    dataJSON.images.fixed_width.height,
                                    dataJSON.images.fixed_width_small.url,
                                    dataJSON.images.fixed_width_small.width,
                                    dataJSON.images.fixed_width_small.width,
                                    dataJSON.images.fixed_width_still.url,
                                    dataJSON.images.fixed_width_still.width,
                                    dataJSON.images.fixed_width_still.height,
                                    position++
                            );
                            mediaModelList.add(mediaModel);
                        }


                        dataListSticky = new DataListSticky(mediaModelList);
                        searchedAdapter = new SearchedAdapter(dataListSticky, getContext());
                        rvSearchedItems.setAdapter(searchedAdapter);
                        tvNumberResults.setText(response.body().pagination.total_count + " results");
                        setLoadMoreItems();


                    }

                    @Override
                    public void onFailure(Call<MediaResponse> call, Throwable t) {
                        tvNumberResults.setText("There isn't any results for " + responseModel.key);
                        if(rlAvi != null){
                            rlAvi.setVisibility(View.GONE);
                        }
                        Toasty.normal(getContext(), "No Internet!").show();

                    }
                });


    }

    private void Definition() {

    }

    private void setLoadMoreItems() {
        rvSearchedItems.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(final int page, final int totalItemsCount) {

                if (rlAvi != null) {
                    if (dataListSticky.totalList.isEmpty()) {
                        rlAvi.setVisibility(View.GONE);
                        return;
                    } else {
                        rlAvi.setVisibility(View.VISIBLE);
                    }
                }
                final int delayTime = 1000;

                CountDownTimer countDownTimer = new CountDownTimer(2 * delayTime, delayTime) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if (rlAvi != null) {
                            rlAvi.setVisibility(View.GONE);

                        }
                        PairModel pairModel = dataListSticky.addMore(10);
                        searchedAdapter.notifyItemRangeInserted(pairModel.first, pairModel.second - 1);


                    }
                }.start();


            }

        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getTopicMessager(SuggestTopicSticky suggestTopicSticky) {
        tvKey.setText(suggestTopicSticky.topic);
        responseModel = new ResponseModel(suggestTopicSticky.topic, "eng", 100);
        tvKey.setSelected(true);
        TopicDatabaseManager.getInstance(getContext()).saveSearchedTopic(suggestTopicSticky.topic);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getDataListMessager(DataListSticky dataListSticky1) {
        dataListSticky = dataListSticky1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        Utils.backFragment(getFragmentManager());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


}