package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHolder {

    private static final String PREF_CONF_FILE_NAME = "AppConfigurations";
    private static final String CONF_KEY_HOUR = "alarm_hour" ;

    static int getAlarmHour(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getInt("alarm_hour", 0);
    }

    static void setAlarmHour(final Context context, int hour){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(CONF_KEY_HOUR, hour);
        editor.apply();
    }

}
