package com.abcd.hayvandogumtakibi2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class ActivityTumKayitlar extends AppCompatActivity {

    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    ProgressBar mProgressBar;
    final Context context=this;
    int selection_code=0, selectedRadioButtonFilter=R.id.radio_button_isim, selectedRadioButtonOrder=R.id.radio_button_AtoZ;
    String selection_gerceklesen_dogumlar=null,table_name="isim", orderByClause=table_name+" ASC";
    TaskKayitlariYukle taskKayitlariYukle;
    boolean switchIsChecked=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tum_kayitlar);
        final ImageView cross=findViewById(R.id.iptal);
        final Button btn_filter=findViewById(R.id.btn_filter);
        recyclerView=findViewById(R.id.recyclerView);
        final GridLayoutManager layoutManager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(layoutManager);
        relativeLayout=findViewById(R.id.parent);
        taskKayitlariYukle=new TaskKayitlariYukle();
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
        mProgressBar=new ProgressBar(this);
        mProgressBar.setId(R.id.progress1);
        mProgressBar.setIndeterminate(true);
        final RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setLayoutParams(mLayoutParams);
        relativeLayout.addView(mProgressBar);
        taskKayitlariYukle.execute();
    }

    @SuppressLint("StaticFieldLeak")
    class TaskKayitlariYukle extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerView.animate().alpha(0f).setDuration(200).start();
            recyclerView.setAdapter(null);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(context);
            final ArrayList<DataModel> dataModelArrayList=databaseHelper.getSimpleData(selection_gerceklesen_dogumlar, orderByClause);
            final KayitlarAdapter kayitlarAdapter=new KayitlarAdapter(context,dataModelArrayList,selection_code);
            recyclerView.setAdapter(kayitlarAdapter);
            relativeLayout.removeView(mProgressBar);
            recyclerView.animate().alpha(1f).setDuration(200).start();
            mProgressBar=null;
        }
    }

    void openFilterMenu(){
        final Dialog dialog = new Dialog(context,R.style.DialogStyleTest);
        dialog.setContentView(R.layout.layout_filter_and_sort);
        final Button buttonApply=dialog.findViewById(R.id.btn_apply);
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
                    table_name="isim";
                }
                else if(checkedId==R.id.radio_button_id){
                    selection_code=1;
                    table_name="kupe_no";
                }
                else if(checkedId==R.id.radio_button_date1){
                    selection_code=2;
                    table_name="tohumlama_tarihi";
                }
                else if(checkedId==R.id.radio_button_date2){
                    selection_code=3;
                    table_name="dogum_tarihi";
                }
                selectedRadioButtonFilter=checkedId;
            }
        });
        radioGroupOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_button_AtoZ){
                    orderByClause=table_name+" ASC";
                }
                else if(checkedId==R.id.radio_button_ZtoA){
                    orderByClause=table_name+" DESC";
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
                taskKayitlariYukle=new TaskKayitlariYukle();
                initProgressBarAndTask();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}