package com.example.mypc.officaligif.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.adapters.GridViewAdapter;
import com.example.mypc.officaligif.adapters.RecentSearchesAdapter;
import com.example.mypc.officaligif.databases.TopicDatabaseManager;
import com.example.mypc.officaligif.messages.SuggestTopicSticky;
import com.example.mypc.officaligif.models.SuggestTopicModel;
import com.example.mypc.officaligif.utils.Utils;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.ContentValues.TAG;


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
    @BindView(R.id.gv_recent_searches)
    GridView gvRecentSearches;
    @BindView(R.id.ll_recent_searches)
    LinearLayout llRecentSearches;

    private BubblesManager bubblesManager;
    private RecentSearchesAdapter recentSearchesAdapter;
    private List<String> recentSearchList ;

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
        editSearch(true, false);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    editSearch(true, true);
                    return true;
                }
                return false;
            }
        });


    }

    private void Initialization() {
        initializeBubblesManager();
        List<SuggestTopicModel> suggestTopicModelList =  TopicDatabaseManager.getInstance(getContext()).getSuggestTopicModelList();
        gridView.setAdapter(new GridViewAdapter(suggestTopicModelList, getContext()));
        recentSearchList = TopicDatabaseManager.getInstance(getContext()).getRecentSearchList();
        recentSearchesAdapter = new RecentSearchesAdapter(recentSearchList);
        gvRecentSearches.setAdapter(recentSearchesAdapter);
    }

    private void Definition() {


    }

    private void initializeBubblesManager() {
        bubblesManager = new BubblesManager.Builder(getContext())
                .setTrashLayout(R.layout.layout_bubble_trash)
                .build();
        bubblesManager.initialize();
    }

    private void addNewBubble() {
        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_bubbles, null);
        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
            }
        });
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Toast.makeText(getContext(), "Clicked !",
                        Toast.LENGTH_SHORT).show();
            }
        });
        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView, 60, 20);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        bubblesManager.recycle();
    }

    private void editSearch(boolean done, boolean searchTopic) {
        recentSearchList = TopicDatabaseManager.getInstance(getContext()).getRecentSearchList();
        recentSearchesAdapter.notifyDataSetChanged();

        if (!done) {

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

            if (!recentSearchList.isEmpty()) {
               // llRecentSearches.setVisibility(View.VISIBLE);
                Log.d(TAG, "editSearch: " + recentSearchList.size());
            }else{
                Log.d(TAG, "editSearch: " + "empty");
            }
        } else {
            rlTitleHide.setVisibility(View.GONE);
            titleSearch.setVisibility(View.GONE);
            rlTitle.setVisibility(View.VISIBLE);
            etSearch.clearFocus();
            InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
            if (searchTopic) {
                String topic = etSearch.getText().toString();
                Searching(topic);
                recentSearchesAdapter.notifyDataSetChanged();
            }

            llRecentSearches.setVisibility(View.GONE);

            etSearch.setText("");


        }
    }

    public void Searching(String topic) {
        EventBus.getDefault().postSticky(new SuggestTopicSticky(topic));
        Utils.openFragment(getFragmentManager(), R.id.cl_home_fragment, new SearchFragment());
    }

    @OnClick({R.id.iv_icon, R.id.iv_search_title, R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_icon:
                addNewBubble();
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
            case R.id.iv_search_title:
                editSearch(false, false);
                break;
            case R.id.iv_back:
                editSearch(true, false);

                break;
            case R.id.iv_search:
                editSearch(true, true);
                break;
        }
    }


}
