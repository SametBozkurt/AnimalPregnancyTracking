package com.abcd.hayvandogumtakibi2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TarihHesaplayici {

    String isim_tur,dogum_tarihi,dogum_tarihi_ayrilacak;
    SimpleDateFormat format_date,dateFormatAyrilcak;
    Calendar calendar;
    Date date1;
    private static final int DAY_COW = 283;
    private static final int DAY_SHEEP = 152;
    private static final int DAY_GOAT = 150;
    private static final int DAY_CAT = 65;
    private static final int DAY_DOG = 64;
    private static final int DAY_HAMSTER = 16;
    int ispet;

    public TarihHesaplayici(int isPet, String isim, Date tarih){
        isim_tur=isim;
        date1=tarih;
        ispet=isPet;
    }

    public TarihHesaplayici(String parseble_date){
        dogum_tarihi_ayrilacak=parseble_date;
        dateFormatAyrilcak=new SimpleDateFormat("dd/MM/yyyy");
    }

    public Calendar get_tarih_bilgileri(){
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

    public String getTarih(){
        format_date=new SimpleDateFormat("dd/MM/yyyy");
        calendar=Calendar.getInstance();
        calendar.setTime(date1);
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
                }
                break;
        }
        return null;
    }

}
