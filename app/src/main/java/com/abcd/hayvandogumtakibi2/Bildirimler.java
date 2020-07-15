package com.abcd.hayvandogumtakibi2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import static android.content.Context.MODE_PRIVATE;

class Bildirimler {

    private static final String pref_key = "dates_converted";
    private static final String pref_file = "preferences";
    private static final String NOTIFICATION_CHANNEL_ID="Tarih Kontrol";
    private static final String NOTIFICATION_CHANNEL_NAME="Kritik Uyarılar";
    private final Context mContext;
    private final SharedPreferences preferences;
    private final SQLiteDatabaseHelper databaseHelper;

    Bildirimler(Context context){
        mContext=context;
        preferences = context.getSharedPreferences(pref_file,MODE_PRIVATE);
        databaseHelper=SQLiteDatabaseHelper.getInstance(mContext);
        int dbSize = databaseHelper.getSize();
        if(dbSize >0){
            tarih_donustur();
        }
        dbSize =databaseHelper.getKritikOlanlar().size();
        if(dbSize >0){
            bildirim_ver();
        }
    }
    private void bildirim_ver(){
        final Intent intent = new Intent(mContext, ActivityKritikler.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            final NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        final PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
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
    private void tarih_donustur(){
        if(!preferences.contains(pref_key)){
            final SharedPreferences.Editor editor=preferences.edit();
            editor.putInt(pref_key,1);
            editor.apply();
            final ArrayList<HayvanVeriler> hayvanVerilerArrayList=databaseHelper.getSimpleData();
            final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
            int sayac=0;
            Date date_dollenme = new Date(),date_dogum = new Date();
            while(sayac<hayvanVerilerArrayList.size()) {
                try {
                    date_dollenme.setTime(simpleDateFormat.parse(hayvanVerilerArrayList.get(sayac).getTohumlama_tarihi()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                    date_dollenme.setTime(Long.parseLong(hayvanVerilerArrayList.get(sayac).getTohumlama_tarihi()));
                }
                try {
                    date_dogum.setTime(simpleDateFormat.parse(hayvanVerilerArrayList.get(sayac).getDogum_tarihi()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                    date_dogum.setTime(Long.parseLong(hayvanVerilerArrayList.get(sayac).getDogum_tarihi()));
                }
                databaseHelper.tarih_donustur(hayvanVerilerArrayList.get(sayac).getId(),
                        String.valueOf(date_dollenme.getTime()),
                        String.valueOf(date_dogum.getTime()));
                sayac++;
            }
        }
    }
}
