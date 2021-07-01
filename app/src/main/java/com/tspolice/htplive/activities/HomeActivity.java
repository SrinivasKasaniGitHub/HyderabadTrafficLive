package com.tspolice.htplive.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tspolice.htplive.R;
import com.tspolice.htplive.fragments.HomeFragment;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.HardwareUtils;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "";
    public static FragmentManager mFragmentManager;
    private UiHelper mUiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        mFragmentManager = getSupportFragmentManager();
        mUiHelper = new UiHelper(HomeActivity.this);

        FirebaseApp.initializeApp(this);

        String fcmToken = "";
        SharedPrefManager mSharedPrefManager = SharedPrefManager.getInstance(this);
        fcmToken = mSharedPrefManager.getString(Constants.FCM_TOKEN);
        Log.i(TAG, "fcmToken-->" + fcmToken);
        if ("".equalsIgnoreCase(fcmToken)) {
            fcmToken = FirebaseInstanceId.getInstance().getToken();
            mSharedPrefManager.putString(Constants.FCM_TOKEN, fcmToken);
            Log.i(TAG, "fcmToken-->" + fcmToken);
        }

        sendRegistrationToServer(fcmToken);

        FrameLayout frameLayout = findViewById(R.id.content_frame);
        overridePendingTransition(R.anim.fade_enter, R.anim.fade_leave);
        if (frameLayout != null) {
            if (savedInstanceState != null) {
                return;
            }
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.content_frame, homeFragment, null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
       /* Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof HomeFragment) {
            mUiHelper.alertDialogOkCancel(getResources().getString(R.string.do_you_want_exit), false, Constants.HOME);
        } else {*/
        //   super.onBackPressed();
        finish();
        //}
    }

    private void sendRegistrationToServer(String fcmToken) {
        VolleySingleton.getInstance(HomeActivity.this).addToRequestQueue(new StringRequest(Request.Method.GET,
                URLs.saveRegIds(fcmToken, Constants.ANDROID, HardwareUtils.getDeviceUUID(HomeActivity.this)),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "response-->" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error-->" + error.toString());
            }
        }));
    }

}