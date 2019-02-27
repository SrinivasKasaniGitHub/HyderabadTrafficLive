package com.tspolice.htplive.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tspolice.htplive.R;
import com.tspolice.htplive.models.TrPSInfoModel;

import java.util.List;

public class TrPSInfoAdapter extends RecyclerView.Adapter<TrPSInfoAdapter.MyViewHolder> {

    private Context mContext;
    private List<TrPSInfoModel> trPSInfoList;

    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(TrPSInfoModel item, int position);
    }

    public TrPSInfoAdapter(List<TrPSInfoModel> list, OnItemClickListener listener, Context context) {
        this.mContext = context;
        this.trPSInfoList = list;
        this.onItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rel_trps_info;
        private TextView tv_trps_name;

        MyViewHolder(View itemView) {
            super(itemView);
            rel_trps_info = itemView.findViewById(R.id.rel_trps_info);
            tv_trps_name = itemView.findViewById(R.id.tv_trps_name);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_trps_info, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final TrPSInfoModel trPSInfoModel = trPSInfoList.get(position);
        if (position % 2 == 0) {
            holder.rel_trps_info.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecyclerViewItem));
        } else {
            holder.rel_trps_info.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }
        holder.tv_trps_name.setText(trPSInfoModel.getStationName());
        holder.rel_trps_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(trPSInfoModel, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trPSInfoList.size();
    }

    public void filterList(List<TrPSInfoModel> filteredNames) {
        this.trPSInfoList = filteredNames;
        notifyDataSetChanged();
    }
}
