package com.abcd.hayvandogumtakibi2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityPermission extends AppCompatActivity {

    private static final int PERMISSION_REQ_CODE = 21323;
    SQLiteDatabaseHelper databaseHelper;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    SharedPreferences preferences;
    private static final String pref_key = "dates_converted";
    private static final String pref_file = "preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(pref_file,MODE_PRIVATE);
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        izin_kontrol();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==
                PackageManager.PERMISSION_GRANTED){
            tarih_donustur();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void init(){
        setContentView(R.layout.activity_permission);
        Button req_perms=findViewById(R.id.allow);
        req_perms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                izin_kontrol();
            }
        });
    }

    private void izin_kontrol(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            //Kamera ve depolama izinlerini ister.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, PERMISSION_REQ_CODE);
        }
        else if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED && ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED){
            //Sadece depolama izni ister.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        }
        else if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==
                PackageManager.PERMISSION_DENIED){
            //Sadece kamera izni ister.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, PERMISSION_REQ_CODE);
        }
        else{
            tarih_donustur();
            //İzinler tamamdır
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
                        startActivity(new Intent(ActivityPermission.this, PrimaryActivity.class));
                    }
                },600);
            }
            else{
                startActivity(new Intent(ActivityPermission.this, PrimaryActivity.class));
            }
        }
        else{
            startActivity(new Intent(ActivityPermission.this, PrimaryActivity.class));
        }
    }

}
