package com.tspolice.htplive.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tspolice.htplive.R;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.PermissionUtil;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

public class TrafficControlRoomFrag extends Fragment {

    private UiHelper mUiHelper;
    private SharedPrefManager mSharedPrefManager;

    public TrafficControlRoomFrag() {
        //default constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_traffic_control_room, container, false);

        mUiHelper = new UiHelper(getActivity());
        mSharedPrefManager = SharedPrefManager.getInstance(getActivity());

        ImageView img_traffic_control_room = view.findViewById(R.id.img_traffic_control_room);
        img_traffic_control_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (PermissionUtil.checkPermission(getActivity(), Constants.INT_CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                            PermissionUtil.showPermissionExplanation(getActivity(), Constants.INT_CALL_PHONE);
                        } else if (!mSharedPrefManager.getBoolean(Constants.CALL_PHONE)) {
                            PermissionUtil.requestPermission(getActivity(), Constants.INT_CALL_PHONE);
                            mSharedPrefManager.putBoolean(Constants.CALL_PHONE, true);
                        } else {
                            PermissionUtil.redirectAppSettings(getActivity());
                        }
                    } else {
                        makeCall();
                    }
                } else {
                    PermissionUtil.redirectAppSettings(getActivity());
                }
            }
        });
        return view;
    }

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "+" + Constants.CONTROL_ROOM_PH_NO));
        if (PermissionUtil.checkPermission(getActivity(), Constants.INT_CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    mUiHelper.showToastShort(getResources().getString(R.string.permission_denied));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
