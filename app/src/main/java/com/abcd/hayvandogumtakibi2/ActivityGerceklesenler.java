package com.abcd.hayvandogumtakibi2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.ArrayList;

public class ActivityGerceklesenler extends AppCompatActivity {

    SQLiteDatabaseHelper databaseHelper;
    ArrayList<DataModel> dataModelArrayList;
    FrameLayout fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerceklesenler);
        final ImageView cross=findViewById(R.id.iptal);
        fragment_container=findViewById(R.id.fragment_container);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        dataModelArrayList=databaseHelper.getGerceklesenler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseHelper=null;
        dataModelArrayList=null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragment_container.removeAllViews();
        dataModelArrayList=databaseHelper.getGerceklesenler();
        if(dataModelArrayList.size()==0){
            final FragmentGerceklesenDogumYok fragmentGerceklesenDogumYok=new FragmentGerceklesenDogumYok();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentGerceklesenDogumYok).commitAllowingStateLoss();
        }
        else{
            final FragmentGerceklesenDogumlar fragmentGerceklesenDogumlar=new FragmentGerceklesenDogumlar();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentGerceklesenDogumlar).commitAllowingStateLoss();
        }
    }
}