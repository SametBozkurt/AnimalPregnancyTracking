package com.abcd.hayvandogumtakibi2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

    Context context;
    Animation fab_open, fab_close, fab_clock, fab_anticlock;
    RecyclerView recyclerView;
    Spinner goruntuleme_kategorisi;
    ProgressBar mProgressBar;
    RelativeLayout relativeLayout;
    boolean is_opened = false;
    int mPosition=0;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context=context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_records,container,false);
        relativeLayout=view.findViewById(R.id.main_layout);
        final FloatingActionButton btn_add = view.findViewById(R.id.create);
        final FloatingActionButton btn_pet = view.findViewById(R.id.fab_pet);
        final FloatingActionButton btn_barn = view.findViewById(R.id.fab_barn);
        final TextView txt_pet = view.findViewById(R.id.text_pet);
        final TextView txt_barn = view.findViewById(R.id.text_barn);
        view.post(new Runnable() {
            @Override
            public void run() {
                fab_open= AnimationUtils.loadAnimation(context,R.anim.fab_on);
                fab_close=AnimationUtils.loadAnimation(context,R.anim.fab_off);
                fab_clock=AnimationUtils.loadAnimation(context,R.anim.rotation_clock);
                fab_anticlock=AnimationUtils.loadAnimation(context,R.anim.rotation_anticlock);
            }
        });
        recyclerView=view.findViewById(R.id.recyclerView);
        goruntuleme_kategorisi = view.findViewById(R.id.kategori);
        final ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(context,R.layout.spinner_kategori,
                getResources().getStringArray(R.array.goruntuleme_kategorileri));
        spinner_adapter.setDropDownViewResource(R.layout.spinner_kategori);
        final GridLayoutManager layoutManager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(layoutManager);
        goruntuleme_kategorisi.setAdapter(spinner_adapter);
        goruntuleme_kategorisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {
                mPosition=position;
                mProgressBar=new ProgressBar(context);
                final RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                mProgressBar.setLayoutParams(mLayoutParams);
                mProgressBar.setIndeterminate(true);
                relativeLayout.addView(mProgressBar);
                new Task1().execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(is_opened){
                    txt_barn.startAnimation(fab_close);
                    btn_barn.startAnimation(fab_close);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt_pet.startAnimation(fab_close);
                            btn_pet.startAnimation(fab_close);
                        }
                    },200);
                    btn_add.startAnimation(fab_anticlock);
                    btn_pet.setClickable(false);
                    btn_barn.setClickable(false);
                    is_opened=false;
                }
                else{
                    txt_pet.startAnimation(fab_open);
                    btn_pet.startAnimation(fab_open);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt_barn.startAnimation(fab_open);
                            btn_barn.startAnimation(fab_open);
                        }
                    },200);
                    btn_add.startAnimation(fab_clock);
                    btn_pet.setClickable(true);
                    btn_barn.setClickable(true);
                    is_opened = true;
                }
            }
        });
        btn_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent bundle_intent=new Intent(context,ActivityDogumKayit.class);
                final Bundle datas=new Bundle();
                datas.putInt("isPet",1);
                bundle_intent.putExtras(datas);
                context.startActivity(bundle_intent);
                //Evcil hayvan ise 1
            }
        });
        btn_barn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent bundle_intent=new Intent(getContext(),ActivityDogumKayit.class);
                final Bundle datas=new Bundle();
                datas.putInt("isPet",2);
                bundle_intent.putExtras(datas);
                context.startActivity(bundle_intent);
                //Besi hayvanı ise 2
            }
        });
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    class Task1 extends AsyncTask<String, Integer, Boolean> {

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
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            relativeLayout.removeView(mProgressBar);
            final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(context);
            final ArrayList<HayvanVeriler> hayvanVerilerArrayList=databaseHelper.getSimpleData();
            recyclerView.setAdapter(new KayitlarAdapter(context,hayvanVerilerArrayList,mPosition));
            recyclerView.animate().alpha(1f).setDuration(200).start();
            mProgressBar=null;
        }
    }

}