package com.tspolice.htplive.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.tspolice.htplive.R;
import com.tspolice.htplive.activities.HomeActivity;
import com.tspolice.htplive.activities.MainActivity;

import java.io.IOException;
import java.io.InputStream;

public class UiHelper {

    public ProgressDialog mProgressDialog;
    private Context mContext;

    public UiHelper(Context context) {
        this.mContext = context;
    }

    public void showProgressDialog(String message, boolean cancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public void showProgressDialogLogoTitle(String message, boolean cancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setTitle(mContext.getResources().getString(R.string.app_name));
        mProgressDialog.setIcon(R.drawable.ic_logo);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void alertDialogOkCancel(String message, boolean cancelable, final String actionFlag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.app_name));
        builder.setIcon(mContext.getResources().getDrawable(R.drawable.ic_logo));
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Constants.HOME.equals(actionFlag)) {
                    Activity activity = (Activity) mContext;
                    activity.finish();
                    /*Intent intent_Login = new Intent(mContext, MainActivity.class);
                    intent_Login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent_Login);*/
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void alertDialogOk(String message, boolean cancelable, final String actionFlag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.app_name));
        builder.setIcon(mContext.getResources().getDrawable(R.drawable.ic_logo));
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void alertDialogOkCancelNeutral(String message, boolean cancelable, final String actionFlag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.app_name));
        builder.setIcon(mContext.getResources().getDrawable(R.drawable.ic_logo));
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton(R.string.neutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void alertDialog(String message, boolean cancelable, final String actionFlag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showToastShort(String message) {
        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String message) {
        Toast.makeText(mContext, "" + message, Toast.LENGTH_LONG).show();
    }

    public void showToastShortCentre(String message) {
        Toast toast = Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showToastLongCentre(String message) {
        Toast toast = Toast.makeText(mContext, "" + message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void intent(Class<?> mClass) {
        Intent intent = new Intent(mContext, mClass);
        mContext.startActivity(intent);
    }

    public void replaceFragment(Fragment fragment) {
        HomeActivity.mFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.fade_enter, R.anim.fade_leave)
                .commit();
    }

    public void setTitle(Context context, int resource) {
        ((AppCompatActivity) context).getSupportActionBar().setTitle(resource);
    }

    public void setSubTitle(Context context, int resource) {
        ((AppCompatActivity) context).getSupportActionBar().setSubtitle(resource);
    }

    public String loadJSONFromAssets(final String fileName) {
        String jsonString;
        try {
            InputStream inputStream = mContext.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public void setError(EditText editText, String msg) {
        editText.setError(msg);
    }

    public void requestFocus(EditText editText) {
        editText.requestFocus();
    }
}
