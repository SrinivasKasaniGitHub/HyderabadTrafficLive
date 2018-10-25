package com.tspolice.htplive.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tspolice.htplive.R;
import com.tspolice.htplive.models.Distance;
import com.tspolice.htplive.models.DistancePojo;
import com.tspolice.htplive.models.Elements;
import com.tspolice.htplive.models.Rows;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.LocationTrack;
import com.tspolice.htplive.utils.PermissionUtil;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

import com.google.android.gms.location.LocationListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class AutoFareEstmActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        View.OnClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private UiHelper mUiHelper;
    private LocationTrack mLocationTrack;
    private SharedPrefManager mSharedPrefManager;

    private static final String TAG = "AutoFareEstmActivity-->";
    private EditText et_search, et_destination;

    private Dialog mDialogRateChart, mDialogFareEstm;
    public static final int REQUEST_LOCATION_CODE = 99;
    private double latitude, longitude;
    private LatLng latLng;
    private final int SOURCE_PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private final int DEST_PLACE_AUTOCOMPLETE_REQUEST_CODE = 3;
    TextView txt_distance, txt_MinFare, txt_FareEsti, txtB_Day, txtB_Night;
    private String dayPrice, nightPrice, distanceInKm = "";
    ;
    float length, day, night;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autofare_estm);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViews();

        initObjects();

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
            }
        } else {
            PermissionUtil.redirectAppSettings(this);
        }
    }

    private void initViews() {
        et_search = findViewById(R.id.et_search);
        ImageView img_rate_chart = findViewById(R.id.img_rate_chart);
        ImageView img_estimation = findViewById(R.id.img_estimation);
        et_search.setOnClickListener(this);
        img_rate_chart.setOnClickListener(this);
        img_estimation.setOnClickListener(this);
    }

    public void initObjects() {
        mUiHelper = new UiHelper(this);
        mLocationTrack = new LocationTrack(this);
        mSharedPrefManager = SharedPrefManager.getInstance(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    mUiHelper.showToastShort(getResources().getString(R.string.permission_denied));
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            if (mLocationTrack.canGetLocation()) {
                latitude = mLocationTrack.getLatitude();
                longitude = mLocationTrack.getLongitude();
                latLng = new LatLng(latitude, longitude);
                et_search.setText(getAddressFromLatLng(latitude, longitude));
                addMapMarker(latLng);
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mMap.setOnCameraMoveStartedListener(this);
            }
        }
    }

    private String getAddressFromLatLng(double lat, double lng) {
        String address = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder addressBuilder = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    addressBuilder.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                address = addressBuilder.toString();
            } else {
                Log.w(TAG, "No address returned !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Cannot get address !");
        }
        return address;
    }

    private void addMapMarker(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            mMap.setOnCameraMoveCanceledListener(this);
            CameraPosition position = mMap.getCameraPosition();
            latitude = position.target.latitude;
            longitude = position.target.longitude;
            latLng = new LatLng(latitude, longitude);
            addMapMarker(latLng);
            et_search.setText(getAddressFromLatLng(latitude, longitude));
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
            case R.id.et_search:
                autoCompleteMethod(SOURCE_PLACE_AUTOCOMPLETE_REQUEST_CODE);
                break;
            case R.id.img_rate_chart:
                getAutoFares();
                break;
            case R.id.img_estimation:
                fareEstimation();
                break;
            case R.id.btn_rate_chart_dialog_close:
                mDialogRateChart.dismiss();
                break;
            case R.id.btn_fare_estimation_dialog_close:
                mDialogFareEstm.dismiss();
                break;
            case R.id.ll_public_complaints:
                mDialogFareEstm.cancel();
                mUiHelper.intent(PublicComplaintsActivity.class);
                break;
            default:
                break;
        }
    }

    public void autoCompleteMethod(final int place_Code) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            startActivityForResult(intent, place_Code);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SOURCE_PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                addMapMarker(place.getLatLng());
                et_search.setText(place.getAddress());
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                Log.d(TAG, "source_latitude-->" + latitude + ", source_longitude-->" + longitude);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(TAG, "onActivityResult: source_status-->" + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                mUiHelper.showToastLong("User canceled the operation.");
            }
        } else if (requestCode == DEST_PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                et_destination.setText(place.getAddress());
                double dest_latitude = place.getLatLng().latitude;
                double dest_longitude = place.getLatLng().longitude;
                Log.d(TAG, "dest_latitude" + dest_latitude + ", dest_longitude" + dest_longitude);
                getDistanceOnRoad(latitude, longitude, dest_latitude, dest_longitude);
                txt_MinFare.setText("Rs: 20 in Day");
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(TAG, "onActivityResult: dest_status-->" + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                mUiHelper.showToastLong("The user canceled the operation.");
            }
        }
    }

    private void getDistanceOnRoad(double latitude, double longitude, double dest_latitude, double dest_longitude) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        distanceInKm = "";
        final String url_Distance = "https://maps.googleapis.com/maps/api/distancematrix/json?" +
                "origins=" + latitude + "," + longitude + "&destinations=" + dest_latitude + "," + dest_longitude + "&departure_time=now&" +
                "key=AIzaSyAdJ3Z4n1oMYfyViKxK2G1rjCf1pUYpY6Y";
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                url_Distance, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(response.toString());
                        Gson gson = new Gson();
                        DistancePojo distancePojo = gson.fromJson(jsonElement, DistancePojo.class);

                        Rows[] t = distancePojo.getRows();
                        for (Rows rows : t) {
                            Elements[] elmt = rows.getElements();
                            for (Elements emt : elmt) {
                                Distance dist = emt.getDistance();
                                distanceInKm = dist.getText();
                                if (!distanceInKm.isEmpty()) {
                                    break;
                                }
                            }
                        }
                        txt_distance.setText(distanceInKm);
                        getAutoFaresByDistance(distanceInKm);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
    }
    // zjh g

    public void getAutoFaresByDistance(final String distance) {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        String intDistance = distance.replace(" km", "");
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                URLs.getAutoFaresByDistance(intDistance), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mUiHelper.dismissProgressDialog();
                if (response != null && !"".equals(response.toString()) && response.length() > 0) {
                    try {
                        float dist = NumberFormat.getInstance().parse(distance).floatValue();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            length = jsonObject.getLong("meter");
                            day = jsonObject.getLong("day");
                            night = jsonObject.getLong("night");
                            try {
                                float differ = (Math.round(dist)) - length;
                                if (differ > 1) {
                                    int extraDay = (int) (differ + day);
                                    int extraNight = (int) (differ + night);
                                    dayPrice = "Rs : " + Math.round(extraDay * 0.9) + "-" + (Math.round(extraDay * 0.9) + 6) + " Approximately";
                                    nightPrice = "Rs : " + Math.round(extraNight * 0.9) + "-" + (Math.round(extraNight * 0.9) + 6) + " Approximately";
                                    txt_FareEsti.setText(dayPrice);
                                } else {
                                    dayPrice = "Rs : " + Math.round(day * 0.9) + "-" + (Math.round(day * 0.9) + 6) + " Approximately";
                                    nightPrice = "Rs : " + Math.round(night * 0.9) + "-" + (Math.round(night * 0.9) + 6) + " Approximately";
                                    txt_FareEsti.setText(dayPrice);
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                        mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
                    }
                } else {
                    mUiHelper.showToastShort(getResources().getString(R.string.empty_response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mUiHelper.dismissProgressDialog();
                mUiHelper.showToastShort(getResources().getString(R.string.error));
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public void getAutoFares() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET,
                URLs.getAutoFares, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null && !"".equals(response.toString()) && response.length() > 0) {
                            rateChart(response);
                        } else {
                            try {
                                String jsonString = mUiHelper.loadJSONFromAssets("autoFares.json");
                                if (jsonString != null && !"".equals(jsonString)) {
                                    rateChart(new JSONArray(jsonString));
                                } else {
                                    mUiHelper.showToastShort(getResources().getString(R.string.empty_response));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
                            }
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

    private void rateChart(JSONArray jsonArray) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AutoFareEstmActivity.this);
        LayoutInflater inflater = LayoutInflater.from(AutoFareEstmActivity.this);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dlg_rate_chart, null);
        builder.setView(view);
        builder.setCancelable(false);
        mDialogRateChart = builder.create();
        mDialogRateChart.show();

        TableLayout tbl_rate_chart = view.findViewById(R.id.tbl_rate_chart);
        tbl_rate_chart.removeAllViews();

        TableRow tableRow = new TableRow(AutoFareEstmActivity.this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView tv_meters = new TextView(AutoFareEstmActivity.this);
        tv_meters.setText(getResources().getString(R.string.meter_kms));
        tv_meters.setBackgroundResource(R.drawable.cell_heading);
        tv_meters.setGravity(Gravity.CENTER);
        tv_meters.setTextColor(Color.WHITE);
        tv_meters.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tv_meters.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_meters.setPadding(6, 6, 6, 6);
        final int tableTextSize = 30;
        tv_meters.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
        tableRow.addView(tv_meters);

        TextView tv_day_inr = new TextView(AutoFareEstmActivity.this);
        tv_day_inr.setText(getResources().getString(R.string.day_inr));
        tv_day_inr.setBackgroundResource(R.drawable.cell_heading);
        tv_day_inr.setGravity(Gravity.CENTER);
        tv_day_inr.setTextColor(Color.WHITE);
        tv_day_inr.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tv_day_inr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_day_inr.setPadding(6, 6, 6, 6);
        tv_day_inr.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
        tableRow.addView(tv_day_inr);

        TextView tv_night_inr = new TextView(AutoFareEstmActivity.this);
        tv_night_inr.setText(getResources().getString(R.string.night_inr));
        tv_night_inr.setBackgroundResource(R.drawable.cell_heading);
        tv_night_inr.setGravity(Gravity.CENTER);
        tv_night_inr.setTextColor(Color.WHITE);
        tv_night_inr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_night_inr.setPadding(6, 6, 6, 6);
        tv_night_inr.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
        tv_night_inr.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tableRow.addView(tv_night_inr);

        tbl_rate_chart.addView(tableRow, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String meter = jsonObject.getString("meter");
                String day = jsonObject.getString("day");
                String night = jsonObject.getString("night");

                final TableRow tableRow1 = new TableRow(AutoFareEstmActivity.this);
                tableRow1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tableRow1.setClickable(true);

                for (int j = 1; j <= 3; j++) {
                    if (j == 1) {
                        TextView tv = new TextView(AutoFareEstmActivity.this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setBackgroundResource(R.drawable.cell_shape);
                        tv.setPadding(6, 6, 6, 6);
                        tv.setText(meter);
                        tv.setTextColor(Color.BLACK);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
                        tableRow1.addView(tv);
                    } else if (j == 2) {
                        TextView tv = new TextView(AutoFareEstmActivity.this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setBackgroundResource(R.drawable.cell_shape);
                        tv.setPadding(6, 6, 6, 6);
                        tv.setText(day);
                        tv.setTextColor(Color.BLACK);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
                        tableRow1.addView(tv);
                    } else {
                        TextView tv = new TextView(AutoFareEstmActivity.this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setBackgroundResource(R.drawable.cell_shape);
                        tv.setPadding(6, 6, 6, 6);
                        tv.setText(night);
                        tv.setTextColor(Color.BLACK);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableTextSize);
                        tableRow1.addView(tv);
                    }
                }
                tbl_rate_chart.addView(tableRow1);
            }
            mUiHelper.dismissProgressDialog();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button btn_rate_chart_dialog_close = view.findViewById(R.id.btn_rate_chart_dialog_close);
        btn_rate_chart_dialog_close.setOnClickListener(AutoFareEstmActivity.this);
    }

    private void fareEstimation() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AutoFareEstmActivity.this);
        LayoutInflater inflater = LayoutInflater.from(AutoFareEstmActivity.this);
        View view = inflater.inflate(R.layout.dlg_fare_estimation, null);
        builder.setView(view);
        builder.setCancelable(false);
        mDialogFareEstm = builder.create();
        mDialogFareEstm.show();

        et_destination = view.findViewById(R.id.et_destination);
        txt_distance = view.findViewById(R.id.txt_distance);
        txt_MinFare = view.findViewById(R.id.txt_MinFare);
        txt_FareEsti = view.findViewById(R.id.txt_FareEsti);
        txtB_Day = view.findViewById(R.id.txtB_Day);
        txtB_Night = view.findViewById(R.id.txtB_Night);
        LinearLayout ll_public_complaints = view.findViewById(R.id.ll_public_complaints);
        ll_public_complaints.setOnClickListener(this);
        Button btn_fare_estimation_dialog_close = view.findViewById(R.id.btn_fare_estimation_dialog_close);

        btn_fare_estimation_dialog_close.setOnClickListener(AutoFareEstmActivity.this);

        txtB_Day.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                txt_FareEsti.setText(dayPrice);
                txt_MinFare.setText("Rs: 20 in Day");
            }
        });

        txtB_Night.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                txt_FareEsti.setText(nightPrice);
                txt_MinFare.setText("Rs: 30 in Night");
            }
        });

        et_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteMethod(DEST_PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        });
    }
}