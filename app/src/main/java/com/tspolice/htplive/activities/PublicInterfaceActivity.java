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

import com.tspolice.htplive.R;
import com.tspolice.htplive.utils.Constants;
import com.tspolice.htplive.utils.PermissionUtil;
import com.tspolice.htplive.utils.SharedPrefManager;
import com.tspolice.htplive.utils.UiHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PublicInterfaceActivity extends AppCompatActivity implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = "PublicInterfaceFrag-->";
    private UiHelper mUiHelper;
    private Spinner spinner_category;
    private ImageView iv_camera, iv_gallery, iv_display;
    EditText et_when_why_whom_and_how, et_phone_no, et_location;
    private Button btn_submit;
    private String imageFlag = "0", userChoosenTask, category, whenWhyWhomHow, phoneNo, location;
    private SharedPrefManager mSharedPrefManager;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_interface);

        /*getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);*/

        initViews();

        initObjects();

        //spinner_category.setSelection(0);
        spinner_category.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("-- Select Category --");
        categories.add("Traffic Violation");
        categories.add("Happening Crime");
        categories.add("Violation by Police");
        categories.add("Suggestions");
        categories.add("Others");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(arrayAdapter);

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
        switch (v.getId()) {
            case R.id.iv_camera:
                userChoosenTask = "CAMERA";
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
                userChoosenTask = "STORAGE";
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
                whenWhyWhomHow = et_when_why_whom_and_how.getText().toString();
                phoneNo = et_phone_no.getText().toString().trim();
                location = et_location.getText().toString();
                if (categoryId == 0) {
                    mUiHelper.showToastShort("Please select category");
                } else if ("0".equals(imageFlag)) {
                    mUiHelper.showToastShort("Please attach photo");
                } else if (whenWhyWhomHow.isEmpty()) {
                    mUiHelper.showToastShort("Please select the reason");
                } else if (phoneNo.isEmpty()) {
                    mUiHelper.showToastShort("Please enter the phone no.");
                } else if (location.isEmpty()) {
                    mUiHelper.showToastShort("Please enter a location");
                } else {
                    // make POST request
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_vehicle_type:
                categoryId = parent.getSelectedItemPosition();
                category = String.valueOf(parent.getSelectedItem());
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
                    if (userChoosenTask.equals("CAMERA"))
                        openCamera();
                    if (userChoosenTask.equals("STORAGE"))
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

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
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
        String imageData = bitmapToString(bitmap);
        Log.i(TAG, "imageData-->" + imageData);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bitmap = null;
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
        String imageData = bitmapToString(bitmap);
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
