package com.abcd.hayvandogumtakibi2;

import java.util.Calendar;
import java.util.Date;

public class TarihHesaplayici {

    static final int DAY_COW = 283;
    static final int DAY_SHEEP = 152;
    static final int DAY_GOAT = 150;
    static final int DAY_CAT = 65;
    static final int DAY_DOG = 64;
    static final int DAY_HAMSTER = 16;
    static final int DAY_CAMEL = 390;
    static final int DAY_DONKEY = 365;
    static final int DAY_HORSE = 335;
    static final int DAY_TO_NEXT_INS_COW = 64;
    static final int DAY_TO_ABORT_MILKING_COW = -60;
    private static final String ActivityName = "com.abcd.hayvandogumtakibi2.FragmentTarihHesaplayici";
    private DateChangeListener dateChangeListener;
    private static TarihHesaplayici tarihHesaplayici=null;

    private TarihHesaplayici(){}

    public static TarihHesaplayici getInstance(){
        if(tarihHesaplayici==null){
            tarihHesaplayici=new TarihHesaplayici();
        }
        return tarihHesaplayici;
    }

    public static Calendar get_dogum_tarihi(final int isPet,final String tur_isim,final Date tarih,final String class_name){
        final Calendar calendar=Calendar.getInstance();
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
                    }
                    break;
            }
        }
        return calendar;
    }

    public static long get_kizdirma_tarihi(long birth_date_in_millis){
        final Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(birth_date_in_millis);
        calendar.add(Calendar.DATE,DAY_TO_NEXT_INS_COW);
        return calendar.getTimeInMillis();
    }

    static long get_kuruya_alma_tarihi(long birth_date_in_millis){
        final Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(birth_date_in_millis);
        calendar.add(Calendar.DATE,DAY_TO_ABORT_MILKING_COW);
        return calendar.getTimeInMillis();
    }

    public interface DateChangeListener{
        void onNewDateSet();
    }

    public void setDateChangeListener(final DateChangeListener dateChangeListener){
        this.dateChangeListener=dateChangeListener;
    }

    public void sendDate(){
        if(dateChangeListener!=null){
            dateChangeListener.onNewDateSet();
        }
    }

}
