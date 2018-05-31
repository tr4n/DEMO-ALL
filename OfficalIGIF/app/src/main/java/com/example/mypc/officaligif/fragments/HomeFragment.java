package com.example.mypc.officaligif.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.adapters.GridViewAdapter;
import com.example.mypc.officaligif.databases.TopicDatabaseManager;
import com.example.mypc.officaligif.models.ResponseModel;
import com.example.mypc.officaligif.models.ResultResponseListModel;
import com.example.mypc.officaligif.models.SuggestTopicModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.iv_search_title)
    ImageView ivSearchTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_title_hide)
    RelativeLayout rlTitleHide;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.title_search)
    RelativeLayout titleSearch;
    @BindView(R.id.title)
    LinearLayout title;
    Unbinder unbinder;
    @BindView(R.id.grid_view)
    GridView gridView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        Definition();
        Initialization();
        setupUI();


        return view;
    }

    private void setupUI() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    editSearch(false);


                    return true;
                }
                return false;
            }
        });

    }

    private void Initialization() {
        editSearch(false);
        searching();

        List<SuggestTopicModel> suggestTopicModelList = new ArrayList<>();
        suggestTopicModelList = TopicDatabaseManager.getInstance(getContext()).getSuggestTopicModelList();


        gridView.setAdapter(new GridViewAdapter(suggestTopicModelList, getContext()));


    }

    private void Definition() {

    }

    private void searching() {
        ResponseModel responseModel = new ResponseModel(
                "naruto",
                "vie",
                25
        );
        ResultResponseListModel resultResponseListModel = new ResultResponseListModel();
        //  Utils.getResultResponseList(responseModel, resultResponseListModel, getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void editSearch(boolean editting) {
        if (editting == true) {
            CountDownTimer countDownTimer = new CountDownTimer(300, 150) {
                @Override
                public void onTick(long millisUntilFinished) {
                    rlTitle.setVisibility(View.GONE);
                    rlTitleHide.setVisibility(View.VISIBLE);
                    titleSearch.setVisibility(View.VISIBLE);
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
        } else {
            rlTitleHide.setVisibility(View.GONE);
            titleSearch.setVisibility(View.GONE);
            rlTitle.setVisibility(View.VISIBLE);
            etSearch.clearFocus();
            InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
            etSearch.setText("");

        }
    }

    @OnClick({R.id.iv_icon, R.id.iv_search_title, R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_icon:
                break;
            case R.id.iv_search_title:
                editSearch(true);

                break;
            case R.id.iv_back:
                editSearch(false);

                break;
            case R.id.iv_search:
                editSearch(false);
                break;
        }
    }
}
