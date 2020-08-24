package com.abcd.hayvandogumtakibi2;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
        final Button req_perms=findViewById(R.id.allow);
        final LinearLayout per_storage=findViewById(R.id.permission_storage);
        final LinearLayout per_camera=findViewById(R.id.permission_camera);
        req_perms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                izin_kontrol();
            }
        });
        per_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(ActivityPermission.this,R.style.ImageDialogStyle);
                dialog.setContentView(R.layout.per_info_layout);
                final TextView title=dialog.findViewById(R.id.per_title);
                final TextView body=dialog.findViewById(R.id.per_explanation);
                final ImageView icon=dialog.findViewById(R.id.per_icon);
                final Button button_okay=dialog.findViewById(R.id.btn_ok);
                title.setText(getString(R.string.storage_per_title));
                body.setText(getString(R.string.storage_per_exp));
                icon.setImageResource(R.drawable.ic_storage);
                button_okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        per_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(ActivityPermission.this,R.style.ImageDialogStyle);
                dialog.setContentView(R.layout.per_info_layout);
                final TextView title=dialog.findViewById(R.id.per_title);
                final TextView body=dialog.findViewById(R.id.per_explanation);
                final ImageView icon=dialog.findViewById(R.id.per_icon);
                final Button button_okay=dialog.findViewById(R.id.btn_ok);
                title.setText(getString(R.string.cam_per_title));
                body.setText(getString(R.string.cam_per_exp));
                icon.setImageResource(R.drawable.ic_photo_camera);
                button_okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
