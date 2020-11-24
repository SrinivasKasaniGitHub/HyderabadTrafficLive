package com.tspolice.htplive.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.HardwareUtils;

public class GCMRegistrationIntentService {

  /*  private static final String TAG = "GCMRegnIntentService-->";

    public GCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        registerGcm();
    }

    private void registerGcm() {
        Intent intentRegnComplete;
        try {
            InstanceID instanceID = InstanceID.getInstance(GCMRegistrationIntentService.this);
            String gcmToken = instanceID.getToken("391430358860", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "gcmToken-->" + gcmToken);
            String deviceUUID = HardwareUtils.getDeviceUUID(GCMRegistrationIntentService.this);
            Log.i(TAG, "deviceUUID-->" + deviceUUID);
            sendGcmTokenToServer(gcmToken, deviceUUID);
            intentRegnComplete = new Intent(Constants.REGISTRATION_SUCCESS);
            intentRegnComplete.putExtra(Constants.GCM_TOKEN, gcmToken);
        } catch (Exception e) {
            e.printStackTrace();
            intentRegnComplete = new Intent(Constants.REGISTRATION_ERROR);
        }
        LocalBroadcastManager.getInstance(GCMRegistrationIntentService.this).sendBroadcast(intentRegnComplete);
    }

    private void sendGcmTokenToServer(String gcmToken, String deviceUUID) {
        String url = URLs.saveRegIds(gcmToken, Constants.ANDROID, deviceUUID);
        Log.i(TAG, "url_form-->" + url);
        VolleySingleton.getInstance(GCMRegistrationIntentService.this).addToRequestQueue(new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent registrationComplete = new Intent(Constants.REGISTRATION_TOKEN_SENT);
                LocalBroadcastManager.getInstance(GCMRegistrationIntentService.this).sendBroadcast(registrationComplete);
                Log.i(TAG, "sendGcmTokenToServer--> Success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "sendGcmTokenToServer--> Error");
            }
        }));
    }*/
}