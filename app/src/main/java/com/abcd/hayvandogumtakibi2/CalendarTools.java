package com.abcd.hayvandogumtakibi2;

import java.util.Date;

public interface CalendarTools {

    void oto_tarih_hesapla(Date date);
    int get_gun_sayisi(long dogum_tarihi_in_millis);

}
