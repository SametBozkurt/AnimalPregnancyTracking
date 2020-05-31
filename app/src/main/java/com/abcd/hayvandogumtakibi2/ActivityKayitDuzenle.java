package com.abcd.hayvandogumtakibi2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
                startActivity(new Intent(ActivityKayitDuzenle.this,PrimaryActivity.class));
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        databaseHelper=new SQLiteDatabaseHelper(ActivityKayitDuzenle.this);
        hayvanVerilerArrayList=databaseHelper.getAllData();
        DuzenleAdapter duzenleAdapter =new DuzenleAdapter(ActivityKayitDuzenle.this,hayvanVerilerArrayList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivityKayitDuzenle.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(duzenleAdapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityKayitDuzenle.this,PrimaryActivity.class));
    }
}
