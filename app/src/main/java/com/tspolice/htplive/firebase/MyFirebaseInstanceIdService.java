package com.tspolice.htplive.firebase;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tspolice.htplive.R;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.HardwareUtils;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "InstanceIdService-->";
    private UiHelper mUiHelper;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        SharedPrefManager mSharedPrefManager = SharedPrefManager.getInstance(this);
        mSharedPrefManager.putString(Constants.FCM_TOKEN, fcmToken);
        Log.i(TAG, "fcmToken-->" + fcmToken);
        sendRegistrationToServer(fcmToken);
    }

    private void sendRegistrationToServer(String fcmToken) {
        //mUiHelper = new UiHelper(getApplicationContext());
        //mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(MyFirebaseInstanceIdService.this).addToRequestQueue(new StringRequest(Request.Method.GET,
                URLs.saveRegIds(fcmToken, Constants.ANDROID, HardwareUtils.getDeviceUUID(MyFirebaseInstanceIdService.this)),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //mUiHelper.dismissProgressDialog();
                        //.showToastLong(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mUiHelper.dismissProgressDialog();
                //mUiHelper.showToastShort(getResources().getString(R.string.error));
            }
        }));
    }
}
