package com.abcd.hayvandogumtakibi2;

import android.os.Bundle;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityKritikler extends AppCompatActivity {

    SQLiteDatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper=new SQLiteDatabaseHelper(ActivityKritikler.this);
        dosya_kontrol(databaseHelper);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void dosya_kontrol(SQLiteDatabaseHelper sqLiteDatabaseHelper){
        hayvanVerilerArrayList=sqLiteDatabaseHelper.getKritikOlanlar();
        if(hayvanVerilerArrayList.size()==0){
            setContentView(R.layout.yaklasan_dogum_yok);
        }
        else{
            setContentView(R.layout.activity_kritikler);
            recyclerView=findViewById(R.id.recyclerView);
            KritiklerAdapter kritiklerAdapter =new KritiklerAdapter(ActivityKritikler.this,hayvanVerilerArrayList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivityKritikler.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(kritiklerAdapter);
        }
    }
}
