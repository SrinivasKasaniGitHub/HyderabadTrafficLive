package com.tspolice.htplive.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RtaTowingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RtaTowingActivity-->";
    private UiHelper mUiHelper;
    private SharedPrefManager mSharedPrefManager;
    private Dialog towingRTAInfoDialog;
    private EditText et_vehicle_no, et_captcha, et_enter_above_captcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rta_towing);

        initViews();

        initObjects();

        getCaptchaForVehicleDetails();
    }

    private void initViews() {
        et_vehicle_no = findViewById(R.id.et_vehicle_no);
        et_captcha = findViewById(R.id.et_captcha);
        et_enter_above_captcha = findViewById(R.id.et_enter_above_captcha);
        Button btn_clear = findViewById(R.id.btn_clear);
        Button btn_get_data = findViewById(R.id.btn_get_data);
        btn_clear.setOnClickListener(this);
        btn_get_data.setOnClickListener(this);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
        mSharedPrefManager = SharedPrefManager.getInstance(this);
    }

    private void getCaptchaForVehicleDetails() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                URLs.getCaptchaForVehicleDetails, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"null".equals(response.toString()) && response.length() > 0) {
                            try {
                                String captcha = (String) response.get(0);
                                captcha = captcha.replaceAll(" ", "");
                                et_captcha.setText(captcha);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
                            } catch (Exception e) {
                                e.printStackTrace();
                                mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
                            }
                        } else {
                            mUiHelper.showToastShort(getResources().getString(R.string.empty_response));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mUiHelper.dismissProgressDialog();
                        mUiHelper.showToastShort(getResources().getString(R.string.error));
                    }
                }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:
                et_vehicle_no.setText("");
                et_vehicle_no.setHint(getResources().getString(R.string.enter_vehicle_no));
                et_enter_above_captcha.setText("");
                et_enter_above_captcha.setHint(getResources().getString(R.string.enter_above_captcha));
                getCaptchaForVehicleDetails();
                break;
            case R.id.btn_get_data:
                String vehicleNo = et_vehicle_no.getText().toString().trim();
                final String captcha = et_captcha.getText().toString().trim();
                Log.i(TAG, "onClick: captcha-->" + captcha);
                final String matchCaptcha = et_enter_above_captcha.getText().toString().trim();
                if (TextUtils.isEmpty(vehicleNo)) {
                    mUiHelper.showToastShort(getResources().getString(R.string.enter_vehicle_no));
                    et_vehicle_no.requestFocus();
                } else if (TextUtils.isEmpty(matchCaptcha)) {
                    mUiHelper.showToastShort(getResources().getString(R.string.enter_above_captcha));
                    et_enter_above_captcha.requestFocus();
                } else if (!matchCaptcha.equals(captcha)) {
                    mUiHelper.showToastShort(getResources().getString(R.string.enter_valid_captcha));
                    et_enter_above_captcha.requestFocus();
                } else {
                    getTowingDetails(vehicleNo);
                }
                break;
            case R.id.btn_dialog_close:
                towingRTAInfoDialog.dismiss();
                et_vehicle_no.setText("");
                et_vehicle_no.setHint(getResources().getString(R.string.enter_vehicle_no));
                et_enter_above_captcha.setText("");
                et_enter_above_captcha.setHint(getResources().getString(R.string.enter_above_captcha));
                getCaptchaForVehicleDetails();
                break;
            default:
                break;
        }
    }

    private void getTowingDetails(String vehicleNo) {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        String ctrl;
        if ("TOWING".equals(mSharedPrefManager.getString(Constants.RTA_TOWING))) {
            ctrl = "towing";
        } else {
            ctrl = "RTA";
        }
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                "" + URLs.getVehicleDetails("" + vehicleNo, "" + ctrl), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString()) && response.length() > 0) {
                            getTowingRTAInfoDialog(response);
                        } else {
                            mUiHelper.showToastShort(getResources().getString(R.string.empty_response));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mUiHelper.dismissProgressDialog();
                        mUiHelper.showToastLong(getResources().getString(R.string.error));
                    }
                }));

    }

    @SuppressLint("InflateParams")
    public void getTowingRTAInfoDialog(JSONObject response) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(RtaTowingActivity.this);
        LayoutInflater inflater = LayoutInflater.from(RtaTowingActivity.this);
        View view;
        if ("TOWING".equals(mSharedPrefManager.getString(Constants.RTA_TOWING))) {
            view = inflater.inflate(R.layout.dlg_towed_vehicle_details, null);
            TextView tv_regn_no = view.findViewById(R.id.tv_regn_no);
            TextView tv_owner_name = view.findViewById(R.id.tv_owner_name);
            TextView tv_detained_date = view.findViewById(R.id.tv_detained_date);
            TextView tv_trps_name = view.findViewById(R.id.tv_trps_name);
            TextView tv_location = view.findViewById(R.id.tv_location);
            TextView tv_officer_name = view.findViewById(R.id.tv_officer_name);
            TextView tv_trps_contact_no = view.findViewById(R.id.tv_trps_contact_no);
            Button btn_dialog_close = view.findViewById(R.id.btn_dialog_close);
            btn_dialog_close.setOnClickListener(this);
            try {
                String regNum = response.getString("regNum");
                if (regNum != null && !"".equals(regNum) && !"null".equals(regNum)) {
                    tv_regn_no.setText(regNum);
                } else {
                    tv_regn_no.setText("");
                }
                String ownerName = response.getString("ownerName");
                if (ownerName != null && !"".equals(ownerName) && !"null".equals(ownerName)) {
                    tv_owner_name.setText(ownerName);
                } else {
                    tv_owner_name.setText("");
                }
                String detainedDate = response.getString("detainedDate");
                if (detainedDate != null && !"".equals(detainedDate) && !"null".equals(detainedDate)) {
                    tv_detained_date.setText(detainedDate);
                } else {
                    tv_detained_date.setText("");
                }
                String trpsName = response.getString("trpsName");
                if (trpsName != null && !"".equals(trpsName) && !"null".equals(trpsName)) {
                    tv_trps_name.setText(trpsName);
                } else {
                    tv_trps_name.setText("");
                }
                String location = response.getString("location");
                if (location != null && !"".equals(location) && !"null".equals(location)) {
                    tv_location.setText(location);
                } else {
                    tv_location.setText("");
                }
                String officer = response.getString("officer");
                if (officer != null && !"".equals(officer) && !"null".equals(officer)) {
                    tv_officer_name.setText(officer);
                } else {
                    tv_officer_name.setText("");
                }
                String policeMobile = response.getString("policeMobile");
                if (policeMobile != null && !"".equals(policeMobile)
                        && !"null".equals(policeMobile) && policeMobile.contains(":")) {
                    String[] split = policeMobile.split(":");
                    tv_trps_contact_no.setText(split[1].trim());
                } else {
                    tv_trps_contact_no.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
            }
        } else {
            view = inflater.inflate(R.layout.dlg_vehicle_details, null);
            TextView tv_regn_no = view.findViewById(R.id.tv_regn_no);
            TextView tv_owner_name = view.findViewById(R.id.tv_owner_name);
            TextView tv_fuel_type = view.findViewById(R.id.tv_fuel_type);
            TextView tv_vehicle_color = view.findViewById(R.id.tv_vehicle_color);
            TextView tv_makers_name = view.findViewById(R.id.tv_makers_name);
            TextView tv_mfg_year = view.findViewById(R.id.tv_mfg_year);
            TextView tv_date_of_registration = view.findViewById(R.id.tv_date_of_registration);
            TextView tv_chassis_no = view.findViewById(R.id.tv_chassis_no);
            Button btn_dialog_close = view.findViewById(R.id.btn_dialog_close);
            try {
                String regNum = response.getString("regNum");
                if (regNum != null && !"".equals(regNum) && !"null".equals(regNum)) {
                    tv_regn_no.setText(regNum);
                } else {
                    tv_regn_no.setText("");
                }
                String fulelType = response.getString("fulelType");
                if (fulelType != null && !"".equals(fulelType) && !"null".equals(fulelType)) {
                    tv_fuel_type.setText(fulelType);
                } else {
                    tv_fuel_type.setText("");
                }
                String ownerName = response.getString("ownerName");
                if (fulelType != null && !"".equals(fulelType) && !"null".equals(fulelType)) {
                    tv_owner_name.setText(ownerName);
                } else {
                    tv_owner_name.setText("");
                }
                String makerName = response.getString("makerName");
                if (makerName != null && !"".equals(makerName) && !"null".equals(makerName)) {
                    tv_makers_name.setText(makerName);
                } else {
                    tv_makers_name.setText("");
                }
                String mfgyr = response.getString("mfgyr");
                if (mfgyr != null && !"".equals(mfgyr) && !"null".equals(mfgyr)) {
                    tv_mfg_year.setText(mfgyr);
                } else {
                    tv_mfg_year.setText("");
                }
                String dtOfReg = response.getString("dtOfReg");
                if (dtOfReg != null && !"".equals(dtOfReg) && !"null".equals(dtOfReg)) {
                    tv_date_of_registration.setText(dtOfReg);
                } else {
                    tv_date_of_registration.setText("");
                }
                String chasisNo = response.getString("chasisNo");
                if (chasisNo != null && !"".equals(chasisNo) && !"null".equals(chasisNo)) {
                    tv_chassis_no.setText(chasisNo);
                } else {
                    tv_chassis_no.setText("");
                }
                String color = response.getString("color");
                if (color != null && !"".equals(color) && !"null".equals(color)) {
                    tv_vehicle_color.setText(color);
                } else {
                    tv_vehicle_color.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
            }
            btn_dialog_close.setOnClickListener(this);
        }
        builder.setView(view);
        builder.setCancelable(false);
        towingRTAInfoDialog = builder.create();
        towingRTAInfoDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
