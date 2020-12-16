package com.abcd.hayvandogumtakibi2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmLauncher extends BroadcastReceiver {


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("UYARI","AlarmLauncher");
        int dbSize = SQLiteDatabaseHelper.getInstance(context).getSize();
        if(dbSize!=0){
            final AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            final Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1233, alarmIntent, 0);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, PreferencesHolder.getAlarmHour(context));
            calendar.set(Calendar.MINUTE, 30);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),pendingIntent);
            Log.e("UYARI","AlarmSet");
            if(alarmClockInfo!=null){
                calendar.setTimeInMillis(alarmClockInfo.getTriggerTime());
                Log.e("Info-Ay",String.valueOf(calendar.get(Calendar.MONTH)+1));
                Log.e("Info-Gun",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                Log.e("Info-Saat",String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
                Log.e("Info-Dakika",String.valueOf(calendar.get(Calendar.MINUTE)));
            }
        }
    }

}
