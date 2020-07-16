package com.abcd.hayvandogumtakibi2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;

public class ActivityGerceklesenler extends AppCompatActivity {

    SQLiteDatabaseHelper databaseHelper;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerceklesenler);
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        hayvanVerilerArrayList=databaseHelper.getGerceklesenler();
        final Toolbar toolbar=findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(hayvanVerilerArrayList.size()==0){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentGerceklesenDogumYok()).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentGerceklesenDogumlar()).commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseHelper=null;
        hayvanVerilerArrayList=null;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        hayvanVerilerArrayList=databaseHelper.getGerceklesenler();
        if(hayvanVerilerArrayList.size()==0){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentGerceklesenDogumYok()).commitAllowingStateLoss();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentGerceklesenDogumlar()).commitAllowingStateLoss();
        }
    }
}