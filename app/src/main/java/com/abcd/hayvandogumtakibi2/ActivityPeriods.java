package com.abcd.hayvandogumtakibi2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

public class ActivityPeriods extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    byte sayac=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periods);
        toolbar=findViewById(R.id.toolbar);
        ActivityPeriods.this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new PeriodsAdapter(this));
    }

    @Override
    protected void onResume() {
        if(sayac!=0){
            recyclerView.setAdapter(new PeriodsAdapter(this));
        }
        else{
            sayac+=1;
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        recyclerView.setAdapter(null);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
