package com.abcd.hayvandogumtakibi2.Misc;

public class DataModel {

    private final int id;
    private final String isim;
    private final String tur;
    private final String kupe_no;
    private final String tohumlama_tarihi;
    private final String dogum_tarihi;
    private final String fotograf_isim;
    private final String sperma_kullanilan;
    private final int is_evcilhayvan;
    private final int dogum_grcklsti;

    public DataModel(final int ID,final String isim,final String tur,
                         final String kupe_no,final String tohumlama_tarihi,
                         final String dogum_tarihi,final String fotograf_isim,final int is_evcilhayvan,
                         final int dogum_grcklsti, final String sperma_kullanilan){
        id=ID;
        this.isim=isim;
        this.tur=tur;
        this.kupe_no=kupe_no;
        this.tohumlama_tarihi=tohumlama_tarihi;
        this.dogum_tarihi=dogum_tarihi;
        this.fotograf_isim=fotograf_isim;
        this.is_evcilhayvan=is_evcilhayvan;
        this.dogum_grcklsti=dogum_grcklsti;
        this.sperma_kullanilan=sperma_kullanilan;
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

    public String getSperma_kullanilan() {
        return sperma_kullanilan;
    }

}
