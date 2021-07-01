package com.tspolice.htplive.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.adapters.PendingChlnDlg;
import com.tspolice.htplive.models.PendingChallanModel;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PendingChallansActivity extends AppCompatActivity {

    EditText et_vehicle_no;
    private UiHelper mUiHelper;
    Button btn_submit;
    ArrayList<PendingChallanModel> pendingChallanModels = new ArrayList<>();
    PendingChlnDlg pendingChlnDlg;
    ArrayList<PendingChallanModel> mArrayList_SelectedVltnLst = new ArrayList<>();
    ArrayList<Integer> preVltnSelectdIds = new ArrayList<>();
    public static String strNoChlns = "", str_PAmnt = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challans);
        mUiHelper = new UiHelper(this);
        et_vehicle_no = findViewById(R.id.et_vehicle_no);
        btn_submit = findViewById(R.id.btn_submit);
        et_vehicle_no.setText("TS09EY9950");
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_vehicle_no.getText().toString().isEmpty()) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.enter_vehicle_no));
                    et_vehicle_no.requestFocus();
                } else {
                    getPendingChallans(et_vehicle_no.getText().toString().trim());
                }
            }
        });

    }

    private void getPendingChallans(final String vehicleNo) {
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
                                JSONObject jb = response.getJSONObject("PendChallansByRegn");
                                strNoChlns = jb.getString("NoOfPendingChallans");
                                str_PAmnt = jb.getString("totalPendingAmount");

                                String code = response.getString("ResponseCode");
                                if ("0".equalsIgnoreCase(code)) {
                                    JSONArray jsonArray = response.getJSONArray("PendChallansDetailsByRegn");
                                    pendingChallanModels = new ArrayList<>(jsonArray.length());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        PendingChallanModel model = new PendingChallanModel();
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        model.setUnitName("" + obj.getString("UnitName"));
                                        model.setChallanNo("" + obj.getString("ChallanNo"));
                                        model.setDate("" + obj.getString("Date"));
                                        model.setPointName("" + obj.getString("PointName"));
                                        model.setPSName("" + obj.getString("PSName"));
                                        model.setCompoundingAmount("" + obj.getString("CompoundingAmount"));
                                        JSONArray array = obj.getJSONArray("ViolationDetails");
                                        ArrayList<String> arrayList = new ArrayList<>(array.length());
                                        for (int j = 0; j < array.length(); j++) {
                                            JSONObject jsonObject = array.getJSONObject(j);
                                            arrayList.add(jsonObject.getString("ViolationType") + "\n");
                                        }
                                        model.setViolations("" + getDataListString(arrayList));
                                        pendingChallanModels.add(model);
                                    }
                                    showChallnsList(pendingChallanModels, vehicleNo);

                                } else {
                                    mUiHelper.showToastLongCentre("No Pending Challans ");
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

    private void showChallnsList(ArrayList<PendingChallanModel> listModels, final String vehNo) {
        mArrayList_SelectedVltnLst = new ArrayList<>();
        PendingChlnDlg.selectedIdsForCallback.clear();
        pendingChlnDlg = new PendingChlnDlg()
                .title(getResources().getString(R.string.txt_pateints))
                .titleSize(16)
                .positiveText("Payment")
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
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLs.paymentChalnsUrl + vehNo));
                        startActivity(browserIntent);

                    }

                    @Override
                    public void onCancel() {
                        Log.d("Spot", "Dialog cancelled");

                    }
                });
        pendingChlnDlg.show(getSupportFragmentManager(), "MultiSelectDlg");
    }
}
