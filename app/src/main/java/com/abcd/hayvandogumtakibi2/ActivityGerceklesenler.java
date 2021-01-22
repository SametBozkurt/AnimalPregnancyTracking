package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityGerceklesenler extends AppCompatActivity {

    private SQLiteDatabaseHelper databaseHelper;
    private ArrayList<DataModel> dataModelArrayList;
    private FrameLayout fragment_container;
    private final Context context=this;
    private boolean listModeEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerceklesenler);
        final ImageView cross=findViewById(R.id.iptal), imgListMode=findViewById(R.id.listMode);
        fragment_container=findViewById(R.id.fragment_container);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        databaseHelper=SQLiteDatabaseHelper.getInstance(context);
        dataModelArrayList=databaseHelper.getSimpleData("dogum_grcklsti=1",null);
        if(dataModelArrayList.isEmpty()){
            imgListMode.setVisibility(View.INVISIBLE);
        }
        else{
            listModeEnabled=PreferencesHolder.getIsListedViewEnabled(context);
            if(listModeEnabled){
                imgListMode.setImageResource(R.drawable.ic_view_all);
            }
            else{
                imgListMode.setImageResource(R.drawable.ic_tile);
            }
            imgListMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listModeEnabled){
                        listModeEnabled=false;
                        imgListMode.setImageResource(R.drawable.ic_tile);
                    }
                    else{
                        listModeEnabled=true;
                        imgListMode.setImageResource(R.drawable.ic_view_all);
                    }
                    Log.e("HEEY","this section is running");
                    onStart();
                    PreferencesHolder.setIsListedViewEnabled(context,listModeEnabled);
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataModelArrayList=null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragment_container.removeAllViews();
        dataModelArrayList=databaseHelper.getSimpleData("dogum_grcklsti=1",null);
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