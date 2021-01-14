package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHolder {

    private static final String PREF_CONF_FILE_NAME = "AppConfigurations";
    private static final String CONF_KEY_HOUR = "alarm_hour";
    private static final String CONF_KEY_DAY_RANGE = "critical_day_range";
    private static final String CONF_KEY_INCOMING_BIRTH_NOT = "is_incoming_birth_not_enabled";
    private static final String CONF_KEY_TEXT_SIZE_OF_CARDS = "text_size_of_cards";
    private static final String CONF_KEY_CACHE_CLEANER = "is_cache_cleaner_enabled";

    static int getAlarmHour(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getInt(CONF_KEY_HOUR, 0);
    }

    static void setAlarmHour(final Context context, int hour){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(CONF_KEY_HOUR, hour);
        editor.apply();
    }

    static int getDayRange(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getInt(CONF_KEY_DAY_RANGE, 30);
    }

    static void setDayRange(final Context context, int range){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(CONF_KEY_DAY_RANGE, range);
        editor.apply();
    }

    static boolean getIsIncomingBirthNotEnabled(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getBoolean(CONF_KEY_INCOMING_BIRTH_NOT, true);
    }

    static void setIsIncomingBirthNotEnabled(final Context context, boolean preference){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(CONF_KEY_INCOMING_BIRTH_NOT, preference);
        editor.apply();
    }

    static float getCardTextSize(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getFloat(CONF_KEY_TEXT_SIZE_OF_CARDS, 14.0f);
    }

    static void setCardTextSize(final Context context, float size){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(CONF_KEY_TEXT_SIZE_OF_CARDS, size);
        editor.apply();
    }

    static boolean getIsCacheCleanerEnabled(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getBoolean(CONF_KEY_CACHE_CLEANER, true);
    }

    static void setIsCacheCleanerEnabled(final Context context, boolean preference){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(CONF_KEY_CACHE_CLEANER, preference);
        editor.apply();
    }

}
