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
import com.tspolice.htplive.adapters.CommonRecyclerAdapter;
import com.tspolice.htplive.adapters.MyRecyclerViewItemDecoration;
import com.tspolice.htplive.models.CommonModel;
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

public class TrOfficersContactsActivity extends AppCompatActivity {

    private UiHelper mUiHelper;
    private EditText et_search_tr_officers;
    private RecyclerView mRecyclerView;
    private CommonRecyclerAdapter mCommonRecyclerAdapter;
    private List<CommonModel> mCommonList;
    private CommonModel mCommonModel;
    private SharedPrefManager mSharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tr_officers_contacts);

        initViews();

        initObjects();

        getTrafficOfficers();

        et_search_tr_officers.addTextChangedListener(new TextWatcher() {
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
        et_search_tr_officers = findViewById(R.id.et_search_tr_officers);
        mRecyclerView = findViewById(R.id.mRecyclerViewTrOfficers);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setSelected(true);
        mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(this, DividerItemDecoration.VERTICAL, 8));
    }

    private void initObjects() {
        mSharedPrefManager = SharedPrefManager.getInstance(this);
        mUiHelper = new UiHelper(this);
    }

    public void getTrafficOfficers() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                URLs.getTrafficOfficers, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString()) && response.length() > 0) {
                            try {
                                mCommonList = new ArrayList<>(response.length());
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    CommonModel model = new CommonModel();
                                    model.setId(Integer.parseInt(jsonObject.getString("id")));
                                    model.setCreatedDate(jsonObject.getString("createdDate"));
                                    model.setUpdatedDate(jsonObject.getString("updatedDate"));
                                    model.setName(jsonObject.getString("name"));
                                    model.setDesignation(jsonObject.getString("designation"));
                                    model.setMobileNumber(jsonObject.getString("mobileNumber"));
                                    model.setPhotoPath(jsonObject.getString("photoPath"));
                                    model.setLanguage(jsonObject.getString("language"));
                                    mCommonList.add(model);
                                }
                                mCommonRecyclerAdapter = new CommonRecyclerAdapter(""+Constants.TRAFFIC_OFFICERS, mCommonList,
                                        new CommonRecyclerAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(CommonModel item, int position) {
                                                mCommonModel = item;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    if (PermissionUtil.checkPermission(TrOfficersContactsActivity.this, Constants.INT_CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                        if (ActivityCompat.shouldShowRequestPermissionRationale(TrOfficersContactsActivity.this, Manifest.permission.CALL_PHONE)) {
                                                            PermissionUtil.showPermissionExplanation(TrOfficersContactsActivity.this, Constants.INT_CALL_PHONE);
                                                        } else if (!mSharedPrefManager.getBoolean(Constants.CALL_PHONE)) {
                                                            PermissionUtil.requestPermission(TrOfficersContactsActivity.this, Constants.INT_CALL_PHONE);
                                                            mSharedPrefManager.putBoolean(Constants.CALL_PHONE, true);
                                                        } else {
                                                            PermissionUtil.redirectAppSettings(TrOfficersContactsActivity.this);
                                                        }
                                                    } else {
                                                        makeCall();
                                                    }
                                                } else {
                                                    PermissionUtil.redirectAppSettings(TrOfficersContactsActivity.this);
                                                }
                                            }
                                        }, TrOfficersContactsActivity.this);
                                mRecyclerView.setAdapter(mCommonRecyclerAdapter);
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
        intent.setData(Uri.parse("tel:" + "+" + mCommonModel.getMobileNumber()));
        if (PermissionUtil.checkPermission(this, Constants.INT_CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
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
        List<CommonModel> filteredNames = new ArrayList<>();
        for (CommonModel model : mCommonList) {
            if (model.getName().toLowerCase().contains(text.toLowerCase())) {//|| model.getMobileNumber().contains(text)
                filteredNames.add(model);
            }
        }
        mCommonRecyclerAdapter.filterList(filteredNames);
    }

    @Override
    public void onBackPressed() {
        if (et_search_tr_officers.getText().toString().length()>0) {
            et_search_tr_officers.setText("");
            et_search_tr_officers.setHint(R.string.search_officer_name);
        } else {
            //super.onBackPressed();
            finish();
        }
    }
}
