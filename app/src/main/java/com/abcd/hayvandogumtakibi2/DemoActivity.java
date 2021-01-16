package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class DemoActivity extends AppCompatActivity {

    final Context context=this;
    private static final String THREAD_CLEANER_NAME = "CleanerThread";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        if(PreferencesHolder.getIsCacheCleanerEnabled(context)){
            final Thread copcuThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    GarbageCleaner.clean_caches(context);
                }
            });
            copcuThread.setName(THREAD_CLEANER_NAME);
            copcuThread.start();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(context, PrimaryActivity.class));
            }
        },1000);
    }



}
