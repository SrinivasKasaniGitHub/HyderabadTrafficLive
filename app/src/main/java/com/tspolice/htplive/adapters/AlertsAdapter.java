package com.tspolice.htplive.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_alerts_item,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AlertsModel alertsModel = alertsList.get(position);
        holder.tv_alert_text.setText(alertsModel.getAlertText());
        holder.tv_alert_updated_date.setText(alertsModel.getAlertUpdatedDt());
    }

    @Override
    public int getItemCount() {
        return alertsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_alert_text, tv_alert_updated_date;

        MyViewHolder(View itemView) {
            super(itemView);
            tv_alert_text = itemView.findViewById(R.id.tv_alert_text);
            tv_alert_updated_date = itemView.findViewById(R.id.tv_alert_updated_date);
        }
    }
}
