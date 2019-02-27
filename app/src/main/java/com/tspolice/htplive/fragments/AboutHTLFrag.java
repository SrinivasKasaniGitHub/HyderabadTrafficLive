package com.tspolice.htplive.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.models.CommonModel;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AboutHTLFrag extends Fragment {

    private TextView tv_about_htp;
    private UiHelper mUiHelper;
    private List<CommonModel> commonList;
    private CommonModel commonModel;

    public AboutHTLFrag() {
        //default constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_about_htl, container, false);

        tv_about_htp = view.findViewById(R.id.tv_about_htp);

        mUiHelper = new UiHelper(getActivity());

        //getAboutHtpApp();

        return view;
    }

    public void getAboutHtpApp() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        commonList = new ArrayList<>();
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                URLs.getAboutHtpApp, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString())
                                && !"null".equals(response.toString()) && response.length() > 0) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    CommonModel model = new CommonModel();
                                    model.setId1(jsonObject.getString("id"));
                                    model.setCreatedDate(jsonObject.getString("createdDate"));
                                    model.setUpdatedDate(jsonObject.getString("updatedDate"));
                                    model.setAboutApp(jsonObject.getString("aboutApp"));
                                    //tv_about_htp.setText(model.getAboutApp());
                                    model.setLanguage(jsonObject.getString("language"));
                                    commonList.add(model);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mUiHelper.showToastLongCentre(getResources().getString(R.string.something_went_wrong));
                            }
                        } else {
                            mUiHelper.showToastLongCentre(getResources().getString(R.string.empty_response));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mUiHelper.dismissProgressDialog();
                mUiHelper.showToastLongCentre(getResources().getString(R.string.error));
            }
        }));
    }
}
