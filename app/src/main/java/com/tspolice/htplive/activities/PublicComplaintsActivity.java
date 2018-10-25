package com.tspolice.htplive.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tspolice.htplive.R;
import com.tspolice.htplive.utils.UiHelper;

public class PublicComplaintsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_complaint, et_complaint_type, et_vehicle_no, et_drivers_name, et_type_a_complaint,
            et_your_name, et_your_email_id, et_your_mobile_no;
    private Button btn_submit;
    private String complaint, complaintType, vehicleNo, driversName, typeComplaint, yourName, yourEmailId, yourMobileNo;
    private UiHelper mUiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_complaints);

        initViews();

        initObjects();
    }

    private void initViews() {
        et_complaint = findViewById(R.id.et_complaint);
        et_complaint_type = findViewById(R.id.et_complaint_type);
        et_vehicle_no = findViewById(R.id.et_vehicle_no);
        et_drivers_name = findViewById(R.id.et_drivers_name);
        et_type_a_complaint = findViewById(R.id.et_type_a_complaint);

        et_your_name = findViewById(R.id.et_your_name);
        et_your_email_id = findViewById(R.id.et_your_email_id);
        et_your_mobile_no = findViewById(R.id.et_your_mobile_no);

        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
    }

    public void initObjects() {
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
                yourMobileNo = et_your_mobile_no.getText().toString().trim();
                if (yourMobileNo.isEmpty()) {
                    mUiHelper.showToastShort("Please enter your mobile no.");
                } else {
                    // make POST request
                }
                break;
            default:
                break;
        }
    }
}
