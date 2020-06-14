package com.abcd.hayvandogumtakibi2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;

public class ActivityGerceklesenler extends AppCompatActivity {

    SQLiteDatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper=new SQLiteDatabaseHelper(this);
        hayvanVerilerArrayList=databaseHelper.getGerceklesenler();
        if(hayvanVerilerArrayList.size()==0){
            setContentView(R.layout.gerceklesen_dogum_yok);
        }
        else{
            setContentView(R.layout.activity_gerceklesenler);
            recyclerView=findViewById(R.id.recyclerView);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(ActivityGerceklesenler.this,3);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(new KayitlarAdapter(ActivityGerceklesenler.this,hayvanVerilerArrayList,0));
        }
        toolbar=findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}