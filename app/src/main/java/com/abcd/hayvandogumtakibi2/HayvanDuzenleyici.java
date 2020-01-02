package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Arrays;

public class HayvanDuzenleyici {

    Context context;

    public HayvanDuzenleyici(Context mContext){
        context=mContext;
    }

    public void set_text(int isPet, int tur_kodu, TextView textView_tur){
        ArrayList<String> arrayList_all = new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list)));
        ArrayList<String> arrayList_pet = new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list_pet)));
        ArrayList<String> arrayList_barn = new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list_barn)));
        switch(isPet){
            case 0: //hepsi
                textView_tur.setText(new StringBuilder(context.getString(R.string.listView_tur))
                        .append(arrayList_all.get(tur_kodu)));
                break;
            case 1: //sadece evcil hayvanlar
                textView_tur.setText(new StringBuilder(context.getString(R.string.listView_tur))
                        .append(arrayList_pet.get(tur_kodu)));
                break;
            case 2: //sadece besi hayvanları
                textView_tur.setText(new StringBuilder(context.getString(R.string.listView_tur))
                        .append(arrayList_barn.get(tur_kodu)));
                break;
        }
    }
    public void set_img(int isPet, int tur_kodu, ImageView imgView){
        switch (isPet){
            case 0: //hepsi
                switch(tur_kodu){
                    case 0:
                        Glide.with(context).load(R.mipmap.cow).into(imgView);
                        break;
                    case 1:
                        Glide.with(context).load(R.mipmap.sheep).into(imgView);
                        break;
                    case 2:
                        Glide.with(context).load(R.mipmap.goat).into(imgView);
                        break;
                    case 3:
                        Glide.with(context).load(R.mipmap.cat).into(imgView);
                        break;
                    case 4:
                        Glide.with(context).load(R.mipmap.dog).into(imgView);
                        break;
                    case 5:
                        Glide.with(context).load(R.mipmap.hamster).into(imgView);
                        break;
                    case 6:
                        Glide.with(context).load(R.mipmap.interrogation_mark).into(imgView);
                        break;
                }
                break;
            case 1: //sadece evcil hayvanlar
                switch(tur_kodu){
                    case 0:
                        Glide.with(context).load(R.mipmap.cat).into(imgView);
                        break;
                    case 1:
                        Glide.with(context).load(R.mipmap.dog).into(imgView);
                        break;
                    case 2:
                        Glide.with(context).load(R.mipmap.hamster).into(imgView);
                        break;
                    case 3:
                        Glide.with(context).load(R.mipmap.interrogation_mark).into(imgView);
                        break;
                }
                break;
            case 2: //sadece besi hayvanları
                switch(tur_kodu){
                    case 0:
                        Glide.with(context).load(R.mipmap.cow).into(imgView);
                        break;
                    case 1:
                        Glide.with(context).load(R.mipmap.sheep).into(imgView);
                        break;
                    case 2:
                        Glide.with(context).load(R.mipmap.goat).into(imgView);
                        break;
                    case 3:
                        Glide.with(context).load(R.mipmap.interrogation_mark).into(imgView);
                        break;
                }
                break;

        }

    }


}
