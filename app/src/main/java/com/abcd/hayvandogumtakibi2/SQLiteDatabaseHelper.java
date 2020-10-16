package com.abcd.hayvandogumtakibi2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private static final String SUTUN_9="sperma_kullanilan";
    private static final String DATABASE_ALTER_CONF_V2 = "ALTER TABLE "
            + VERITABANI_ISIM + " ADD COLUMN " + SUTUN_6 + " TEXT DEFAULT '';";
    private static final String DATABASE_ALTER_CONF_V3 = "ALTER TABLE "
            + VERITABANI_ISIM + " ADD COLUMN " + SUTUN_7 + " INTEGER;";
    private static final String DATABASE_ALTER_CONF_V4 = "ALTER TABLE "
            + VERITABANI_ISIM + " ADD COLUMN " + SUTUN_8 + " INTEGER DEFAULT 0;";
    private static final String DATABASE_ALTER_CONF_V5 = "ALTER TABLE "
            + VERITABANI_ISIM + " ADD COLUMN " + SUTUN_9 + " TEXT DEFAULT '';";
    private String CONVERTED_DATE1, CONVERTED_DATE2;

    public static SQLiteDatabaseHelper getInstance(Context context){
        if(databaseHelper==null){
            databaseHelper=new SQLiteDatabaseHelper(context);
        }
        return databaseHelper;
    }

    private SQLiteDatabaseHelper(Context context) {
        super(context, VERITABANI_ISIM, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+VERITABANI_ISIM+"(id INTEGER PRIMARY KEY,isim TEXT,hayvan_turu TEXT,kupe_no TEXT," +
                "tohumlama_tarihi TEXT,dogum_tarihi TEXT,fotograf_isim TEXT,evcil_hayvan INTEGER," +
                "dogum_grcklsti INTEGER DEFAULT 0,sperma_kullanilan TEXT DEFAULT ''" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //i--->Eski veritabani surum kodu
        //i1--->Yeni veritabani surun kodu
        if(i<i1){
            if(i==4){
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V5);
            }
            else if(i==3){
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V4);
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V5);
            }
            else if(i==2){
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V3);
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V4);
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V5);
            }
            else if(i==1){
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V2);
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V3);
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V4);
                sqLiteDatabase.execSQL(DATABASE_ALTER_CONF_V5);
            }
        }
    }

    void veri_yaz(DataModel dataModel){
        final SQLiteDatabase database=this.getWritableDatabase();
        final ContentValues values=new ContentValues();
        values.put(SUTUN_1,dataModel.getIsim());
        values.put(SUTUN_2,dataModel.getTur());
        values.put(SUTUN_3,dataModel.getKupe_no());
        values.put(SUTUN_4,dataModel.getTohumlama_tarihi());
        values.put(SUTUN_5,dataModel.getDogum_tarihi());
        values.put(SUTUN_7,dataModel.getIs_evcilhayvan());
        values.put(SUTUN_9,dataModel.getSperma_kullanilan());
        database.insert(VERITABANI_ISIM,null,values);
        database.close();
    }

    ArrayList<DataModel> getSimpleData(@Nullable String selectionClause, @Nullable String orderClause){
        long date_in_millis;
        final ArrayList<DataModel> dataModelArrayList=new ArrayList<>();
        final SQLiteDatabase database=this.getReadableDatabase();
        final Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7,SUTUN_8},
                selectionClause,null,null,null, orderClause);
        while(cursor.moveToNext()){
            CONVERTED_DATE1=cursor.getString(4);
            CONVERTED_DATE2=cursor.getString(5);
            try{
                date_in_millis=Long.parseLong(cursor.getString(4));
                date_in_millis=Long.parseLong(cursor.getString(5));
            }
            catch(Exception e){
                check_date_compatibility(cursor.getInt(0), cursor.getString(4), cursor.getString(5));
            }
            finally{
                dataModelArrayList.add(new DataModel(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        CONVERTED_DATE1,
                        CONVERTED_DATE2,
                        cursor.getString(6),
                        cursor.getInt(7),0,null));
            }
        }
        cursor.close();
        return dataModelArrayList;
    }

    ArrayList<DataModel> getAllData(@Nullable String orderClause){
        final ArrayList<DataModel> dataModelArrayList=new ArrayList<>();
        final SQLiteDatabase database=this.getReadableDatabase();
        final Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7,SUTUN_8,SUTUN_9},
                null,null,null,null,orderClause);
        while(cursor.moveToNext()){
            dataModelArrayList.add(new DataModel(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getInt(8),
                    cursor.getString(9)));
        }
        cursor.close();
        return dataModelArrayList;
    }

    ArrayList<DataModel> getKritikOlanlar(@Nullable String orderClause){
        long date_dogum_in_millis = 0;
        final ArrayList<DataModel> dataModelArrayList=new ArrayList<>();
        final SQLiteDatabase database=this.getReadableDatabase();
        final Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7,SUTUN_8},
                null,null,null,null,orderClause);
        while(cursor.moveToNext()){
            if(cursor.getString(5) == null ||cursor.getString(5).isEmpty()){
                continue;
            }
            else{
                CONVERTED_DATE1=cursor.getString(4);
                CONVERTED_DATE2=cursor.getString(5);
                try{
                    date_dogum_in_millis=Long.parseLong(cursor.getString(4));
                    date_dogum_in_millis=Long.parseLong(cursor.getString(5));
                }
                catch(Exception e){
                    check_date_compatibility(cursor.getInt(0), cursor.getString(4), cursor.getString(5));
                    date_dogum_in_millis=Long.parseLong(CONVERTED_DATE2);
                }
                finally{
                    if(get_gun_sayisi(date_dogum_in_millis)<30 && cursor.getInt(8)==0){
                        dataModelArrayList.add(new DataModel(cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                CONVERTED_DATE1,
                                CONVERTED_DATE2,
                                cursor.getString(6),
                                cursor.getInt(7),0,null));
                    }
                }
            }
        }
        cursor.close();
        return dataModelArrayList;
    }

    DataModel getDataById(int id){
        DataModel dataModel = null;
        final SQLiteDatabase database=this.getReadableDatabase();
        final Cursor cursor=database.rawQuery("SELECT * FROM kayitlar WHERE ID='"+id+"'", null);
        while(cursor.moveToNext()){
            dataModel=new DataModel(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getInt(8),
                    cursor.getString(9));
        }
        cursor.close();
        return dataModel;
    }

    ArrayList<DataModel> getAramaSonuclari(boolean isimAranacak, String aranacak){
        final ArrayList<DataModel> hayvanVerilerArrayList=new ArrayList<>();
        final SQLiteDatabase database=this.getReadableDatabase();
        final Cursor cursor;
        if(isimAranacak){
            cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7},
                    SUTUN_1+" LIKE ?",new String[]{"%"+aranacak+"%"},
                    null,null,null);

        }
        else{
            cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7},
                    SUTUN_3+" LIKE ?",new String[]{"%"+aranacak+"%"},
                    null,null,null);
        }
        while(cursor.moveToNext()){
            hayvanVerilerArrayList.add(new DataModel(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),0,null));
        }
        cursor.close();
        return hayvanVerilerArrayList;
    }

    void girdiSil(int ID){
        final SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        sqLiteDatabase.delete(VERITABANI_ISIM,"id=? ",new String[]{Integer.toString(ID)});
    }

    void guncelle(DataModel dataModel){
        final SQLiteDatabase database=this.getReadableDatabase();
        final ContentValues newValues=new ContentValues();
        newValues.put(SUTUN_1,dataModel.getIsim());
        newValues.put(SUTUN_2,dataModel.getTur());
        newValues.put(SUTUN_3,dataModel.getKupe_no());
        newValues.put(SUTUN_4,dataModel.getTohumlama_tarihi());
        newValues.put(SUTUN_5,dataModel.getDogum_tarihi());
        newValues.put(SUTUN_6,dataModel.getFotograf_isim());
        newValues.put(SUTUN_8,dataModel.getDogum_grcklsti());
        newValues.put(SUTUN_9,dataModel.getSperma_kullanilan());
        database.update(VERITABANI_ISIM,newValues,"id=? ",new String[]{Integer.toString(dataModel.getId())});
    }

    void isaretle_dogum_gerceklesti(int ID, long now_in_millis, long est_birth_date_millis){
        final SQLiteDatabase database=this.getReadableDatabase();
        final ContentValues newValues=new ContentValues();
        if(est_birth_date_millis>now_in_millis){
            newValues.put(SUTUN_5,String.valueOf(now_in_millis));
            /* Eğer koşul doğruysa doğum beklenenden önce gerçekleşmiş demektir.
            * Eğer doğum beklenenden önce gerçekleşirse doğum tarihi bugünün tarihi ile değiştirilecektir.
            * */
        }
        newValues.put(SUTUN_8, 1);
        database.update(VERITABANI_ISIM,newValues,"id=? ",new String[]{Integer.toString(ID)});
    }

    void tarih_donustur(int id, String tarih1, String tarih2){
        final SQLiteDatabase database=this.getReadableDatabase();
        final ContentValues newValues=new ContentValues();
        newValues.put(SUTUN_4,tarih1);
        newValues.put(SUTUN_5,tarih2);
        database.update(VERITABANI_ISIM,newValues,"id=? ",new String[]{Integer.toString(id)});
    }

    @Override
    public void oto_tarih_hesapla(Date date) {}

    @Override
    public int get_gun_sayisi(long dogum_tarihi_in_millis) {
        final long gun=(dogum_tarihi_in_millis-System.currentTimeMillis())/DAY_IN_MILLIS;
        return (int)gun;
    }

    void check_date_compatibility(int id, String date1, String date2){
        final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        final Date date_dollenme = new Date();
        final Date date_dogum = new Date();
        if(date1==null||date1.isEmpty()){
            date_dollenme.setTime(System.currentTimeMillis());
        }
        else{
            try {
                date_dollenme.setTime(simpleDateFormat.parse(date1).getTime());
                CONVERTED_DATE1=String.valueOf(date_dollenme.getTime());
            } catch (ParseException e) {
                date_dollenme.setTime(Long.parseLong(date1));
            }
        }
        if(date2==null||date2.isEmpty()){
            date_dogum.setTime(System.currentTimeMillis());
        }
        else{
            try {
                date_dogum.setTime(simpleDateFormat.parse(date2).getTime());
                CONVERTED_DATE2=String.valueOf(date_dogum.getTime());
            } catch (ParseException e) {
                date_dogum.setTime(Long.parseLong(date2));
            }
        }
        tarih_donustur(id, String.valueOf(date_dollenme.getTime()), String.valueOf(date_dogum.getTime()));
    }

    int getSize(){
        final ArrayList<DataModel> arrayList=new ArrayList<>();
        final SQLiteDatabase database=this.getReadableDatabase();
        final Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id"},
                null,null,null,null,null);
        while (cursor.moveToNext()){
            arrayList.add(new DataModel(cursor.getInt(0),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    0,
                    0,
                    null));
        }
        cursor.close();
        return arrayList.size();
    }

}
