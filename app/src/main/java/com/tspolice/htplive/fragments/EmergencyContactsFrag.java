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
import com.tspolice.htplive.activities.EmergencyContactsActivity;
import com.tspolice.htplive.network.Networking;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

public class EmergencyContactsFrag extends Fragment implements
        View.OnClickListener {

    private ImageView iv_ambulance, iv_blood_bank, iv_fire_station, iv_crime_alert, iv_women_child, iv_senior_citizen_helpline;
    private UiHelper mUiHelper;
    private SharedPrefManager sharedPrefManager;

    public EmergencyContactsFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_emergency_contacts, container, false);

        mUiHelper = new UiHelper(getActivity());
        sharedPrefManager = SharedPrefManager.getInstance(getActivity());

        iv_ambulance = view.findViewById(R.id.iv_ambulance);
        iv_blood_bank = view.findViewById(R.id.iv_blood_bank);
        iv_fire_station = view.findViewById(R.id.iv_fire_station);
        iv_crime_alert = view.findViewById(R.id.iv_crime_alert);
        iv_women_child = view.findViewById(R.id.iv_women_child);
        iv_senior_citizen_helpline = view.findViewById(R.id.iv_senior_citizen_helpline);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_ambulance.setOnClickListener(this);
        iv_blood_bank.setOnClickListener(this);
        iv_fire_station.setOnClickListener(this);
        iv_crime_alert.setOnClickListener(this);
        iv_women_child.setOnClickListener(this);
        iv_senior_citizen_helpline.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_ambulance:
                nextActivity(EmergencyContactsActivity.class, Constants.AMBULANCE);
                break;
            case R.id.iv_blood_bank:
                nextActivity(EmergencyContactsActivity.class, Constants.BLOOD_BANK);
                break;
            case R.id.iv_fire_station:
                nextActivity(EmergencyContactsActivity.class, Constants.FIRE_STATION);
                break;
            case R.id.iv_crime_alert:
                nextActivity(EmergencyContactsActivity.class, Constants.CRIME_ALERT);
                break;
            case R.id.iv_women_child:
                nextActivity(EmergencyContactsActivity.class, Constants.WOMEN_CHILD);
                break;
            case R.id.iv_senior_citizen_helpline:
                nextActivity(EmergencyContactsActivity.class, Constants.SENIOR_CITIZEN);
                break;
            default:
                break;
        }
    }

    public void nextActivity(Class<?> mClass, String contactType) {
        if (!Networking.isNetworkAvailable(getActivity())) {
            mUiHelper.showToastLongCentre(getResources().getString(R.string.network_error));
        } else {
            sharedPrefManager.putString(Constants.EMERGENCY_CONTACTS, contactType);
            mUiHelper.intent(mClass);
        }
    }
}
