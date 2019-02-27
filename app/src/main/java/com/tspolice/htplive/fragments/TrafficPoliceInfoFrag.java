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
import com.tspolice.htplive.activities.TrPSInfoActivity;
import com.tspolice.htplive.network.Networking;
import com.tspolice.htplive.utils.UiHelper;

public class TrafficPoliceInfoFrag extends Fragment implements View.OnClickListener {

    private ImageView iv_control_room, iv_hyd_traffic_police_stations, iv_emergency_contacts;
    private UiHelper mUiHelper;

    public TrafficPoliceInfoFrag() {
        //default constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_traffic_police_info, container, false);

        mUiHelper = new UiHelper(getActivity());

        iv_control_room = view.findViewById(R.id.iv_control_room);
        iv_hyd_traffic_police_stations = view.findViewById(R.id.iv_hyd_traffic_police_stations);
        iv_emergency_contacts = view.findViewById(R.id.iv_emergency_contacts);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_control_room.setOnClickListener(this);
        iv_hyd_traffic_police_stations.setOnClickListener(this);
        iv_emergency_contacts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_control_room:
                mUiHelper.replaceFragment(new TrafficControlRoomFrag());
                break;
            case R.id.iv_hyd_traffic_police_stations:
                nextActivity(TrPSInfoActivity.class);
                break;
            case R.id.iv_emergency_contacts:
                mUiHelper.replaceFragment(new EmergencyContactsFrag());
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
