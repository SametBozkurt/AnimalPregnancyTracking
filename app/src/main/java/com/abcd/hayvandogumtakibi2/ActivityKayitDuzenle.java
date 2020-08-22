package com.abcd.hayvandogumtakibi2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class ActivityKayitDuzenle extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteDatabaseHelper databaseHelper;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    Toolbar toolbar;
    RelativeLayout relativeLayout;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_duzenle);
        toolbar=findViewById(R.id.activity_toolbar);
        relativeLayout=findViewById(R.id.main_layout);
        this.setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        mProgressBar=new ProgressBar(this);
        final RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setLayoutParams(mLayoutParams);
        mProgressBar.setIndeterminate(true);
        relativeLayout.addView(mProgressBar);
        new Task1().execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        relativeLayout.addView(mProgressBar);
        new Task1().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hayvanVerilerArrayList=null;
        recyclerView.setAdapter(null);
    }

    @SuppressLint("StaticFieldLeak")
    class Task1 extends AsyncTask<String, Integer, Boolean> {

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
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            relativeLayout.removeView(mProgressBar);
            recyclerView.animate().alpha(1f).setDuration(200).start();
            hayvanVerilerArrayList=databaseHelper.getAllData();
            final DuzenleAdapter duzenleAdapter =new DuzenleAdapter(ActivityKayitDuzenle.this,hayvanVerilerArrayList);
            recyclerView.setAdapter(duzenleAdapter);
        }
    }

}
