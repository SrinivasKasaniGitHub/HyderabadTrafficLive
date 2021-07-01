package com.tspolice.htplive.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tspolice.htplive.R;
import com.tspolice.htplive.models.PendingChallanModel;

import java.util.ArrayList;

public class PendingChallansAdapter extends RecyclerView.Adapter<PendingChallansAdapter.PendingChlnDlgViewHolder> {

    private ArrayList<PendingChallanModel> mDataSet = new ArrayList<>();
    private String mSearchQuery = "", str_DisType = "";
    private Context mContext;
    private AlertDialog alertDialog;
    public TextView txt_Veh_Hed;
    public Button Btn_ExtraPasSubmit, btn_cancel, btn_Update;
    public EditText edtTxt_DORD, edtTxt_Amnt;
    LinearLayout lyt_chlns, lyt_Remark;
    RadioGroup rg_DType;
    RadioButton rb_DRcvr, rb_DDied;
    RequestQueue requestQueue;
    boolean v_Traced = true;
    ArrayList<String> mArrayList_Remarks = new ArrayList<>();
    
    AppCompatImageView img_Dob;

    TextView txt_timePerd, txt_Temp, txt_Sp, txt_Sugar, txt_Bp, txt_OxzCon, txt_VntSprt;


    PendingChallansAdapter(ArrayList<PendingChallanModel> dataSet, Context context) {
        this.mDataSet = dataSet;
        this.mContext = context;
    }

    @Override
    public PendingChallansAdapter.PendingChlnDlgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_challanslist, parent, false);
        requestQueue = Volley.newRequestQueue(mContext);
        return new PendingChallansAdapter.PendingChlnDlgViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final PendingChallansAdapter.PendingChlnDlgViewHolder holder, final int position) {

        if (!mSearchQuery.equals("") && mSearchQuery.length() > 1) {

            if (PendingChlnDlg.sercType == 0) {
                setHighlightedText(position, holder.txt_ChlnNo, mDataSet.get(position).getChallanNo());
                // holder.txt_ChlnNo.setText(mDataSet.get(position).getPatientName());
                holder.txt_UnitName.setText(mDataSet.get(position).getUnitName());
                holder.txt_Date.setText(mDataSet.get(position).getDate());
                holder.txt_PS.setText(mDataSet.get(position).getPSName());

                holder.txt_PntName.setText(mDataSet.get(position).getPointName());
                holder.txt_Amnt.setText(mDataSet.get(position).getCompoundingAmount());
                holder.txt_Vltns.setText(mDataSet.get(position).getViolations());
            } else {
                setHighlightedText(position, holder.txt_ChlnNo, mDataSet.get(position).getChallanNo());
                holder.txt_UnitName.setText(mDataSet.get(position).getUnitName());
                holder.txt_Date.setText(mDataSet.get(position).getDate());
                holder.txt_PS.setText(mDataSet.get(position).getPSName());

                holder.txt_PntName.setText(mDataSet.get(position).getPointName());
                holder.txt_Amnt.setText(mDataSet.get(position).getCompoundingAmount());
                holder.txt_Vltns.setText(mDataSet.get(position).getViolations());
            }

        } else {

            holder.txt_ChlnNo.setText(mDataSet.get(position).getChallanNo());
            holder.txt_UnitName.setText(mDataSet.get(position).getUnitName());
            holder.txt_Date.setText(mDataSet.get(position).getDate());
            holder.txt_PS.setText(mDataSet.get(position).getPSName());

            holder.txt_PntName.setText(mDataSet.get(position).getPointName());
            holder.txt_Amnt.setText(mDataSet.get(position).getCompoundingAmount());
            holder.txt_Vltns.setText(mDataSet.get(position).getViolations());

        }

       /* if (mDataSet.get(position).getSelected()) {

            if (!PendingChlnDlg.selectedIdsForCallback.contains(mDataSet.get(position).getId())) {
                PendingChlnDlg.selectedIdsForCallback.add(mDataSet.get(position).getId());
            }
        }


        if (checkForSelection(mDataSet.get(position).getId())) {
            holder.dialog_item_checkbox.setChecked(true);
        } else {
            holder.dialog_item_checkbox.setChecked(false);
        }*/

        /*holder.dialog_item_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.dialog_item_checkbox.isChecked()) {
                    PendingChlnDlg.selectedIdsForCallback.add(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(true);
                } else {
                    removeFromSelection(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(false);
                }
            }
        });*/



    }

    private void setHighlightedText(int position, TextView textview, String name) {

        // String name = mDataSet.get(position).getAddress();
        SpannableString str = new SpannableString(name);
        int endLength = name.toLowerCase().indexOf(mSearchQuery) + mSearchQuery.length();
        ColorStateList highlightedColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{ContextCompat.getColor(mContext, R.color.colorAccent)});
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(null, Typeface.NORMAL, -1, highlightedColor, null);
        str.setSpan(textAppearanceSpan, name.toLowerCase().indexOf(mSearchQuery), endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(str);
    }

    private void removeFromSelection(Integer id) {
        for (int i = 0; i < PendingChlnDlg.selectedIdsForCallback.size(); i++) {
            if (id.equals(PendingChlnDlg.selectedIdsForCallback.get(i))) {
                PendingChlnDlg.selectedIdsForCallback.remove(i);
            }
        }
    }

    private boolean checkForSelection(Integer id) {
        for (int i = 0; i < PendingChlnDlg.selectedIdsForCallback.size(); i++) {
            if (id.equals(PendingChlnDlg.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }
    /*//get selected name string seperated by coma
    public String getDataString() {
        String data = "";
        for (int i = 0; i < mDataSet.size(); i++) {
            if (checkForSelection(mDataSet.get(i).getId())) {
                data = data + ", " + mDataSet.get(i).getName();
            }
        }
        if (data.length() > 0) {
            return data.substring(1);
        } else {
            return "";
        }
    }
    //get selected name ararylist
    public ArrayList<String> getSelectedNameList() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < mDataSet.size(); i++) {
            if (checkForSelection(mDataSet.get(i).getId())) {
                names.add(mDataSet.get(i).getName());
            }
        }
        //  return names.toArray(new String[names.size()]);
        return names;
    }*/

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    void setData(ArrayList<PendingChallanModel> data, String query, PendingChallansAdapter PendingChallansAdapter) {
        this.mDataSet = new ArrayList<>();
        this.mDataSet = data;
        this.mSearchQuery = query;
        PendingChallansAdapter.notifyDataSetChanged();
    }

    class PendingChlnDlgViewHolder extends RecyclerView.ViewHolder {
        TextView txt_ChlnNo, txt_UnitName, txt_Date, txt_PS, txt_PntName,
                txt_Amnt, txt_Vltns;
        private LinearLayout main_container, lyt_Details;

        PendingChlnDlgViewHolder(View view) {
            super(view);
            txt_ChlnNo = view.findViewById(R.id.txt_ChlnNo);
            txt_UnitName = view.findViewById(R.id.txt_UnitName);
            txt_Date = view.findViewById(R.id.txt_Date);
            txt_PS = view.findViewById(R.id.txt_PS);
            main_container = view.findViewById(R.id.main_container);
            txt_PntName = view.findViewById(R.id.txt_PntName);
            txt_Amnt = view.findViewById(R.id.txt_Amnt);
            txt_Vltns = view.findViewById(R.id.txt_Vltns);



        }
    }

    /*@SuppressLint("SetTextI18n")
    public void moreInfo_Dialog(final int position, final PatientListModel patientListModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.payment_updatndlg, null);
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        txt_timePerd = view.findViewById(R.id.txt_timePerd);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        txt_Temp = view.findViewById(R.id.txt_Temp);
        txt_Sp = view.findViewById(R.id.txt_Sp);
        txt_Sugar = view.findViewById(R.id.txt_Sugar);
        txt_Bp = view.findViewById(R.id.txt_Bp);
        txt_OxzCon = view.findViewById(R.id.txt_OxzCon);
        txt_VntSprt = view.findViewById(R.id.txt_VntSprt);
        btn_Update = view.findViewById(R.id.btn_Update);
        rg_DType = view.findViewById(R.id.rg_DType);
        rb_DRcvr = view.findViewById(R.id.rb_DRcvr);
        rb_DDied = view.findViewById(R.id.rb_DDied);
        edtTxt_DORD = view.findViewById(R.id.edtTxt_DORD);
        img_Dob = view.findViewById(R.id.img_Dob);
        txt_timePerd.setText("" + patientListModel.getPatientName());
        txt_Temp.setText("" + patientListModel.getTEMP());
        txt_Sp.setText("" + patientListModel.getSPO2());
        txt_Sugar.setText("" + patientListModel.getSUGAR());
        txt_OxzCon.setText(patientListModel.getOXYGEN_SUPPORT().equalsIgnoreCase("S") ? "Yes" : "No");
        txt_VntSprt.setText(patientListModel.getVENTILATOR_SUPPORT().equalsIgnoreCase("S") ? "Yes" : "No");


        rg_DType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_DRcvr:
                        str_DisType = "R";
                        break;
                    case R.id.rb_DDied:
                        str_DisType = "D";
                        break;
                    default:
                        break;
                }
            }
        });

        img_Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dord();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equalsIgnoreCase(str_DisType)) {
                    Toaster.showWarningMessage("Please select discharge Type !");
                } else if (edtTxt_DORD.getText().toString().trim().isEmpty()) {
                    edtTxt_DORD.setError("Please enter date !");
                    edtTxt_DORD.requestFocus();
                } else {
                    updateHealthStatusbyAdmin(DashBoardActivity.str_PidCode, patientListModel.getEmpID(), str_DisType, edtTxt_DORD.getText().toString().trim());
                }
            }
        });


    }

    private void dord() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String s = dayOfMonth + "-" + (month + 1) + "-" + year;
                edtTxt_DORD.setText(new DateUtil().changeDateFormat(s));
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        //datePickerDialog.getDatePicker().setCalendarViewShown(true);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.setTitle("Please Select Date");
        datePickerDialog.show();
    }

    public void updateHealthStatusbyAdmin(final String pidCd, final String empID, final String recoverType, final String recoverDt) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DialogUtils.showProgressDialog(mContext);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("pidCd", "" + pidCd);
            jsonObject.put("empID", "" + empID);
            jsonObject.put("recoverType", "" + recoverType);
            jsonObject.put("recoverDt", "" + recoverDt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest user_Login_Req = new JsonObjectRequest(AppConfig.updateHealthStatusbyAdmin_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("LoginResponse", "" + response);
                DialogUtils.dismissDialog();
                try {
                    String status = response.getString("ResponseCode");
                    if ("0".equalsIgnoreCase(status)) {
                        Toaster.showSuccessMessage("Updated Successfully");
                        alertDialog.dismiss();
                    } else {
                        Toaster.showErrorMessage("Login Failed !");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogUtils.dismissDialog();
                Toaster.showErrorMessage("Please check the Network And Try again!");

            }
        });
        user_Login_Req.setRetryPolicy(App.mRetryPolicy);
        requestQueue.add(user_Login_Req);
    }
*/

}