package com.tspolice.htplive.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class GCMTokenRefreshListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        startService(new Intent(GCMTokenRefreshListenerService.this, GCMRegistrationIntentService.class));
    }
}
