package com.tspolice.htplive.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.tspolice.htplive.R;
import com.tspolice.htplive.activities.AlertsActivity;

public class GCMPushReceiverService  {
/*
    private static final String TAG = "GCMReceiverService-->";extends GcmListenerService
    private Context context = this;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        if (data != null) {
            try {
                Bundle notificationData = data.getBundle("notification");
                assert notificationData != null;
                String message = notificationData.getString("body");
                String title = notificationData.getString("title");
                sendNotificationToUser(message, title);
                Log.d(TAG, "onMessageReceived()= [" + from + "], data = [" + data + "]");
            } catch (Exception e) {
                e.printStackTrace();
                String message = data.getString("message");
                sendNotificationToUser(message, "");
                Log.d(TAG, "onMessageReceived()= [" + from + "], data = [" + data + "]");
            }
        }
    }

    private void sendNotificationToUser(String message, String title) {
        Intent intent = new Intent(context, AlertsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(GCMPushReceiverService.this);
        builder.setSmallIcon(R.drawable.ic_logo);
        builder.setContentIntent(PendingIntent.getActivity(GCMPushReceiverService.this, 0, intent, 0));
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo));
        builder.setContentTitle(title);
        builder.setContentText(message);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }*/
}