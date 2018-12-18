package com.tspolice.htplive.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tspolice.htplive.R;
import com.tspolice.htplive.models.ParkingDetailsModel;
import com.tspolice.htplive.models.ParkingTypeModel;
import com.tspolice.htplive.models.VehicleTypeModel;
import com.tspolice.htplive.network.URLParams;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.LocationTrack;
import com.tspolice.htplive.utils.PermissionUtil;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearByActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = "NearByActivity-->";
    private GoogleMap mMap;
    private SharedPrefManager mSharedPrefManager;
    private UiHelper mUiHelper;
    private LocationTrack mLocationTrack;
    private LatLng latLng;
    private double mLatitude, mLongitude;
    private Button btn_check_parking_space;
    private Dialog mDialogParkingSpace;
    private List<String> vehicleTypes;
    //private List<ParkingDetailsModel> parkingDetailsList;
    private int vehicleTypeId = 0, parkingTypeId = 0, psId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViews();

        initObjects();

        btn_check_parking_space.setOnClickListener(this);
    }

    private void initViews() {
        btn_check_parking_space = findViewById(R.id.btn_check_parking_space);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
        mLocationTrack = new LocationTrack(this);
        mSharedPrefManager = SharedPrefManager.getInstance(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkPermission(this, Constants.INT_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    PermissionUtil.showPermissionExplanation(this, Constants.INT_FINE_LOCATION);
                } else if (!mSharedPrefManager.getBoolean(Constants.FINE_LOCATION)) {
                    PermissionUtil.requestPermission(this, Constants.INT_FINE_LOCATION);
                    mSharedPrefManager.putBoolean(Constants.FINE_LOCATION, true);
                } else {
                    PermissionUtil.redirectAppSettings(this);
                }
            } else {
                setCurrentLocation();
                getHydPoliceStations();
            }
        } else {
            PermissionUtil.redirectAppSettings(this);
        }
    }

    public void setCurrentLocation() {
        if (mLocationTrack.canGetLocation()) {
            mLatitude = mLocationTrack.getLatitude();
            mLongitude = mLocationTrack.getLongitude();
            latLng = new LatLng(mLatitude, mLongitude);
            addMarker(latLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            mMap.setOnCameraMoveStartedListener(this);
        }
    }

    private void addMarker(LatLng newLatLng) {
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(newLatLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptions.title("You are here");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }

    private void getHydPoliceStations() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                URLs.getHydPoliceStations, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString()) && response.length() > 0) {
                            ArrayList<Double> distances = new ArrayList<>();
                            ArrayList<Double> lat = new ArrayList<>();
                            ArrayList<Double> lang = new ArrayList<>();
                            ArrayList<Integer> psIds = new ArrayList<>();
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String stLat = jsonObject.getString("latitude");
                                    String stLong = jsonObject.getString("langitude");
                                    if (stLat != null && !"null".equals(stLat) && stLat.length() > 0
                                            && stLong != null && !"null".equals(stLong) && stLong.length() > 0) {
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        double psLatitude = 0.0, psLongitude = 0.0;
                                        try {
                                            psLatitude = Double.parseDouble(stLat);
                                            psLongitude = Double.parseDouble(stLong);
                                            lat.add(psLatitude);
                                            lang.add(psLongitude);
                                            markerOptions.position(new LatLng(psLatitude, psLongitude));
                                            int psId1 = Integer.parseInt(jsonObject.getString("id"));
                                            psIds.add(psId1);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                        int radius = 6371;
                                        double dLat = Math.toRadians(psLatitude - mLatitude);
                                        double dLon = Math.toRadians(psLongitude - mLongitude);
                                        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(mLatitude))
                                                * Math.cos(Math.toRadians(psLatitude)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
                                        double c = 2 * Math.asin(Math.sqrt(a));
                                        double distance = radius * c;
                                        distances.add(distance);
                                        DecimalFormat df = new DecimalFormat("#.##");
                                        String stationName = jsonObject.getString("stationName");
                                        if (stationName != null && !"null".equals(stationName) && stationName.length() > 0) {
                                            markerOptions.title(stationName + " (" + String.valueOf(df.format(distance)) + "km)");
                                        }
                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                        mMap.addMarker(markerOptions);
                                    }
                                }
                                int minDistance = minIndex(distances);
                                if (minDistance <= distances.size()) {
                                    LatLng latLng = new LatLng(lat.get(minDistance), lang.get(minDistance));
                                    PolylineOptions polylineOptions = new PolylineOptions();
                                    polylineOptions.add(new LatLng(mLatitude, mLongitude), new LatLng(latLng.latitude, latLng.longitude));
                                    polylineOptions.width(4);
                                    polylineOptions.color(Color.RED);
                                    polylineOptions.geodesic(true);
                                    mMap.addPolyline(polylineOptions);
                                    psId = psIds.get(minDistance);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
                            }
                        } else {
                            mUiHelper.showToastShort(getResources().getString(R.string.empty_response));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mUiHelper.dismissProgressDialog();
                        mUiHelper.showToastShort(getResources().getString(R.string.error));
                    }
                }));
    }

    public static int minIndex(ArrayList<Double> list) {
        return list.indexOf(Collections.min(list));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setCurrentLocation();
                } else {
                    mUiHelper.showToastShort(getResources().getString(R.string.permission_denied));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            mMap.setOnCameraMoveCanceledListener(this);
            CameraPosition position = mMap.getCameraPosition();
            mLatitude = position.target.latitude;
            mLongitude = position.target.longitude;
            latLng = new LatLng(mLatitude, mLongitude);
        }
    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveCanceled() {
        mMap.setOnCameraMoveStartedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_parking_space:
                getZones();
                break;
            case R.id.img_close_btn:
                mDialogParkingSpace.dismiss();
                setCurrentLocation();
                getHydPoliceStations();
                break;
            default:
                break;
        }
    }

    private void getZones() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                URLs.getZones, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mUiHelper.dismissProgressDialog();
                        if (response != null && !"".equals(response.toString()) && response.length() > 0) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("vehicleTypes");
                                vehicleTypes = new ArrayList<>(jsonArray.length());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    vehicleTypes.add(jsonObject.getString("name"));
                                }
                                parkingSpaceDialog();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
                            }
                        } else {
                            mUiHelper.showToastShort(getResources().getString(R.string.empty_response));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mUiHelper.dismissProgressDialog();
                        mUiHelper.showToastShort(getResources().getString(R.string.error));
                    }
                }));
    }

    private void parkingSpaceDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(NearByActivity.this);
        LayoutInflater inflater = LayoutInflater.from(NearByActivity.this);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dlg_check_parking_details, null);
        builder.setView(view);
        builder.setCancelable(false);
        mDialogParkingSpace = builder.create();
        mDialogParkingSpace.show();

        ImageView img_close_btn = view.findViewById(R.id.img_close_btn);
        final Spinner spinner_vehicle_type = view.findViewById(R.id.spinner_vehicle_type);
        final CheckBox chb_select_all, chb_free_parking, chb_paid_parking, chb_water_loggings, chb_busbays_stops, chb_auto_parking;
        chb_select_all = view.findViewById(R.id.chb_select_all);
        chb_free_parking = view.findViewById(R.id.chb_free_parking);
        chb_paid_parking = view.findViewById(R.id.chb_paid_parking);
        chb_water_loggings = view.findViewById(R.id.chb_water_loggings);
        chb_busbays_stops = view.findViewById(R.id.chb_busbays_stops);
        chb_auto_parking = view.findViewById(R.id.chb_auto_parking);
        Button btn_parking_details_dialog_submit = view.findViewById(R.id.btn_parking_details_dialog_submit);

        img_close_btn.setOnClickListener(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_vehicle_type.setAdapter(arrayAdapter);

        spinner_vehicle_type.setOnItemSelectedListener(this);

        chb_select_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chb_select_all.isChecked()) {
                    chb_free_parking.setChecked(true);
                    chb_paid_parking.setChecked(true);
                    chb_water_loggings.setChecked(true);
                    chb_busbays_stops.setChecked(true);
                    chb_auto_parking.setChecked(true);
                    parkingTypeId = 1;
                } else {
                    chb_free_parking.setChecked(false);
                    chb_paid_parking.setChecked(false);
                    chb_water_loggings.setChecked(false);
                    chb_busbays_stops.setChecked(false);
                    chb_auto_parking.setChecked(false);
                    parkingTypeId = 0;
                }
            }
        });

        btn_parking_details_dialog_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chb_select_all.isChecked() && !chb_free_parking.isChecked()
                        && !chb_paid_parking.isChecked() && !chb_water_loggings.isChecked()
                        && !chb_busbays_stops.isChecked() && !chb_auto_parking.isChecked()) {
                    mUiHelper.showToastShort("Please select parking type");
                } else {
                    if (chb_select_all.isChecked()) {
                        parkingTypeId = 1;
                    }
                    if (!chb_select_all.isChecked() && chb_free_parking.isChecked()) {
                        parkingTypeId = 2;
                    }
                    if (!chb_select_all.isChecked() && chb_paid_parking.isChecked()) {
                        parkingTypeId = 3;
                    }
                    if (!chb_select_all.isChecked() && chb_water_loggings.isChecked()) {
                        parkingTypeId = 4;
                    }
                    if (!chb_select_all.isChecked() && chb_busbays_stops.isChecked()) {
                        parkingTypeId = 5;
                    }
                    if (!chb_select_all.isChecked() && chb_auto_parking.isChecked()) {
                        parkingTypeId = 6;
                    }
                    getParkingDetails();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_vehicle_type:
                vehicleTypeId = 1 + parent.getSelectedItemPosition();
                String vehicleType = String.valueOf(parent.getSelectedItem());
                Log.i(TAG, "vehicleTypeId-->" + String.valueOf(vehicleTypeId));
                Log.i(TAG, "vehicleType-->" + vehicleType);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getParkingDetails() {
        setCurrentLocation();
        getHydPoliceStations();
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        final JSONObject jsonRequest;
        final String mRequestBody;
        try {
            Map<String, String> params = new HashMap<>();
            params.put(URLParams.parkingTypeId, String.valueOf(parkingTypeId));
            params.put(URLParams.vehicleTypeId, String.valueOf(vehicleTypeId));
            params.put(URLParams.psId, String.valueOf(psId));
            jsonRequest = new JSONObject(params);
            mRequestBody = jsonRequest.toString();
            VolleySingleton.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.POST,
                    URLs.getParkingDetails,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mUiHelper.dismissProgressDialog();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                //parkingDetailsList = new ArrayList<>(jsonArray.length());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    ParkingDetailsModel parkingDetails = new ParkingDetailsModel();
                                    parkingDetails.setId(Integer.parseInt(jsonObject.getString("id")));

                                    JSONObject vehicleTypeObject = jsonObject.getJSONObject("vehicleType");
                                    VehicleTypeModel vehicleType = new VehicleTypeModel();
                                    int vehicleTypeNewId = 0;
                                    try {
                                        vehicleTypeNewId = Integer.parseInt(vehicleTypeObject.getString("id"));
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    vehicleType.setId(vehicleTypeNewId);

                                    String vehicleTypeName = vehicleTypeObject.getString("name");
                                    vehicleType.setName(vehicleTypeName);
                                    parkingDetails.setVehicleType(vehicleType);

                                    JSONObject parkingTypeObject = jsonObject.getJSONObject("parkingType");
                                    ParkingTypeModel parkingType = new ParkingTypeModel();
                                    int parkingTypeNewId = 0;
                                    try {
                                        parkingTypeNewId = Integer.parseInt(parkingTypeObject.getString("id"));
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    parkingType.setId(parkingTypeNewId);

                                    String parkingTypeName = parkingTypeObject.getString("name");
                                    parkingType.setName(parkingTypeName);
                                    parkingType.setIcon(parkingTypeObject.getString("icon"));
                                    parkingType.setBigicon(parkingTypeObject.getString("bigicon"));
                                    parkingDetails.setParkingType(parkingType);

                                    parkingDetails.setLocation(jsonObject.getString("location"));
                                    parkingDetails.setRemarks(jsonObject.getString("remarks"));

                                    String stLat = jsonObject.getString("latitude");
                                    String stLong = jsonObject.getString("langitude");
                                    parkingDetails.setLatitude(stLat);
                                    parkingDetails.setLongitude(stLong);

                                    //parkingDetailsList.add(parkingDetails);

                                    if (stLat != null && !"null".equals(stLat) && stLat.length() > 0
                                            && stLong != null && !"null".equals(stLong) && stLong.length() > 0) {
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        try {
                                            double psLatitude, psLongitude;
                                            psLatitude = Double.parseDouble(stLat);
                                            psLongitude = Double.parseDouble(stLong);
                                            markerOptions.position(new LatLng(psLatitude, psLongitude));
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }

                                        // vehicle type
                                        if (vehicleTypeName != null && !"null".equals(vehicleTypeName) && vehicleTypeName.length() > 0) {
                                            markerOptions.title(vehicleTypeName);
                                            if (vehicleTypeNewId == 1) {
                                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)); // need logo
                                            }
                                            if (vehicleTypeNewId == 2) {
                                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)); // need logo
                                            }
                                            if (vehicleTypeNewId == 3) {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_auto));
                                            }
                                            if (vehicleTypeNewId == 4) {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus));
                                            }
                                            if (vehicleTypeNewId == 5) {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus));
                                            }
                                            if (vehicleTypeNewId == 6) {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus));
                                            }
                                        }

                                        // parking type
                                        if (parkingTypeName != null && !"null".equals(parkingTypeName) && parkingTypeName.length() > 0) {
                                            markerOptions.title(parkingTypeName);
                                            if (parkingTypeNewId == 1) {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bigbus));// all icon
                                            }
                                            if (parkingTypeNewId == 2) {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bigfreepark));
                                            }
                                            if (parkingTypeNewId == 3) {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_paidpark));
                                            }
                                            if (parkingTypeNewId == 4) {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_water));
                                            }
                                            if (parkingTypeNewId == 5) {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bigbus));
                                            }
                                            if (parkingTypeNewId == 6) {
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bigauto));
                                            }
                                        }
                                        mMap.addMarker(markerOptions);
                                    }
                                }
                                mDialogParkingSpace.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mUiHelper.dismissProgressDialog();
                                mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
                            }
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
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mUiHelper.dismissProgressDialog();
            mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
        }
    }
}
