package com.abcd.hayvandogumtakibi2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmLauncher extends BroadcastReceiver {

    //private static final String INTENT_BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED";
    //private static final String INTENT_SET_AN_ALARM_ACTION = "SET_AN_ALARM";
    private static final String INTENT_REPEAT_ALARM_ACTION = "REPEAT_THE_ALARM_DAILY";
    private static final String NOTIFICATION_CHANNEL_ID="Tarih Kontrol";
    private static final String NOTIFICATION_CHANNEL_NAME="Kritik Uyarılar";
    private static final int ALARM_REQ_CODE = 1233;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(PreferencesHolder.getIsIncomingBirthNotEnabled(context)){
            final int dbSize = SQLiteDatabaseHelper.getInstance(context).getSize();
            if(dbSize!=0){
                final AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                final Intent alarmIntent = new Intent(context, AlarmLauncher.class);
                alarmIntent.setAction(INTENT_REPEAT_ALARM_ACTION);
                final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQ_CODE, alarmIntent, 0);
                final Calendar calendar = Calendar.getInstance();
                if(intent.getAction().equals(INTENT_REPEAT_ALARM_ACTION)){
                    final int sizeKritikler = SQLiteDatabaseHelper.getInstance(context)
                            .getKritikOlanlar(null,PreferencesHolder.getDayRange(context)).size();
                    if(sizeKritikler>0){
                        bildirim_ver(context);
                    }
                    calendar.setTimeInMillis(System.currentTimeMillis()+24*60*60*1000);
                }
                else{
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, PreferencesHolder.getAlarmHour(context));
                    calendar.set(Calendar.MINUTE, 30);
                    if(calendar.before(Calendar.getInstance())) {
                        //If alarm time is before current time, 1 day will be added over alarm time.
                        calendar.add(Calendar.DATE, 1);
                    }
                }
            /*
            AlarmManager.setRepeating is buggy and crap, so i have to do it on my own.
            So my plan is creating a custom Intent Action and putting that action in Alarm Intent as an action.
            After that, the app will repeat the alarm help of my algorithm. Annnnnd it's done :)
             */
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            }
        }
    }

    private void bildirim_ver(final Context mContext){
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
        notificationBuilder.setContentTitle(mContext.getString(R.string.bildirim_baslik));
        notificationBuilder.setContentText(mContext.getString(R.string.bildirim_govde_text));
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());
    }

}
