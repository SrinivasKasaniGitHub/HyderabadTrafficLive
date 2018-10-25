package com.tspolice.htplive.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.tspolice.htplive.R;

public class PermissionUtil {

    public static int checkPermission(final Context context, int permission) {
        int status = PackageManager.PERMISSION_DENIED;
        switch (permission) {
            case Constants.INT_CAMERA:
                status = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
                break;
            case Constants.INT_STORAGE:
                status = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case Constants.INT_FINE_LOCATION:
                status = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
                break;
            case Constants.INT_COARSE_LOCATION:
                status = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
                break;
            case Constants.INT_CALL_PHONE:
                status = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                break;
            default:
                break;
        }
        return status;
    }

    public static void showPermissionExplanation(final Context context, final int permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_logo);
        builder.setTitle("Permission needed");
        builder.setCancelable(false);
        switch (permission) {
            case Constants.INT_CAMERA:
                builder.setMessage("This app need to access your device camera, Please allow !");
                break;
            case Constants.INT_STORAGE:
                builder.setMessage("This app need to access your device storage, Please allow !");
                break;
            case Constants.INT_FINE_LOCATION:
                builder.setMessage("This app need to access your device fine location, Please allow !");
                break;
            case Constants.INT_COARSE_LOCATION:
                builder.setMessage("This app need to access your device coarse location, Please allow !");
                break;
            case Constants.INT_CALL_PHONE:
                builder.setMessage("This app need to access your device phone calls, Please allow !");
                break;
        }
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermission(context, permission);
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void requestPermission(final Context context, int permission) {
        switch (permission) {
            case Constants.INT_CAMERA:
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, Constants.REQUEST_CAMERA);
                break;
            case Constants.INT_STORAGE:
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_STORAGE);
                break;
            case Constants.INT_FINE_LOCATION:
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_FINE_LOCATION);
                break;
            case Constants.INT_COARSE_LOCATION:
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.REQUEST_COARSE_LOCATION);
                break;
            case Constants.INT_CALL_PHONE:
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, Constants.REQUEST_CALL_PHONE);
                break;
            default:
                break;
        }
    }

    public static void redirectAppSettings(Context context) {
        Toast.makeText(context, "Please allow requested permission in your app settings", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
