package com.tspolice.htplive.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.tspolice.htplive.R;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.HardwareUtils;
import com.tspolice.htplive.utils.UiHelper;

public class GCMRegistrationIntentService extends IntentService {

    private static final String TAG = "GCMRegnIntentService-->";
    private UiHelper mUiHelper;

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
            String gcmToken = instanceID.getToken("391430358860",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "gcmToken-->" + gcmToken);
            String[] split = gcmToken.split(":");
            Log.i(TAG, "gcmTokenSplit-->" + split[1]);
            sendGcmTokenToServer(split[1]);
            intentRegnComplete = new Intent(Constants.REGISTRATION_SUCCESS);
            intentRegnComplete.putExtra(Constants.GCM_TOKEN, gcmToken);
        } catch (Exception e) {
            e.printStackTrace();
            intentRegnComplete = new Intent(Constants.REGISTRATION_ERROR);
        }
        LocalBroadcastManager.getInstance(GCMRegistrationIntentService.this).sendBroadcast(intentRegnComplete);
    }

    private void sendGcmTokenToServer(String gcmToken) {
        //mUiHelper = new UiHelper(getApplicationContext());
        //mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        //Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_LONG).show();
        VolleySingleton.getInstance(GCMRegistrationIntentService.this).addToRequestQueue(new StringRequest(Request.Method.GET,
                URLs.saveRegIds(gcmToken, Constants.ANDROID, HardwareUtils.getDeviceUUID(GCMRegistrationIntentService.this)),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // mUiHelper.dismissProgressDialog();
                        // mUiHelper.showToastLong(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mUiHelper.dismissProgressDialog();
                // mUiHelper.showToastShort(getResources().getString(R.string.error));
            }
        }));
    }

}
