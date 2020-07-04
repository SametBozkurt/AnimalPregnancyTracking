package com.abcd.hayvandogumtakibi2;

import java.util.Calendar;
import java.util.Date;

class TarihHesaplayici {

    private final String isim_tur;
    private final Date date1;
    static final int DAY_COW = 283;
    static final int DAY_SHEEP = 152;
    static final int DAY_GOAT = 150;
    static final int DAY_CAT = 65;
    static final int DAY_DOG = 64;
    static final int DAY_HAMSTER = 16;
    static final int DAY_CAMEL = 390;
    static final int DAY_DONKEY = 365;
    static final int DAY_HORSE = 335;
    private static final String ActivityName = "com.abcd.hayvandogumtakibi2.ActivityTarihHesapla";
    private final int ispet;
    String class_name;

    TarihHesaplayici(int isPet, String tur_isim, Date tarih, String class_name){
        isim_tur=tur_isim;
        date1=tarih;
        ispet=isPet;
        this.class_name=class_name;
    }

    Calendar get_tarih(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date1);
        if(class_name.equals(ActivityName)){
            switch(isim_tur){
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
            switch(ispet){
                case 0: //tum hayvanlar
                    switch(isim_tur){
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
                    switch(isim_tur){
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
                    switch(isim_tur){
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

}
