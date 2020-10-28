package com.abcd.hayvandogumtakibi2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class ActivityTumKayitlar extends AppCompatActivity {

    RelativeLayout relativeLayout;
    final Context context=this;
    int selection_code=0, selectedRadioButtonFilter=0, selectedRadioButtonOrder=R.id.radio_button_AtoZ;
    String selection_gerceklesen_dogumlar=null,table_name=SQLiteDatabaseHelper.SUTUN_0, orderKey=" ASC", orderBy=table_name+orderKey;
    boolean switchIsChecked=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tum_kayitlar);
        final ImageView cross=findViewById(R.id.iptal);
        final Button btn_filter=findViewById(R.id.btn_filter);
        relativeLayout=findViewById(R.id.parent);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterMenu();
            }
        });
        initProgressBarAndTask();
    }

    void initProgressBarAndTask(){
        final RecyclerView recyclerView=findViewById(R.id.recyclerView);
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
                final KayitlarAdapter kayitlarAdapter=new KayitlarAdapter(context,selection_code,selection_gerceklesen_dogumlar, orderBy);
                recyclerView.setAdapter(kayitlarAdapter);
                relativeLayout.removeView(progressBar);
                recyclerView.animate().alpha(1f).setDuration(200).start();
            }
        },600);
    }

    void openFilterMenu(){
        final Dialog dialog = new Dialog(context,R.style.DialogStyleTest);
        dialog.setContentView(R.layout.layout_filter_and_sort);
        final Window window=dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final WindowManager.LayoutParams winLP=window.getAttributes();
        winLP.gravity= Gravity.BOTTOM;
        window.setAttributes(winLP);
        final Button buttonApply=dialog.findViewById(R.id.btn_apply), buttonReset=dialog.findViewById(R.id.btn_reset);
        final RadioGroup radioGroupFilter=dialog.findViewById(R.id.radio_group_filter);
        final RadioGroup radioGroupOrder=dialog.findViewById(R.id.radio_group_order);
        final SwitchMaterial switchMaterial=dialog.findViewById(R.id.switch_show_happeneds);
        switchMaterial.setChecked(switchIsChecked);
        radioGroupFilter.check(selectedRadioButtonFilter);
        radioGroupOrder.check(selectedRadioButtonOrder);
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
                orderBy=table_name+orderKey;
                selectedRadioButtonFilter=checkedId;
            }
        });
        radioGroupOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_button_AtoZ){
                    orderKey=" ASC";
                }
                else if(checkedId==R.id.radio_button_ZtoA){
                    orderKey=" DESC";
                }
                orderBy=table_name+orderKey;
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
                dialog.dismiss();
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupOrder.check(R.id.radio_button_AtoZ);
                selection_code=0;
                selectedRadioButtonFilter=0;
                switchIsChecked=true;
                table_name=SQLiteDatabaseHelper.SUTUN_0;
                orderKey=" ASC";
                orderBy=table_name+orderKey;
                selection_gerceklesen_dogumlar=null;
                initProgressBarAndTask();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}