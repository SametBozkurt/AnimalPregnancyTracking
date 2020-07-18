package com.abcd.hayvandogumtakibi2;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.Calendar;

public class DateDedectorService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        final Calendar calendar=Calendar.getInstance();
        final SharedPreferences preferences = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.preferences_tag_day_code), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        int day_code= preferences.getInt(getApplicationContext().getString(R.string.preferences_tag_day_code),0);
        if(day_code==0){
            editor.putInt(getApplicationContext().getString(R.string.preferences_tag_day_code),calendar.get(Calendar.DAY_OF_WEEK));
            editor.apply();
        }
        else{
            if(day_code!=calendar.get(Calendar.DAY_OF_WEEK)){
                new Bildirimler(getApplicationContext());
                editor.putInt(getApplicationContext().getString(R.string.preferences_tag_day_code),calendar.get(Calendar.DAY_OF_WEEK));
                editor.apply();
            }
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}
