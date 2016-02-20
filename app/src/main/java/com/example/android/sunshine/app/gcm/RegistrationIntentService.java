package com.example.android.sunshine.app.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by niteshgarg on 18/02/16.
 */
public class RegistrationIntentService extends IntentService {

    public static final String TAG = "RegIntentService";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    ;


    public RegistrationIntentService() {
        super(TAG);
        Log.e(LOG_TAG, "regintentservice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Log.e(LOG_TAG, "regintentservice");

        try {
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);

                String senderId = getString(R.string.gcm_defaultSenderId);
                Log.e(LOG_TAG, "sender id:" + senderId);
                if (senderId.length() != 0) {
                    String token = instanceID.getToken(senderId,
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    sendRegistrationToServer(token);
                }
                sharedPreferences.edit().putBoolean(MainActivity.SENT_TOKEN_TO_SERVER, true).apply();

            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(MainActivity.SENT_TOKEN_TO_SERVER, false).apply();
        }

    }

    private void sendRegistrationToServer(String token) {
        Log.e(TAG, "GCM Registration Token : " + token);
    }
}
