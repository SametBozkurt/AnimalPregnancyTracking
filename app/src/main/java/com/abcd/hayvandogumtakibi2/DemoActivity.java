package com.abcd.hayvandogumtakibi2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

public class DemoActivity extends AppCompatActivity {

    Timer timer;
    TimerTask timerTask;
    private static final String APP_URL = "https://play.google.com/store/apps/details?id=com.sariyazilim.hayvangebeliktakibi";
    //private static final String NOTIFICATION_CHANNEL_ID="GCM Service";
    //private static final String NOTIFICATION_CHANNEL_NAME="CloudMessages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        veri_kontrol();
    }

    private void veri_kontrol(){
       if(getIntent().getExtras()!=null){
           if(getIntent().getExtras().containsKey("key_update")){
               Intent intent=new Intent(Intent.ACTION_VIEW).setData(Uri.parse(APP_URL));
               startActivity(intent);
           }
           else{
               timerTask=new TimerTask() {
                   @Override
                   public void run() {
                       startActivity(new Intent(DemoActivity.this, PrimaryActivity.class));
                   }
               };
               timer=new Timer();
               timer.schedule(timerTask,1000);
           }
        }
       else{
           timerTask=new TimerTask() {
               @Override
               public void run() {
                   startActivity(new Intent(DemoActivity.this, PrimaryActivity.class));
               }
           };
           timer=new Timer();
           timer.schedule(timerTask,1000);
       }
    }

}
