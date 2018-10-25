package com.tspolice.htplive.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.adapters.MyRecyclerViewItemDecoration;
import com.tspolice.htplive.adapters.TrPSInfoAdapter;
import com.tspolice.htplive.models.TrPSInfoModel;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.PermissionUtil;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrPSInfoActivity extends AppCompatActivity {

    private UiHelper mUiHelper;
    //private Toolbar mainToolbar;
    private EditText et_search_traffic_ps_name;
    private RecyclerView mRecyclerView;
    private TrPSInfoAdapter mTrPSInfoAdapter;
    private List<TrPSInfoModel> mTrPSInfoList;
    private TrPSInfoModel mTrPSInfoModel;
    private SharedPrefManager mSharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trps_info);

        initViews();

        initObjects();

        //toolbarSetup();

        recyclerViewSetup();

        getHydPoliceStations();

        et_search_traffic_ps_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    public void initViews() {
        //mainToolbar = findViewById(R.id.toolbar);
        et_search_traffic_ps_name = findViewById(R.id.et_search_traffic_ps_name);
        mRecyclerView = findViewById(R.id.mRecyclerViewTrPSInfo);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
        mSharedPrefManager = SharedPrefManager.getInstance(this);
    }

    /*private void toolbarSetup() {
        if (mainToolbar != null) {
            setSupportActionBar(mainToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle("Search Traffic PS");
            }
            mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }*/

    private void recyclerViewSetup() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setSelected(true);
        mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(this, DividerItemDecoration.VERTICAL, 8));
    }

    private void getHydPoliceStations() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                URLs.getHydPoliceStations, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString()) && response.length() > 0) {
                            try {
                                mTrPSInfoList = new ArrayList<>(response.length());
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    TrPSInfoModel trPSInfoModel = new TrPSInfoModel();
                                    trPSInfoModel.setId(Integer.parseInt(jsonObject.getString("id")));
                                    trPSInfoModel.setCreatedDate(jsonObject.getString("createdDate"));
                                    trPSInfoModel.setUpdatedDate(jsonObject.getString("updatedDate"));
                                    trPSInfoModel.setStationName(jsonObject.getString("stationName"));
                                    trPSInfoModel.setMobileNumber(jsonObject.getString("mobileNumber"));
                                    trPSInfoModel.setLanguage(jsonObject.getString("language"));
                                    trPSInfoModel.setLatitude(jsonObject.getString("latitude"));
                                    trPSInfoModel.setLongitude(jsonObject.getString("langitude"));
                                    trPSInfoModel.setMobileAppSimNo(jsonObject.getString("mobileAppSimNo"));
                                    trPSInfoModel.setInspectorNo(jsonObject.getString("inspectorNo"));
                                    trPSInfoModel.setAcpNo(jsonObject.getString("acpNo"));
                                    trPSInfoModel.setConstableNo2(jsonObject.getString("constableNo2"));
                                    trPSInfoModel.setConstableNo3(jsonObject.getString("constableNo3"));
                                    trPSInfoModel.setConstableNo4(jsonObject.getString("constableNo4"));
                                    mTrPSInfoList.add(trPSInfoModel);
                                }
                                mTrPSInfoAdapter = new TrPSInfoAdapter(mTrPSInfoList, new TrPSInfoAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(TrPSInfoModel item, int position) {
                                        mTrPSInfoModel = item;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            if (PermissionUtil.checkPermission(TrPSInfoActivity.this, Constants.INT_CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                if (ActivityCompat.shouldShowRequestPermissionRationale(TrPSInfoActivity.this, Manifest.permission.CALL_PHONE)) {
                                                    PermissionUtil.showPermissionExplanation(TrPSInfoActivity.this, Constants.INT_CALL_PHONE);
                                                } else if (!mSharedPrefManager.getBoolean(Constants.CALL_PHONE)) {
                                                    PermissionUtil.requestPermission(TrPSInfoActivity.this, Constants.INT_CALL_PHONE);
                                                    mSharedPrefManager.putBoolean(Constants.CALL_PHONE, true);
                                                } else {
                                                    PermissionUtil.redirectAppSettings(TrPSInfoActivity.this);
                                                }
                                            } else {
                                                makeCall();
                                            }
                                        } else {
                                            PermissionUtil.redirectAppSettings(TrPSInfoActivity.this);
                                        }
                                    }
                                }, TrPSInfoActivity.this);
                                mRecyclerView.setAdapter(mTrPSInfoAdapter);
                            } catch (JSONException e) {
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

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "+" + mTrPSInfoModel.getMobileNumber()));
        if (PermissionUtil.checkPermission(TrPSInfoActivity.this, Constants.INT_CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    mUiHelper.showToastShort(getResources().getString(R.string.permission_denied));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void filter(String text) {
        List<TrPSInfoModel> filteredNames = new ArrayList<>();
        for (TrPSInfoModel model : mTrPSInfoList) {
            if (model.getStationName().toLowerCase().contains(text.toLowerCase())) {
                filteredNames.add(model);
            }
        }
        mTrPSInfoAdapter.filterList(filteredNames);
    }

    @Override
    public void onBackPressed() {
        if (et_search_traffic_ps_name.getText().toString().length()>0) {
            et_search_traffic_ps_name.setText("");
            et_search_traffic_ps_name.setHint(R.string.search_traffic_ps_name);
        } else {
            //super.onBackPressed();
            finish();
        }
    }
}
