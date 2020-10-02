package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentKayitYok extends Fragment {

    Context context;
    Animation fab_open, fab_close, fab_clock, fab_anticlock;
    boolean is_opened = false;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context=context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_no_record,container,false);
        if(container!=null){
            container.clearDisappearingChildren();
        }
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
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}
