package com.abcd.hayvandogumtakibi2;

import android.content.Context;
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
    RadioGroup myRadioGroup;
    RadioButton radioButtonIsimler;
    RelativeLayout relativeLayout;
    FrameLayout sonuc_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ara);
        final TextInputEditText inputAranacak=findViewById(R.id.bul);
        final ImageView cross=findViewById(R.id.iptal);
        myRadioGroup=findViewById(R.id.radio_group);
        radioButtonIsimler=findViewById(R.id.radio_button_isim);
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
                if(!charSequence.toString().isEmpty()){
                    initProgressBarAndTask(charSequence.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    void initProgressBarAndTask(final String aranacak){
        final ProgressBar progressBar=new ProgressBar(context);
        progressBar.setIndeterminate(true);
        final RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(mLayoutParams);
        relativeLayout.addView(progressBar);
        relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                final LayoutInflater layoutInflater=LayoutInflater.from(context);
                final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(context);
                final ArrayList<DataModel> dataModelArrayList;
                if(myRadioGroup.getCheckedRadioButtonId()==radioButtonIsimler.getId()){
                    dataModelArrayList=databaseHelper.getAramaSonuclari(true,aranacak);
                    final View sonuclar_view;
                    if(dataModelArrayList.isEmpty()){
                        sonuclar_view = layoutInflater.inflate(R.layout.arama_snclari_sonuc_yok_lyt, sonuc_container, false);
                    }
                    else{
                        sonuclar_view = layoutInflater.inflate(R.layout.arama_snclari_lyt, sonuc_container, false);
                        final RecyclerView recyclerView=sonuclar_view.findViewById(R.id.recyclerView);
                        final AramalarAdapter aramalarAdapter=new AramalarAdapter(context,dataModelArrayList,true,aranacak);
                        final LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(aramalarAdapter);
                    }
                    sonuc_container.addView(sonuclar_view);
                }
                else{
                    dataModelArrayList=databaseHelper.getAramaSonuclari(false,aranacak);
                    final View sonuclar_view;
                    if(dataModelArrayList.isEmpty()){
                        sonuclar_view = layoutInflater.inflate(R.layout.arama_snclari_sonuc_yok_lyt, sonuc_container, false);
                    }
                    else{
                        sonuclar_view = layoutInflater.inflate(R.layout.arama_snclari_lyt, sonuc_container, false);
                        final RecyclerView recyclerView=sonuclar_view.findViewById(R.id.recyclerView);
                        final AramalarAdapter aramalarAdapter=new AramalarAdapter(context,dataModelArrayList,false,aranacak);
                        final LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(aramalarAdapter);
                    }
                    sonuc_container.addView(sonuclar_view);
                }
                relativeLayout.removeView(progressBar);
            }
        },200);
    }

}
