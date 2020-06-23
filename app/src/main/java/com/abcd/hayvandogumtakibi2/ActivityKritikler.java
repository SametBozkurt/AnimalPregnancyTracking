package com.abcd.hayvandogumtakibi2;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityKritikler extends AppCompatActivity {

    SQLiteDatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper=new SQLiteDatabaseHelper(ActivityKritikler.this);
        dosya_kontrol();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void dosya_kontrol(){
        ArrayList<HayvanVeriler> hayvanVerilerArrayList=databaseHelper.getKritikOlanlar();
        if(hayvanVerilerArrayList.size()==0){
            setContentView(R.layout.yaklasan_dogum_yok);
        }
        else{
            setContentView(R.layout.activity_kritikler);
            recyclerView=findViewById(R.id.recyclerView);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(ActivityKritikler.this,3);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(new KritiklerAdapter(ActivityKritikler.this,hayvanVerilerArrayList));
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
