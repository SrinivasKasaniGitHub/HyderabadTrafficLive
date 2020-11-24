package com.tspolice.htplive.firebase;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.HardwareUtils;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

public class MyFirebaseInstanceIdService  {

   /* private static final String TAG = "InstanceIdService-->";

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
        VolleySingleton.getInstance(MyFirebaseInstanceIdService.this).addToRequestQueue(new StringRequest(Request.Method.GET,
                URLs.saveRegIds(fcmToken, Constants.ANDROID, HardwareUtils.getDeviceUUID(MyFirebaseInstanceIdService.this)),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "response-->"+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error-->"+error.toString());
            }
        }));
    }*/
}
