package com.tspolice.htplive.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.tspolice.htplive.activities.MainActivity;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "htpSharedPref";
    private static SharedPrefManager mInstance;
    private Context mCtx;
    private SharedPreferences preferences;

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    private SharedPrefManager(Context context) {
        mCtx = context;
        preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public void removeString(String s) {
        preferences.edit().remove(s).apply();
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public void removeBoolean(String s) {
        preferences.edit().remove(s).apply();
    }

    public void putInteger(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public int getInteger(String key) {
        return preferences.getInt(key, 0);
    }

    public void removeInteger(String s) {
        preferences.edit().remove(s).apply();
    }

    public void clearPreferences() {
        preferences.edit().clear().apply();
        mCtx.startActivity(new Intent(mCtx, MainActivity.class));
    }
}
