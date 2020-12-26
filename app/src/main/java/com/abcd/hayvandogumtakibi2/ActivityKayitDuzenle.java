package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ActivityKayitDuzenle extends AppCompatActivity {

    RelativeLayout relativeLayout;
    final Context context=this;
    RecyclerView recyclerView;
    int selection_code=0, selectedRadioButtonFilter=R.id.radio_button_isim, selectedRadioButtonOrder=R.id.radio_button_first;
    String selection_gerceklesen_dogumlar=null, table_name=SQLiteDatabaseHelper.SUTUN_1, orderBy=null;
    boolean switchIsChecked=true;
    BottomSheetDialog bottomSheetDialog;
    RadioGroup radioGroupFilter,radioGroupOrder;
    SwitchMaterial switchMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_duzenle);
        relativeLayout=findViewById(R.id.main_layout);
        final ImageView cross=findViewById(R.id.iptal);
        final Button btn_filter=findViewById(R.id.btn_filter);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetDialog!=null){
                    radioGroupFilter.check(selectedRadioButtonFilter);
                    radioGroupOrder.check(selectedRadioButtonOrder);
                    switchMaterial.setChecked(switchIsChecked);
                    bottomSheetDialog.show();
                }
            }
        });
        initProgressBarAndTask();
        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                bottomSheetDialog=new BottomSheetDialog(context,R.style.FilterDialogTheme);
                initFilterMenu();
            }
        });
    }

    void initProgressBarAndTask(){
        recyclerView=findViewById(R.id.recyclerView);
        final GridLayoutManager layoutManager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(layoutManager);
        final ProgressBar progressBar=new ProgressBar(this);
        progressBar.setIndeterminate(true);
        final RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(mLayoutParams);
        relativeLayout.addView(progressBar);
        recyclerView.animate().alpha(0f).setDuration(200).start();
        recyclerView.setAdapter(null);
        relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                relativeLayout.removeView(progressBar);
                final DuzenleAdapter duzenleAdapter =new DuzenleAdapter(context,selection_code,selection_gerceklesen_dogumlar,orderBy);
                recyclerView.setAdapter(duzenleAdapter);
                recyclerView.animate().alpha(1f).setDuration(200).start();
            }
        },600);
    }

    void initFilterMenu(){
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

    @Override
    protected void onRestart() {
        super.onRestart();
        initProgressBarAndTask();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        recyclerView.setAdapter(null);
    }

}
