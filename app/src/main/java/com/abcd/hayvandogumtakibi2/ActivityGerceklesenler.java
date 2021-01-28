package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityGerceklesenler extends AppCompatActivity {

    private SQLiteDatabaseHelper databaseHelper;
    private ArrayList<DataModel> dataModelArrayList;
    private FrameLayout fragment_container;
    private final Context context=this;
    private boolean listModeEnabled;
    private ImageView imgListMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerceklesenler);
        ImageView cross=findViewById(R.id.iptal);
        imgListMode=findViewById(R.id.listMode);
        fragment_container=findViewById(R.id.fragment_container);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initTask();
    }

    private void initTask(){
        doAsyncTaskAndPost();
    }

    private void doAsyncTaskAndPost(){
        HandlerThread handlerThread=new HandlerThread("AsyncTasks");
        handlerThread.start();
        final Handler asyncHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                taskPostOnUI();
            }
        };
        asyncHandler.post(new Runnable() {
            @Override
            public void run() {
                databaseHelper=SQLiteDatabaseHelper.getInstance(context);
                dataModelArrayList=databaseHelper.getSimpleData("dogum_grcklsti=1",null);
                Message message=new Message();
                message.obj="InitializeUIProcess";
                asyncHandler.sendMessage(message);
            }
        });
    }

    private void taskPostOnUI(){
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
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
                            onStart();
                            PreferencesHolder.setIsListedViewEnabled(context,listModeEnabled);
                        }
                    });
                }
            }
        });
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
        HandlerThread handlerThread=new HandlerThread("FragmentThread");
        handlerThread.start();
        final Handler asyncHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Handler uiHandler = new Handler(getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(dataModelArrayList.isEmpty()){
                            final FragmentGerceklesenDogumYok fragmentGerceklesenDogumYok=new FragmentGerceklesenDogumYok();
                            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentGerceklesenDogumYok).commitAllowingStateLoss();
                        }
                        else{
                            final FragmentGerceklesenDogumlar fragmentGerceklesenDogumlar=new FragmentGerceklesenDogumlar();
                            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentGerceklesenDogumlar).commitAllowingStateLoss();
                        }
                    }
                });
            }
        };
        asyncHandler.post(new Runnable() {
            @Override
            public void run() {
                dataModelArrayList=databaseHelper.getSimpleData("dogum_grcklsti=1",null);
                Message message=new Message();
                message.obj="InitializeUIProcess";
                asyncHandler.sendMessage(message);
            }
        });
    }
}