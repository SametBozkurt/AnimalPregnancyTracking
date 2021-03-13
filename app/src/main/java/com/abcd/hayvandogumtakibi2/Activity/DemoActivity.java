package com.abcd.hayvandogumtakibi2.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.abcd.hayvandogumtakibi2.Misc.GarbageCleaner;
import com.abcd.hayvandogumtakibi2.Misc.HayvanDuzenleyici;
import com.abcd.hayvandogumtakibi2.Misc.PreferencesHolder;
import com.abcd.hayvandogumtakibi2.R;

import java.util.Timer;
import java.util.TimerTask;

public class DemoActivity extends AppCompatActivity {

    private static final String THREAD_CLEANER_NAME = "CleanerThread";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Thread copcuThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(PreferencesHolder.getIsCacheCleanerEnabled(DemoActivity.this)){
                    GarbageCleaner.clean_caches(DemoActivity.this);
                }
                GarbageCleaner.clean_redundants(DemoActivity.this);
            }
        });
        copcuThread.setName(THREAD_CLEANER_NAME);
        copcuThread.start();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                HayvanDuzenleyici.load(DemoActivity.this);
            }
        });
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(DemoActivity.this, PrimaryActivity.class));
            }
        },1000);
    }

}
