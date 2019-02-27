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
import com.tspolice.htplive.utils.ValidationUtils;

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
                    mUiHelper.showToastShortCentre(getString(R.string.enter_your_name));
                    mUiHelper.requestFocus(et_name);
                } else if (email.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_your_email_id));
                    mUiHelper.requestFocus(et_email_id);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_valid_email_id));
                    mUiHelper.requestFocus(et_email_id);
                } else if (contactNo.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_your_contact_no));
                    mUiHelper.requestFocus(et_contact_no);
                } else if (!ValidationUtils.isValidMobile(contactNo)) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_valid_contact_no));
                    mUiHelper.requestFocus(et_contact_no);
                } else if (suggestion.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_a_suggestion));
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
                        mUiHelper.showToastShortCentre(response);
                        et_name.setText("");
                        et_name.setHint(getString(R.string.name));
                        et_email_id.setText("");
                        et_email_id.setHint(getString(R.string.email));
                        et_contact_no.setText("");
                        et_contact_no.setHint(getString(R.string.contact_no));
                        et_suggestion.setText("");
                        et_suggestion.setHint(getString(R.string.write_a_suggestion));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mUiHelper.dismissProgressDialog();
                mUiHelper.showToastShortCentre(error.toString());
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
