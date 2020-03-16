package com.abcd.hayvandogumtakibi2;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;

public class CloudMesgService extends FirebaseMessagingService {

    private static final String TAG = "CloudMesgService";
    //private static final String NOTIFICATION_CHANNEL_ID="GCM Service";
    //private static final String NOTIFICATION_CHANNEL_NAME="CloudMessages";

    @Override
    public void onNewToken(@NonNull String s) {
        Log.e(TAG,"new Token: "+s);
    }

}
