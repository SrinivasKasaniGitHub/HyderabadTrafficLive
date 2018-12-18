package com.tspolice.htplive.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tspolice.htplive.R;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.GPSTracker;
import com.tspolice.htplive.utils.PermissionUtil;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

import java.util.List;
import java.util.Locale;

public class LiveTrafficActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        View.OnClickListener {

    private static final String TAG = "LiveTrafficActivity-->";
    private GoogleMap mMap;
    private SharedPrefManager mSharedPrefManager;
    private UiHelper mUiHelper;
    private GPSTracker mGpsTracker;
    private LatLng mLatLng;
    private double mLatitude, mLongitude;
    private EditText et_source, et_destination;
    private final int SOURCE_PLACE_AUTO_COMPLETE_REQUEST_CODE = 2, DEST_PLACE_AUTO_COMPLETE_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_traffic);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        initViews();

        initObjects();

        et_source.setOnClickListener(this);

        et_destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_destination.getText().toString().length() > 0) {
                    autoCompleteMethod(DEST_PLACE_AUTO_COMPLETE_REQUEST_CODE);
                }
            }
        });
    }

    private void initViews() {
        et_source = findViewById(R.id.et_source);
        et_destination = findViewById(R.id.et_destination);
    }

    private void initObjects() {
        mGpsTracker = new GPSTracker(this);
        mUiHelper = new UiHelper(this);
        mSharedPrefManager = SharedPrefManager.getInstance(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setTrafficEnabled(true);
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
            }
        } else {
            PermissionUtil.redirectAppSettings(this);
        }
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

    public void setCurrentLocation() {
        if (mGpsTracker.canGetLocation()) {
            mLatitude = mGpsTracker.getLatitude();
            mLongitude = mGpsTracker.getLongitude();
            mLatLng = new LatLng(mLatitude, mLongitude);
            et_source.setText(getAddressFromLatLng(mLatitude, mLongitude));
            addMarker(mLatLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
            mMap.setOnCameraMoveStartedListener(this);
        }
    }

    private String getAddressFromLatLng(double lat, double lng) {
        String s = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                Address address = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(address.getAddressLine(i)).append("\n");
                }
                s = stringBuilder.toString();
            } else {
                Log.w(TAG, "No address returned !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot get address !");
        }
        return s;
    }

    private void addMarker(LatLng latLng) {
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions.title("You are here");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (mGpsTracker.canGetLocation()) {
            if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                mMap.setOnCameraMoveCanceledListener(this);
                CameraPosition position = mMap.getCameraPosition();
                mLatitude = position.target.latitude;
                mLongitude = position.target.longitude;
                mLatLng = new LatLng(mLatitude, mLongitude);
            }
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
            case R.id.et_source:
                autoCompleteMethod(SOURCE_PLACE_AUTO_COMPLETE_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    public void autoCompleteMethod(final int placeCode) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            startActivityForResult(intent, placeCode);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SOURCE_PLACE_AUTO_COMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                addMarker(place.getLatLng());
                et_source.setText(place.getAddress());
                mLatitude = place.getLatLng().latitude;
                mLongitude = place.getLatLng().longitude;
            } else if (resultCode == RESULT_CANCELED) {
                mUiHelper.showToastLong("User canceled the operation");
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "status-->" + status.toString());
            }
        }
        if (requestCode == DEST_PLACE_AUTO_COMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                et_destination.setText(place.getAddress());
                double destLatitude = place.getLatLng().latitude;
                double destLongitude = place.getLatLng().longitude;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" +
                        mLatitude + "," + mLongitude + "&daddr=" + destLatitude + "," + destLongitude));
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                mUiHelper.showToastLong("User canceled the operation");
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "status-->" + status.toString());
            }
        }
    }
}
