package com.tspolice.htplive.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tspolice.htplive.R;
import com.tspolice.htplive.activities.AutoFareEstmActivity;
import com.tspolice.htplive.activities.NearByActivity;
import com.tspolice.htplive.activities.PendingChallansActivity;
import com.tspolice.htplive.activities.PublicComplaintsActivity;
import com.tspolice.htplive.activities.PublicInterfaceActivity;
import com.tspolice.htplive.activities.RtaTowingActivity;
import com.tspolice.htplive.activities.T20TestActivity;
import com.tspolice.htplive.adapters.PendingChlnDlg;
import com.tspolice.htplive.models.PendingChallanModel;
import com.tspolice.htplive.network.Networking;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.GPSTracker;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment implements
        View.OnClickListener {

    private ImageView iv_public_interface, iv_auto_fare_estimation, iv_live_traffic, iv_near_by,
            iv_echallan_status, iv_find_towed_vhcl, iv_getRTADetails, iv_public_complaints, iv_public_info, iv_T20Test;
    private UiHelper mUiHelper;
    private SharedPrefManager mSharedPrefManager;
    private GPSTracker mGpsTracker;
    ArrayList<PendingChallanModel> pendingChallanModels = new ArrayList<>();
    PendingChlnDlg pendingChlnDlg;
    ArrayList<PendingChallanModel> mArrayList_SelectedVltnLst = new ArrayList<>();
    ArrayList<Integer> preVltnSelectdIds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        mUiHelper = new UiHelper(getActivity());
        mSharedPrefManager = SharedPrefManager.getInstance(getActivity());
        mGpsTracker = new GPSTracker(getActivity());

        iv_public_interface = view.findViewById(R.id.iv_public_interface);
        iv_auto_fare_estimation = view.findViewById(R.id.iv_auto_fare_estimation);
        iv_live_traffic = view.findViewById(R.id.iv_live_traffic);
        iv_near_by = view.findViewById(R.id.iv_near_by);
        iv_echallan_status = view.findViewById(R.id.iv_echallan_status);
        iv_find_towed_vhcl = view.findViewById(R.id.iv_find_towed_vhcl);
        iv_getRTADetails = view.findViewById(R.id.iv_getRTADetails);
        iv_public_complaints = view.findViewById(R.id.iv_public_complaints);
        iv_public_info = view.findViewById(R.id.iv_public_info);
        iv_T20Test=view.findViewById(R.id.iv_T20Test);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_public_interface.setOnClickListener(this);
        iv_auto_fare_estimation.setOnClickListener(this);
        iv_live_traffic.setOnClickListener(this);
        iv_near_by.setOnClickListener(this);
        iv_echallan_status.setOnClickListener(this);
        iv_find_towed_vhcl.setOnClickListener(this);
        iv_getRTADetails.setOnClickListener(this);
        iv_public_complaints.setOnClickListener(this);
        iv_public_info.setOnClickListener(this);
        iv_T20Test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_public_interface:
                nextActivity(PublicInterfaceActivity.class);
                break;
            case R.id.iv_auto_fare_estimation:
                nextActivity(AutoFareEstmActivity.class);
                break;
            case R.id.iv_live_traffic:
                //nextActivity(LiveTrafficActivity.class);
                if (mGpsTracker.canGetLocation()) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", mGpsTracker.getLatitude(), mGpsTracker.getLongitude());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
                break;
            case R.id.iv_near_by:
                nextActivity(NearByActivity.class);
                break;
            case R.id.iv_echallan_status:

                nextActivity(PendingChallansActivity.class);
               /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLs.eChallanStatus));
                startActivity(browserIntent);*/
                break;
            case R.id.iv_find_towed_vhcl:
                mSharedPrefManager.putString(Constants.RTA_TOWING, Constants.TOWING);
                nextActivity(RtaTowingActivity.class);
                break;
            case R.id.iv_getRTADetails:
                mSharedPrefManager.putString(Constants.RTA_TOWING, Constants.RTA);
                nextActivity(RtaTowingActivity.class);
                break;
            case R.id.iv_public_complaints:
                nextActivity(PublicComplaintsActivity.class);
                break;
            case R.id.iv_T20Test:
                nextActivity(T20TestActivity.class);
                break;
            case R.id.iv_public_info:
                mUiHelper.replaceFragment(new PublicInformationFrag());
                break;
            default:
                break;
        }
    }

    public void nextActivity(Class<?> mClass) {
        if (!Networking.isNetworkAvailable(getActivity())) {
            mUiHelper.showToastLong(getResources().getString(R.string.network_error));
        } else {
            mUiHelper.intent(mClass);
        }
    }

    private void getPendingChallans(String vehicleNo) {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        String url;
        url = URLs.pendingChalnsUrl + vehicleNo;

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                "" + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString())
                                && !"null".equals(response.toString()) && response.length() > 0) {
                            try {
                                String code = response.getString("ResponseCode");
                                if ("0".equalsIgnoreCase(code)) {
                                    JSONArray jsonArray = response.getJSONArray("PendChallansDetailsByRegn");
                                    pendingChallanModels = new ArrayList<>(jsonArray.length());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        PendingChallanModel model = new PendingChallanModel();
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        model.setUnitName("" + obj.getString("UnitName"));
                                        model.setUnitName("" + obj.getString("ChallanNo"));
                                        model.setUnitName("" + obj.getString("Date"));
                                        model.setUnitName("" + obj.getString("PointName"));
                                        model.setUnitName("" + obj.getString("PSName"));
                                        model.setUnitName("" + obj.getString("CompoundingAmount"));
                                        JSONArray array = obj.getJSONArray("ViolationDetails");
                                        ArrayList<String> arrayList = new ArrayList<>(array.length());
                                        for (int j = 0; j < array.length(); j++) {
                                            JSONObject jsonObject = array.getJSONObject(j);
                                            arrayList.add(jsonObject.getString("ViolationType") + "\n");
                                        }
                                        model.setViolations("" + getDataListString(arrayList));
                                        pendingChallanModels.add(model);
                                    }
                                    showChallnsList(pendingChallanModels);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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

    public String getDataListString(ArrayList<String> list) {
        String data = "";
        for (int i = 0; i < list.size(); i++) {

            data = data + " " + list.get(i);

        }
        return data;
    }

    private void showChallnsList(ArrayList<PendingChallanModel> listModels) {
        mArrayList_SelectedVltnLst = new ArrayList<>();
        PendingChlnDlg.selectedIdsForCallback.clear();
        pendingChlnDlg = new PendingChlnDlg()
                .title(getResources().getString(R.string.txt_pateints))
                .titleSize(16)
                .positiveText("OK")
                .negativeText("Cancel")
                .setMinSelectionLimit(0)
                .setMaxSelectionLimit(listModels.size())
                .preSelectIDsList(preVltnSelectdIds) // List of ids that you need to be selected
                .multiSelectList(listModels) // the multi select model list with ids and name
                .onSubmit(new PendingChlnDlg.SubmitCallbackListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSelected(final ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        mArrayList_SelectedVltnLst = new ArrayList<>(selectedIds.size());
                        preVltnSelectdIds = selectedIds;
                        pendingChlnDlg.dismiss();

                    }

                    @Override
                    public void onCancel() {
                        Log.d("Spot", "Dialog cancelled");

                    }
                });
        pendingChlnDlg.show(getChildFragmentManager(), "MultiSelectDlg");
    }

}
