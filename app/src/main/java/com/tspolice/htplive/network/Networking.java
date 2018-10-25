package com.tspolice.htplive.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Networking {

    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } else {
            return false;
        }
    }
}
