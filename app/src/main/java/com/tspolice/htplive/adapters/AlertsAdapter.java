package com.tspolice.htplive.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tspolice.htplive.R;
import com.tspolice.htplive.models.AlertsModel;

import java.util.List;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.MyViewHolder> {

    private List<AlertsModel> alertsList;
    private Context mContext;

    public AlertsAdapter(List<AlertsModel> alertsList, Context mContext) {
        this.alertsList = alertsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_alerts, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final AlertsModel alertsModel = alertsList.get(position);
        if (position%2 == 0) {
            holder.tv_alert_text_left.setVisibility(View.VISIBLE);
            holder.tv_alert_text_left.setText(alertsModel.getAdvise());
            holder.tv_alert_text_right.setVisibility(View.GONE);

            holder.img_shape_left.setVisibility(View.GONE);
            holder.img_shape_right.setVisibility(View.VISIBLE);

            holder.rel_lyt_updated_date_left.setVisibility(View.GONE);
            holder.rel_lyt_updated_date_right.setVisibility(View.VISIBLE);
            holder.tv_updated_date_right.setText(alertsModel.getUpdatedDate());
        } else {
            holder.tv_alert_text_left.setVisibility(View.GONE);
            holder.tv_alert_text_right.setVisibility(View.VISIBLE);
            holder.tv_alert_text_right.setText(alertsModel.getAdvise());

            holder.img_shape_left.setVisibility(View.VISIBLE);
            holder.img_shape_right.setVisibility(View.GONE);

            holder.rel_lyt_updated_date_left.setVisibility(View.VISIBLE);
            holder.tv_updated_date_left.setText(alertsModel.getUpdatedDate());
            holder.rel_lyt_updated_date_right.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return alertsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_alert_text_left, tv_alert_text_right, tv_updated_date_left, tv_updated_date_right;
        ImageView img_shape_left, img_shape_right;
        RelativeLayout rel_lyt_updated_date_left, rel_lyt_updated_date_right;

        MyViewHolder(View itemView) {
            super(itemView);
            tv_alert_text_left = itemView.findViewById(R.id.tv_alert_text_left);
            tv_alert_text_right = itemView.findViewById(R.id.tv_alert_text_right);

            img_shape_left = itemView.findViewById(R.id.img_shape_left);
            img_shape_right = itemView.findViewById(R.id.img_shape_right);

            rel_lyt_updated_date_left = itemView.findViewById(R.id.rel_lyt_updated_date_left);
            rel_lyt_updated_date_right = itemView.findViewById(R.id.rel_lyt_updated_date_right);

            tv_updated_date_left = itemView.findViewById(R.id.tv_updated_date_left);
            tv_updated_date_right = itemView.findViewById(R.id.tv_updated_date_right);
        }
    }
}
