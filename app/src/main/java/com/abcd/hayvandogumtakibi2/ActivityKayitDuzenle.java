package com.abcd.hayvandogumtakibi2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityKayitDuzenle extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteDatabaseHelper databaseHelper;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_duzenle);
        toolbar=findViewById(R.id.activity_toolbar);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        final ProgressDialog[] dialog = {new ProgressDialog(this)};
        dialog[0].setTitle(R.string.dialog_title1);
        dialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog[0].setCancelable(false);
        dialog[0].show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        hayvanVerilerArrayList=databaseHelper.getAllData();
                        DuzenleAdapter duzenleAdapter =new DuzenleAdapter(ActivityKayitDuzenle.this,hayvanVerilerArrayList);
                        recyclerView.setAdapter(duzenleAdapter);
                        dialog[0].dismiss();
                        dialog[0] =null;
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        final ProgressDialog[] dialog = {new ProgressDialog(this)};
        dialog[0].setTitle(R.string.dialog_title1);
        dialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog[0].setCancelable(false);
        dialog[0].show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        hayvanVerilerArrayList=databaseHelper.getAllData();
                        final DuzenleAdapter duzenleAdapter =new DuzenleAdapter(ActivityKayitDuzenle.this,hayvanVerilerArrayList);
                        recyclerView.setAdapter(duzenleAdapter);
                        dialog[0].dismiss();
                        dialog[0] =null;
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hayvanVerilerArrayList=null;
        recyclerView.setAdapter(null);
    }
}
