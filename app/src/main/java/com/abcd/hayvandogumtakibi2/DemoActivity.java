package com.abcd.hayvandogumtakibi2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DemoActivity extends AppCompatActivity {

    SQLiteDatabaseHelper databaseHelper;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    SharedPreferences preferences;
    private static final String pref_key = "dates_converted";
    private static final String pref_file = "preferences";
    int sayac = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        preferences = getSharedPreferences(pref_file,MODE_PRIVATE);
        izinler();
    }

    @Override
    protected void onStart() {
        if(sayac>0){
            super.onStart();
            izinler();
        }
        else{
            sayac+=1;
            super.onStart();
        }
    }

    private void izinler(){
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1){
            if(ContextCompat.checkSelfPermission(DemoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED
                    ||ContextCompat.checkSelfPermission(DemoActivity.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent(DemoActivity.this, ActivityPermission.class));
                    }
                },1000);
            }
            else{
                veri_kontrol();
            }
        }
        else{
            veri_kontrol();
        }
    }

    private void veri_kontrol(){
       if(getIntent().getExtras()!=null){
           if(getIntent().getExtras().containsKey("key_update")){
               Intent intent=new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.APP_URL)));
               startActivity(intent);
           }
           else{
               tarih_donustur();
           }
        }
       else{
           tarih_donustur();
       }
    }

    private void tarih_donustur(){
        if(!preferences.contains(pref_key)){
            databaseHelper=SQLiteDatabaseHelper.getInstance(this);
            if(databaseHelper.getSize()>0){
                SharedPreferences.Editor editor=preferences.edit();
                editor.putInt(pref_key,1);
                editor.apply();
                final ProgressDialog progressDialog=new ProgressDialog(this);
                progressDialog.setTitle(R.string.dialog_title1);
                progressDialog.setMessage(getString(R.string.dialog_msg1));
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        hayvanVerilerArrayList=databaseHelper.getSimpleData();
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                        int sayac=0;
                        Date date_dollenme = new Date(),date_dogum = new Date();
                        while(sayac<hayvanVerilerArrayList.size()) {
                            try {
                                date_dollenme.setTime(simpleDateFormat.parse(hayvanVerilerArrayList.get(sayac).getTohumlama_tarihi()).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                                date_dollenme.setTime(Long.parseLong(hayvanVerilerArrayList.get(sayac).getTohumlama_tarihi()));
                            }
                            try {
                                date_dogum.setTime(simpleDateFormat.parse(hayvanVerilerArrayList.get(sayac).getDogum_tarihi()).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                                date_dogum.setTime(Long.parseLong(hayvanVerilerArrayList.get(sayac).getDogum_tarihi()));
                            }
                            databaseHelper.tarih_donustur(hayvanVerilerArrayList.get(sayac).getId(),
                                    String.valueOf(date_dollenme.getTime()),
                                    String.valueOf(date_dogum.getTime()));
                            sayac++;
                        }
                        progressDialog.dismiss();
                        startActivity(new Intent(DemoActivity.this, PrimaryActivity.class));
                    }
                },600);
            }
            else{
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent(DemoActivity.this, PrimaryActivity.class));
                    }
                },1000);
            }
        }
        else{
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(DemoActivity.this, PrimaryActivity.class));
                }
            },1000);
        }
    }

}
