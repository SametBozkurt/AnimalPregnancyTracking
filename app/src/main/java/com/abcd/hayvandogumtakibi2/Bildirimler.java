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

public class Bildirimler {

    static final String NOTIFICATION_CHANNEL_ID="Tarih Kontrol";
    static final String NOTIFICATION_CHANNEL_NAME="Kritik Uyarılar";
    Context mContext;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    SQLiteDatabaseHelper veritabani_yonetici;

    public Bildirimler(Context context){
        mContext=context;
        veritabani_yonetici=new SQLiteDatabaseHelper(context);
        hayvanVerilerArrayList=veritabani_yonetici.getKritikOlanlar();
        if(hayvanVerilerArrayList.size()>0){
            bildirim_ver();
        }
    }

    public void bildirim_ver(){
        if(hayvanVerilerArrayList.size()>0){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH);
                NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext,NOTIFICATION_CHANNEL_ID);
                builder.setColorized(true);
                builder.setDefaults(Notification.DEFAULT_ALL);
                    builder.setColor(Color.BLUE);
                builder.setSmallIcon(R.drawable.icon_bildirim);
                builder.setContentTitle(new StringBuilder(mContext.getString(R.string.bildirim_baslik)));
                builder.setContentText(new StringBuilder(mContext.getString(R.string.bildirim_govde_text)));
                Intent intent_bildirim=new Intent(mContext,ActivityKritikler.class);
                PendingIntent pendingIntent=PendingIntent.getActivity(mContext,0,intent_bildirim,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.addAction(R.drawable.icon_not_action,mContext.getString(R.string.bildirim_eylem_text),pendingIntent);
                NotificationManager manager=(NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.createNotificationChannel(notificationChannel);
                manager.notify(0,builder.build());
            }
            else{
                NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext);
                builder.setColorized(true);
                builder.setDefaults(Notification.DEFAULT_ALL);
                builder.setPriority(Notification.PRIORITY_HIGH);
                builder.setColor(Color.BLUE);
                builder.setSmallIcon(R.drawable.icon_bildirim);
                builder.setContentTitle(new StringBuilder(mContext.getString(R.string.bildirim_baslik)));
                builder.setContentText(new StringBuilder(mContext.getString(R.string.bildirim_govde_text)));
                Intent intent_bildirim=new Intent(mContext,ActivityKritikler.class);
                PendingIntent pendingIntent=PendingIntent.getActivity(mContext,0,intent_bildirim,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.addAction(R.drawable.icon_not_action,mContext.getString(R.string.bildirim_eylem_text),pendingIntent);
                NotificationManager manager=(NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0,builder.build());
            }
        }
    }
}
