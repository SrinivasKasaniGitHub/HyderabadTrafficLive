package com.tspolice.htplive.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.models.ViolationsModel;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class TrViolationsActivity extends AppCompatActivity {

    private UiHelper mUiHelper;
    private List<ViolationsModel> violationsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_violations);

        sectionMasterByWheeler();
    }

    private void sectionMasterByWheeler() {
        mUiHelper = new UiHelper(TrViolationsActivity.this);
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(TrViolationsActivity.this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                URLs.sectionMasterByWheeler("2"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("respCode") == 1) {
                                loadTrViolationsTable(response);
                            } else {
                                mUiHelper.showToastShortCentre(getResources().getString(R.string.empty_response));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mUiHelper.dismissProgressDialog();
                            mUiHelper.showToastShortCentre(getResources().getString(R.string.something_went_wrong));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String jsonFile = mUiHelper.loadJSONFromAssets("trafficViolations.json");
                    if (jsonFile != null && !"".equals(jsonFile)) {
                        loadTrViolationsTable(new JSONObject(jsonFile));
                    } else {
                        mUiHelper.showToastShortCentre(getResources().getString(R.string.empty_response));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mUiHelper.dismissProgressDialog();
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.something_went_wrong));
                }
            }
        }));
    }

    private void loadTrViolationsTable(JSONObject response) {
        TableLayout tableLayoutViolations = findViewById(R.id.tbl_traffic_violations);
        tableLayoutViolations.removeAllViews();

        TableRow tableRow = new TableRow(TrViolationsActivity.this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        final int tableTextSize = 18, textPadding = 6;

        TextView tv_s_no = new TextView(TrViolationsActivity.this);
        tv_s_no.setText(getResources().getString(R.string.s_no));
        tv_s_no.setBackgroundResource(R.drawable.cell_heading);
        tv_s_no.setGravity(Gravity.CENTER);
        tv_s_no.setTextColor(Color.WHITE);
        tv_s_no.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tv_s_no.setLayoutParams(layoutParams);
        tv_s_no.setPadding(textPadding, textPadding, textPadding, textPadding);
        tv_s_no.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
        tableRow.addView(tv_s_no);

        TextView tv_name_of_the_offence = new TextView(TrViolationsActivity.this);
        tv_name_of_the_offence.setText(getResources().getString(R.string.name_of_the_offence));
        tv_name_of_the_offence.setBackgroundResource(R.drawable.cell_heading);
        tv_name_of_the_offence.setGravity(Gravity.CENTER);
        tv_name_of_the_offence.setTextColor(Color.WHITE);
        tv_name_of_the_offence.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tv_name_of_the_offence.setLayoutParams(layoutParams);
        tv_name_of_the_offence.setPadding(textPadding, textPadding, textPadding, textPadding);
        tv_name_of_the_offence.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
        tableRow.addView(tv_name_of_the_offence);

        TextView tv_penal_section = new TextView(TrViolationsActivity.this);
        tv_penal_section.setText(getResources().getString(R.string.penal_section));
        tv_penal_section.setBackgroundResource(R.drawable.cell_heading);
        tv_penal_section.setGravity(Gravity.CENTER);
        tv_penal_section.setTextColor(Color.WHITE);
        tv_penal_section.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tv_penal_section.setLayoutParams(layoutParams);
        tv_penal_section.setPadding(textPadding, textPadding, textPadding, textPadding);
        tv_penal_section.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
        tableRow.addView(tv_penal_section);

        TextView tv_amount = new TextView(TrViolationsActivity.this);
        tv_amount.setText(getResources().getString(R.string.amount));
        tv_amount.setBackgroundResource(R.drawable.cell_heading);
        tv_amount.setGravity(Gravity.CENTER);
        tv_amount.setTextColor(Color.WHITE);
        tv_amount.setLayoutParams(layoutParams);
        tv_amount.setPadding(textPadding, textPadding, textPadding, textPadding);
        tv_amount.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
        tv_amount.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tableRow.addView(tv_amount);

        tableLayoutViolations.addView(tableRow, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        try {
            JSONArray jsonArray = response.getJSONArray("respRemark");
            violationsList = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ViolationsModel violationsModel = new ViolationsModel();
                violationsModel.setOffenceCd(jsonObject.getInt("offence_cd"));
                violationsModel.setSection(jsonObject.getString("section"));
                violationsModel.setOffenceDesc(jsonObject.getString("offence_desc"));
                violationsModel.setFineMin(jsonObject.getString("fine_min"));
                violationsModel.setFineMax(jsonObject.getString("fine_max"));
                violationsModel.setPenaltyPoints(jsonObject.getString("penalty_points"));
                violationsList.add(violationsModel);

                TableRow tableRow1 = new TableRow(TrViolationsActivity.this);
                tableRow1.setLayoutParams(layoutParams);
                tableRow1.setClickable(true);

                for (int j = 1; j <= 4; j++) {
                    if (j == 1) {
                        TextView tv = new TextView(TrViolationsActivity.this);
                        tv.setLayoutParams(layoutParams);
                        tv.setBackgroundResource(R.drawable.cell_shape);
                        tv.setPadding(textPadding, textPadding, textPadding, textPadding);
                        String wrapInt = String.valueOf(i + 1);
                        tv.setText(wrapInt);
                        tv.setTextColor(Color.BLACK);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
                        tableRow1.addView(tv);
                    } else if (j == 2) {
                        TextView tv = new TextView(TrViolationsActivity.this);
                        tv.setLayoutParams(layoutParams);
                        tv.setBackgroundResource(R.drawable.cell_shape);
                        tv.setPadding(textPadding, textPadding, textPadding, textPadding);
                        tv.setText(violationsModel.getOffenceDesc());
                        tv.setTextColor(Color.BLACK);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
                        tableRow1.addView(tv);
                    } else if (j == 3) {
                        TextView tv = new TextView(TrViolationsActivity.this);
                        tv.setLayoutParams(layoutParams);
                        tv.setBackgroundResource(R.drawable.cell_shape);
                        tv.setPadding(textPadding, textPadding, textPadding, textPadding);
                        tv.setText(violationsModel.getSection());
                        tv.setTextColor(Color.BLACK);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
                        tableRow1.addView(tv);
                    } else {
                        TextView tv = new TextView(TrViolationsActivity.this);
                        tv.setLayoutParams(layoutParams);
                        tv.setBackgroundResource(R.drawable.cell_shape);
                        tv.setPadding(textPadding, textPadding, textPadding, textPadding);
                        tv.setText(violationsModel.getFineMax());
                        tv.setTextColor(Color.BLACK);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
                        tableRow1.addView(tv);
                    }
                }
                tableLayoutViolations.addView(tableRow1);
            }
            mUiHelper.dismissProgressDialog();
        } catch (JSONException e) {
            e.printStackTrace();
            mUiHelper.dismissProgressDialog();
            mUiHelper.showToastShortCentre(getResources().getString(R.string.something_went_wrong));
        }
    }

}
