package com.abcd.hayvandogumtakibi2.Misc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static SQLiteDatabaseHelper databaseHelper=null;
    private static final long DAY_IN_MILLIS = 1000*60*60*24;
    private static final String VERITABANI_ISIM="kayitlar";
    public static final String SUTUN_0="id";
    public static final String SUTUN_1="isim";
    public static final String SUTUN_2="hayvan_turu";
    public static final String SUTUN_3="kupe_no";
    public static final String SUTUN_4="tohumlama_tarihi";
    public static final String SUTUN_5="dogum_tarihi";
    public static final String SUTUN_6="fotograf_isim";
    public static final String SUTUN_7="evcil_hayvan";
    public static final String SUTUN_8="dogum_grcklsti";
    public static final String SUTUN_9="sperma_kullanilan";
    private static final String DATABASE_ALTER_CONF_V2 = "ALTER TABLE "
            + VERITABANI_ISIM + " ADD COLUMN " + SUTUN_6 + " TEXT DEFAULT '';";
    private static final String DATABASE_ALTER_CONF_V3 = "ALTER TABLE "
            + VERITABANI_ISIM + " ADD COLUMN " + SUTUN_7 + " INTEGER;";
    private static final String DATABASE_ALTER_CONF_V4 = "ALTER TABLE "
            + VERITABANI_ISIM + " ADD COLUMN " + SUTUN_8 + " INTEGER DEFAULT 0;";
    private static final String DATABASE_ALTER_CONF_V5 = "ALTER TABLE "
            + VERITABANI_ISIM + " ADD COLUMN " + SUTUN_9 + " TEXT DEFAULT '';";
    private String CONVERTED_DATE1, CONVERTED_DATE2;

    public static SQLiteDatabaseHelper getInstance(final Context context){
        if(databaseHelper==null){
            databaseHelper=new SQLiteDatabaseHelper(context);
        }
        return databaseHelper;
    }

    private SQLiteDatabaseHelper(final Context context) {
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

    public void kayit_ekle(DataModel dataModel){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SUTUN_1,dataModel.getIsim());
        values.put(SUTUN_2,dataModel.getTur());
        values.put(SUTUN_3,dataModel.getKupe_no());
        values.put(SUTUN_4,dataModel.getTohumlama_tarihi());
        values.put(SUTUN_5,dataModel.getDogum_tarihi());
        values.put(SUTUN_6,dataModel.getFotograf_isim());
        values.put(SUTUN_7,dataModel.getIs_evcilhayvan());
        values.put(SUTUN_9,dataModel.getSperma_kullanilan());
        database.insert(VERITABANI_ISIM,null,values);
        database.close();
    }

    public ArrayList<DataModel> getSimpleData(@Nullable String selectionClause, @Nullable String orderClause){
        long date_in_millis;
        ArrayList<DataModel> dataModelArrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7,SUTUN_8},
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

    public ArrayList<DataModel> getAllData(@Nullable String selectionClause, @Nullable String orderClause){
        ArrayList<DataModel> dataModelArrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7,SUTUN_8,SUTUN_9},
                selectionClause,null,null,null,orderClause);
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

    public ArrayList<DataModel> getKritikOlanlar(@Nullable String orderClause, int range){
        long date_dogum_in_millis = 0;
        ArrayList<DataModel> dataModelArrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7,SUTUN_8},
                null,null,null,null,orderClause);
        while(cursor.moveToNext()){
            if(cursor.getString(5) != null && !cursor.getString(5).isEmpty()){
                CONVERTED_DATE1=cursor.getString(4);
                CONVERTED_DATE2=cursor.getString(5);
                try{
                    date_dogum_in_millis=Long.parseLong(cursor.getString(4));
                    date_dogum_in_millis=Long.parseLong(cursor.getString(5));
                }
                catch(Exception e){
                    check_date_compatibility(cursor.getInt(0),cursor.getString(4),cursor.getString(5));
                    date_dogum_in_millis=Long.parseLong(CONVERTED_DATE2);
                }
                finally{
                    if(get_gun_sayisi(date_dogum_in_millis)<range && cursor.getInt(8)==0){
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

    public DataModel getDataById(int id){
        DataModel dataModel = null;
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM kayitlar WHERE ID='"+id+"'", null);
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

    public ArrayList<DataModel> getAramaSonuclari(@NonNull String selection, @NonNull String aranacak, @Nullable String orderBy){
        ArrayList<DataModel> hayvanVerilerArrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7},
                selection+" LIKE ?",new String[]{"%"+aranacak+"%"},
                null,null,orderBy);
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

    public ArrayList<DataModel> getEnYakinDogumlar(){
        byte sayac=0;
        long date_dogum_in_millis = 0;
        ArrayList<DataModel> dataModelArrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7,SUTUN_8},
                "dogum_grcklsti=0",null,null,null,SUTUN_5+" ASC");
        while(cursor.moveToNext() && sayac<3){
            if(cursor.getString(5) != null && !cursor.getString(5).isEmpty()){
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
                                null,
                                null,
                                CONVERTED_DATE2,
                                cursor.getString(6),
                                cursor.getInt(7),0,null));
                        sayac+=1;
                    }
                }
            }
        }
        cursor.close();
        return dataModelArrayList;
    }

    public ArrayList<DataModel> getSonOlusturulanlar(){
        byte sayac=0;
        ArrayList<DataModel> dataModelArrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id",SUTUN_1,SUTUN_2,SUTUN_3,SUTUN_4,SUTUN_5,SUTUN_6,SUTUN_7,SUTUN_8,SUTUN_9},
                null,null,null,null,SUTUN_0+" DESC");
        while(cursor.moveToNext() && sayac<3){
            dataModelArrayList.add(new DataModel(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    null,
                    null,
                    null,
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getInt(8),
                    cursor.getString(9)));
            sayac+=1;
        }
        cursor.close();
        return dataModelArrayList;
    }

    public void girdiSil(int ID){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        sqLiteDatabase.delete(VERITABANI_ISIM,"id=? ",new String[]{Integer.toString(ID)});
    }

    public void guncelle(DataModel dataModel){
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues newValues=new ContentValues();
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

    public void isaretle_dogum_gerceklesti(int ID, long now_in_millis, long est_birth_date_millis){
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues newValues=new ContentValues();
        if(est_birth_date_millis>now_in_millis){
            newValues.put(SUTUN_5,String.valueOf(now_in_millis));
            /* Eğer koşul doğruysa doğum beklenenden önce gerçekleşmiş demektir.
            * Eğer doğum beklenenden önce gerçekleşirse doğum tarihi bugünün tarihi ile değiştirilecektir.
            * */
        }
        newValues.put(SUTUN_8, 1);
        database.update(VERITABANI_ISIM,newValues,"id=? ",new String[]{Integer.toString(ID)});
    }

    private void tarih_donustur(int id, String tarih1, String tarih2){
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues newValues=new ContentValues();
        newValues.put(SUTUN_4,tarih1);
        newValues.put(SUTUN_5,tarih2);
        database.update(VERITABANI_ISIM,newValues,"id=? ",new String[]{Integer.toString(id)});
    }

    public int get_gun_sayisi(long dogum_tarihi_in_millis) {
        long gun=(dogum_tarihi_in_millis-System.currentTimeMillis())/DAY_IN_MILLIS;
        return (int)gun;
    }

    private void check_date_compatibility(int id, String date1, String date2){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        Date date_dollenme = new Date();
        Date date_dogum = new Date();
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

    public int getSize(){
        ArrayList<DataModel> arrayList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(VERITABANI_ISIM,new String[]{"id"},
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

    public void recalculateEstBirthDates(final Context context){
        TarihHesaplayici tarihHesaplayici=TarihHesaplayici.getInstance(context);
        final SQLiteDatabase database=this.getReadableDatabase();
        final ContentValues newValues=new ContentValues();
        Date date=new Date();
        ArrayList<DataModel> dataModelArrayList;
        for(int y=0;y<3;y++){
            dataModelArrayList=getSimpleData("dogum_grcklsti=0 AND evcil_hayvan="+y,null);
            switch(y){
                case 0:
                    for(int x=0;x<dataModelArrayList.size();x++){
                        final DataModel dataModel=dataModelArrayList.get(x);
                        if(!dataModel.getTur().equals("6")){
                            date.setTime(Long.parseLong(dataModel.getTohumlama_tarihi()));
                            tarihHesaplayici.setDateChangeListener(new TarihHesaplayici.DateChangeListener() {
                                @Override
                                public void onNewDateCalculated(Date dateCalculated) {
                                    newValues.put(SUTUN_5,String.valueOf(dateCalculated.getTime()));
                                    database.update(VERITABANI_ISIM,newValues,"id=? ",new String[]{Integer.toString(dataModel.getId())});
                                }
                            });
                            tarihHesaplayici.dogum_tarihi_hesapla(dataModel.getIs_evcilhayvan(),dataModel.getTur(),date,getClass().getName());
                        }
                    }
                    break;
                case 1: case 2:
                    for(int x=0;x<dataModelArrayList.size();x++){
                        final DataModel dataModel=dataModelArrayList.get(x);
                        if(!dataModel.getTur().equals("3")){
                            date.setTime(Long.parseLong(dataModel.getTohumlama_tarihi()));
                            tarihHesaplayici.setDateChangeListener(new TarihHesaplayici.DateChangeListener() {
                                @Override
                                public void onNewDateCalculated(Date dateCalculated) {
                                    newValues.put(SUTUN_5,String.valueOf(dateCalculated.getTime()));
                                    database.update(VERITABANI_ISIM,newValues,"id=? ",new String[]{Integer.toString(dataModel.getId())});
                                }
                            });
                            tarihHesaplayici.dogum_tarihi_hesapla(dataModel.getIs_evcilhayvan(),dataModel.getTur(),date,getClass().getName());
                        }
                    }
                    break;
            }
        }
    }
}