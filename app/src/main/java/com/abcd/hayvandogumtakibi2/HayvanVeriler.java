package com.abcd.hayvandogumtakibi2;

public class HayvanVeriler {

    private final int id;
    private final String isim;
    private final String tur;
    private final String kupe_no;
    private final String tohumlama_tarihi;
    private final String dogum_tarihi;
    private final String fotograf_isim;
    private final int is_evcilhayvan;
    private final int dogum_grcklsti;

    public HayvanVeriler(int ID,String str1,String str2,String str3,String str4,String str5,String str6,int evcilhayvan,int dogum_grcklsti){
        id=ID;
        isim=str1;
        tur=str2;
        kupe_no=str3;
        tohumlama_tarihi=str4;
        dogum_tarihi=str5;
        fotograf_isim=str6;
        is_evcilhayvan=evcilhayvan;
        this.dogum_grcklsti=dogum_grcklsti;
    }

    public int getId() {
        return id;
    }

    public String getIsim() {
        return isim;
    }

    public String getTur() {
        return tur;
    }

    public String getKupe_no() {
        return kupe_no;
    }

    public String getTohumlama_tarihi() {
        return tohumlama_tarihi;
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

    public int getIs_evcilhayvan() {
        //Default olarak 0 döner
        return is_evcilhayvan;
    }

    public int getDogum_grcklsti() {
        return dogum_grcklsti;
    }

}
