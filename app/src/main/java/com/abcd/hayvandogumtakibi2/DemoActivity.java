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

    short sayac = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
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
                Timer timer=new Timer();
                TimerTask task=new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent(DemoActivity.this,ActivityPermission.class));
                    }
                };
                timer.schedule(task,1500);
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
               TimerTask timerTask=new TimerTask() {
                   @Override
                   public void run() {
                       startActivity(new Intent(DemoActivity.this, PrimaryActivity.class));
                   }
               };
               Timer timer=new Timer();
               timer.schedule(timerTask,1500);
           }
        }
       else{
           TimerTask timerTask=new TimerTask() {
               @Override
               public void run() {
                   startActivity(new Intent(DemoActivity.this, PrimaryActivity.class));
               }
           };
           Timer timer=new Timer();
           timer.schedule(timerTask,1500);
       }
    }

}
