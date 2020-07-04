package com.abcd.hayvandogumtakibi2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper implements CalendarTools {

    private static SQLiteDatabaseHelper databaseHelper=null;
    private static final long DAY_IN_MILLIS = 1000*60*60*24;
    private static final String VERITABANI_ISIM="kayitlar";
    private static final String SUTUN_1="isim";
    private static final String SUTUN_2="hayvan_turu";
    private static final String SUTUN_3="kupe_no";
    private static final String SUTUN_4="tohumlama_tarihi";
    private static final String SUTUN_5="dogum_tarihi";
    private static final String SUTUN_6="fotograf_isim";
    private static final String SUTUN_7="evcil_hayvan";
    private static final String SUTUN_8="dogum_grcklsti";
    private static final String DATABASE_ALTER_CONF_V2 = "ALTER TABLE "
            + VERITABANI_ISIM + " ADD COLUMN " + SUTUN_6 + " string;";
    private static final String DATABASE_ALTER_CONF_V3 = "ALTER TABLE "
            + VERITABANI_ISIM + " ADD COLUMN " + SUTUN_7 + " INTEGER;";
    private static final String DATABASE_ALTER_CONF_V4 = "ALTER TABLE "
            + VERITABANI_ISIM + " ADD COLUMN " + SUTUN_8 + " INTEGER DEFAULT 0;";

    public static SQLiteDatabaseHelper getInstance(Context context){
        if(databaseHelper==null){
            databaseHelper=new SQLiteDatabaseHelper(context);
        }
        return databaseHelper;
    }

    private SQLiteDatabaseHelper(Context context) {
        super(context, VERITABANI_ISIM, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+VERITABANI_ISIM+"(id INTEGER PRIMARY KEY,isim TEXT,hayvan_turu TEXT,kupe_no TEXT," +
                "tohumlama_tarihi TEXT,dogum_tarihi TEXT,fotograf_isim TEXT,evcil_hayvan INTEGER,dogum_grcklsti INTEGER DEFAULT 0" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //i--->Eski veritabani surum kodu
        //i1--->Yeni veritabani surun kodu
        if(i<i1){
            if(i==3){
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V4);
            }
            else if(i==2){
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V3);
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V4);
            }
            else if(i==1){
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V2);
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V3);
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V4);
            }
        }
    }

    void veri_yaz(HayvanVeriler hayvanVeriler){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SUTUN_1,hayvanVeriler.getIsim());
        values.put(SUTUN_2,hayvanVeriler.getTur());
        values.put(SUTUN_3,hayvanVeriler.getKupe_no());
        values.put(SUTUN_4,hayvanVeriler.getTohumlama_tarihi());
        values.put(SUTUN_5,hayvanVeriler.getDogum_tarihi());
        values.put(SUTUN_6,hayvanVeriler.getFotograf_isim());
        values.put(SUTUN_7,hayvanVeriler.getIs_evcilhayvan());
        database.insert(VERITABANI_ISIM,null,values);
        database.close();
    }
    ArrayList<HayvanVeriler> getSimpleData(){
        ArrayList<HayvanVeriler> hayvanVerilerArrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7},
                null,null,null,null,null);
        while(cursor.moveToNext()){
            hayvanVerilerArrayList.add(new HayvanVeriler(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7), 0));
        }
        cursor.close();
        return hayvanVerilerArrayList;
    }

    int getSize(){
        ArrayList<HayvanVeriler> arrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id"},
                null,null,null,null,null);
        while (cursor.moveToNext()){
            arrayList.add(new HayvanVeriler(cursor.getInt(0),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    0,
                    0));
        }
        cursor.close();
        return arrayList.size();
    }

    ArrayList<HayvanVeriler> getAllData(){
        ArrayList<HayvanVeriler> hayvanVerilerArrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7,SUTUN_8},
                null,null,null,null,null);
        while(cursor.moveToNext()){
            hayvanVerilerArrayList.add(new HayvanVeriler(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getInt(8)));
        }
        cursor.close();
        return hayvanVerilerArrayList;
    }

    ArrayList<HayvanVeriler> getKritikOlanlar(){
        long date_dogum_in_millis;
        ArrayList<HayvanVeriler> hayvanVerilerArrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7,SUTUN_8},
                null,null,null,null,null);
        while(cursor.moveToNext()){
            if(cursor.getString(5) == null ||cursor.getString(5).equals("")){
                continue;
            }
            else{
                date_dogum_in_millis=Long.parseLong(cursor.getString(5));
            }
            if(get_gun_sayisi(date_dogum_in_millis)<30 && cursor.getInt(8)==0){
                hayvanVerilerArrayList.add(new HayvanVeriler(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),0));
            }
        }
        cursor.close();
        return  hayvanVerilerArrayList;
    }

    HayvanVeriler getDataById(int id){
        HayvanVeriler hayvanVeriler = null;
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM kayitlar WHERE ID='"+id+"'", null);
        while(cursor.moveToNext()){
            hayvanVeriler=new HayvanVeriler(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getInt(8));
        }
        cursor.close();
        return hayvanVeriler;
    }

    ArrayList<HayvanVeriler> getAramaSonuclari(boolean isimAranacak, String aranacak){
        ArrayList<HayvanVeriler> hayvanVerilerArrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        if(isimAranacak){
            Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7},
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
                        cursor.getInt(7),0));
            }
            cursor.close();
        }
        else{
            Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7},
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
                        cursor.getInt(7),0));
            }
            cursor.close();
        }
        return hayvanVerilerArrayList;
    }

    ArrayList<HayvanVeriler> getGerceklesenler(){
        ArrayList<HayvanVeriler> hayvanVerilerArrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7,SUTUN_8},
                SUTUN_8+" LIKE ?",new String[]{"%1%"},
                null,null,null);
        while(cursor.moveToNext()){
            hayvanVerilerArrayList.add(new HayvanVeriler(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    null,
                    null,
                    null,
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getInt(8)));
        }
        cursor.close();
        return hayvanVerilerArrayList;
    }

    void girdiSil(int ID){
        SQLiteDatabase sqLiteDatabase=SQLiteDatabaseHelper.this.getReadableDatabase();
        sqLiteDatabase.delete(VERITABANI_ISIM,"id=? ",new String[]{Integer.toString(ID)});
    }
    void guncelle(int ID, String newIsim, String newTur, String newKupe_No,
                  String newTohumTarihi, String newDogumTarihi, String newFotografIsim,int dogum_grcklsti){
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues newValues=new ContentValues();
        newValues.put(SUTUN_1,newIsim);
        newValues.put(SUTUN_2,newTur);
        newValues.put(SUTUN_3,newKupe_No);
        newValues.put(SUTUN_4,newTohumTarihi);
        newValues.put(SUTUN_5,newDogumTarihi);
        newValues.put(SUTUN_6,newFotografIsim);
        newValues.put(SUTUN_8,dogum_grcklsti);
        database.update(VERITABANI_ISIM,newValues,"id=? ",new String[]{Integer.toString(ID)});
    }
    void isaretle_dogum_gerceklesti(int ID){
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues newValues=new ContentValues();
        newValues.put(SUTUN_8, 1);
        database.update(VERITABANI_ISIM,newValues,"id=? ",new String[]{Integer.toString(ID)});
    }
    void tarih_donustur(int id, String tarih1, String tarih2){
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues newValues=new ContentValues();
        newValues.put(SUTUN_4,tarih1);
        newValues.put(SUTUN_5,tarih2);
        database.update(VERITABANI_ISIM,newValues,"id=? ",new String[]{Integer.toString(id)});
    }

    @Override
    public void oto_tarih_hesapla(Date date) {

    }

    @Override
    public int get_gun_sayisi(long dogum_tarihi_in_millis) {
        long gun=(dogum_tarihi_in_millis-Calendar.getInstance().getTimeInMillis())/DAY_IN_MILLIS;
        return (int)gun;
    }
}
