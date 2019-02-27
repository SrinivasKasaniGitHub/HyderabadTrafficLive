package com.tspolice.htplive.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.adapters.AlertsAdapter;
import com.tspolice.htplive.models.AlertsModel;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlertsActivity extends AppCompatActivity {

    private UiHelper mUiHelper;
    private RecyclerView mRecyclerView;
    private List<AlertsModel> mAlertsList;
    private AlertsAdapter mAlertsAdapter;
    private String lastId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        mRecyclerView = findViewById(R.id.mRecyclerViewAlerts);
        TextView tv_load_more = findViewById(R.id.tv_load_more);

        mUiHelper = new UiHelper(AlertsActivity.this);
        mAlertsList = new ArrayList<>();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setSelected(true);
        //mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(AlertsActivity.this, DividerItemDecoration.VERTICAL, 8));

        getPublicAdvisaryData();

        tv_load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPubAdvNextRec(lastId);
            }
        });
    }

    private void getPublicAdvisaryData() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(AlertsActivity.this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                URLs.getPublicAdvisaryData, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString())
                                && !"null".equals(response.toString()) && response.length() > 0) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    AlertsModel model = new AlertsModel();
                                    model.setId(jsonObject.getString("id"));
                                    String advice = jsonObject.getString("advise");
                                    try {
                                        String[] advices = advice.split("\n\n");
                                        model.setAdvise(advices[1]);
                                        //model.setUpdatedDate(jsonObject.getString("updatedDate"));
                                        String[] sArray = advices[0].split("at");
                                        model.setUpdatedDate(sArray[0].trim() + "\n" + sArray[1].trim());
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                        model.setAdvise("");
                                        model.setUpdatedDate("");
                                    }
                                    mAlertsList.add(model);
                                    if (i == 2) {
                                        lastId = mAlertsList.get(i).getId();
                                        Log.i("lastId-->", lastId);
                                    }
                                }
                                mAlertsAdapter = new AlertsAdapter(mAlertsList, AlertsActivity.this);
                                mRecyclerView.setAdapter(mAlertsAdapter);
                            } catch (JSONException e) {
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

    private void loadPubAdvNextRec(final String id1) {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(AlertsActivity.this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                URLs.loadPubAdvNextRec(id1), null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString())
                                && !"null".equals(response.toString()) && response.length() > 0) {
                            try {
                                ArrayList<String> arrayList = new ArrayList<>();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    AlertsModel model = new AlertsModel();
                                    model.setId(jsonObject.getString("id"));
                                    arrayList.add(jsonObject.getString("id"));
                                    String advice = jsonObject.getString("advise");
                                    try {
                                        String[] advices = advice.split("\n\n");
                                        model.setAdvise(advices[1]);
                                        //model.setUpdatedDate(jsonObject.getString("updatedDate"));
                                        String[] sArray = advices[0].split("at");
                                        model.setUpdatedDate(sArray[0].trim() + "\n" + sArray[1].trim());
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                        model.setAdvise("");
                                        model.setUpdatedDate("");
                                    }
                                    mAlertsList.add(model);
                                    if (i == 2) {
                                        lastId = arrayList.get(i);
                                        Log.i("lastIdAgain-->", lastId);
                                    }
                                }
                                mAlertsAdapter = new AlertsAdapter(mAlertsList, AlertsActivity.this);
                                mRecyclerView.setAdapter(mAlertsAdapter);
                                mAlertsAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
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
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
