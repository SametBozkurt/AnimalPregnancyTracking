package com.abcd.hayvandogumtakibi2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import java.util.concurrent.TimeUnit;

public class TarihKontrol extends BroadcastReceiver {

    private static final String NOTIFICATION_CHANNEL_ID="Tarih Kontrol";
    private static final String NOTIFICATION_CHANNEL_NAME="Kritik Uyarılar";
    private static final long JOB_PERIOD = 30; //30 dk...
    private static final long JOB_DEADLINE = 600000; //10dk;
    private static final int JOB_ID = 0;
    private static final String ACTION_DAY_CHANGED = "android.intent.action.DATE_CHANGED" ;
    //private static final long JOB_PERIOD_TEST = 3;
    //private static final long JOB_DEADLINE_TEST = 180000;

    @Override
    public void onReceive(Context context, Intent intent) {
        int dbSize = SQLiteDatabaseHelper.getInstance(context).getSize();
        if(dbSize!=0){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                JobScheduler scheduler=(JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo.Builder jobBuilder=new JobInfo.Builder(JOB_ID,new ComponentName(context.getPackageName(),DateDedectorService.class.getName()));
                jobBuilder.setMinimumLatency(TimeUnit.MILLISECONDS.convert(JOB_PERIOD,TimeUnit.MINUTES));
                jobBuilder.setOverrideDeadline(JOB_DEADLINE);
                scheduler.schedule(jobBuilder.build());
            }
            else if(intent.getAction().equals(ACTION_DAY_CHANGED)){
                dbSize = SQLiteDatabaseHelper.getInstance(context).getKritikOlanlar(null).size();
                if(dbSize > 0){
                    bildirim_ver(context);
                }
            }
        }
    }

    private void bildirim_ver(Context mContext){
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

}
