package com.abcd.hayvandogumtakibi2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ActivityKayitAra extends AppCompatActivity {

    final Context context=this;
    ProgressBar progressBar;
    RadioGroup myRadioGroup;
    RadioButton radioButtonIsimler;
    RelativeLayout relativeLayout;
    FrameLayout sonuc_container;
    String aranacak;
    TaskSearch taskSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ara);
        final TextInputEditText inputAranacak=findViewById(R.id.bul);
        final RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        final ImageView cross=findViewById(R.id.iptal);
        myRadioGroup=findViewById(R.id.radio_group);
        radioButtonIsimler=findViewById(R.id.radio_button_isim);
        sonuc_container=findViewById(R.id.layout_sonuclar);
        relativeLayout=findViewById(R.id.main_layout);
        taskSearch=new TaskSearch();
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
                if(taskSearch.getStatus()==AsyncTask.Status.RUNNING){
                    taskSearch.cancel(true);
                    taskSearch=null;
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sonuc_container.removeAllViews();
                relativeLayout.removeView(progressBar);
                if(!charSequence.toString().isEmpty()){
                    aranacak=charSequence.toString();
                    progressBar=new ProgressBar(context);
                    mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    progressBar.setLayoutParams(mLayoutParams);
                    progressBar.setIndeterminate(true);
                    relativeLayout.addView(progressBar);
                    taskSearch=new TaskSearch();
                    taskSearch.execute();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    class TaskSearch extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            final LayoutInflater layoutInflater=LayoutInflater.from(context);
            final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(context);
            ArrayList<DataModel> dataModelArrayList;
            if(myRadioGroup.getCheckedRadioButtonId()==radioButtonIsimler.getId()){
                dataModelArrayList=databaseHelper.getAramaSonuclari(true,aranacak);
                if(dataModelArrayList.isEmpty()){
                    final View sonuclar_view=layoutInflater.inflate(R.layout.arama_snclari_sonuc_yok_lyt,sonuc_container,false);
                    sonuc_container.addView(sonuclar_view);
                }
                else{
                    final View sonuclar_view=layoutInflater.inflate(R.layout.arama_snclari_lyt,sonuc_container,false);
                    final RecyclerView recyclerView=sonuclar_view.findViewById(R.id.recyclerView);
                    final AramalarAdapter aramalarAdapter=new AramalarAdapter(context,
                            dataModelArrayList,
                            true,
                            aranacak);
                    final LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(aramalarAdapter);
                    sonuc_container.addView(sonuclar_view);
                }
            }
            else{
                dataModelArrayList=databaseHelper.getAramaSonuclari(false,aranacak);
                if(dataModelArrayList.isEmpty()){
                    final View sonuclar_view=layoutInflater.inflate(R.layout.arama_snclari_sonuc_yok_lyt,sonuc_container,false);
                    sonuc_container.addView(sonuclar_view);
                }
                else{
                    final View sonuclar_view=layoutInflater.inflate(R.layout.arama_snclari_lyt,sonuc_container,false);
                    final RecyclerView recyclerView=sonuclar_view.findViewById(R.id.recyclerView);
                    final AramalarAdapter aramalarAdapter=new AramalarAdapter(context,
                            dataModelArrayList,
                            false,
                            aranacak);
                    final LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(aramalarAdapter);
                    sonuc_container.addView(sonuclar_view);
                }
            }
            relativeLayout.removeView(progressBar);
            progressBar=null;
        }
    }

}
