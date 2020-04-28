package com.omniscient.omnisegment_sample_java.tools.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omniscient.omnisegment_sample_java.R;
import com.omniscient.omnisegment_sample_java.tools.model.Item;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {

    private Context context;
    private ArrayList<Item> mDatas;
    public RecycleAdapter(Context context, ArrayList<Item> mStrings)
    {
        this.context = context;
        this.mDatas = mStrings;
    }


    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecycleViewHolder holder = new RecycleViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycle_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, final int position) {
        holder.tvTitle.setText(mDatas.get(position).getTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.get(position).collect();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTitle;
        CardView cardview;
        public RecycleViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            cardview = itemView.findViewById(R.id.cardview);
        }
    }
}