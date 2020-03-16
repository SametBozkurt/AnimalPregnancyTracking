package com.abcd.hayvandogumtakibi2;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class TarihHesaplayici {

    private String isim_tur,dogum_tarihi_ayrilacak;
    private SimpleDateFormat dateFormatAyrilcak;
    private Date date1;
    private Context mContext;
    private static final int DAY_COW = 283;
    private static final int DAY_SHEEP = 152;
    private static final int DAY_GOAT = 150;
    private static final int DAY_CAT = 65;
    private static final int DAY_DOG = 64;
    private static final int DAY_HAMSTER = 16;
    private static final int DAY_CAMEL = 390;
    private static final int DAY_DONKEY = 365;
    private static final int DAY_HORSE = 335;
    private static final String ActivityName = "com.abcd.hayvandogumtakibi2.ActivityTarihHesapla";
    private int ispet;

    TarihHesaplayici(int isPet, String isim, Date tarih, Context context){
        isim_tur=isim;
        date1=tarih;
        ispet=isPet;
        mContext=context;
    }

    TarihHesaplayici(String parseble_date){
        dogum_tarihi_ayrilacak=parseble_date;
        dateFormatAyrilcak=new SimpleDateFormat("dd/MM/yyyy");
    }

    Calendar get_tarih_bilgileri(){
        Date date= null;
        try {
            date = dateFormatAyrilcak.parse(dogum_tarihi_ayrilacak);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar mCalendar=Calendar.getInstance();
        mCalendar.setTime(date);
        return mCalendar;
    }

    String getTarih(){
        String dogum_tarihi;
        SimpleDateFormat format_date=new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date1);
        if(mContext.getClass().getName().equals(ActivityName)){
            switch(isim_tur){
                case "0":
                    calendar.add(Calendar.DATE,DAY_COW);
                    dogum_tarihi=format_date.format(calendar.getTime());
                    return dogum_tarihi;
                case "1":
                    calendar.add(Calendar.DATE,DAY_SHEEP);
                    dogum_tarihi=format_date.format(calendar.getTime());
                    return dogum_tarihi;
                case "2":
                    calendar.add(Calendar.DATE,DAY_GOAT);
                    dogum_tarihi=format_date.format(calendar.getTime());
                    return dogum_tarihi;
                case "3":
                    calendar.add(Calendar.DATE,DAY_CAT);
                    dogum_tarihi=format_date.format(calendar.getTime());
                    return dogum_tarihi;
                case "4":
                    calendar.add(Calendar.DATE,DAY_DOG);
                    dogum_tarihi=format_date.format(calendar.getTime());
                    return dogum_tarihi;
                case "5":
                    calendar.add(Calendar.DATE,DAY_HAMSTER);
                    dogum_tarihi=format_date.format(calendar.getTime());
                    return dogum_tarihi;
                case "6":
                    calendar.add(Calendar.DATE,DAY_HORSE);
                    dogum_tarihi=format_date.format(calendar.getTime());
                    return dogum_tarihi;
                case "7":
                    calendar.add(Calendar.DATE,DAY_DONKEY);
                    dogum_tarihi=format_date.format(calendar.getTime());
                    return dogum_tarihi;
                case "8":
                    calendar.add(Calendar.DATE,DAY_CAMEL);
                    dogum_tarihi=format_date.format(calendar.getTime());
                    return dogum_tarihi;
            }
        }
        else{
            switch(ispet){
                case 0: //tum hayvanlar
                    switch(isim_tur){
                        case "0":
                            calendar.add(Calendar.DATE,DAY_COW);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "1":
                            calendar.add(Calendar.DATE,DAY_SHEEP);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "2":
                            calendar.add(Calendar.DATE,DAY_GOAT);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "3":
                            calendar.add(Calendar.DATE,DAY_CAT);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "4":
                            calendar.add(Calendar.DATE,DAY_DOG);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "5":
                            calendar.add(Calendar.DATE,DAY_HAMSTER);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "7":
                            calendar.add(Calendar.DATE,DAY_HORSE);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "8":
                            calendar.add(Calendar.DATE,DAY_DONKEY);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "9":
                            calendar.add(Calendar.DATE,DAY_CAMEL);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                    }
                    break;
                case 1: //evcil hayvanlar
                    switch(isim_tur){
                        case "0":
                            calendar.add(Calendar.DATE,DAY_CAT);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "1":
                            calendar.add(Calendar.DATE,DAY_DOG);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "2":
                            calendar.add(Calendar.DATE,DAY_HAMSTER);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                    }
                    break;
                case 2: //besi hayvanları
                    switch(isim_tur){
                        case "0":
                            calendar.add(Calendar.DATE,DAY_COW);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "1":
                            calendar.add(Calendar.DATE,DAY_SHEEP);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "2":
                            calendar.add(Calendar.DATE,DAY_GOAT);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "4":
                            calendar.add(Calendar.DATE,DAY_HORSE);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "5":
                            calendar.add(Calendar.DATE,DAY_DONKEY);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                        case "6":
                            calendar.add(Calendar.DATE,DAY_CAMEL);
                            dogum_tarihi=format_date.format(calendar.getTime());
                            return dogum_tarihi;
                    }
                    break;
            }
        }
        return null;
    }

}
