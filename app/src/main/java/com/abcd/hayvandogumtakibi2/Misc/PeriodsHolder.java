package com.abcd.hayvandogumtakibi2.Misc;

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
    private static final String PERIOD_PIG = "periodPig";
    private static final String PERIOD_ABORT_MILKING = "periodAbortMilking";
    public static final int CODE_COW=0;
    public static final int CODE_SHEEP=1;
    public static final int CODE_GOAT=2;
    public static final int CODE_CAT=3;
    public static final int CODE_DOG=4;
    public static final int CODE_HAMSTER=5;
    public static final int CODE_HORSE=6;
    public static final int CODE_DONKEY=7;
    public static final int CODE_CAMEL=8;
    public static final int CODE_PIG=9;
    public static final int CODE_ABORT_MILKING=10;
    private static PeriodsHolder periodsHolder=null;
    private final SharedPreferences sharedPref;
    private final SharedPreferences.Editor editor;
    private PeriodUpdaterCallback periodUpdaterCallback;

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

    public int getPeriodPig(){
        return sharedPref.getInt(PERIOD_PIG, 114);
    }

    public int getPeriodAbortMilking(){
        return sharedPref.getInt(PERIOD_ABORT_MILKING, 60);
    }

    public void setPeriodCow(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_COW, Integer.parseInt(day));
            editor.apply();
            if(periodUpdaterCallback!=null){
                periodUpdaterCallback.onPeriodUpdated(CODE_COW,Integer.parseInt(day));
            }
        }
    }

    public void setPeriodSheep(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_SHEEP, Integer.parseInt(day));
            editor.apply();
            if(periodUpdaterCallback!=null){
                periodUpdaterCallback.onPeriodUpdated(CODE_SHEEP,Integer.parseInt(day));
            }
        }
    }

    public void setPeriodGoat(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_GOAT, Integer.parseInt(day));
            editor.apply();
            if(periodUpdaterCallback!=null){
                periodUpdaterCallback.onPeriodUpdated(CODE_GOAT,Integer.parseInt(day));
            }
        }
    }

    public void setPeriodCat(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_CAT, Integer.parseInt(day));
            editor.apply();
            if(periodUpdaterCallback!=null){
                periodUpdaterCallback.onPeriodUpdated(CODE_CAT,Integer.parseInt(day));
            }
        }
    }

    public void setPeriodDog(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_DOG, Integer.parseInt(day));
            editor.apply();
            if(periodUpdaterCallback!=null){
                periodUpdaterCallback.onPeriodUpdated(CODE_DOG,Integer.parseInt(day));
            }
        }
    }

    public void setPeriodHamster(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_HAMSTER, Integer.parseInt(day));
            editor.apply();
            if(periodUpdaterCallback!=null){
                periodUpdaterCallback.onPeriodUpdated(CODE_HAMSTER,Integer.parseInt(day));
            }
        }
    }

    public void setPeriodHorse(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_HORSE, Integer.parseInt(day));
            editor.apply();
            if(periodUpdaterCallback!=null){
                periodUpdaterCallback.onPeriodUpdated(CODE_HORSE,Integer.parseInt(day));
            }
        }
    }

    public void setPeriodDonkey(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_DONKEY, Integer.parseInt(day));
            editor.apply();
            if(periodUpdaterCallback!=null){
                periodUpdaterCallback.onPeriodUpdated(CODE_DONKEY,Integer.parseInt(day));
            }
        }
    }

    public void setPeriodCamel(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_CAMEL, Integer.parseInt(day));
            editor.apply();
            if(periodUpdaterCallback!=null){
                periodUpdaterCallback.onPeriodUpdated(CODE_CAMEL,Integer.parseInt(day));
            }
        }
    }

    public void setPeriodPig(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_PIG, Integer.parseInt(day));
            editor.apply();
            if(periodUpdaterCallback!=null){
                periodUpdaterCallback.onPeriodUpdated(CODE_PIG,Integer.parseInt(day));
            }
        }
    }

    public void setPeriodAbortMilking(String day){
        if(day!=null&&!day.isEmpty()){
            editor.putInt(PERIOD_ABORT_MILKING, Integer.parseInt(day));
            editor.apply();
            if(periodUpdaterCallback!=null){
                periodUpdaterCallback.onPeriodUpdated(CODE_ABORT_MILKING,Integer.parseInt(day));
            }
        }
    }

    public void setPeriodUpdaterCallback(PeriodUpdaterCallback periodUpdaterCallback){
        this.periodUpdaterCallback=periodUpdaterCallback;
    }

    public interface PeriodUpdaterCallback{
        void onPeriodUpdated(int whichOne,int newVal);
    }

}
