package com.abcd.hayvandogumtakibi2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.Timer;
import java.util.TimerTask;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        izinler();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        izinler();
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
               final Intent intent=new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.APP_URL)));
               startActivity(intent);
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
