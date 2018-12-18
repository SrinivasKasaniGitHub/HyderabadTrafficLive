package com.tspolice.htplive.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.network.URLParams;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SuggestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name, et_email_id, et_contact_no, et_suggestion;
    private UiHelper mUiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        initViews();

        initObjects();
    }

    private void initViews() {
        et_name = findViewById(R.id.et_name);
        et_email_id = findViewById(R.id.et_email_id);
        et_contact_no = findViewById(R.id.et_contact_no);
        et_suggestion = findViewById(R.id.et_suggestion);

        Button btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                String name = et_name.getText().toString();
                String email = et_email_id.getText().toString();
                String contactNo = et_contact_no.getText().toString();
                String suggestion = et_suggestion.getText().toString();
                if (name.isEmpty()) {
                    mUiHelper.setError(et_name, getString(R.string.enter_your_name));
                    mUiHelper.requestFocus(et_name);
                } else if (email.isEmpty()) {
                    mUiHelper.setError(et_email_id, getString(R.string.enter_your_email_id));
                    mUiHelper.requestFocus(et_email_id);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mUiHelper.setError(et_email_id, getString(R.string.enter_valid_email_id));
                    mUiHelper.requestFocus(et_email_id);
                } else if (contactNo.isEmpty()) {
                    mUiHelper.setError(et_contact_no, getString(R.string.enter_your_contact_no));
                    mUiHelper.requestFocus(et_contact_no);
                } else if (!Patterns.PHONE.matcher(contactNo).matches()) {
                    mUiHelper.setError(et_contact_no, getString(R.string.enter_valid_contact_no));
                    mUiHelper.requestFocus(et_contact_no);
                } else if (suggestion.isEmpty()) {
                    mUiHelper.setError(et_suggestion, getString(R.string.enter_a_suggestion));
                    mUiHelper.requestFocus(et_suggestion);
                } else {
                    saveSuggestions(name, email, contactNo, suggestion);
                }
                break;
            default:
                break;
        }
    }

    private void saveSuggestions(String name, String email, String contactNo, String suggestion) {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        JSONObject jsonRequest;
        final String mRequestBody;
        Map<String, String> params = new HashMap<>();
        params.put(URLParams.name, name);
        params.put(URLParams.email, email);
        params.put(URLParams.mobileNumber, contactNo);
        params.put(URLParams.suggestion, suggestion);
        jsonRequest = new JSONObject(params);
        mRequestBody = jsonRequest.toString();

        VolleySingleton.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.POST, URLs.saveSuggestions,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mUiHelper.dismissProgressDialog();
                        mUiHelper.showToastLong(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mUiHelper.dismissProgressDialog();
                        mUiHelper.showToastShort(getResources().getString(R.string.error));
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return URLs.contentType;
            }

            @Override
            public byte[] getBody() {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes(URLs.utf_8);
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf(URLs.unSupportedEncodingException, mRequestBody, URLs.utf_8);
                    return null;
                }
            }
        });
    }
}
