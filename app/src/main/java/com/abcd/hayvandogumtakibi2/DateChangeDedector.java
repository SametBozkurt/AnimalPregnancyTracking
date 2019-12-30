package com.abcd.hayvandogumtakibi2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

public class DateChangeDedector extends BroadcastReceiver {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Calendar calendar;

    @Override
    public void onReceive(Context context, Intent intent) {
        calendar=Calendar.getInstance();
        preferences=context.getSharedPreferences(context.getString(R.string.preferences_tag_day_code),Context.MODE_PRIVATE);
        editor=preferences.edit();
        int day_code=preferences.getInt(context.getString(R.string.preferences_tag_day_code),0);
        if(day_code==0){
            editor.putInt(context.getString(R.string.preferences_tag_day_code),calendar.get(Calendar.DAY_OF_WEEK));
            editor.apply();
        }
        else{
            if(day_code!=calendar.get(Calendar.DAY_OF_WEEK)){
                new Bildirimler(context).bildirim_ver();
                editor.putInt(context.getString(R.string.preferences_tag_day_code),calendar.get(Calendar.DAY_OF_WEEK));
                editor.apply();
            }
        }
    }

}
