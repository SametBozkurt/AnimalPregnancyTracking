package com.abcd.hayvandogumtakibi2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PeriodsHolder {

    private static final String PREF_CONF_FILE_NAME = "GestationPeriods";
    private static final String PERIOD_COW = "periodCow";
    private static final String PERIOD_SHEEP = "periodSheep";
    private static final String PERIOD_GOAT = "periodGoat";
    private static final String PERIOD_CAT = "periodCat";
    private static final String PERIOD_DOG = "periodDog";
    private static final String PERIOD_HAMSTER = "periodHamster";
    private static final String PERIOD_HORSE = "periodHorse";
    private static final String PERIOD_DONKEY = "periodDonkey";
    private static final String PERIOD_CAMEL = "periodCamel";
    private static final String PERIOD_ABORT_MILKING = "periodAbortMilking";
    private static PeriodsHolder periodsHolder=null;
    private final SharedPreferences sharedPref;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    private PeriodsHolder(final Context context){
        sharedPref = context.getSharedPreferences(PREF_CONF_FILE_NAME,Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public static PeriodsHolder getInstance(final Context context){
        if(periodsHolder==null){
            periodsHolder=new PeriodsHolder(context);
        }
        return periodsHolder;
    }

    public int getPeriodCow(){
        return sharedPref.getInt(PERIOD_COW, 283);
    }

    public int getPeriodSheep(){
        return sharedPref.getInt(PERIOD_SHEEP, 152);
    }

    public int getPeriodGoat(){
        return sharedPref.getInt(PERIOD_GOAT, 150);
    }

    public int getPeriodCat(){
        return sharedPref.getInt(PERIOD_CAT, 65);
    }

    public int getPeriodDog(){
        return sharedPref.getInt(PERIOD_DOG, 64);
    }

    public int getPeriodHamster(){
        return sharedPref.getInt(PERIOD_HAMSTER, 16);
    }

    public int getPeriodHorse(){
        return sharedPref.getInt(PERIOD_HORSE, 335);
    }

    public int getPeriodDonkey(){
        return sharedPref.getInt(PERIOD_DONKEY, 365);
    }

    public int getPeriodCamel(){
        return sharedPref.getInt(PERIOD_CAMEL, 390);
    }

    public int getPeriodAbortMilking(){
        return sharedPref.getInt(PERIOD_ABORT_MILKING, 60);
    }

    public void setPeriodCow(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_COW, Integer.parseInt(day));
            editor.apply();
        }
    }

    public void setPeriodSheep(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_SHEEP, Integer.parseInt(day));
            editor.apply();
        }
    }

    public void setPeriodGoat(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_GOAT, Integer.parseInt(day));
            editor.apply();
        }
    }

    public void setPeriodCat(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_CAT, Integer.parseInt(day));
            editor.apply();
        }
    }

    public void setPeriodDog(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_DOG, Integer.parseInt(day));
            editor.apply();
        }
    }

    public void setPeriodHamster(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_HAMSTER, Integer.parseInt(day));
            editor.apply();
        }
    }

    public void setPeriodHorse(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_HORSE, Integer.parseInt(day));
            editor.apply();
        }
    }

    public void setPeriodDonkey(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_DONKEY, Integer.parseInt(day));
            editor.apply();
        }
    }

    public void setPeriodCamel(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_CAMEL, Integer.parseInt(day));
            editor.apply();
        }
    }

    public void setPeriodAbortMilking(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_ABORT_MILKING, Integer.parseInt(day));
            editor.apply();
        }
    }

}
