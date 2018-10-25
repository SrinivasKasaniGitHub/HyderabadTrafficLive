package com.tspolice.htplive.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FAQsActivity extends AppCompatActivity {

    private UiHelper mUiHelper;
    private RecyclerView mRecyclerView;
    private CommonRecyclerAdapter mCommonRecyclerAdapter;
    private List<CommonModel> mCommonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        mUiHelper = new UiHelper(this);

        mRecyclerView = findViewById(R.id.mRecyclerViewFAQs);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setSelected(true);
        mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(this, DividerItemDecoration.VERTICAL, 8));

        getHtpQuestions();
    }

    public void getHtpQuestions() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                URLs.getHtpQuestions, null,
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
                                    model.setQuestion(jsonObject.getString("question"));
                                    model.setAnswer(jsonObject.getString("answer"));
                                    model.setLanguage(jsonObject.getString("language"));
                                    mCommonList.add(model);
                                }
                                mCommonRecyclerAdapter = new CommonRecyclerAdapter(Constants.FAQS, mCommonList, FAQsActivity.this);
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
}
