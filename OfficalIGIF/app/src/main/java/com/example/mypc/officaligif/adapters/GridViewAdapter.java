package com.example.mypc.officaligif.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.models.SuggestTopicModel;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    List<SuggestTopicModel> suggestTopicModelList = new ArrayList<>();
    Context context;

    public GridViewAdapter(List<SuggestTopicModel> suggestTopicModelList, Context context) {
        this.suggestTopicModelList = suggestTopicModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return suggestTopicModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestTopicModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.layout_main_topic_item, null);

        TextView tvMainTopic = convertView.findViewById(R.id.tv_main_topic);
        RecyclerView recyclerView = convertView.findViewById(R.id.rv_topic_list);

        SuggestTopicModel suggestTopicModel = suggestTopicModelList.get(position);

        tvMainTopic.setText(suggestTopicModel.key);


        return convertView;
    }
}
