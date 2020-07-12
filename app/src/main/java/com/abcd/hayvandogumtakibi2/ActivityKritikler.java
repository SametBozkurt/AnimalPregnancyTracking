package com.abcd.hayvandogumtakibi2;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityKritikler extends AppCompatActivity {

    private byte sayac=0;
    private SQLiteDatabaseHelper databaseHelper;
    private ArrayList<HayvanVeriler> hayvanVerilerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kritikler);
        final Toolbar toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        hayvanVerilerArrayList=databaseHelper.getKritikOlanlar();
        if(hayvanVerilerArrayList.size()==0){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentYaklasanDogumYok()).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentYaklasanDogumlar()).commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseHelper=null;
        hayvanVerilerArrayList=null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sayac==0){
            sayac+=1;
        }
        else{
            databaseHelper=SQLiteDatabaseHelper.getInstance(this);
            hayvanVerilerArrayList=databaseHelper.getKritikOlanlar();
            if(hayvanVerilerArrayList.size()==0){
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentYaklasanDogumYok()).commit();
            }
            else{
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentYaklasanDogumlar()).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
