package com.abcd.hayvandogumtakibi2.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abcd.hayvandogumtakibi2.Adapter.AramalarAdapter;
import com.abcd.hayvandogumtakibi2.Misc.DataModel;
import com.abcd.hayvandogumtakibi2.R;
import com.abcd.hayvandogumtakibi2.Misc.SQLiteDatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ActivityKayitAra extends AppCompatActivity {

    private final Context context=this;
    private RelativeLayout relativeLayout;
    private FrameLayout sonuc_container;
    private String search_in= SQLiteDatabaseHelper.SUTUN_1, orderBy=search_in+" ASC", toBeSearched;
    private final int RBIsimlerId= R.id.radio_button_isim, RBKupeNoId=R.id.radio_button_id, RBAtoZ=R.id.radio_button_AtoZ, RBZtoA=R.id.radio_button_ZtoA;
    private ProgressBar progressBar;
    private RelativeLayout.LayoutParams mLayoutParams;
    private View sonuclar_view;
    private AramalarAdapter aramalarAdapter;
    private LayoutInflater layoutInflater;
    private SQLiteDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ara);
        TextInputEditText inputAranacak=findViewById(R.id.bul);
        ImageView cross=findViewById(R.id.iptal);
        RadioGroup radioGroup_SearchIn = findViewById(R.id.radio_group);
        RadioGroup radioGroup_OrderBy = findViewById(R.id.radio_group_order);
        sonuc_container=findViewById(R.id.layout_sonuclar);
        relativeLayout=findViewById(R.id.main_layout);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        inputAranacak.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sonuc_container.removeAllViews();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sonuc_container.removeAllViews();
                toBeSearched=charSequence.toString();
                if(!charSequence.toString().isEmpty()){
                    sonuc_container.removeAllViews();
                    initProgressBarAndTask(charSequence.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        radioGroup_SearchIn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==RBIsimlerId){
                    search_in=SQLiteDatabaseHelper.SUTUN_1;
                }
                else if(checkedId==RBKupeNoId){
                    search_in=SQLiteDatabaseHelper.SUTUN_3;
                }
                if(toBeSearched!=null&&!toBeSearched.isEmpty()){
                    Log.e("LOGGER","abcd");
                    sonuc_container.removeAllViews();
                    initProgressBarAndTask(toBeSearched);
                }
            }
        });
        radioGroup_OrderBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==RBAtoZ){
                    orderBy=search_in+" ASC";
                }
                else if(checkedId==RBZtoA){
                    orderBy=search_in+" DESC";
                }
                if(toBeSearched!=null&&!toBeSearched.isEmpty()){
                    Log.e("LOGGER","abcd");
                    sonuc_container.removeAllViews();
                    initProgressBarAndTask(toBeSearched);
                }
            }
        });
    }

    private void initProgressBarAndTask(String toBeSearched){
        taskPrePostOnUI();
        doAsyncTaskAndPost(toBeSearched);
    }

    private void taskPrePostOnUI(){
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar=new ProgressBar(context);
                progressBar.setIndeterminate(true);
                if(mLayoutParams==null){
                    mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                }
                progressBar.setLayoutParams(mLayoutParams);
                relativeLayout.addView(progressBar);
            }
        });
    }

    private void doAsyncTaskAndPost(final String aranacak){
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
                if(layoutInflater==null){
                    layoutInflater=LayoutInflater.from(context);
                }
                if(databaseHelper==null){
                    databaseHelper=SQLiteDatabaseHelper.getInstance(context);
                }
                ArrayList<DataModel> dataModelArrayList=databaseHelper.getAramaSonuclari(search_in,aranacak,orderBy);
                if(dataModelArrayList.isEmpty()){
                    sonuclar_view=layoutInflater.inflate(R.layout.arama_snclari_sonuc_yok_lyt, sonuc_container, false);
                }
                else{
                    sonuclar_view=layoutInflater.inflate(R.layout.arama_snclari_lyt, sonuc_container, false);
                    RecyclerView recyclerView=sonuclar_view.findViewById(R.id.recyclerView);
                    aramalarAdapter=new AramalarAdapter(context,dataModelArrayList,search_in,aranacak);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(aramalarAdapter);
                }
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
                relativeLayout.removeView(progressBar);
                sonuc_container.addView(sonuclar_view);
            }
        });
    }

}
