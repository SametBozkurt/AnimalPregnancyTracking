package com.abcd.hayvandogumtakibi2.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abcd.hayvandogumtakibi2.Adapter.DuzenleAdapter;
import com.abcd.hayvandogumtakibi2.Misc.PreferencesHolder;
import com.abcd.hayvandogumtakibi2.R;
import com.abcd.hayvandogumtakibi2.Misc.SQLiteDatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ActivityKayitDuzenle extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private final Context context=this;
    private RecyclerView recyclerView;
    private int selection_code=0, selectedRadioButtonFilter= R.id.radio_button_isim, selectedRadioButtonOrder=R.id.radio_button_first;
    private String selection_gerceklesen_dogumlar=null, table_name= SQLiteDatabaseHelper.SUTUN_1, orderBy=null;
    private boolean switchIsChecked=true, listModeEnabled;
    private BottomSheetDialog bottomSheetDialog;
    private RadioGroup radioGroupFilter,radioGroupOrder;
    private SwitchMaterial switchMaterial;
    private RelativeLayout.LayoutParams mLayoutParams;
    private DuzenleAdapter duzenleAdapter;
    private ProgressBar progressBar;
    private ImageView imgListMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_duzenle);
        imgListMode=findViewById(R.id.listMode);
        relativeLayout=findViewById(R.id.main_layout);
        recyclerView=findViewById(R.id.recyclerView);
        listModeEnabled= PreferencesHolder.getIsListedViewEnabled(context);
        if(listModeEnabled){
            imgListMode.setImageResource(R.drawable.ic_view_all);
        }
        else{
            imgListMode.setImageResource(R.drawable.ic_tile);
        }
        initProgressBarAndTask();
        bottomSheetDialog=new BottomSheetDialog(context,R.style.FilterDialogTheme);
        initFilterMenu();
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
                initProgressBarAndTask();
                PreferencesHolder.setIsListedViewEnabled(context,listModeEnabled);
            }
        });
    }

    private void initProgressBarAndTask(){
        taskPrePostOnUI();
        doAsyncTaskAndPost();
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
                recyclerView.setAlpha(0f);
            }
        });
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
                try {
                    Thread.sleep(400);
                    duzenleAdapter=new DuzenleAdapter(context,selection_code,selection_gerceklesen_dogumlar,orderBy,listModeEnabled);
                    Message message=new Message();
                    message.obj="InitializeUIProcess";
                    asyncHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void taskPostOnUI(){
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                GridLayoutManager gridLayoutManager;
                if(listModeEnabled){
                     gridLayoutManager=new GridLayoutManager(context,2);
                }
                else{
                    gridLayoutManager=new GridLayoutManager(context,3);
                }
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(duzenleAdapter);
                try {
                    Thread.sleep(200);
                    relativeLayout.removeView(progressBar);
                    recyclerView.animate().alpha(1f).setDuration(200).start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initFilterMenu(){
        final View view= LayoutInflater.from(context).inflate(R.layout.layout_filter_and_sort,(RelativeLayout)findViewById(R.id.parent_layout));
        bottomSheetDialog.setContentView(view);
        final Button buttonApply=view.findViewById(R.id.btn_apply), buttonReset=view.findViewById(R.id.btn_reset);
        radioGroupFilter=view.findViewById(R.id.radio_group_filter);
        radioGroupOrder=view.findViewById(R.id.radio_group_order);
        switchMaterial=view.findViewById(R.id.switch_show_happeneds);
        radioGroupFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_button_isim){
                    selection_code=0;
                    table_name=SQLiteDatabaseHelper.SUTUN_1;
                }
                else if(checkedId==R.id.radio_button_kupe_no){
                    selection_code=1;
                    table_name=SQLiteDatabaseHelper.SUTUN_3;
                }
                else if(checkedId==R.id.radio_button_date1){
                    selection_code=2;
                    table_name=SQLiteDatabaseHelper.SUTUN_4;
                }
                else if(checkedId==R.id.radio_button_date2){
                    selection_code=3;
                    table_name=SQLiteDatabaseHelper.SUTUN_5;
                }
                selectedRadioButtonFilter=checkedId;
            }
        });
        radioGroupOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                orderBy=null;
                if(checkedId==R.id.radio_button_first){
                    orderBy="id ASC";
                }
                else if(checkedId==R.id.radio_button_last){
                    orderBy="id DESC";
                }
                else if(checkedId==R.id.radio_button_AtoZ){
                    orderBy=table_name+" ASC";
                }
                else if(checkedId==R.id.radio_button_ZtoA){
                    orderBy=table_name+" DESC";
                }
                selectedRadioButtonOrder=checkedId;
            }
        });
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selection_gerceklesen_dogumlar=null;
                }
                else{
                    selection_gerceklesen_dogumlar="dogum_grcklsti=0";
                }
                switchIsChecked=isChecked;
            }
        });
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initProgressBarAndTask();
                bottomSheetDialog.dismiss();
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRadioButtonOrder=R.id.radio_button_first;
                selectedRadioButtonFilter=R.id.radio_button_isim;
                selection_code=0;
                switchIsChecked=true;
                table_name=SQLiteDatabaseHelper.SUTUN_1;
                selection_gerceklesen_dogumlar=null;
                orderBy=null;
                initProgressBarAndTask();
                bottomSheetDialog.dismiss();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void viewClick(View view){
        switch(view.getId()){
            case R.id.iptal:
                onBackPressed();
                break;
            case R.id.btn_filter:
                if(bottomSheetDialog!=null){
                    radioGroupFilter.check(selectedRadioButtonFilter);
                    radioGroupOrder.check(selectedRadioButtonOrder);
                    switchMaterial.setChecked(switchIsChecked);
                    bottomSheetDialog.show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
