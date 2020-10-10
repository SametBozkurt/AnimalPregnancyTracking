package com.abcd.hayvandogumtakibi2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class ActivityDevTools extends AppCompatActivity {

    private int sayac=10;
    private static final long one_day_in_millis = 1000*60*60*24;
    final long today_in_millis=System.currentTimeMillis();
    private ProgressBar mProgressBar;
    private FrameLayout progress_container;
    final Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_tools);
        final Button btn=findViewById(R.id.kayit_olustur);
        final Button btn_del=findViewById(R.id.kayit_sil);
        final Button btn_clean = findViewById(R.id.copu_bosalt);
        final SeekBar seekBar=findViewById(R.id.seekbar);
        progress_container=findViewById(R.id.progress_container);
        btn.setText(new StringBuilder(getString(R.string.button_multiple_creation)).append(": ").append(sayac));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar=new ProgressBar(context);
                progress_container.addView(mProgressBar,FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
                new TaskCokluKayit().execute();
            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar=new ProgressBar(context);
                progress_container.addView(mProgressBar,FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
                new TaskTumKayitlariSil().execute();
            }
        });
        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final File f = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                if(f!=null){
                    mProgressBar=new ProgressBar(context);
                    progress_container.addView(mProgressBar,FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
                    new TaskCopuBosalt().execute();
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    class TaskCokluKayit extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            final SQLiteDatabaseHelper sqLiteDatabaseHelper=SQLiteDatabaseHelper.getInstance(context);
            int id=0;
            for(int i=0;i<sayac;i++){
                final String isim="test"+ i;
                final String tur=String.valueOf(new Random().nextInt(4));
                final String kupe_no="0000"+ i;
                final String tarih1=String.valueOf(today_in_millis-(one_day_in_millis*(27+i)));
                final String tarih2=String.valueOf(today_in_millis+(one_day_in_millis*(27+i)));
                final int petCode=new Random().nextInt(2)+1;
                sqLiteDatabaseHelper.veri_yaz(new DataModel(id,isim,tur,kupe_no,tarih1,tarih2,null,petCode,0,""));
            }
            mProgressBar.setVisibility(View.GONE);
            progress_container.removeView(mProgressBar);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class TaskTumKayitlariSil extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            final SQLiteDatabaseHelper sqLiteDatabaseHelper=SQLiteDatabaseHelper.getInstance(context);
            final ArrayList<DataModel> dataModelArrayList=sqLiteDatabaseHelper.getSimpleData();
            if(dataModelArrayList.size()>0){
                for(int index=0;index<dataModelArrayList.size();index++){
                    final int id=dataModelArrayList.get(index).getId();
                    sqLiteDatabaseHelper.girdiSil(id);
                }
            }
            mProgressBar.setVisibility(View.GONE);
            progress_container.removeView(mProgressBar);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class TaskCopuBosalt extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            final SQLiteDatabaseHelper sqLiteDatabaseHelper=SQLiteDatabaseHelper.getInstance(context);
            final ArrayList<DataModel> dataModelArrayList=sqLiteDatabaseHelper.getSimpleData();
            final File dizin=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
            final File[] fileList = dizin.listFiles();
            final ArrayList<String> files_in_db=new ArrayList<>();
            for(int x=0;x<dataModelArrayList.size();x++){
                files_in_db.add(dataModelArrayList.get(x).getFotograf_isim());
            }
            for(int y=0;y<fileList.length;y++){
                if(!files_in_db.contains(fileList[y].getName())){
                    new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),fileList[y].getName()).delete();
                }
            }
            mProgressBar.setVisibility(View.GONE);
            progress_container.removeView(mProgressBar);
        }
    }

}