package com.tspolice.htplive.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.tspolice.htplive.models.CommonModel;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UsefulWebsitesActivity extends AppCompatActivity {

    private UiHelper mUiHelper;
    private EditText et_search_website;
    private RecyclerView mRecyclerView;
    private CommonRecyclerAdapter mCommonRecyclerAdapter;
    private List<CommonModel> mCommonList;
    private CommonModel mCommonModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useful_websites);

        initViews();

        initObjects();

        getHtpWebsites();

        et_search_website.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initViews() {
        et_search_website = findViewById(R.id.et_search_website);
        mRecyclerView = findViewById(R.id.mRecyclerViewUsefulWebsites);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setSelected(true);
        //mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(this, DividerItemDecoration.VERTICAL, 8));
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
    }

    private void getHtpWebsites() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                URLs.getHtpWebsites, null,
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
                                    model.setId1(jsonObject.getString("id"));
                                    model.setCreatedDate(jsonObject.getString("createdDate"));
                                    model.setUpdatedDate(jsonObject.getString("updatedDate"));
                                    model.setName(jsonObject.getString("name"));
                                    model.setUrl(jsonObject.getString("url"));
                                    model.setLanguage(jsonObject.getString("language"));
                                    mCommonList.add(model);
                                }
                                mCommonRecyclerAdapter = new CommonRecyclerAdapter("" + Constants.USEFUL_WEBSITES, mCommonList,
                                        new CommonRecyclerAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(CommonModel item, int position) {
                                                mCommonModel = item;
                                                String url = mCommonModel.getUrl();
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                startActivity(browserIntent);
                                            }
                                        }, UsefulWebsitesActivity.this);
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

    private void filter(String text) {
        List<CommonModel> filteredNames = new ArrayList<>();
        for (CommonModel model : mCommonList) {
            if (model.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredNames.add(model);
            }
        }
        mCommonRecyclerAdapter.filterList(filteredNames);
    }

    @Override
    public void onBackPressed() {
        if (et_search_website.getText().toString().length() > 0) {
            et_search_website.setText("");
            et_search_website.setHint(R.string.search_website_name);
        } else {
            //super.onBackPressed();
            finish();
        }
    }
}
