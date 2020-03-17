package com.abcd.hayvandogumtakibi2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

public class DemoActivity extends AppCompatActivity {

    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        timerTask=new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(DemoActivity.this, PrimaryActivity.class));
            }
        };
        timer=new Timer();
        timer.schedule(timerTask,1000);
    }

    @Override
    protected void onResume() {
        Timer timer1=new Timer();
        TimerTask timerTask1=new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(DemoActivity.this,PrimaryActivity.class));
            }
        };
        timer1.schedule(timerTask1,1000);
        super.onResume();
    }

}
