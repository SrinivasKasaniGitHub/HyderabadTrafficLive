package com.tspolice.htplive.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tspolice.htplive.R;
import com.tspolice.htplive.firebase.MyFirebaseMessagingService;
import com.tspolice.htplive.gcm.GCMRegistrationIntentService;
import com.tspolice.htplive.network.Networking;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.HardwareUtils;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity-->";
    private final int SPLASH_DIALOG = 0, SPLASH_TIME_OUT = 2000;
    private Button btn_english;
    private UiHelper mUiHelper;
    private boolean doubleBackToExitPressedOnce = false;
    private BroadcastReceiver gcmBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int SPLASH_TIME = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent_Login = new Intent(getApplicationContext(), HomeActivity.class);
                intent_Login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_Login);
                finish();
            }
        }, SPLASH_TIME);
    }

}
