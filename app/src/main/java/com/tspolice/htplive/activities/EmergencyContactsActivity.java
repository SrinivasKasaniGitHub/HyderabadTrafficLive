package com.tspolice.htplive.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.adapters.CommonRecyclerAdapter;
import com.tspolice.htplive.models.CommonModel;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.PermissionUtil;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

import android.Manifest;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmergencyContactsActivity extends AppCompatActivity {

    private UiHelper mUiHelper;
    private EditText et_search_emergency_contacts;
    //private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private CommonRecyclerAdapter mCommonRecyclerAdapter;
    private List<CommonModel> mCommonList;
    private SharedPrefManager mSharedPrefManager;
    private CommonModel mCommonModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        initViews();

        initObjects();

        recyclerViewSetup();

        String serviceType;
        if (Constants.AMBULANCE.equals(mSharedPrefManager.getString(Constants.EMERGENCY_CONTACTS))) {
            serviceType = "0";
        } else if (Constants.BLOOD_BANK.equals(mSharedPrefManager.getString(Constants.EMERGENCY_CONTACTS))) {
            serviceType = "1";
        } else if (Constants.FIRE_STATION.equals(mSharedPrefManager.getString(Constants.EMERGENCY_CONTACTS))) {
            serviceType = "2";
        } else if (Constants.CRIME_ALERT.equals(mSharedPrefManager.getString(Constants.EMERGENCY_CONTACTS))) {
            serviceType = "3";
        } else if (Constants.WOMEN_CHILD.equals(mSharedPrefManager.getString(Constants.EMERGENCY_CONTACTS))) {
            serviceType = "4";
        } else if (Constants.SENIOR_CITIZEN.equals(mSharedPrefManager.getString(Constants.EMERGENCY_CONTACTS))) {
            serviceType = "5";
        } else {
            serviceType = "0";
        }

        getEmergencyContacts(serviceType);

        et_search_emergency_contacts.addTextChangedListener(new TextWatcher() {
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

        /*mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });*/
    }

    private void initViews() {
        et_search_emergency_contacts = findViewById(R.id.et_search_emergency_contacts);
        mRecyclerView = findViewById(R.id.mRecyclerViewEmergencyContacts);
        //mSearchView = findViewById(R.id.searchView);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
        mSharedPrefManager = SharedPrefManager.getInstance(this);
    }

    private void recyclerViewSetup() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setSelected(true);
        //mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(this, DividerItemDecoration.VERTICAL, 8));
    }

    public void getEmergencyContacts(String serviceType) {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                "" + URLs.getEmergencyContacts("" + serviceType), null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString())
                                && !"null".equals(response.toString()) && response.length() > 0) {
                            try {
                                mCommonList = new ArrayList<>(response.length());
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    CommonModel model = new CommonModel();
                                    model.setId(Integer.parseInt(jsonObject.getString("id")));
                                    model.setCreatedDate(jsonObject.getString("createdDate"));
                                    model.setUpdatedDate(jsonObject.getString("updatedDate"));
                                    model.setServiceName(jsonObject.getString("serviceName"));
                                    model.setContactNumber(jsonObject.getString("contactNumber"));
                                    model.setServiceType(jsonObject.getString("serviceType"));
                                    model.setLanguage(jsonObject.getString("language"));
                                    mCommonList.add(model);
                                }
                                mCommonRecyclerAdapter = new CommonRecyclerAdapter(Constants.EMERGENCY_CONTACTS, mCommonList,
                                        new CommonRecyclerAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(CommonModel item, int position) {
                                                mCommonModel = item;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    if (PermissionUtil.checkPermission(EmergencyContactsActivity.this, Constants.INT_CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                        if (ActivityCompat.shouldShowRequestPermissionRationale(EmergencyContactsActivity.this, Manifest.permission.CALL_PHONE)) {
                                                            PermissionUtil.showPermissionExplanation(EmergencyContactsActivity.this, Constants.INT_CALL_PHONE);
                                                        } else if (!mSharedPrefManager.getBoolean(Constants.CALL_PHONE)) {
                                                            PermissionUtil.requestPermission(EmergencyContactsActivity.this, Constants.INT_CALL_PHONE);
                                                            mSharedPrefManager.putBoolean(Constants.CALL_PHONE, true);
                                                        } else {
                                                            PermissionUtil.redirectAppSettings(EmergencyContactsActivity.this);
                                                        }
                                                    } else {
                                                        makeCall();
                                                    }
                                                } else {
                                                    PermissionUtil.redirectAppSettings(EmergencyContactsActivity.this);
                                                }
                                            }
                                        }, EmergencyContactsActivity.this);
                                mRecyclerView.setAdapter(mCommonRecyclerAdapter);
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

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "+" + mCommonModel.getContactNumber()));
        if (PermissionUtil.checkPermission(EmergencyContactsActivity.this,
                Constants.INT_CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
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
                    mUiHelper.showToastLongCentre(getResources().getString(R.string.permission_denied));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void filter(String text) {
        List<CommonModel> filteredNames = new ArrayList<>();
        for (CommonModel model : mCommonList) {
            if (model.getServiceName().toLowerCase().contains(text.toLowerCase())) {
                filteredNames.add(model);
            }
        }
        mCommonRecyclerAdapter.filterList(filteredNames);
    }

    @Override
    public void onBackPressed() {
        if (et_search_emergency_contacts.getText().toString().length() > 0) {
            et_search_emergency_contacts.setText("");
            et_search_emergency_contacts.setHint(R.string.search_contact_no);
        } else {
            //super.onBackPressed();
            finish();
        }
    }
}
