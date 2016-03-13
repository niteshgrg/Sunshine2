package com.example.android.sunshine.app.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by niteshgarg on 19/02/16.
 */
public class MyGcmListenerService extends GcmListenerService {

    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "MyGcmListenerService";
    private static final String EXTRA_DATA = "data";
    private static final String EXTRA_WEATHER = "weather";
    private static final String EXTRA_LOCATION = "location";
    private final String LOG_TAG = "GcmListener-trial";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(LOG_TAG, "Listener service started");
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.e(LOG_TAG, "data empty");
        if (!data.isEmpty()) {
            String senderId = getString(R.string.gcm_defaultSenderId);

            if (senderId.length() == 0) {
                Toast.makeText(this, "SenderID string needs to be set", Toast.LENGTH_LONG).show();
            }
            if ((senderId).equals(from)) {
                Log.e(TAG, "hey");
                String weather = data.getString(EXTRA_WEATHER);
                String location = data.getString(EXTRA_LOCATION);
                String alert = String.format(getString(R.string.gcm_weather_alert), weather, location);

                sendNotification(alert);
            }
            Log.i(TAG, "Received: " + data.toString());
        }
    }

    private void sendNotification(String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        // Notifications using both a large and a small icon (which yours should!) need the large
        // icon as a bitmap. So we need to create that here from the resource ID, and pass the
        // object along in our notification builder. Generally, you want to use the app icon as the
        // small icon, so that users understand what app is triggering this notification.
        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.art_storm);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.art_clear)
                        .setLargeIcon(largeIcon)
                        .setContentTitle("Weather Alert!")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}