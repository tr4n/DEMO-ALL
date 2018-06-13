package com.example.mypc.officaligif.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.adapters.FavoriteAdapter;
import com.example.mypc.officaligif.adapters.FavoriteGridViewAdapter;
import com.example.mypc.officaligif.database_dir.TopicDatabaseManager;
import com.example.mypc.officaligif.messages.BackSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    View fragmentView;
    Unbinder unbinder;
    int classID = 0;
    List<MediaModel> favoriteList;
    FavoriteGridViewAdapter favoriteGridViewAdapter;
    @BindView(R.id.gv_favorites)
    GridView gvFavorites;


    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_favorite, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);

        Definition();
        Initialization();
        setupUI();


        return fragmentView;
    }


    private void Definition() {
        EventBus.getDefault().postSticky(new BackSticky(0));
    }

    ;

    private void Initialization() {
        favoriteList = TopicDatabaseManager.getInstance(getContext()).getFavoriteList();
        favoriteGridViewAdapter = new FavoriteGridViewAdapter(favoriteList, getContext());
        gvFavorites.setAdapter(favoriteGridViewAdapter);

    }

    private void setupUI() {
        gvFavorites.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(Utils.getDrawableResource(R.drawable.bubbletrash, getContext()))
                        .setTitle("Remove from favorites")
                        .setMessage("Do you want to remove this item from your favorites?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "removed! ", Toast.LENGTH_SHORT).show();
                                TopicDatabaseManager.getInstance(getContext()).removeFavoriteItem(favoriteList.get(position));
                                favoriteList = TopicDatabaseManager.getInstance(getContext()).getFavoriteList();
                                favoriteGridViewAdapter.notifyDataSetChanged();
                                Utils.replaceFragmentTag(getFragmentManager(), R.id.cl_main_activity, new FavoriteFragment(), "favorite_fragment");
                         //       Utils.refreshFragment(getFragmentManager(), "favorite_fragment");

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                return true;
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
