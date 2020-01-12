package com.abcd.hayvandogumtakibi2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    static final String VERİTABANI_İSİM="kayitlar";
    static final String SUTUN_1="isim";
    static final String SUTUN_2="hayvan_turu";
    static final String SUTUN_3="kupe_no";
    static final String SUTUN_4="tohumlama_tarihi";
    static final String SUTUN_5="dogum_tarihi";
    static final String SUTUN_6="fotograf_isim";
    static final String SUTUN_7="evcil_hayvan";
    static final String DATABASE_ALTER_CONF_V2 = "ALTER TABLE "
            + VERİTABANI_İSİM + " ADD COLUMN " + SUTUN_6 + " string;";
    static final String DATABASE_ALTER_CONF_V3 = "ALTER TABLE "
            + VERİTABANI_İSİM + " ADD COLUMN " + SUTUN_7 + " INTEGER;";
    Calendar takvim=Calendar.getInstance();
    SimpleDateFormat date_formatter=new SimpleDateFormat("dd/MM/yyyy");
    Date bugun,dogum;
    String date_dogum,date_bugun;
    Long fark_ms,gun_sayisi;

    public SQLiteDatabaseHelper(Context context) {
        super(context, VERİTABANI_İSİM, null, 3);
        degerler();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+VERİTABANI_İSİM+"(id INTEGER PRIMARY KEY,isim TEXT,hayvan_turu TEXT,kupe_no TEXT," +
                "tohumlama_tarihi TEXT,dogum_tarihi TEXT,fotograf_isim TEXT,evcil_hayvan INTEGER" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //i--->Eski veritabani surum kodu
        //i1--->Yeni veritabani surun kodu
        if(i<i1){
            if(i==2){
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V3);
            }
            else if(i==1){
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V2);
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V3);
            }
        }
    }

    private void degerler(){
        date_bugun=takvim.get(Calendar.DAY_OF_MONTH)+"/"+(takvim.get(Calendar.MONTH)+1)+"/"+takvim.get(Calendar.YEAR);
        try {
            bugun=date_formatter.parse(date_bugun);
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    public void veri_yaz(HayvanVeriler hayvanVeriler){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SUTUN_1,hayvanVeriler.getIsim());
        values.put(SUTUN_2,hayvanVeriler.getTur());
        values.put(SUTUN_3,hayvanVeriler.getKupe_no());
        values.put(SUTUN_4,hayvanVeriler.getTohumlama_tarihi());
        values.put(SUTUN_5,hayvanVeriler.getDogum_tarihi());
        values.put(SUTUN_6,hayvanVeriler.getFotograf_isim());
        values.put(SUTUN_7,hayvanVeriler.getIs_evcilhayvan());
        database.insert(VERİTABANI_İSİM,null,values);
        database.close();
    }
    public ArrayList<HayvanVeriler> getAllData(){
        ArrayList<HayvanVeriler> hayvanVerilerArrayList=new ArrayList<HayvanVeriler>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERİTABANI_İSİM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7},
                null,null,null,null,null);
        while(cursor.moveToNext()){
            hayvanVerilerArrayList.add(new HayvanVeriler(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7)));
        }
        return hayvanVerilerArrayList;
    }
    public ArrayList<HayvanVeriler> getKritikOlanlar(){
        ArrayList<HayvanVeriler> hayvanVerilerArrayList=new ArrayList<HayvanVeriler>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERİTABANI_İSİM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7},
                null,null,null,null,null);
        while(cursor.moveToNext()){
            date_dogum=cursor.getString(5);
            try {
                dogum=date_formatter.parse(date_dogum);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            fark_ms=dogum.getTime()-bugun.getTime();
            gun_sayisi=(fark_ms/(1000*60*60*24));
            if(gun_sayisi<30){
                hayvanVerilerArrayList.add(new HayvanVeriler(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7)));
            }
        }
        return  hayvanVerilerArrayList;
    }
    public ArrayList<HayvanVeriler> getAramaSonuclari(boolean isimAranacak,String aranacak){
        ArrayList<HayvanVeriler> hayvanVerilerArrayList=new ArrayList<HayvanVeriler>();
        SQLiteDatabase database=this.getReadableDatabase();
        if(isimAranacak){
            Cursor cursor=database.query(VERİTABANI_İSİM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7},
                    SUTUN_1+" LIKE ?",new String[]{"%"+aranacak+"%"},
                    null,null,null);
            while(cursor.moveToNext()){
                hayvanVerilerArrayList.add(new HayvanVeriler(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7)));
            }
        }
        else{
            Cursor cursor=database.query(VERİTABANI_İSİM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7},
                    SUTUN_3+" LIKE ?",new String[]{"%"+aranacak+"%"},
                    null,null,null);
            while(cursor.moveToNext()){
                hayvanVerilerArrayList.add(new HayvanVeriler(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7)));
            }
        }
        return hayvanVerilerArrayList;
    }
    public void girdiSil(int ID){
        SQLiteDatabase sqLiteDatabase=SQLiteDatabaseHelper.this.getReadableDatabase();
        sqLiteDatabase.delete(VERİTABANI_İSİM,"id=? ",new String[]{Integer.toString(ID)});
    }
    public void guncelle(int ID,String newIsim,String newTur,String newKupe_No,
                         String newTohumTarihi,String newDogumTarihi, String newFotografIsim){
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues newValues=new ContentValues();
        newValues.put(SUTUN_1,newIsim);
        newValues.put(SUTUN_2,newTur);
        newValues.put(SUTUN_3,newKupe_No);
        newValues.put(SUTUN_4,newTohumTarihi);
        newValues.put(SUTUN_5,newDogumTarihi);
        newValues.put(SUTUN_6,newFotografIsim);
        database.update(VERİTABANI_İSİM,newValues,"id=? ",new String[]{Integer.toString(ID)});
    }
}
