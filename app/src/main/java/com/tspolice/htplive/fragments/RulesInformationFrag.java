package com.tspolice.htplive.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tspolice.htplive.R;
import com.tspolice.htplive.activities.FAQsActivity;
import com.tspolice.htplive.activities.RoadSafetyTipsActivity;
import com.tspolice.htplive.activities.TrViolationsActivity;
import com.tspolice.htplive.activities.UsefulWebsitesActivity;
import com.tspolice.htplive.network.Networking;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.utils.UiHelper;

public class RulesInformationFrag extends Fragment implements
        View.OnClickListener {

    private ImageView iv_traffic_violations_fines, iv_useful_websites, iv_faqs,
            iv_road_safety_tips, iv_traffic_signs, iv_htl_on_fb;
    private UiHelper mUiHelper;

    public RulesInformationFrag() {
        //default constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_rules_information, container, false);

        mUiHelper = new UiHelper(getActivity());

        iv_traffic_violations_fines = view.findViewById(R.id.iv_traffic_violations_fines);
        iv_useful_websites = view.findViewById(R.id.iv_useful_websites);
        iv_faqs = view.findViewById(R.id.iv_faqs);
        iv_road_safety_tips = view.findViewById(R.id.iv_road_safety_tips);
        iv_traffic_signs = view.findViewById(R.id.iv_traffic_signs);
        iv_htl_on_fb = view.findViewById(R.id.iv_htl_on_fb);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_traffic_violations_fines.setOnClickListener(this);
        iv_useful_websites.setOnClickListener(this);
        iv_faqs.setOnClickListener(this);
        iv_road_safety_tips.setOnClickListener(this);
        iv_traffic_signs.setOnClickListener(this);
        iv_htl_on_fb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_traffic_violations_fines:
                nextActivity(TrViolationsActivity.class);
                break;
            case R.id.iv_useful_websites:
                nextActivity(UsefulWebsitesActivity.class);
                break;
            case R.id.iv_faqs:
                nextActivity(FAQsActivity.class);
                break;
            case R.id.iv_road_safety_tips:
                nextActivity(RoadSafetyTipsActivity.class);
                break;
            case R.id.iv_traffic_signs:
                mUiHelper.replaceFragment(new TrafficSignsFrag());
                break;
            case R.id.iv_htl_on_fb:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLs.htpOnFacebook));
                startActivity(browserIntent);
                break;
            default:
                break;
        }
    }

    public void nextActivity(Class<?> mClass) {
        if (!Networking.isNetworkAvailable(getActivity())) {
            mUiHelper.showToastLongCentre(getResources().getString(R.string.network_error));
        } else {
            mUiHelper.intent(mClass);
        }
    }
}
