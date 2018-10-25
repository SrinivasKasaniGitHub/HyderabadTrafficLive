package com.tspolice.htplive.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tspolice.htplive.R;
import com.tspolice.htplive.activities.AlertsActivity;
import com.tspolice.htplive.activities.TrOfficersContactsActivity;
import com.tspolice.htplive.network.Networking;
import com.tspolice.htplive.utils.UiHelper;

public class AboutTrafficPoliceFrag extends Fragment implements
        View.OnClickListener {

    private ImageView iv_about_htl, iv_traffic_police_info, iv_org_chart, iv_traffic_officers;
    private UiHelper mUiHelper;

    public AboutTrafficPoliceFrag() {
        //default constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_about_traffic_police, container, false);

        mUiHelper = new UiHelper(getActivity());

        iv_about_htl = view.findViewById(R.id.iv_about_htl);
        iv_traffic_police_info = view.findViewById(R.id.iv_traffic_police_info);
        iv_org_chart = view.findViewById(R.id.iv_org_chart);
        iv_traffic_officers = view.findViewById(R.id.iv_traffic_officers);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_about_htl.setOnClickListener(this);
        iv_traffic_police_info.setOnClickListener(this);
        iv_org_chart.setOnClickListener(this);
        iv_traffic_officers.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_about_htl:
                mUiHelper.replaceFragment(new AboutHTLFrag());
                break;
            case R.id.iv_traffic_police_info:
                mUiHelper.replaceFragment(new TrafficPoliceInfoFrag());
                break;
            case R.id.iv_org_chart:
                mUiHelper.replaceFragment(new OrganizationChartFrag());
                break;
            case R.id.iv_traffic_officers:
                nextActivity(TrOfficersContactsActivity.class);
                break;
            default:
                break;
        }
    }

    public void nextActivity(Class<?> mClass) {
        if (!Networking.isNetworkAvailable(getActivity())) {
            mUiHelper.showToastLong(getResources().getString(R.string.network_error));
        } else {
            mUiHelper.intent(mClass);
        }
    }
}
