package com.tspolice.htplive.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.List;
import java.util.concurrent.TimeUnit;

import info.hoang8f.widget.FButton;

public class T20QuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private UiHelper mUiHelper;
    private List<QuestionModel> questionModelList = new ArrayList<>();
    TextView txt_Question, tv_QNO, tv_CntTimer;
    FButton btn_OptA, btn_OptB, btn_OptC, btn_OptD;
    Button btn_Next;
    int qId = 0;
    int score = 0;
    int timerValue = 60000;
    QuestionModel questionModel;
    CountDownTimer countDownTimer;
    ImageView img_Qstn;
    LinearLayout lyt_Remarks;
    EditText et_Remarks;
    AlertDialog alertDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_quiz);
        initview();
        mUiHelper = new UiHelper(T20QuestionsActivity.this);
        loadQuestions();
        resetColor();
        tv_QNO.setText("Score : " + score + "/20");
    }

    private void initview() {

        tv_QNO = findViewById(R.id.tv_QNO);
        tv_CntTimer = findViewById(R.id.tv_CntTimer);
        txt_Question = findViewById(R.id.txt_Question);
        img_Qstn = findViewById(R.id.img_Qstn);
        btn_OptA = findViewById(R.id.btn_OptA);
        btn_OptB = findViewById(R.id.btn_OptB);
        btn_OptC = findViewById(R.id.btn_OptC);
        btn_OptD = findViewById(R.id.btn_OptD);
        btn_Next = findViewById(R.id.btn_Next);
        lyt_Remarks = findViewById(R.id.lyt_Remarks);
        et_Remarks = findViewById(R.id.et_Remarks);
        btn_Next.setOnClickListener(this);
        btn_OptA.setOnClickListener(this);
        btn_OptB.setOnClickListener(this);
        btn_OptC.setOnClickListener(this);
        btn_OptD.setOnClickListener(this);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Next:

                if (qId < 19) {
                    qId++;
                    enableButton();
                    resetColor();
                    updateQuesAnswers();
                    tv_QNO.setText("Score : " + score + "/20");
                    if (qId==19){
                        lyt_Remarks.setVisibility(View.VISIBLE);
                    }
                } else {
                    countDownTimer.cancel();
                    updateTestResult(T20TestActivity.str_Name, T20TestActivity.str_ID, String.valueOf(score), et_Remarks.getText().toString());

                }
                break;
            case R.id.btn_OptA:
                if (questionModel.getOption1().equalsIgnoreCase(questionModel.getCorrectAns())) {
                    btn_OptA.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    disableButton();
                    score = score + 1;
                } else {
                    btn_OptA.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    if (questionModel.getOption2().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptB.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    } else if (questionModel.getOption3().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptC.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    } else if (questionModel.getOption4().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptD.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    }
                    disableButton();
                }
                break;
            case R.id.btn_OptB:
                if (questionModel.getOption2().equalsIgnoreCase(questionModel.getCorrectAns())) {
                    btn_OptB.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    disableButton();
                    score = score + 1;
                } else {
                    btn_OptB.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    if (questionModel.getOption1().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptA.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    } else if (questionModel.getOption3().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptC.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    } else if (questionModel.getOption4().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptD.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    }
                    disableButton();
                }

                break;
            case R.id.btn_OptC:
                if (questionModel.getOption3().equalsIgnoreCase(questionModel.getCorrectAns())) {
                    btn_OptC.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    disableButton();
                    score = score + 1;
                } else {
                    btn_OptC.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    if (questionModel.getOption1().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptA.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    } else if (questionModel.getOption2().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptB.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    } else if (questionModel.getOption4().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptD.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    }
                    disableButton();
                }
                break;
            case R.id.btn_OptD:
                if (questionModel.getOption4().equalsIgnoreCase(questionModel.getCorrectAns())) {
                    btn_OptD.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    disableButton();
                    score = score + 1;
                } else {
                    btn_OptD.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    if (questionModel.getOption2().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptB.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    } else if (questionModel.getOption3().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptC.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    } else if (questionModel.getOption1().equalsIgnoreCase(questionModel.getCorrectAns())) {
                        btn_OptA.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen));
                    }
                    disableButton();
                }
                break;
            default:
                break;
        }

    }

    @SuppressLint("SetTextI18n")
    private void updateQuesAnswers() {
        questionModel = questionModelList.get(qId);
        try {
            if ("NA".equalsIgnoreCase(questionModel.getBase64Photo())) {
                img_Qstn.setVisibility(View.GONE);
            } else {
                img_Qstn.setVisibility(View.VISIBLE);
                img_Qstn.setImageBitmap(convertBase64ToBitmap(questionModel.getBase64Photo()));
            }

            txt_Question.setText("" + questionModel.getQNo() + ".  " + questionModel.getQuestionName());
            btn_OptA.setText("" + questionModel.getOption1());
            btn_OptB.setText("" + questionModel.getOption2());
            btn_OptC.setText("" + questionModel.getOption3());
            btn_OptD.setText("" + questionModel.getOption4());
            countDownTimer.cancel();
            countDownTimer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadQuestions() {

        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);

        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, URLs.getRandomQuestions("1"),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mUiHelper.dismissProgressDialog();

                if (response != null && !"".equals(response.toString())
                        && !"null".equals(response.toString())) {

                    try {
                        JSONArray jsonArray = response.getJSONArray("RandomQuestions");
                        questionModelList = new ArrayList<>(jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            QuestionModel questionModel = new QuestionModel();
                            //  questionModel.setExamName("" + jsonObject.getString("examName"));
                            questionModel.setQuestionName("" + jsonObject.getString("questionName"));
                            questionModel.setId("" + jsonObject.getString("id"));
                            questionModel.setOption1("" + jsonObject.getString("option1"));
                            questionModel.setOption2("" + jsonObject.getString("option2"));
                            questionModel.setOption3("" + jsonObject.getString("option3"));
                            questionModel.setOption4("" + jsonObject.getString("option4"));
                            questionModel.setCorrectAns("" + jsonObject.getString("correctAns"));
                            questionModel.setBase64Photo("" + jsonObject.getString("base64Img"));
                            questionModel.setQNo(i + 1);
                            questionModelList.add(questionModel);
                        }
                        if (questionModelList.size() > 0) {
                            countTimer(timerValue);
                            updateQuesAnswers();
                        }
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

       /* String json_QstnList = mUiHelper.loadJSONFromAssets("traffictest.json");
        try {
            JSONArray jsonArray = new JSONArray(json_QstnList);
            questionModelList = new ArrayList<>(jsonArray.length());
            for (int i = 0; i <= jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                QuestionModel questionModel = new QuestionModel();
                questionModel.setExamName("" + jsonObject.getString("examName"));
                questionModel.setQuestionName("" + jsonObject.getString("questionName"));
                questionModel.setId("" + jsonObject.getString("id"));
                questionModel.setOption1("" + jsonObject.getString("option1"));
                questionModel.setOption2("" + jsonObject.getString("option2"));
                questionModel.setOption3("" + jsonObject.getString("option3"));
                questionModel.setOption4("" + jsonObject.getString("option4"));
                questionModel.setCorrectAns("" + jsonObject.getString("correctAns"));
                questionModel.setBase64Photo("" + jsonObject.getString("base64Photo"));
                questionModelList.add(questionModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    public void countTimer(final int time) {

        countDownTimer = new CountDownTimer(time, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                tv_CntTimer.setText("Time : " + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + "");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                tv_CntTimer.setText("Time's Up!");
                timedOutDlg();

            }
        }.start();
    }

    public void timedOutDlg() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setIcon(getResources().getDrawable(R.drawable.ic_logo));
        builder.setMessage("Question time has been Expired ! \n Please click on next to continue");
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.app_next, new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (qId < 19) {
                    qId++;
                    enableButton();
                    resetColor();
                    updateQuesAnswers();
                    tv_QNO.setText("Score : " + score + "/20");
                    if (qId==19){
                        lyt_Remarks.setVisibility(View.VISIBLE);
                    }
                } else {
                    countDownTimer.cancel();
                    updateTestResult(T20TestActivity.str_Name, T20TestActivity.str_ID, String.valueOf(score), et_Remarks.getText().toString());

                }
            }
        });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void resetColor() {
        btn_OptA.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorbtnBg));
        btn_OptB.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorbtnBg));
        btn_OptC.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorbtnBg));
        btn_OptD.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorbtnBg));
    }

    public void disableButton() {
        btn_OptA.setEnabled(false);
        btn_OptB.setEnabled(false);
        btn_OptC.setEnabled(false);
        btn_OptD.setEnabled(false);
    }

    public void enableButton() {
        btn_OptA.setEnabled(true);
        btn_OptB.setEnabled(true);
        btn_OptC.setEnabled(true);
        btn_OptD.setEnabled(true);
    }

    private Bitmap convertBase64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    private void updateTestResult(final String name, final String id, final String score, final String comments) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("id", id);
            jsonObject.put("score", score);
            jsonObject.put("comments", comments);
        } catch (Exception e) {
            e.printStackTrace();
        }

        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.POST, URLs.testResultUrl,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mUiHelper.dismissProgressDialog();

                if (response != null && !"".equals(response.toString())
                        && !"null".equals(response.toString())) {

                    try {
                        if ("1".equalsIgnoreCase(response.getString("status"))) {
                            successDlg();
                        } else {
                            mUiHelper.showToastLongCentre("You are not reached 18 Score !");
                            //successDlg();
                            Intent intent = new Intent(T20QuestionsActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
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

    private void successDlg() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(T20QuestionsActivity.this);
        LayoutInflater inflater = LayoutInflater.from(T20QuestionsActivity.this);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dlg_suceed, null);
        builder.setView(view);
        builder.setCancelable(false);
        Button btn_Done = view.findViewById(R.id.btn_Done);
        alertDialog = builder.create();
        alertDialog.show();
        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(T20QuestionsActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

}
