package com.abcd.hayvandogumtakibi2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityPermission extends AppCompatActivity {

    private static final int PERMISSION_REQ_CODE = 21323;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            startActivity(new Intent(ActivityPermission.this, PrimaryActivity.class));
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
            startActivity(new Intent(ActivityPermission.this, PrimaryActivity.class));
            //İzinler tamamdır
        }
    }

}
