package com.tspolice.htplive.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
        //mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(this, DividerItemDecoration.VERTICAL, 8));

        getHtpQuestions();
    }

    public void getHtpQuestions() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                URLs.getHtpQuestions, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString())
                                && !"null".equals(response.toString()) && response.length() > 0) {
                            try {
                                JSONArray jsonArray=response.getJSONArray("FAQMaster");
                                mCommonList = new ArrayList<>(jsonArray.length());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    CommonModel model = new CommonModel();
                                    model.setId(Integer.parseInt(jsonObject.getString("ID")));
                                    model.setQuestion(jsonObject.getString("QUESTION"));
                                    model.setAnswer(jsonObject.getString("ANSWER"));
                                    mCommonList.add(model);
                                }
                                mCommonRecyclerAdapter = new CommonRecyclerAdapter(Constants.FAQS, mCommonList, FAQsActivity.this);
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
}
