package com.abcd.hayvandogumtakibi2;

import android.util.Log;

public class HayvanVeriler {

    private int id;
    private String isim;
    private String tur;
    private String kupe_no;
    private String tohumlama_tarihi;
    private String dogum_tarihi;
    private String fotograf_isim;

    public HayvanVeriler(int ID,String str1,String str2,String str3,String str4,String str5,String str6){
        id=ID;
        isim=str1;
        tur=str2;
        kupe_no=str3;
        tohumlama_tarihi=str4;
        dogum_tarihi=str5;
        fotograf_isim=str6;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getIsim() {
        return isim;
    }

    public void setTur(String tur) {
        this.tur = tur;
    }

    public String getTur() {
        return tur;
    }

    public void setKupe_no(String kupe_no) {
        this.kupe_no = kupe_no;
    }

    public String getKupe_no() {
        return kupe_no;
    }

    public void setTohumlama_tarihi(String tohumlama_tarihi) {
        this.tohumlama_tarihi = tohumlama_tarihi;
    }

    public String getTohumlama_tarihi() {
        return tohumlama_tarihi;
    }

    public void setDogum_tarihi(String dogum_tarihi) {
        this.dogum_tarihi = dogum_tarihi;
    }

    public String getDogum_tarihi() {
        return dogum_tarihi;
    }

    public String getFotograf_isim() {
        if(fotograf_isim==null){
            return "";
        }
        else{
            return fotograf_isim;
        }
    }

    public void setFotograf_isim(String fotograf_isim) {
        this.fotograf_isim=fotograf_isim;
    }
}
