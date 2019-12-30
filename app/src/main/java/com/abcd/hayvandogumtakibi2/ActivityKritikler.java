package com.abcd.hayvandogumtakibi2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

public class ActivityKritikler extends AppCompatActivity {

    SQLiteDatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    RelativeLayout activity_layout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kritikler);
        toolbar=findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        activity_layout=findViewById(R.id.parent_layout);
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
            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            TextView textView_uyari=new TextView(ActivityKritikler.this);
            textView_uyari.setText(getString(R.string.kayit_yok_uyari_kritikler));
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,textView_uyari.getId());
            textView_uyari.setGravity(Gravity.CENTER);
            textView_uyari.setTextAppearance(ActivityKritikler.this,R.style.STYLE_WARNING_TEXT);
            textView_uyari.setPadding(5,0,5,0);
            activity_layout.addView(textView_uyari,layoutParams);
        }
        else{
            recyclerView=findViewById(R.id.recyclerView);
            KritiklerAdapter kritiklerAdapter =new KritiklerAdapter(ActivityKritikler.this,hayvanVerilerArrayList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivityKritikler.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(kritiklerAdapter);
        }
    }
}
