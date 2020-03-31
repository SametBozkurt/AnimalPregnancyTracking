package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Arrays;

class HayvanDuzenleyici {

    private Context context;

    HayvanDuzenleyici(Context mContext){
        context=mContext;
    }

    void set_text(int isPet, int tur_kodu, TextView textView_tur){
        ArrayList<String> arrayList_all = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list)));
        ArrayList<String> arrayList_pet = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list_pet)));
        ArrayList<String> arrayList_barn = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list_barn)));
        switch(isPet){
            case 0: //hepsi
                textView_tur.setText(arrayList_all.get(tur_kodu));
                break;
            case 1: //sadece evcil hayvanlar
                textView_tur.setText(arrayList_pet.get(tur_kodu));
                break;
            case 2: //sadece besi hayvanları
                textView_tur.setText(arrayList_barn.get(tur_kodu));
                break;
        }
    }
    void set_img(int isPet, int tur_kodu, ImageView imgView){
        switch (isPet){
            case 0: //hepsi
                switch(tur_kodu){
                    case 0:
                        Glide.with(context).load(R.drawable.icon_cow).into(imgView);
                        break;
                    case 1:
                        Glide.with(context).load(R.drawable.icon_sheep).into(imgView);
                        break;
                    case 2:
                        Glide.with(context).load(R.drawable.icon_goat).into(imgView);
                        break;
                    case 3:
                        Glide.with(context).load(R.drawable.icon_cat).into(imgView);
                        break;
                    case 4:
                        Glide.with(context).load(R.drawable.icon_dog).into(imgView);
                        break;
                    case 5:
                        Glide.with(context).load(R.drawable.icon_hamster).into(imgView);
                        break;
                    case 6:
                        Glide.with(context).load(R.drawable.icon_interrogation_mark).into(imgView);
                        break;
                    case 7:
                        Glide.with(context).load(R.drawable.icon_horse).into(imgView);
                        break;
                    case 8:
                        Glide.with(context).load(R.drawable.icon_donkey).into(imgView);
                        break;
                    case 9:
                        Glide.with(context).load(R.drawable.icon_camel).into(imgView);
                        break;
                }
                break;
            case 1: //sadece evcil hayvanlar
                switch(tur_kodu){
                    case 0:
                        Glide.with(context).load(R.drawable.icon_cat).into(imgView);
                        break;
                    case 1:
                        Glide.with(context).load(R.drawable.icon_dog).into(imgView);
                        break;
                    case 2:
                        Glide.with(context).load(R.drawable.icon_hamster).into(imgView);
                        break;
                    case 3:
                        Glide.with(context).load(R.drawable.icon_interrogation_mark).into(imgView);
                        break;
                }
                break;
            case 2: //sadece besi hayvanları
                switch(tur_kodu){
                    case 0:
                        Glide.with(context).load(R.drawable.icon_cow).into(imgView);
                        break;
                    case 1:
                        Glide.with(context).load(R.drawable.icon_sheep).into(imgView);
                        break;
                    case 2:
                        Glide.with(context).load(R.drawable.icon_goat).into(imgView);
                        break;
                    case 3:
                        Glide.with(context).load(R.drawable.icon_interrogation_mark).into(imgView);
                        break;
                    case 4:
                        Glide.with(context).load(R.drawable.icon_horse).into(imgView);
                        break;
                    case 5:
                        Glide.with(context).load(R.drawable.icon_donkey).into(imgView);
                        break;
                    case 6:
                        Glide.with(context).load(R.drawable.icon_camel).into(imgView);
                        break;
                }
                break;

        }

    }


}
