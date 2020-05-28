package com.abcd.hayvandogumtakibi2;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TarihKontrol extends BroadcastReceiver {

    private static final long JOB_PERIOD = 30; //30 dk...
    //private static final long JOB_PERIOD_TEST = 3;
    //private static final long JOB_DEADLINE_TEST = 180000;
    private static final long JOB_DEADLINE = 600000; //10dk;
    private static final int JOB_ID = 0;
    private static final String ACTION_DAY_CHANGED = "android.intent.action.DATE_CHANGED" ;

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<HayvanVeriler> hayvanVerilerArrayList=new SQLiteDatabaseHelper(context).getSimpleData();
        if(hayvanVerilerArrayList.size()!=0){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                JobScheduler scheduler=(JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo.Builder jobBuilder=new JobInfo.Builder(JOB_ID,new ComponentName(context.getPackageName(),DateDedectorService.class.getName()));
                jobBuilder.setMinimumLatency(TimeUnit.MILLISECONDS.convert(JOB_PERIOD,TimeUnit.MINUTES));
                jobBuilder.setOverrideDeadline(JOB_DEADLINE);
                scheduler.schedule(jobBuilder.build());
            }
            else if(intent.getAction().equals(ACTION_DAY_CHANGED)){
                new Bildirimler(context);
            }
        }
    }
}
