package com.tspolice.htplive.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tspolice.htplive.R;
import com.tspolice.htplive.models.CommonModel;
import com.tspolice.htplive.utils.Constants;

import java.util.List;

public class CommonRecyclerAdapter extends RecyclerView.Adapter<CommonRecyclerAdapter.MyViewHolder> {

    private String mFlag;
    private List<CommonModel> commonList;
    private Context mContext;
    private int currentPosition = 0;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(CommonModel item, int position);
    }

    public CommonRecyclerAdapter(String flag, List<CommonModel> list, Context context) {
        this.mFlag = flag;
        this.commonList = list;
        this.mContext = context;
    }

    public CommonRecyclerAdapter(String flag, List<CommonModel> list, OnItemClickListener listener, Context context) {
        this.mFlag = flag;
        this.commonList = list;
        this.onItemClickListener = listener;
        this.mContext = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rel_website, rel_faqs, rel_emergency_contacts, rel_tr_officers;
        private TextView tv_website_name, tv_question, tv_answer, tv_trps_name, tv_contact_no, tv_name, tv_cadre;
        private ImageView img_call_btn;
        private RelativeLayout rel_animation, rel_collapse_expand;

        MyViewHolder(View itemView) {
            super(itemView);
            switch (mFlag) {
                case Constants.USEFUL_WEBSITES:
                    rel_website = itemView.findViewById(R.id.rel_website);
                    tv_website_name = itemView.findViewById(R.id.tv_website_name);
                    break;
                case Constants.FAQS:
                    rel_faqs = itemView.findViewById(R.id.rel_faqs);
                    rel_animation = itemView.findViewById(R.id.rel_animation);
                    rel_collapse_expand = itemView.findViewById(R.id.rel_collapse_expand);
                    tv_question = itemView.findViewById(R.id.tv_question);
                    tv_answer = itemView.findViewById(R.id.tv_answer);
                    break;
                case Constants.ROAD_SAFETY_TIPS:
                    rel_website = itemView.findViewById(R.id.rel_website);
                    tv_website_name = itemView.findViewById(R.id.tv_website_name);
                    break;
                case Constants.EMERGENCY_CONTACTS:
                    rel_emergency_contacts = itemView.findViewById(R.id.rel_emergency_contacts);
                    tv_trps_name = itemView.findViewById(R.id.tv_trps_name);
                    tv_contact_no = itemView.findViewById(R.id.tv_contact_no);
                    img_call_btn = itemView.findViewById(R.id.img_call_btn);
                    break;
                case Constants.TRAFFIC_OFFICERS:
                    rel_tr_officers = itemView.findViewById(R.id.rel_tr_officers);
                    tv_name = itemView.findViewById(R.id.tv_name);
                    tv_cadre = itemView.findViewById(R.id.tv_cadre);
                    img_call_btn = itemView.findViewById(R.id.img_call_btn);
                    break;
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (mFlag) {
            case Constants.USEFUL_WEBSITES:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_useful_websites, parent, false);
                break;
            case Constants.FAQS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_faqs, parent, false);
                break;
            case Constants.ROAD_SAFETY_TIPS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_useful_websites, parent, false);
                break;
            case Constants.EMERGENCY_CONTACTS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_emergency_contacts, parent, false);
                break;
            case Constants.TRAFFIC_OFFICERS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_tr_officers, parent, false);
                break;
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final CommonModel model = commonList.get(position);
        switch (mFlag) {
            case Constants.USEFUL_WEBSITES:
                if (position % 2 == 0) {
                    holder.rel_website.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecyclerViewItem));
                } else {
                    holder.rel_website.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                }
                holder.tv_website_name.setText(model.getName());
                holder.rel_website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(model, position);
                    }
                });
                break;
            case Constants.FAQS:
                if (position % 2 == 0) {
                    holder.rel_faqs.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecyclerViewItem));
                } else {
                    holder.rel_faqs.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                }
                String question = model.getId() + ". " + model.getQuestion();
                holder.tv_question.setText(question);
                String answer = "A: " + model.getAnswer();
                answer = answer.replace(",", ",\n");
                holder.tv_answer.setText(answer);
                holder.rel_collapse_expand.setVisibility(View.GONE);
                if (currentPosition == position) {
                    Animation slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
                    holder.rel_collapse_expand.setVisibility(View.VISIBLE);
                    holder.rel_collapse_expand.startAnimation(slideDown);
                }
                holder.rel_animation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentPosition = position;
                        notifyDataSetChanged();
                    }
                });
                break;
            case Constants.ROAD_SAFETY_TIPS:
                if (position % 2 == 0) {
                    holder.rel_website.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecyclerViewItem));
                } else {
                    holder.rel_website.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                }
                holder.tv_website_name.setText(model.getTip());
                holder.rel_website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(model, position);
                    }
                });
                break;
            case Constants.EMERGENCY_CONTACTS:
                if (position % 2 == 0) {
                    holder.rel_emergency_contacts.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecyclerViewItem));
                } else {
                    holder.rel_emergency_contacts.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                }
                holder.tv_trps_name.setText(model.getServiceName());
                holder.tv_contact_no.setText(model.getContactNumber());
                holder.img_call_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(model, position);
                    }
                });
                holder.tv_contact_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(model, position);
                    }
                });
                break;
            case Constants.TRAFFIC_OFFICERS:
                if (position % 2 == 0) {
                    holder.rel_tr_officers.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecyclerViewItem));
                } else {
                    holder.rel_tr_officers.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                }
                holder.tv_name.setText(model.getName());
                holder.tv_cadre.setText(model.getDesignation());
                holder.img_call_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(model, position);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return commonList.size();
    }

    public void filterList(List<CommonModel> filteredNames) {
        this.commonList = filteredNames;
        notifyDataSetChanged();
    }
}
