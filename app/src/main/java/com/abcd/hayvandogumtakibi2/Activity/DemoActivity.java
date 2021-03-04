package com.abcd.hayvandogumtakibi2.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.abcd.hayvandogumtakibi2.Misc.GarbageCleaner;
import com.abcd.hayvandogumtakibi2.Misc.PreferencesHolder;
import com.abcd.hayvandogumtakibi2.R;

import java.util.Timer;
import java.util.TimerTask;

public class DemoActivity extends AppCompatActivity {

    private final Context context=this;
    private static final String THREAD_CLEANER_NAME = "CleanerThread";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Thread copcuThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(PreferencesHolder.getIsCacheCleanerEnabled(context)){
                    GarbageCleaner.clean_caches(context);
                }
                GarbageCleaner.clean_redundants(context);
            }
        });
        copcuThread.setName(THREAD_CLEANER_NAME);
        copcuThread.start();
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
