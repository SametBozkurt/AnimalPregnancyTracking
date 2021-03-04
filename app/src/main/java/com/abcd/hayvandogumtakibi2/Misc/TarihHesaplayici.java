package com.abcd.hayvandogumtakibi2.Misc;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

public class TarihHesaplayici {

    public int DAY_COW;
    public int DAY_SHEEP;
    public int DAY_GOAT;
    public int DAY_CAT;
    public int DAY_DOG;
    public int DAY_HAMSTER;
    public int DAY_CAMEL;
    public int DAY_DONKEY;
    public int DAY_HORSE;
    public int DAY_PIG;
    public int DAY_TO_ABORT_MILKING_COW;
    public static final int DAY_TO_NEXT_INS_COW = 64;
    private static final String ActivityName = "FragmentTarihHesaplayici";
    private DateChangeListener dateChangeListener;

    public TarihHesaplayici(final Context context){
        PeriodsHolder periodsHolder = PeriodsHolder.getInstance(context);
        DAY_COW= periodsHolder.getPeriodCow();
        DAY_SHEEP= periodsHolder.getPeriodSheep();
        DAY_GOAT= periodsHolder.getPeriodGoat();
        DAY_CAT= periodsHolder.getPeriodCat();
        DAY_DOG= periodsHolder.getPeriodDog();
        DAY_HAMSTER= periodsHolder.getPeriodHamster();
        DAY_HORSE= periodsHolder.getPeriodHorse();
        DAY_DONKEY= periodsHolder.getPeriodDonkey();
        DAY_CAMEL= periodsHolder.getPeriodCamel();
        DAY_PIG= periodsHolder.getPeriodPig();
        DAY_TO_ABORT_MILKING_COW= periodsHolder.getPeriodAbortMilking();
    }

    public void dogum_tarihi_hesapla(final int isPet,final String tur_isim,final Date tarih,final String class_name){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(tarih);
        if(class_name.equals(ActivityName)){
            switch(tur_isim){
                case "0":
                    calendar.add(Calendar.DATE,DAY_COW);
                    break;
                case "1":
                    calendar.add(Calendar.DATE,DAY_SHEEP);
                    break;
                case "2":
                    calendar.add(Calendar.DATE,DAY_GOAT);
                    break;
                case "3":
                    calendar.add(Calendar.DATE,DAY_CAT);
                    break;
                case "4":
                    calendar.add(Calendar.DATE,DAY_DOG);
                    break;
                case "5":
                    calendar.add(Calendar.DATE,DAY_HAMSTER);
                    break;
                case "6":
                    calendar.add(Calendar.DATE,DAY_HORSE);
                    break;
                case "7":
                    calendar.add(Calendar.DATE,DAY_DONKEY);
                    break;
                case "8":
                    calendar.add(Calendar.DATE,DAY_CAMEL);
                    break;
                case "9":
                    calendar.add(Calendar.DATE,DAY_PIG);
                    break;
            }
        }
        else{
            switch(isPet){
                case 0: //tum hayvanlar
                    switch(tur_isim){
                        case "0":
                            calendar.add(Calendar.DATE,DAY_COW);
                            break;
                        case "1":
                            calendar.add(Calendar.DATE,DAY_SHEEP);
                            break;
                        case "2":
                            calendar.add(Calendar.DATE,DAY_GOAT);
                            break;
                        case "3":
                            calendar.add(Calendar.DATE,DAY_CAT);
                            break;
                        case "4":
                            calendar.add(Calendar.DATE,DAY_DOG);
                            break;
                        case "5":
                            calendar.add(Calendar.DATE,DAY_HAMSTER);
                            break;
                        case "6":
                            calendar.add(Calendar.DATE,0);
                            break;
                        case "7":
                            calendar.add(Calendar.DATE,DAY_HORSE);
                            break;
                        case "8":
                            calendar.add(Calendar.DATE,DAY_DONKEY);
                            break;
                        case "9":
                            calendar.add(Calendar.DATE,DAY_CAMEL);
                            break;
                        case "10":
                            calendar.add(Calendar.DATE,DAY_PIG);
                            break;
                    }
                    break;
                case 1: //evcil hayvanlar
                    switch(tur_isim){
                        case "0":
                            calendar.add(Calendar.DATE,DAY_CAT);
                            break;
                        case "1":
                            calendar.add(Calendar.DATE,DAY_DOG);
                            break;
                        case "2":
                            calendar.add(Calendar.DATE,DAY_HAMSTER);
                            break;
                        case "3":
                            calendar.add(Calendar.DATE,0);
                            break;
                    }
                    break;
                case 2: //besi hayvanları
                    switch(tur_isim){
                        case "0":
                            calendar.add(Calendar.DATE,DAY_COW);
                            break;
                        case "1":
                            calendar.add(Calendar.DATE,DAY_SHEEP);
                            break;
                        case "2":
                            calendar.add(Calendar.DATE,DAY_GOAT);
                            break;
                        case "3":
                            calendar.add(Calendar.DATE,0);
                            break;
                        case "4":
                            calendar.add(Calendar.DATE,DAY_HORSE);
                            break;
                        case "5":
                            calendar.add(Calendar.DATE,DAY_DONKEY);
                            break;
                        case "6":
                            calendar.add(Calendar.DATE,DAY_CAMEL);
                            break;
                        case "7":
                            calendar.add(Calendar.DATE,DAY_PIG);
                            break;
                    }
                    break;
            }
        }
        notifyListener(calendar.getTime());
    }

    public static long get_kizdirma_tarihi(long birth_date_in_millis){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(birth_date_in_millis);
        calendar.add(Calendar.DATE,DAY_TO_NEXT_INS_COW);
        return calendar.getTimeInMillis();
    }

    public long get_kuruya_alma_tarihi(long birth_date_in_millis){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(birth_date_in_millis);
        calendar.add(Calendar.DATE,DAY_TO_ABORT_MILKING_COW*-1);
        return calendar.getTimeInMillis();
    }

    public interface DateChangeListener{
        void onNewDateCalculated(Date dateCalculated);
    }

    public void setDateChangeListener(DateChangeListener dateChangeListener){
        this.dateChangeListener=dateChangeListener;
    }

    public void notifyListener(Date date){
        if(dateChangeListener!=null){
            dateChangeListener.onNewDateCalculated(date);
        }
    }

}
