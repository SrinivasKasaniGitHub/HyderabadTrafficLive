package com.tspolice.htplive.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tspolice.htplive.R;
import com.tspolice.htplive.utils.UiHelper;

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
                    mUiHelper.showToastShort("Please enter your name");
                } else if (email.isEmpty()) {
                    mUiHelper.showToastShort("Please enter your email id");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mUiHelper.showToastShort("Please enter valid email id");
                } else if (contactNo.isEmpty()) {
                    mUiHelper.showToastShort("Please enter your contact no.");
                } else if (suggestion.isEmpty()) {
                    mUiHelper.showToastShort("Please enter a suggestion");
                } else {
                    // make POST request
                }
                break;
            default:
                break;
        }
    }
}
