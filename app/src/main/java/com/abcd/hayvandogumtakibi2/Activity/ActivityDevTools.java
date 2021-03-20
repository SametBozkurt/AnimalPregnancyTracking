package com.abcd.hayvandogumtakibi2.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abcd.hayvandogumtakibi2.Misc.ActivityInteractor;
import com.abcd.hayvandogumtakibi2.Misc.DataModel;
import com.abcd.hayvandogumtakibi2.Misc.PreferencesHolder;
import com.abcd.hayvandogumtakibi2.Misc.SQLiteDatabaseHelper;
import com.abcd.hayvandogumtakibi2.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class ActivityDevTools extends AppCompatActivity {

    private int sayac=10,saat=0;
    private static final long one_day_in_millis = 1000*60*60*24;
    final long today_in_millis=System.currentTimeMillis();
    private FrameLayout progress_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_tools);
        final Button btn=findViewById(R.id.kayit_olustur);
        final Button btn_del=findViewById(R.id.kayit_sil);
        final Button btn_clean = findViewById(R.id.copu_bosalt);
        final SeekBar seekBar=findViewById(R.id.seekbar);
        final SeekBar seekBar1=findViewById(R.id.seekbar_alarm_hour);
        final Button btnSetAlarmHour=findViewById(R.id.setAlarmHour);
        progress_container=findViewById(R.id.progress_container);
        btn.setText(new StringBuilder(getString(R.string.button_multiple_creation)).append(": ").append(sayac));
        btnSetAlarmHour.setText(new StringBuilder(getString(R.string.button_alarm_hour)).append(": ").append(saat));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar mProgressBar=new ProgressBar(ActivityDevTools.this);
                progress_container.addView(mProgressBar,FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
                runTaskCokluKayit(mProgressBar);
            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar mProgressBar=new ProgressBar(ActivityDevTools.this);
                progress_container.addView(mProgressBar,FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
                runTaskTumKayitlariSil(mProgressBar);
            }
        });
        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final File f = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                if(f!=null){
                    ProgressBar mProgressBar=new ProgressBar(ActivityDevTools.this);
                    progress_container.addView(mProgressBar,FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
                    runTaskCopuBosalt(mProgressBar);
                }
                else{
                    Snackbar.make(findViewById(R.id.parent),"Cöp boş.",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sayac = progress;
                btn.setText(new StringBuilder(getString(R.string.button_multiple_creation)).append(": ").append(sayac));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                saat = progress;
                btnSetAlarmHour.setText(new StringBuilder(getString(R.string.button_alarm_hour)).append(": ").append(saat));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        btnSetAlarmHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesHolder.setAlarmHour(ActivityDevTools.this,saat);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityInteractor.getInstance().notifyPrimaryActivity(null);
        finish();
    }

    private void runTaskCokluKayit(final ProgressBar progressBar){
        HandlerThread handlerThread=new HandlerThread("AsyncTasks");
        handlerThread.start();
        final Handler asyncHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Handler handler=new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        progress_container.removeView(progressBar);
                    }
                });
            }
        };
        asyncHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                    final SQLiteDatabaseHelper sqLiteDatabaseHelper=SQLiteDatabaseHelper.getInstance(ActivityDevTools.this);
                    int id=0;
                    for(int i=0;i<sayac;i++){
                        final String isim="test"+ i;
                        final String tur=String.valueOf(new Random().nextInt(4));
                        final String kupe_no="0000"+ i;
                        final String tarih1=String.valueOf(today_in_millis-(one_day_in_millis*(27+i)));
                        final String tarih2=String.valueOf(today_in_millis+(one_day_in_millis*(27+i)));
                        final int petCode=new Random().nextInt(2)+1;
                        sqLiteDatabaseHelper.kayit_ekle(new DataModel(id,isim,tur,kupe_no,tarih1,tarih2,null,petCode,0,""));
                    }
                    Message message=new Message();
                    message.obj="InitializeUIProcess";
                    asyncHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void runTaskTumKayitlariSil(final ProgressBar progressBar){
        HandlerThread handlerThread=new HandlerThread("AsyncTasks");
        handlerThread.start();
        final Handler asyncHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Handler handler=new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        progress_container.removeView(progressBar);
                    }
                });
            }
        };
        asyncHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                    final SQLiteDatabaseHelper sqLiteDatabaseHelper=SQLiteDatabaseHelper.getInstance(ActivityDevTools.this);
                    final ArrayList<DataModel> dataModelArrayList=sqLiteDatabaseHelper.getAllData(null,null);
                    if(dataModelArrayList.size()>0){
                        for(int index=0;index<dataModelArrayList.size();index++){
                            final int id=dataModelArrayList.get(index).getId();
                            sqLiteDatabaseHelper.girdiSil(id);
                        }
                    }
                    Message message=new Message();
                    message.obj="InitializeUIProcess";
                    asyncHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void runTaskCopuBosalt(final ProgressBar progressBar){
        HandlerThread handlerThread=new HandlerThread("AsyncTasks");
        handlerThread.start();
        final Handler asyncHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Handler handler=new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        progress_container.removeView(progressBar);
                    }
                });
            }
        };
        asyncHandler.post(new Runnable() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                    final SQLiteDatabaseHelper sqLiteDatabaseHelper=SQLiteDatabaseHelper.getInstance(ActivityDevTools.this);
                    final ArrayList<DataModel> dataModelArrayList=sqLiteDatabaseHelper.getAllData(null,null);
                    final File dizin=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
                    final File[] fileList = dizin.listFiles();
                    final ArrayList<String> files_in_db=new ArrayList<>();
                    for(int x=0;x<dataModelArrayList.size();x++){
                        files_in_db.add(dataModelArrayList.get(x).getFotograf_isim());
                    }
                    for (File file : fileList) {
                        if (!files_in_db.contains(file.getName())) {
                            new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), file.getName()).delete();
                        }
                    }
                    Message message=new Message();
                    message.obj="InitializeUIProcess";
                    asyncHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}