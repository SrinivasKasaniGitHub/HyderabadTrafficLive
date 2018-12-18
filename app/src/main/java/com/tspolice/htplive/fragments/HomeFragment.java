package com.tspolice.htplive.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tspolice.htplive.R;
import com.tspolice.htplive.activities.AutoFareEstmActivity;
import com.tspolice.htplive.activities.LiveTrafficActivity;
import com.tspolice.htplive.activities.NearByActivity;
import com.tspolice.htplive.activities.PublicComplaintsActivity;
import com.tspolice.htplive.activities.PublicInterfaceActivity;
import com.tspolice.htplive.activities.RtaTowingActivity;
import com.tspolice.htplive.network.Networking;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private ImageView iv_public_interface, iv_auto_fare_estimation, iv_live_traffic, iv_near_by,
            iv_echallan_status, iv_find_towed_vhcl, iv_getRTADetails, iv_public_complaints, iv_public_info;
    private UiHelper mUiHelper;
    private SharedPrefManager mSharedPrefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        mUiHelper = new UiHelper(getActivity());
        mSharedPrefManager = SharedPrefManager.getInstance(getActivity());
        //GPSTracker mGpsTracker = new GPSTracker(getActivity());
        iv_public_interface = view.findViewById(R.id.iv_public_interface);
        iv_auto_fare_estimation = view.findViewById(R.id.iv_auto_fare_estimation);
        iv_live_traffic = view.findViewById(R.id.iv_live_traffic);
        iv_near_by = view.findViewById(R.id.iv_near_by);
        iv_echallan_status = view.findViewById(R.id.iv_echallan_status);
        iv_find_towed_vhcl = view.findViewById(R.id.iv_find_towed_vhcl);
        iv_getRTADetails = view.findViewById(R.id.iv_getRTADetails);
        iv_public_complaints = view.findViewById(R.id.iv_public_complaints);
        iv_public_info = view.findViewById(R.id.iv_public_info);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_public_interface.setOnClickListener(this);
        iv_auto_fare_estimation.setOnClickListener(this);
        iv_live_traffic.setOnClickListener(this);
        iv_near_by.setOnClickListener(this);
        iv_echallan_status.setOnClickListener(this);
        iv_find_towed_vhcl.setOnClickListener(this);
        iv_getRTADetails.setOnClickListener(this);
        iv_public_complaints.setOnClickListener(this);
        iv_public_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_public_interface:
                nextActivity(PublicInterfaceActivity.class);
                break;
            case R.id.iv_auto_fare_estimation:
                nextActivity(AutoFareEstmActivity.class);
                break;
            case R.id.iv_live_traffic:
                nextActivity(LiveTrafficActivity.class);
                /*if (mGpsTracker.canGetLocation()) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", mGpsTracker.getLatitude(), mGpsTracker.getLongitude());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }*/
                break;
            case R.id.iv_near_by:
                nextActivity(NearByActivity.class);
                break;
            case R.id.iv_echallan_status:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLs.eChallanStatus));
                startActivity(browserIntent);
                break;
            case R.id.iv_find_towed_vhcl:
                mSharedPrefManager.putString(Constants.RTA_TOWING, "TOWING");
                nextActivity(RtaTowingActivity.class);
                break;
            case R.id.iv_getRTADetails:
                mSharedPrefManager.putString(Constants.RTA_TOWING, "RTA");
                nextActivity(RtaTowingActivity.class);
                break;
            case R.id.iv_public_complaints:
                nextActivity(PublicComplaintsActivity.class);
                break;
            case R.id.iv_public_info:
                mUiHelper.replaceFragment(new PublicInformationFrag());
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
