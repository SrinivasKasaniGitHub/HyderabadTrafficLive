package com.tspolice.htplive.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.tspolice.htplive.R;
import com.tspolice.htplive.activities.PendingChallansActivity;
import com.tspolice.htplive.models.PendingChallanModel;
import com.tspolice.htplive.utils.RecyclerViewEmptySupport;

import java.util.ArrayList;
import java.util.Objects;

public class PendingChlnDlg extends AppCompatDialogFragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    public static ArrayList<Integer> selectedIdsForCallback = new ArrayList<>();

    public ArrayList<PendingChallanModel> mainListOfAdapter = new ArrayList<>();
    private PendingChallansAdapter mutliSelectAdapter;
    //Default Values
    private String title;
    private float titleSize = 25;
    private String positiveText = "DONE";
    private String negativeText = "CANCEL";
    TextView dialogTitle, dialogSubmit, dialogCancel, txt_NOChlns, txt_Pamnt;
    LinearLayout lyt_SUbmit;
    private ArrayList<Integer> previouslySelectedIdsList = new ArrayList<>();


    private ArrayList<Integer> tempPreviouslySelectedIdsList = new ArrayList<>();
    private ArrayList<PendingChallanModel> tempMainListOfAdapter = new ArrayList<>();

    private PendingChlnDlg.SubmitCallbackListener submitCallbackListener;

    private int minSelectionLimit = 1;
    private String minSelectionMessage = null;
    private int maxSelectionLimit = 0;
    private String maxSelectionMessage = null;
    RadioGroup rGrp_serch;
    RadioButton rBtn_Name, rBtn_Cndn;
    public static int sercType = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity(), R.style.DialogFragmentTheme);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.custom_multi_select);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        RecyclerViewEmptySupport mrecyclerView = dialog.findViewById(R.id.recycler_view);
        SearchView searchView = dialog.findViewById(R.id.search_view);
        dialogTitle = dialog.findViewById(R.id.title);
        dialogSubmit = dialog.findViewById(R.id.done);
        dialogCancel = dialog.findViewById(R.id.cancel);
        lyt_SUbmit = dialog.findViewById(R.id.lyt_SUbmit);
        rGrp_serch = dialog.findViewById(R.id.rGrp_serch);
        rBtn_Name = dialog.findViewById(R.id.rBtn_Name);
        rBtn_Cndn = dialog.findViewById(R.id.rBtn_Cndn);
        txt_NOChlns = dialog.findViewById(R.id.txt_NOChlns);
        txt_Pamnt = dialog.findViewById(R.id.txt_Pamnt);

        mrecyclerView.setEmptyView(dialog.findViewById(R.id.list_empty1));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mrecyclerView.setLayoutManager(layoutManager);

        dialogSubmit.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);

        settingValues();

        mainListOfAdapter = setCheckedIDS(mainListOfAdapter, previouslySelectedIdsList);
        mutliSelectAdapter = new PendingChallansAdapter(mainListOfAdapter, getContext());
        mrecyclerView.setAdapter(mutliSelectAdapter);

        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        rGrp_serch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rBtn_Name:
                        sercType = 0;
                        break;
                    case R.id.rBtn_Cndn:
                        sercType = 1;
                        break;
                    default:
                        sercType = 0;
                        break;
                }
            }
        });


        return dialog;
    }

    public PendingChlnDlg title(String title) {
        this.title = title;
        return this;
    }

    public PendingChlnDlg titleSize(float titleSize) {
        this.titleSize = titleSize;
        return this;
    }

    public PendingChlnDlg positiveText(String message) {
        this.positiveText = message;
        return this;
    }

    public PendingChlnDlg negativeText(String message) {
        this.negativeText = message;
        return this;
    }

    public PendingChlnDlg preSelectIDsList(ArrayList<Integer> list) {
        this.previouslySelectedIdsList = list;
        this.tempPreviouslySelectedIdsList = new ArrayList<>(previouslySelectedIdsList);
        return this;
    }

    public PendingChlnDlg multiSelectList(ArrayList<PendingChallanModel> list) {
        this.mainListOfAdapter = list;
        this.tempMainListOfAdapter = new ArrayList<>(mainListOfAdapter);
        if (maxSelectionLimit == 0)
            maxSelectionLimit = list.size();
        return this;
    }

    public PendingChlnDlg setMaxSelectionLimit(int limit) {
        this.maxSelectionLimit = limit;
        return this;
    }

    public PendingChlnDlg setMaxSelectionMessage(String message) {
        this.maxSelectionMessage = message;
        return this;
    }

    public PendingChlnDlg setMinSelectionLimit(int limit) {
        this.minSelectionLimit = limit;
        return this;
    }

    public PendingChlnDlg setMinSelectionMessage(String message) {
        this.minSelectionMessage = message;
        return this;
    }

    public PendingChlnDlg onSubmit(PendingChlnDlg.SubmitCallbackListener callback) {
        this.submitCallbackListener = callback;
        return this;
    }

    @SuppressLint("SetTextI18n")
    private void settingValues() {
        dialogTitle.setText(title);
        dialogTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
        dialogSubmit.setText(positiveText.toUpperCase());
        dialogCancel.setText(negativeText.toUpperCase());
        txt_NOChlns.setText("" + PendingChallansActivity.strNoChlns);
        txt_Pamnt.setText("" + PendingChallansActivity.str_PAmnt);
    }

    private ArrayList<PendingChallanModel> setCheckedIDS(ArrayList<PendingChallanModel> multiselectdata, ArrayList<Integer> listOfIdsSelected) {
        for (int i = 0; i < multiselectdata.size(); i++) {
            multiselectdata.get(i).setSelected(false);
            for (int j = 0; j < listOfIdsSelected.size(); j++) {
                if (listOfIdsSelected.get(j) == (multiselectdata.get(i).getId())) {
                    multiselectdata.get(i).setSelected(true);
                }
            }
        }
        return multiselectdata;
    }

    private ArrayList<PendingChallanModel> filter(ArrayList<PendingChallanModel> models, String query) {
        query = query.toLowerCase();
        final ArrayList<PendingChallanModel> filteredModelList = new ArrayList<>();
        if (query.equals("") | query.isEmpty()) {
            filteredModelList.addAll(models);
            return filteredModelList;
        }

        for (PendingChallanModel model : models) {

          /*  final String name = model.getPatientName().toLowerCase();
            final String condition = model.getCONDITION().toLowerCase();
            if (name.contains(query) || condition.contains(query)) {
                filteredModelList.add(model);
            }*/

            if (sercType == 0) {
                final String name = model.getUnitName().toLowerCase();
                if (name.contains(query)) {
                    filteredModelList.add(model);
                }
            } else {
                final String name = model.getUnitName().toLowerCase();
                if (name.contains(query)) {
                    filteredModelList.add(model);
                }
            }

        }

        return filteredModelList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        selectedIdsForCallback = previouslySelectedIdsList;
        mainListOfAdapter = setCheckedIDS(mainListOfAdapter, selectedIdsForCallback);
        ArrayList<PendingChallanModel> filteredlist = filter(mainListOfAdapter, newText);
        mutliSelectAdapter.setData(filteredlist, newText.toLowerCase(), mutliSelectAdapter);
        return false;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.done) {
            ArrayList<Integer> callBackListOfIds = selectedIdsForCallback;

            if (callBackListOfIds.size() >= minSelectionLimit) {
                if (callBackListOfIds.size() <= maxSelectionLimit) {

                    //to remember last selected ids which were successfully done
                    tempPreviouslySelectedIdsList = new ArrayList<>(callBackListOfIds);

                    if (submitCallbackListener != null) {
                        submitCallbackListener.onSelected(callBackListOfIds, getSelectNameList(), getSelectedDataString());
                    }
                    // dismiss();
                } else {
                    String youCan = getResources().getString(R.string.you_can_only_select_upto);
                    String options = getResources().getString(R.string.options);
                    String option = getResources().getString(R.string.option);
                    String message = "";

                    if (this.maxSelectionMessage != null) {
                        message = maxSelectionMessage;
                    } else {
                        if (maxSelectionLimit > 1)
                            message = youCan + " " + maxSelectionLimit + " " + options;
                        else
                            message = youCan + " " + maxSelectionLimit + " " + option;
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            } else {
                String pleaseSelect = getResources().getString(R.string.you_can_only_select_upto);
                String options = getResources().getString(R.string.options);
                String option = getResources().getString(R.string.option);
                String message = "";

                if (this.minSelectionMessage != null) {
                    message = minSelectionMessage;
                } else {
                    if (minSelectionLimit > 1)
                        message = pleaseSelect + " " + minSelectionLimit + " " + options;
                    else
                        message = pleaseSelect + " " + minSelectionLimit + " " + option;
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }

        if (view.getId() == R.id.cancel) {
            if (submitCallbackListener != null) {
                selectedIdsForCallback.clear();
                selectedIdsForCallback.addAll(tempPreviouslySelectedIdsList);
                submitCallbackListener.onCancel();
            }
            dismiss();
        }
    }

    private String getSelectedDataString() {
        String data = "";
        for (int i = 0; i < tempMainListOfAdapter.size(); i++) {
            if (checkForSelection(tempMainListOfAdapter.get(i).getId())) {
                data = data + ", " + tempMainListOfAdapter.get(i).getUnitName();
            }
        }
        if (data.length() > 0) {
            return data.substring(1);
        } else {
            return "";
        }
    }

    private ArrayList<String> getSelectNameList() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < tempMainListOfAdapter.size(); i++) {
            if (checkForSelection(tempMainListOfAdapter.get(i).getId())) {
                names.add(tempMainListOfAdapter.get(i).getUnitName());
            }
        }
        return names;
    }

    private boolean checkForSelection(Integer id) {
        for (int i = 0; i < PendingChlnDlg.selectedIdsForCallback.size(); i++) {
            if (id.equals(PendingChlnDlg.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }

   /* public void setCallbackListener(SubmitCallbackListener submitCallbackListener) {
        this.submitCallbackListener = submitCallbackListener;
    }*/

    public interface SubmitCallbackListener {
        void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String commonSeperatedData);

        void onCancel();
    }

}