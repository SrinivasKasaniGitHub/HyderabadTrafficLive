package com.tspolice.htplive.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.network.URLParams;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.GPSTracker;
import com.tspolice.htplive.utils.HardwareUtils;
import com.tspolice.htplive.utils.PermissionUtil;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;
import com.tspolice.htplive.utils.ValidationUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PublicComplaintsActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "PublicComplaintsAct-->";
    private EditText et_complaint, et_complaint_type, et_vehicle_no, et_drivers_name, et_type_a_complaint,
            et_your_name, et_your_email_id, et_your_mobile_no;
    private Button btn_submit;
    private UiHelper mUiHelper;
    private GPSTracker mGpsTracker;
    private SharedPrefManager mSharedPrefManager;
    private double mLatitude, mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_complaints);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        initViews();

        initObjects();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkPermission(this, Constants.INT_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    PermissionUtil.showPermissionExplanation(this, Constants.INT_FINE_LOCATION);
                } else if (!mSharedPrefManager.getBoolean(Constants.FINE_LOCATION)) {
                    PermissionUtil.requestPermission(this, Constants.INT_FINE_LOCATION);
                    mSharedPrefManager.putBoolean(Constants.FINE_LOCATION, true);
                } else {
                    PermissionUtil.redirectAppSettings(this);
                }
            } else {
                getCurrentLocation();
            }
        } else {
            PermissionUtil.redirectAppSettings(this);
        }

        btn_submit.setOnClickListener(this);
    }

    private void initViews() {
        et_complaint = findViewById(R.id.et_complaint);
        et_complaint_type = findViewById(R.id.et_complaint_type);
        et_vehicle_no = findViewById(R.id.et_vehicle_no);
        et_drivers_name = findViewById(R.id.et_drivers_name);
        et_type_a_complaint = findViewById(R.id.et_type_a_complaint);

        et_your_name = findViewById(R.id.et_your_name);
        et_your_email_id = findViewById(R.id.et_your_email_id);
        et_your_mobile_no = findViewById(R.id.et_your_mobile_no);

        btn_submit = findViewById(R.id.btn_submit);
    }

    public void initObjects() {
        mUiHelper = new UiHelper(this);
        mGpsTracker = new GPSTracker(this);
        mSharedPrefManager = SharedPrefManager.getInstance(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    mUiHelper.showToastLongCentre(getResources().getString(R.string.permission_denied));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getCurrentLocation() {
        if (mGpsTracker.canGetLocation()) {
            mLatitude = mGpsTracker.getLatitude();
            mLongitude = mGpsTracker.getLongitude();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                String complaint = et_complaint.getText().toString().trim();
                String complaintType = et_complaint_type.getText().toString().trim();
                String vehicleNo = et_vehicle_no.getText().toString().trim();
                String driversName = et_drivers_name.getText().toString().trim();
                String typeComplaint = et_type_a_complaint.getText().toString().trim();
                String yourName = et_your_name.getText().toString().trim();
                String yourEmailId = et_your_email_id.getText().toString().trim();
                String yourMobileNo = et_your_mobile_no.getText().toString().trim();
                if (complaint.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_complaint));
                    mUiHelper.requestFocus(et_complaint);
                } else if (complaintType.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_complaint_type));
                    mUiHelper.requestFocus(et_complaint_type);
                } else if (vehicleNo.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_vehicle_no));
                    mUiHelper.requestFocus(et_vehicle_no);
                } else if (driversName.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_driver_name));
                    mUiHelper.requestFocus(et_drivers_name);
                } else if (typeComplaint.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.type_a_complaint));
                    mUiHelper.requestFocus(et_type_a_complaint);
                } else if (yourName.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_your_name));
                    mUiHelper.requestFocus(et_your_name);
                } else if (yourEmailId.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_your_email_id));
                    mUiHelper.requestFocus(et_your_email_id);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(yourEmailId).matches()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_valid_email_id));
                    mUiHelper.requestFocus(et_your_email_id);
                } else if (yourMobileNo.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_your_mobile_no));
                    mUiHelper.requestFocus(et_your_mobile_no);
                } else if (!ValidationUtils.isValidMobile(yourMobileNo)) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_valid_mobile_no));
                    mUiHelper.requestFocus(et_your_mobile_no);
                } else {
                    saveAutocomplainData(complaint, complaintType, vehicleNo, driversName,
                            typeComplaint, yourName, yourEmailId, yourMobileNo);
                }
                break;
            default:
                break;
        }
    }

    // not finished
    public void saveAutocomplainData(String complaint, String complaintType, String vehicleNo, String driversName,
                                     String typeComplaint, String yourName, String yourEmailId, String yourMobileNo) {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        JSONObject jsonRequest;
        final String mRequestBody;
        Map<String, String> params = new HashMap<>();
        params.put(URLParams.comments, complaint);
        params.put(URLParams.type, complaintType);
        params.put(URLParams.vehicleNo, vehicleNo);
        params.put(URLParams.driverName, driversName);
        params.put(URLParams.complaintTravelBy, typeComplaint);
        params.put(URLParams.name, yourName);
        params.put(URLParams.email, yourEmailId);
        params.put(URLParams.mobileNo, yourMobileNo);
        params.put(URLParams.lat, String.valueOf(mLatitude));
        params.put(URLParams.lang, String.valueOf(mLongitude));
        params.put(URLParams.deviceId, HardwareUtils.getDeviceUUID(PublicComplaintsActivity.this));
        jsonRequest = new JSONObject(params);
        mRequestBody = jsonRequest.toString();

        VolleySingleton.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.POST,
                URLs.saveAutocomplainData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mUiHelper.dismissProgressDialog();
                        mUiHelper.showToastShortCentre(response);
                        Log.i(TAG, "response-->" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mUiHelper.dismissProgressDialog();
                mUiHelper.showToastShortCentre(getResources().getString(R.string.error));
                Log.i(TAG, "response-->" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(URLParams.jsonData, mRequestBody);
                return params;
            }
        });
    }
}
