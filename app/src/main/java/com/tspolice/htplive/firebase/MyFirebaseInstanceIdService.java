package com.tspolice.htplive.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.SharedPrefManager;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "InstanceIdService-->";

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPrefManager mSharedPrefManager = SharedPrefManager.getInstance(this);
        mSharedPrefManager.putString(Constants.FIRE_BASE_TOKEN, refreshedToken);
        Log.d(TAG, "onTokenRefresh() called: " + refreshedToken);
        //sendRegistrationToServer(refreshedToken);
    }
}
