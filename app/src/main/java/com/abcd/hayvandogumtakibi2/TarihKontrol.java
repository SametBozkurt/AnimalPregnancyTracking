package com.abcd.hayvandogumtakibi2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import java.util.ArrayList;

public class TarihKontrol extends BroadcastReceiver {

    private static final int PENDING_INTENT_REQUEST_CODE = 0;
    private static final long TEST_ALARM_INTERVAL = 60*1000; //1dk...
    private static final String ACTION_DAY_CHANGED = "android.intent.action.DATE_CHANGED" ;

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<HayvanVeriler> hayvanVerilerArrayList=new SQLiteDatabaseHelper(context).getAllData();
        if(hayvanVerilerArrayList.size()!=0){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                Intent i=new Intent(context,DateChangeDedector.class);
                PendingIntent alarmPendingIntent=PendingIntent.getBroadcast(context,PENDING_INTENT_REQUEST_CODE,i,PendingIntent.FLAG_UPDATE_CURRENT);
                long test_trigger_time=SystemClock.elapsedRealtime()+TEST_ALARM_INTERVAL;
                AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,test_trigger_time,TEST_ALARM_INTERVAL,alarmPendingIntent);
            }
            else if(intent.getAction().equals(ACTION_DAY_CHANGED)){
                new Bildirimler(context).bildirim_ver();
            }
        }
    }
}
