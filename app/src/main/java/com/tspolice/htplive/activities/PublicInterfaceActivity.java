package com.tspolice.htplive.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tspolice.htplive.R;
import com.tspolice.htplive.network.URLParams;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleyMultipartRequest;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.PermissionUtil;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicInterfaceActivity extends AppCompatActivity implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = "PublicInterfaceFrag-->";
    private UiHelper mUiHelper;
    private Spinner spinner_category;
    private ImageView iv_camera, iv_gallery, iv_display;
    EditText et_when_why_whom_and_how, et_phone_no, et_location;
    private Button btn_submit;
    private String imageFlag = "0", imageData = "", category = "", remarks, phoneNo, location;
    private SharedPrefManager mSharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_interface);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        initViews();

        initObjects();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.categories));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(arrayAdapter);
        spinner_category.setSelection(0);
        spinner_category.setOnItemSelectedListener(this);

        iv_camera.setOnClickListener(this);
        iv_gallery.setOnClickListener(this);

        btn_submit.setOnClickListener(this);
    }

    private void initViews() {
        spinner_category = findViewById(R.id.spinner_category);

        iv_camera = findViewById(R.id.iv_camera);
        iv_gallery = findViewById(R.id.iv_gallery);
        iv_display = findViewById(R.id.iv_display);

        et_when_why_whom_and_how = findViewById(R.id.et_when_why_whom_and_how);
        et_phone_no = findViewById(R.id.et_phone_no);
        et_location = findViewById(R.id.et_location);

        btn_submit = findViewById(R.id.btn_submit);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
        mSharedPrefManager = SharedPrefManager.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        final String finalCategory = "-- Select Category --", finalImageFlag = "0";
        switch (v.getId()) {
            case R.id.iv_camera:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (PermissionUtil.checkPermission(this, Constants.INT_CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                            PermissionUtil.showPermissionExplanation(this, Constants.INT_CAMERA);
                        } else if (!mSharedPrefManager.getBoolean(Constants.CAMERA)) {
                            PermissionUtil.requestPermission(this, Constants.INT_CAMERA);
                            mSharedPrefManager.putBoolean(Constants.CAMERA, true);
                        } else {
                            PermissionUtil.redirectAppSettings(this);
                        }
                    } else {
                        openCamera();
                    }
                } else {
                    PermissionUtil.redirectAppSettings(this);
                }
                break;
            case R.id.iv_gallery:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (PermissionUtil.checkPermission(this, Constants.INT_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            PermissionUtil.showPermissionExplanation(this, Constants.INT_STORAGE);
                        } else if (!mSharedPrefManager.getBoolean(Constants.STORAGE)) {
                            PermissionUtil.requestPermission(this, Constants.INT_STORAGE);
                            mSharedPrefManager.putBoolean(Constants.STORAGE, true);
                        } else {
                            PermissionUtil.redirectAppSettings(this);
                        }
                    } else {
                        openGallery();
                    }
                } else {
                    PermissionUtil.redirectAppSettings(this);
                }
                break;
            case R.id.btn_submit:
                remarks = et_when_why_whom_and_how.getText().toString();
                phoneNo = et_phone_no.getText().toString().trim();
                location = et_location.getText().toString();
                if (finalImageFlag.equals(imageFlag)) {
                    mUiHelper.showToastShort(getString(R.string.please_attach_photo));
                } else if (finalCategory.equals(category)) {
                    mUiHelper.showToastShort(getString(R.string.please_select_category));
                } else if (remarks.isEmpty()) {
                    mUiHelper.showToastShort(getString(R.string.please_select_the_reason));
                } else if (phoneNo.isEmpty()) {
                    mUiHelper.showToastShort(getString(R.string.please_enter_phone_no));
                } else if (location.isEmpty()) {
                    mUiHelper.showToastShort(getString(R.string.please_enter_a_location));
                } else {
                    saveCapturedImage();
                }
                break;
            default:
                break;
        }
    }

    private void saveCapturedImage() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        JSONObject jsonRequest;
        final String mRequestBody;
        Map<String, String> params = new HashMap<>();
        params.put(URLParams.mobileNumber, phoneNo);
        params.put(URLParams.geoLocation, location);
        params.put(URLParams.remarks, remarks);
        params.put(URLParams.category, category);
        params.put(URLParams.image, imageData);
        params.put(URLParams.reason, "It is a Sample Reason");
        jsonRequest = new JSONObject(params);
        mRequestBody = jsonRequest.toString();

        VolleySingleton.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.POST, URLs.saveCapturedImage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mUiHelper.dismissProgressDialog();
                        mUiHelper.showToastLong(response);
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
                    VolleyLog.wtf(URLs.unSupportedEncodingException, mRequestBody, "utf-8");
                    return null;
                }
            }
        });

        /*VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                URLs.saveCapturedImage,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        mUiHelper.dismissProgressDialog();
                        Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                        try {
                            JSONObject obj = new JSONObject(response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobileNumber", phoneNo);
                params.put("geoLocation", location);
                params.put("remarks", remarks);
                params.put("category", "Traffic Violation");
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(multipartRequest);*/
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_category:
                int categoryId = parent.getSelectedItemPosition();
                category = (String) parent.getSelectedItem();
                Log.i(TAG, "categoryId-->" + String.valueOf(categoryId));
                Log.i(TAG, "category-->" + category);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    mUiHelper.showToastShort(getResources().getString(R.string.permission_denied));
                }
                break;
            case Constants.REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    mUiHelper.showToastShort(getResources().getString(R.string.permission_denied));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == Constants.REQUEST_STORAGE) {
                onSelectFromGalleryResult(data);
            }
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.REQUEST_STORAGE);
    }

    private Bitmap bitmap;

    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        assert bitmap != null;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        //byte[] imgByteArray = bytes.toByteArray();
        //Log.i(TAG, " imgByteArray--> " + imgByteArray);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageFlag = "1";
        iv_display.setVisibility(View.VISIBLE);
        iv_display.setImageBitmap(bitmap);
        imageData = bitmapToString(bitmap);
        Log.i(TAG, "imageData-->" + imageData);
    }

    private void onSelectFromGalleryResult(Intent data) {
        bitmap = null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageFlag = "2";
        iv_display.setVisibility(View.VISIBLE);
        iv_display.setImageBitmap(bitmap);
        imageData = bitmapToString(bitmap);
        Log.i(TAG, "imageData-->" + imageData);
    }

    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
