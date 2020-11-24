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
import com.tspolice.htplive.activities.SuggestionsActivity;
import com.tspolice.htplive.network.Networking;
import com.tspolice.htplive.utils.UiHelper;

public class PublicInformationFrag extends Fragment implements
        View.OnClickListener {

    private ImageView iv_alerts, iv_suggestions, iv_rules_info, iv_about_traffic_ps;
    private UiHelper mUiHelper;

    public PublicInformationFrag() {
        // default constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_public_information, container, false);

        mUiHelper = new UiHelper(getActivity());

        iv_alerts = view.findViewById(R.id.iv_alerts);
        iv_suggestions = view.findViewById(R.id.iv_suggestions);
        iv_rules_info = view.findViewById(R.id.iv_rules_info);
        iv_about_traffic_ps = view.findViewById(R.id.iv_about_traffic_ps);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_alerts.setOnClickListener(this);
        iv_suggestions.setOnClickListener(this);
        iv_rules_info.setOnClickListener(this);
        iv_about_traffic_ps.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_alerts:
                nextActivity(AlertsActivity.class);
                break;
            case R.id.iv_suggestions:
                nextActivity(SuggestionsActivity.class);
                break;
            case R.id.iv_rules_info:
                mUiHelper.replaceFragment(new RulesInformationFrag());
                break;
            case R.id.iv_about_traffic_ps:
                mUiHelper.replaceFragment(new AboutTrafficPoliceFrag());
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
