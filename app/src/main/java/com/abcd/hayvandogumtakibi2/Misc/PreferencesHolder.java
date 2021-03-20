package com.abcd.hayvandogumtakibi2.Misc;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHolder {

    private static final String PREF_CONF_FILE_NAME = "AppConfigurations";
    private static final String CONF_KEY_HOUR = "alarm_hour";
    private static final String CONF_KEY_DAY_RANGE = "critical_day_range";
    private static final String CONF_KEY_INCOMING_BIRTH_NOT = "is_incoming_birth_not_enabled";
    private static final String CONF_KEY_TEXT_SIZE_OF_CARDS = "text_size_of_cards";
    private static final String CONF_KEY_CACHE_CLEANER = "is_cache_cleaner_enabled";
    private static final String CONF_KEY_IS_LISTED = "is_listed_view_enabled";
    private static final String CONF_KEY_LAST_AD_SHOW_TIME = "last_ad_show_time";

    public static int getAlarmHour(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getInt(CONF_KEY_HOUR, 0);
    }

    public static void setAlarmHour(final Context context, int hour){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(CONF_KEY_HOUR, hour);
        editor.apply();
    }

    public static int getDayRange(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getInt(CONF_KEY_DAY_RANGE, 30);
    }

    public static void setDayRange(final Context context, int range){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(CONF_KEY_DAY_RANGE, range);
        editor.apply();
    }

    public static boolean getIsIncomingBirthNotEnabled(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getBoolean(CONF_KEY_INCOMING_BIRTH_NOT, true);
    }

    public static void setIsIncomingBirthNotEnabled(final Context context, boolean preference){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(CONF_KEY_INCOMING_BIRTH_NOT, preference);
        editor.apply();
    }

    public static float getCardTextSize(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getFloat(CONF_KEY_TEXT_SIZE_OF_CARDS, 14.0f);
    }

    public static void setCardTextSize(final Context context, float size){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(CONF_KEY_TEXT_SIZE_OF_CARDS, size);
        editor.apply();
    }

    public static boolean getIsCacheCleanerEnabled(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getBoolean(CONF_KEY_CACHE_CLEANER, true);
    }

    public static void setIsCacheCleanerEnabled(final Context context, boolean preference){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(CONF_KEY_CACHE_CLEANER, preference);
        editor.apply();
    }

    public static boolean getIsListedViewEnabled(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getBoolean(CONF_KEY_IS_LISTED, false);
    }

    public static void setIsListedViewEnabled(final Context context, boolean preference){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(CONF_KEY_IS_LISTED, preference);
        editor.apply();
    }

    public static long getLastAdShowTime(final Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getLong(CONF_KEY_LAST_AD_SHOW_TIME, 0);
    }

    public static void setLastAdShowTime(final Context context, long newTime){
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(CONF_KEY_LAST_AD_SHOW_TIME, newTime);
        editor.apply();
    }

}
