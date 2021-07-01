package com.tspolice.htplive.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.adapters.PendingChlnDlg;
import com.tspolice.htplive.captcha.MathCaptcha;
import com.tspolice.htplive.captcha.TextCaptcha;
import com.tspolice.htplive.models.PendingChallanModel;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RtaTowingActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "RtaTowingActivity-->";
    private UiHelper mUiHelper;
    private SharedPrefManager mSharedPrefManager;
    private Dialog towingRTAInfoDialog;
    private TextView tv_rta_towing;
    private EditText et_vehicle_no, et_captcha, et_enter_above_captcha;
    private ImageView img_refresh;
    LinearLayout lyt_ChassisNo;
    ArrayList<PendingChallanModel> pendingChallanModels = new ArrayList<>();
    PendingChlnDlg pendingChlnDlg;
    ArrayList<PendingChallanModel> mArrayList_SelectedVltnLst = new ArrayList<>();
    ArrayList<Integer> preVltnSelectdIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rta_towing);

        initViews();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ITCBLKAD.TTF");
        et_captcha.setTypeface(typeface);

        initObjects();

        if (Constants.RTA.equals(mSharedPrefManager.getString(Constants.RTA_TOWING))) {
            tv_rta_towing.setText(getResources().getString(R.string.vehicle_details));
            lyt_ChassisNo.setVisibility(View.VISIBLE);
        } else {
            tv_rta_towing.setText(getResources().getString(R.string.towed_vehicle_details));
            lyt_ChassisNo.setVisibility(View.GONE);
        }

        // getCaptchaForVehicleDetails();
    }

    private void initViews() {
        tv_rta_towing = findViewById(R.id.tv_rta_towing);
        et_vehicle_no = findViewById(R.id.et_vehicle_no);
        et_captcha = findViewById(R.id.et_captcha);
        et_enter_above_captcha = findViewById(R.id.et_enter_above_captcha);
        lyt_ChassisNo = findViewById(R.id.lyt_ChassisNo);
        img_refresh = findViewById(R.id.img_refresh);
        Button btn_submit = findViewById(R.id.btn_submit);
        img_refresh.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
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
                        if (response != null && !"".equals(response.toString())
                                && !"null".equals(response.toString()) && response.length() > 0) {
                            try {
                                String captcha = (String) response.get(0);
                                captcha = captcha.replaceAll(" ", "");
                                et_captcha.setText(captcha);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mUiHelper.showToastShortCentre(getResources().getString(R.string.something_went_wrong));
                            } catch (Exception e) {
                                e.printStackTrace();
                                mUiHelper.showToastShortCentre(getResources().getString(R.string.something_went_wrong));
                            }
                        } else {
                            mUiHelper.showToastShortCentre(getResources().getString(R.string.empty_response));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mUiHelper.dismissProgressDialog();
                mUiHelper.showToastShortCentre(getResources().getString(R.string.error));
            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_refresh:
                //et_vehicle_no.setText("");
                //et_vehicle_no.setHint(getResources().getString(R.string.enter_vehicle_no));
                et_enter_above_captcha.setText("");
                et_enter_above_captcha.setHint(getResources().getString(R.string.enter_above_captcha));
                getCaptchaForVehicleDetails();
                break;
            case R.id.btn_submit:
                final String vehicleNo = et_vehicle_no.getText().toString().trim();
                final String captcha = et_captcha.getText().toString().trim();
                final String chasisNo = et_enter_above_captcha.getText().toString().trim();
                if (TextUtils.isEmpty(vehicleNo)) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.enter_vehicle_no));
                    et_vehicle_no.requestFocus();
                } else if (TextUtils.isEmpty(chasisNo) && !Constants.TOWING.equals(mSharedPrefManager.getString(Constants.RTA_TOWING))) {
                    mUiHelper.showToastShortCentre("Enter last 5 digits of Chassis No");
                    et_enter_above_captcha.requestFocus();
                } else {


                        getTowingDetails(vehicleNo, chasisNo);

                }
                break;
            case R.id.btn_dialog_rta_details_close:
                towingRTAInfoDialog.dismiss();
                et_vehicle_no.setText("");
                et_vehicle_no.setHint(getResources().getString(R.string.enter_vehicle_no));
                et_enter_above_captcha.setText("");
                et_enter_above_captcha.setHint("Enter last 5 digits of Chassis No");

                break;

            case R.id.img_towed_vehicle_details_close:
                towingRTAInfoDialog.dismiss();
                et_vehicle_no.setText("");
                et_vehicle_no.setHint(getResources().getString(R.string.enter_vehicle_no));
                et_enter_above_captcha.setText("");
                et_enter_above_captcha.setHint("Enter last 5 digits of Chassis No");

            case R.id.img_vehicle_details_close:
                towingRTAInfoDialog.dismiss();
                et_vehicle_no.setText("");
                et_vehicle_no.setHint(getResources().getString(R.string.enter_vehicle_no));
                et_enter_above_captcha.setText("");
                et_enter_above_captcha.setHint("Enter last 5 digits of Chassis No");

                break;
            default:
                break;
        }
    }


    private void getPendingChallans(String vehicleNo) {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        String url;
        url = URLs.pendingChalnsUrl + vehicleNo;

        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                "" + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString())
                                && !"null".equals(response.toString()) && response.length() > 0) {
                            try {
                                String code = response.getString("ResponseCode");
                                if ("0".equalsIgnoreCase(code)) {
                                    JSONArray jsonArray = response.getJSONArray("PendChallansDetailsByRegn");
                                    pendingChallanModels = new ArrayList<>(jsonArray.length());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        PendingChallanModel model = new PendingChallanModel();
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        model.setUnitName("" + obj.getString("UnitName"));
                                        model.setUnitName("" + obj.getString("ChallanNo"));
                                        model.setUnitName("" + obj.getString("Date"));
                                        model.setUnitName("" + obj.getString("PointName"));
                                        model.setUnitName("" + obj.getString("PSName"));
                                        model.setUnitName("" + obj.getString("CompoundingAmount"));
                                        JSONArray array = obj.getJSONArray("ViolationDetails");
                                        ArrayList<String> arrayList = new ArrayList<>(array.length());
                                        for (int j = 0; j < array.length(); j++) {
                                            JSONObject jsonObject = array.getJSONObject(j);
                                            arrayList.add(jsonObject.getString("ViolationType") + "\n");
                                        }
                                        model.setViolations("" + getDataListString(arrayList));
                                        pendingChallanModels.add(model);
                                    }
                                    showChallnsList(pendingChallanModels);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            mUiHelper.showToastShortCentre(getResources().getString(R.string.empty_response));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mUiHelper.dismissProgressDialog();
                mUiHelper.showToastShortCentre(getResources().getString(R.string.error));
            }
        }));
    }

    public String getDataListString(ArrayList<String> list) {
        String data = "";
        for (int i = 0; i < list.size(); i++) {

            data = data + " " + list.get(i);

        }
        return data;
    }

    private void showChallnsList(ArrayList<PendingChallanModel> listModels) {
        mArrayList_SelectedVltnLst = new ArrayList<>();
        PendingChlnDlg.selectedIdsForCallback.clear();
        pendingChlnDlg = new PendingChlnDlg()
                .title(getResources().getString(R.string.txt_pateints))
                .titleSize(16)
                .positiveText("OK")
                .negativeText("Cancel")
                .setMinSelectionLimit(0)
                .setMaxSelectionLimit(listModels.size())
                .preSelectIDsList(preVltnSelectdIds) // List of ids that you need to be selected
                .multiSelectList(listModels) // the multi select model list with ids and name
                .onSubmit(new PendingChlnDlg.SubmitCallbackListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSelected(final ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        mArrayList_SelectedVltnLst = new ArrayList<>(selectedIds.size());
                        preVltnSelectdIds = selectedIds;
                        pendingChlnDlg.dismiss();

                    }

                    @Override
                    public void onCancel() {
                        Log.d("Spot", "Dialog cancelled");

                    }
                });
        pendingChlnDlg.show(getSupportFragmentManager(), "MultiSelectDlg");
    }

    private void getTowingDetails(String vehicleNo, String chassisNo) {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        String url;
        if ("TOWING".equals(mSharedPrefManager.getString(Constants.RTA_TOWING))) {
            url = URLs.rootLocalUrl + "getTowingVehicle?regNo=" + vehicleNo;

        } else {

            url = URLs.rootLocalUrl + "getRTADetails?regNo=" + vehicleNo + "&chasNo=" + chassisNo;
        }
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                "" + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString())
                                && !"null".equals(response.toString()) && response.length() > 0) {
                            getTowingRTAInfoDialog(response);
                        } else {
                            mUiHelper.showToastShortCentre(getResources().getString(R.string.empty_response));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mUiHelper.dismissProgressDialog();
                mUiHelper.showToastShortCentre(getResources().getString(R.string.error));
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

            ImageView img_towed_vehicle_details_close = view.findViewById(R.id.img_towed_vehicle_details_close);

            TextView tv_regn_no = view.findViewById(R.id.tv_regn_no);
            TextView tv_owner_name = view.findViewById(R.id.tv_owner_name);
            TextView tv_detained_date = view.findViewById(R.id.tv_detained_date);
            TextView tv_trps_name = view.findViewById(R.id.tv_trps_name);
            TextView tv_location = view.findViewById(R.id.tv_location);
            TextView tv_officer_name = view.findViewById(R.id.tv_officer_name);
            TextView tv_trps_contact_no = view.findViewById(R.id.tv_trps_contact_no);
            Button btn_dialog_towed_detials_close = view.findViewById(R.id.btn_dialog_towed_detials_close);

            btn_dialog_towed_detials_close.setOnClickListener(this);
            img_towed_vehicle_details_close.setOnClickListener(this);

            try {
                if (response.getString("RESPONSE_CODE").equalsIgnoreCase("1")) {

                    String regNum = response.getString("REGN_NO");
                    if (regNum != null && !"".equals(regNum) && !"null".equals(regNum)) {
                        tv_regn_no.setText(regNum);
                    } else {
                        tv_regn_no.setText("");
                    }
                   /* String ownerName = response.getString("O_NAME");
                    if (ownerName != null && !"".equals(ownerName) && !"null".equals(ownerName)) {
                        tv_owner_name.setText(ownerName);
                    } else {
                        tv_owner_name.setText("");
                    }*/
                    String detainedDate = response.getString("DETAINED_DT");
                    if (detainedDate != null && !"".equals(detainedDate) && !"null".equals(detainedDate)) {
                        tv_detained_date.setText(detainedDate);
                    } else {
                        tv_detained_date.setText("");
                    }
                    String trpsName = response.getString("PS_JURIS");
                    if (trpsName != null && !"".equals(trpsName) && !"null".equals(trpsName)) {
                        tv_trps_name.setText(trpsName);
                    } else {
                        tv_trps_name.setText("");
                    }
                    String location = response.getString("POINT_NAME");
                    if (location != null && !"".equals(location) && !"null".equals(location)) {
                        tv_location.setText(location);
                    } else {
                        tv_location.setText("");
                    }
                    String officer = response.getString("DTN_BY_PID_NAME");
                    if (officer != null && !"".equals(officer) && !"null".equals(officer)) {
                        tv_officer_name.setText(officer);
                    } else {
                        tv_officer_name.setText("");
                    }
                    String policeMobile = response.getString("CONTACT_NO");
                    if (policeMobile != null && !"".equals(policeMobile)
                            && !"null".equals(policeMobile)) {

                        tv_trps_contact_no.setText(policeMobile);
                    } else {
                        tv_trps_contact_no.setText("");
                    }
                } else {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.something_went_wrong));
                }


            } catch (JSONException e) {
                e.printStackTrace();
                mUiHelper.showToastShortCentre(getResources().getString(R.string.something_went_wrong));
            }
        } else {
            view = inflater.inflate(R.layout.dlg_vehicle_details, null);
            ImageView img_vehicle_details_close = view.findViewById(R.id.img_vehicle_details_close);
            TextView tv_regn_no = view.findViewById(R.id.tv_regn_no);
            TextView tv_owner_name = view.findViewById(R.id.tv_owner_name);
            TextView tv_fuel_type = view.findViewById(R.id.tv_fuel_type);
            TextView tv_vehicle_color = view.findViewById(R.id.tv_vehicle_color);
            TextView tv_makers_name = view.findViewById(R.id.tv_makers_name);
            TextView tv_mfg_year = view.findViewById(R.id.tv_mfg_year);
            TextView tv_date_of_registration = view.findViewById(R.id.tv_date_of_registration);
            TextView tv_chassis_no = view.findViewById(R.id.tv_chassis_no);
            Button btn_dialog_rta_details_close = view.findViewById(R.id.btn_dialog_rta_details_close);

            btn_dialog_rta_details_close.setOnClickListener(this);
            img_vehicle_details_close.setOnClickListener(this);

            try {
                if (response.getString("RESPONSE_CODE").equalsIgnoreCase("1")) {

                    String regNum = response.getString("REGN_NO");
                    if (regNum != null && !"".equals(regNum) && !"null".equals(regNum)) {
                        tv_regn_no.setText(regNum);
                    } else {
                        tv_regn_no.setText("");
                    }
                    String fulelType = response.getString("FUEL");
                    if (fulelType != null && !"".equals(fulelType) && !"null".equals(fulelType)) {
                        tv_fuel_type.setText(fulelType);
                    } else {
                        tv_fuel_type.setText("");
                    }
                    String ownerName = response.getString("O_NAME");
                    if (fulelType != null && !"".equals(fulelType) && !"null".equals(fulelType)) {
                        tv_owner_name.setText(ownerName);
                    } else {
                        tv_owner_name.setText("");
                    }
                    String makerName = response.getString("MKR_NAME");
                    if (makerName != null && !"".equals(makerName) && !"null".equals(makerName)) {
                        tv_makers_name.setText(makerName);
                    } else {
                        tv_makers_name.setText("");
                    }
                    String mfgyr = response.getString("MKR_CLAS");
                    if (mfgyr != null && !"".equals(mfgyr) && !"null".equals(mfgyr)) {
                        tv_mfg_year.setText(mfgyr);
                    } else {
                        tv_mfg_year.setText("");
                    }
               /* String dtOfReg = response.getString("dtOfReg");
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
                }*/
                    String color = response.getString("COLOUR");
                    if (color != null && !"".equals(color) && !"null".equals(color)) {
                        tv_vehicle_color.setText(color);
                    } else {
                        tv_vehicle_color.setText("");
                    }
                } else {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.something_went_wrong));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mUiHelper.showToastShortCentre(getResources().getString(R.string.something_went_wrong));
            }
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
