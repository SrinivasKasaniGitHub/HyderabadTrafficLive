package com.tspolice.htplive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.models.QuestionModel;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class T20TestActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_Name, et_Age;
    Button btn_StartQuiz;
    RadioGroup radioGroup_gender, radioGroup_Lng;
    RadioButton rBtn_Male, rBtn_FeMale, rBtn_Others, rBtn_English, rBtn_Telugu, rBtn_Hindi;
    public static String str_Gender = "", str_Lang = "", str_ID = "", str_Name = "";
    private UiHelper mUiHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testquiz);
        mUiHelper = new UiHelper(this);
        str_Gender = "";
        str_Lang = "";
        initview();

    }

    private void initview() {
        btn_StartQuiz = findViewById(R.id.btn_StartQuiz);
        et_Name = findViewById(R.id.et_Name);
        et_Age = findViewById(R.id.et_Age);
        radioGroup_gender = findViewById(R.id.radioGroup_gender);
        rBtn_Male = findViewById(R.id.rBtn_Male);
        rBtn_FeMale = findViewById(R.id.rBtn_FeMale);
        rBtn_Others = findViewById(R.id.rBtn_Others);

        radioGroup_Lng = findViewById(R.id.radioGroup_Lng);
        rBtn_English = findViewById(R.id.rBtn_English);
        rBtn_Telugu = findViewById(R.id.rBtn_Telugu);
        rBtn_Hindi = findViewById(R.id.rBtn_Hindi);
        btn_StartQuiz.setOnClickListener(this);

        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rBtn_Male:
                        str_Gender = "M";
                        break;
                    case R.id.rBtn_FeMale:
                        str_Gender = "F";
                        break;
                    case R.id.rBtn_Others:
                        str_Gender = "O";
                        break;
                    default:
                        break;
                }
            }
        });

        radioGroup_Lng.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rBtn_English:
                        str_Lang = "1";
                        break;
                    case R.id.rBtn_Telugu:
                        str_Lang = "2";
                        break;
                    case R.id.rBtn_Hindi:
                        str_Lang = "3";
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_StartQuiz:
                if (et_Name.getText().toString().isEmpty()) {
                    et_Name.setError("Please enter Name ");
                    et_Name.requestFocus();
                } else if (et_Age.getText().toString().isEmpty()) {
                    et_Age.setError("Please enter Age");
                    et_Age.requestFocus();
                } else if ("".equalsIgnoreCase(str_Gender)) {
                    mUiHelper.showToastShortCentre("Please select Gender ");
                } else if ("".equalsIgnoreCase(str_Lang)) {
                    mUiHelper.showToastShortCentre("Please select Language ");
                } else {
                   loginUser(et_Name.getText().toString().trim(),et_Age.getText().toString().trim(),str_Gender);
                }
                break;
            default:
                break;
        }
    }

    private void loginUser(final String name, final String age, final String gender) {
        JSONObject jsonObject=null;
        try{
            jsonObject=new JSONObject();
            jsonObject.put("name",name);
            jsonObject.put("age",age);
            jsonObject.put("gender",gender);
        }catch (Exception e){
            e.printStackTrace();
        }

        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.POST, URLs.loginUserURL,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mUiHelper.dismissProgressDialog();

                if (response != null && !"".equals(response.toString())
                        && !"null".equals(response.toString())) {

                    try {
                        JSONObject jsonObject = response.getJSONObject("LoginDetails");
                        str_ID = jsonObject.getString("id");
                        str_Name = jsonObject.getString("name");
                        Intent intent = new Intent(getApplicationContext(), T20QuestionsActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
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
