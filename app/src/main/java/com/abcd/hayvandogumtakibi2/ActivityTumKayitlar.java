package com.abcd.hayvandogumtakibi2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityTumKayitlar extends AppCompatActivity {

    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    ProgressBar mProgressBar;
    RadioGroup myRadioGroup;
    final Context context=this;
    int selection_code=0;
    TaskKayitlariYukle taskKayitlariYukle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tum_kayitlar);
        final ImageView cross=findViewById(R.id.iptal);
        recyclerView=findViewById(R.id.recyclerView);
        myRadioGroup=findViewById(R.id.radio_group);
        final GridLayoutManager layoutManager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(layoutManager);
        relativeLayout=findViewById(R.id.parent);
        taskKayitlariYukle=new TaskKayitlariYukle();
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initProgressBarAndTask();
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(taskKayitlariYukle.getStatus()==AsyncTask.Status.RUNNING){
                    relativeLayout.removeView(mProgressBar);
                    taskKayitlariYukle.cancel(true);
                    taskKayitlariYukle=null;
                }
                if(checkedId==R.id.radio_button_isim){
                    taskKayitlariYukle=new TaskKayitlariYukle();
                    selection_code=0;
                    initProgressBarAndTask();
                }
                else if(checkedId==R.id.radio_button_id){
                    taskKayitlariYukle=new TaskKayitlariYukle();
                    selection_code=1;
                    initProgressBarAndTask();
                }
                else if(checkedId==R.id.radio_button_date1){
                    taskKayitlariYukle=new TaskKayitlariYukle();
                    selection_code=2;
                    initProgressBarAndTask();
                }
                else if(checkedId==R.id.radio_button_date2){
                    taskKayitlariYukle=new TaskKayitlariYukle();
                    selection_code=3;
                    initProgressBarAndTask();
                }
            }
        });
    }

    void initProgressBarAndTask(){
        mProgressBar=new ProgressBar(this);
        mProgressBar.setId(R.id.progress1);
        mProgressBar.setIndeterminate(true);
        final RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setLayoutParams(mLayoutParams);
        relativeLayout.addView(mProgressBar);
        taskKayitlariYukle.execute();
    }

    @SuppressLint("StaticFieldLeak")
    class TaskKayitlariYukle extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerView.animate().alpha(0f).setDuration(200).start();
            recyclerView.setAdapter(null);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(context);
            final ArrayList<DataModel> dataModelArrayList=databaseHelper.getSimpleData();
            final KayitlarAdapter kayitlarAdapter=new KayitlarAdapter(context,dataModelArrayList,selection_code);
            recyclerView.setAdapter(kayitlarAdapter);
            relativeLayout.removeView(mProgressBar);
            recyclerView.animate().alpha(1f).setDuration(200).start();
            mProgressBar=null;
        }
    }

}