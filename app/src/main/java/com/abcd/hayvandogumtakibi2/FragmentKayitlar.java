package com.abcd.hayvandogumtakibi2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class FragmentKayitlar extends Fragment {

    SQLiteDatabaseHelper databaseHelper;
    Context context;
    Animation fab_open, fab_close, fab_clock, fab_anticlock;
    FloatingActionButton btn_add,btn_pet,btn_barn;
    TextView txt_pet,txt_barn;
    RecyclerView recyclerView;
    Spinner goruntuleme_kategorisi;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    boolean is_opened = false;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context=context;
        databaseHelper=SQLiteDatabaseHelper.getInstance(context);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_records,container,false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fab_open= AnimationUtils.loadAnimation(context,R.anim.fab_on);
                fab_close=AnimationUtils.loadAnimation(context,R.anim.fab_off);
                fab_clock=AnimationUtils.loadAnimation(context,R.anim.rotation_clock);
                fab_anticlock=AnimationUtils.loadAnimation(context,R.anim.rotation_anticlock);
            }
        });
        btn_add = view.findViewById(R.id.create);
        btn_pet = view.findViewById(R.id.fab_pet);
        btn_barn = view.findViewById(R.id.fab_barn);
        txt_pet = view.findViewById(R.id.text_pet);
        txt_barn = view.findViewById(R.id.text_barn);
        recyclerView=view.findViewById(R.id.recyclerView);
        goruntuleme_kategorisi = view.findViewById(R.id.kategori);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(context,R.layout.spinner_kategori,
                getResources().getStringArray(R.array.goruntuleme_kategorileri));
        spinner_adapter.setDropDownViewResource(R.layout.spinner_kategori);
        GridLayoutManager layoutManager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(layoutManager);
        goruntuleme_kategorisi.setAdapter(spinner_adapter);
        goruntuleme_kategorisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                final ProgressDialog dialog=new ProgressDialog(context);
                dialog.setTitle(R.string.dialog_title1);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(false);
                dialog.show();
                Thread kayitlar_thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                hayvanVerilerArrayList = databaseHelper.getSimpleData();
                                recyclerView.setAdapter(new KayitlarAdapter(context,hayvanVerilerArrayList,position));
                                dialog.dismiss();
                            }
                        });
                    }
                });
                kayitlar_thread.start();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_opened){
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            txt_pet.startAnimation(fab_close);
                            btn_pet.startAnimation(fab_close);
                            txt_barn.startAnimation(fab_close);
                            btn_barn.startAnimation(fab_close);
                            btn_add.startAnimation(fab_anticlock);
                        }
                    });
                    btn_pet.setClickable(false);
                    btn_barn.setClickable(false);
                    is_opened=false;
                }
                else{
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            txt_pet.startAnimation(fab_open);
                            btn_pet.startAnimation(fab_open);
                            txt_barn.startAnimation(fab_open);
                            btn_barn.startAnimation(fab_open);
                            btn_add.startAnimation(fab_clock);
                        }
                    });
                    btn_pet.setClickable(true);
                    btn_barn.setClickable(true);
                    is_opened = true;
                }
            }
        });
        btn_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bundle_intent=new Intent(context,ActivityDogumKayit.class);
                Bundle datas=new Bundle();
                datas.putInt("isPet",1);
                bundle_intent.putExtras(datas);
                context.startActivity(bundle_intent);
                //Evcil hayvan ise 1
            }
        });
        btn_barn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bundle_intent=new Intent(getContext(),ActivityDogumKayit.class);
                Bundle datas=new Bundle();
                datas.putInt("isPet",2);
                bundle_intent.putExtras(datas);
                context.startActivity(bundle_intent);
                //Besi hayvanı ise 2
            }
        });
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            String INTENT_ACTION= "SET_AN_ALARM" ;
            context.sendBroadcast(new Intent(context,TarihKontrol.class).setAction(INTENT_ACTION));
        }
        return view;
    }



}