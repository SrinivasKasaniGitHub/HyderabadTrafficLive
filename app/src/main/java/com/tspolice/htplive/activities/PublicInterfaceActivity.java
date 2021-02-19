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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tspolice.htplive.R;
import com.tspolice.htplive.network.URLParams;
import com.tspolice.htplive.network.URLs;
import com.tspolice.htplive.network.VolleySingleton;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.PermissionUtil;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;
import com.tspolice.htplive.utils.ValidationUtils;
import com.tspolice.htplive.utils.VolleyMultipartRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PublicInterfaceActivity extends AppCompatActivity implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = "PublicInterfaceFrag-->";
    private UiHelper mUiHelper;
    private Spinner spinner_category;
    private ImageView iv_camera, iv_gallery, iv_display, iv_arrow_next;
    private EditText et_when_why_whom_and_how, et_phone_no, et_reason, et_location;
    private View view;
    private Button btn_submit;
    private String imageFlag = "0", imageData = "", category = "";
    private SharedPrefManager mSharedPrefManager;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_interface);
        requestQueue = Volley.newRequestQueue(this);

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

        //btn_submit.setOnClickListener(this);
        iv_arrow_next.setOnClickListener(this);
    }

    private void initViews() {
        spinner_category = findViewById(R.id.spinner_category);

        iv_camera = findViewById(R.id.iv_camera);
        iv_gallery = findViewById(R.id.iv_gallery);
        iv_display = findViewById(R.id.iv_display);
        iv_arrow_next = findViewById(R.id.iv_arrow_next);

        et_when_why_whom_and_how = findViewById(R.id.et_when_why_whom_and_how);
        et_phone_no = findViewById(R.id.et_phone_no);
        et_reason = findViewById(R.id.et_reason);
        et_location = findViewById(R.id.et_location);
        view = findViewById(R.id.view);
        //btn_submit = findViewById(R.id.btn_submit);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
        mSharedPrefManager = SharedPrefManager.getInstance(this);
    }

    @Override
    public void onClick(View v) {
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
            case R.id.iv_arrow_next:
                String remarks = et_when_why_whom_and_how.getText().toString();
                String phoneNo = et_phone_no.getText().toString().trim();
                String reason = et_reason.getText().toString().trim();
                String location = et_location.getText().toString();
                if (Constants.finalImageFlag.equals(imageFlag)) {
                    mUiHelper.showToastShortCentre(getString(R.string.please_attach_photo));
                } else if (Constants.finalCategory.equals(category)) {
                    mUiHelper.showToastShortCentre(getString(R.string.please_select_category));
                } else if (remarks.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.please_enter_remarks));
                    mUiHelper.requestFocus(et_when_why_whom_and_how);
                } else if (phoneNo.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.please_enter_phone_no));
                    mUiHelper.requestFocus(et_phone_no);
                } else if (!ValidationUtils.isValidMobile(phoneNo)) {
                    mUiHelper.showToastShortCentre(getString(R.string.enter_valid_contact_no));
                    mUiHelper.requestFocus(et_phone_no);
                } else if (reason.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.please_type_a_reason));
                    mUiHelper.requestFocus(et_reason);
                } else if (location.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.please_enter_a_location));
                    mUiHelper.requestFocus(et_location);
                } else {
                    saveCapturedImage(remarks, phoneNo, reason, location);
                }
                break;
            default:
                break;
        }
    }

    // finished with test url
    private void saveCapturedImage(String remarks, String phoneNo, String reason, String location) {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        final JSONObject jsonRequest;
        final String mRequestBody;
        Map<String, String> params = new HashMap<>();
        params.put(URLParams.category, category);
        params.put(URLParams.remarks, remarks);
        params.put(URLParams.mobileNumber, phoneNo);
        params.put(URLParams.reason, reason);
        params.put(URLParams.geoLocation, location);
        jsonRequest = new JSONObject(params);
        mRequestBody = jsonRequest.toString();

        VolleyMultipartRequest spotGenReq = new VolleyMultipartRequest(Request.Method.POST, URLs.saveCapturedImage,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            mUiHelper.dismissProgressDialog();
                            String resPonse = new String(response.data);
                            Log.d("PublicInterface", "" + resPonse);
                            mUiHelper.showToastShortCentre(resPonse);
                            imageData = "";
                            imageFlag = "0";
                            iv_display.setImageDrawable(getResources().getDrawable(R.drawable.ic_gallery2));
                            iv_display.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                            spinner_category.setSelection(0);
                            et_when_why_whom_and_how.setText("");
                            et_when_why_whom_and_how.setHint(getString(R.string.when_why_whom_and_how));
                            et_phone_no.setText("");
                            et_phone_no.setHint(getString(R.string.phone_no));
                            et_reason.setText("");
                            et_reason.setHint(getString(R.string.reason));
                            et_location.setText("");
                            et_location.setHint(getString(R.string.type_location));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mUiHelper.dismissProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("msg", mRequestBody);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("uploadFile", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        requestQueue.add(spotGenReq);


        /*VolleySingleton.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.POST, URLs.saveCapturedImage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mUiHelper.dismissProgressDialog();
                        mUiHelper.showToastShortCentre(response);
                        Log.i(TAG, "response-->"+response);
                        imageData = "";
                        imageFlag = "0";
                        iv_display.setImageDrawable(getResources().getDrawable(R.drawable.ic_gallery2));
                        iv_display.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        spinner_category.setSelection(0);
                        et_when_why_whom_and_how.setText("");
                        et_when_why_whom_and_how.setHint(getString(R.string.when_why_whom_and_how));
                        et_phone_no.setText("");
                        et_phone_no.setHint(getString(R.string.phone_no));
                        et_reason.setText("");
                        et_reason.setHint(getString(R.string.reason));
                        et_location.setText("");
                        et_location.setHint(getString(R.string.type_location));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mUiHelper.dismissProgressDialog();
                mUiHelper.showToastShortCentre(getResources().getString(R.string.error));
                Log.i(TAG, "response-->"+error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(URLParams.jsonData, mRequestBody);
                return params;
            }
        });*/
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
                    mUiHelper.showToastLongCentre(getResources().getString(R.string.permission_denied));
                }
                break;
            case Constants.REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    mUiHelper.showToastLongCentre(getResources().getString(R.string.permission_denied));
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
        view.setVisibility(View.GONE);
        iv_display.setImageBitmap(bitmap);
        imageData = bitmapToString(bitmap);
        //   Log.i(TAG, "imageData-->" + imageData);
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
        view.setVisibility(View.GONE);
        iv_display.setImageBitmap(bitmap);
        imageData = bitmapToString(bitmap);
        // Log.i(TAG, "imageData-->" + imageData);
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
