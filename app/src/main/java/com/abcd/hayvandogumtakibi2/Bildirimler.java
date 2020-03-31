package com.abcd.hayvandogumtakibi2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import java.util.ArrayList;
import androidx.core.app.NotificationCompat;

class Bildirimler {

    private static final String NOTIFICATION_CHANNEL_ID="Tarih Kontrol";
    private static final String NOTIFICATION_CHANNEL_NAME="Kritik Uyarılar";
    private Context mContext;
    private ArrayList<HayvanVeriler> hayvanVerilerArrayList;

    Bildirimler(Context context){
        mContext=context;
        SQLiteDatabaseHelper veritabani_yonetici=new SQLiteDatabaseHelper(context);
        hayvanVerilerArrayList=veritabani_yonetici.getKritikOlanlar();
        if(hayvanVerilerArrayList.size()>0){
            bildirim_ver();
        }
    }

    private void bildirim_ver(){
        if(hayvanVerilerArrayList.size()>0){
            Intent intent = new Intent(mContext, ActivityKritikler.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
            notificationBuilder.setColorized(true);
            notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
            notificationBuilder.setColor(Color.BLUE);
            notificationBuilder.setSmallIcon(R.drawable.icon_bildirim);
            notificationBuilder.setContentTitle(new StringBuilder(mContext.getString(R.string.bildirim_baslik)));
            notificationBuilder.setContentText(new StringBuilder(mContext.getString(R.string.bildirim_govde_text)));
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setAutoCancel(true);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
