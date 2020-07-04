package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Arrays;

class HayvanDuzenleyici {

    private final Context context;

    HayvanDuzenleyici(Context mContext){
        context=mContext;
    }

    void set_text(int isPet, int tur_kodu, TextView textView){
        ArrayList<String> arrayList_all = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list)));
        ArrayList<String> arrayList_pet = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list_pet)));
        ArrayList<String> arrayList_barn = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list_barn)));
        switch(isPet){
            case 0: //hepsi
                textView.setText(arrayList_all.get(tur_kodu));
                break;
            case 1: //sadece evcil hayvanlar
                textView.setText(arrayList_pet.get(tur_kodu));
                break;
            case 2: //sadece besi hayvanları
                textView.setText(arrayList_barn.get(tur_kodu));
                break;
            case 3: //Tüm türler-Diğer yok.
                switch (tur_kodu){
                    case 0:
                        textView.setText(TarihHesaplayici.DAY_COW+" "+context.getString(R.string.txt_day_2));
                        break;
                    case 1:
                        textView.setText(TarihHesaplayici.DAY_SHEEP+" "+context.getString(R.string.txt_day_2));
                        break;
                    case 2:
                        textView.setText(TarihHesaplayici.DAY_GOAT+" "+context.getString(R.string.txt_day_2));
                        break;
                    case 3:
                        textView.setText(TarihHesaplayici.DAY_CAT+" "+context.getString(R.string.txt_day_2));
                        break;
                    case 4:
                        textView.setText(TarihHesaplayici.DAY_DOG+" "+context.getString(R.string.txt_day_2));
                        break;
                    case 5:
                        textView.setText(TarihHesaplayici.DAY_HAMSTER+" "+context.getString(R.string.txt_day_2));
                        break;
                    case 6:
                        textView.setText(TarihHesaplayici.DAY_HORSE+" "+context.getString(R.string.txt_day_2));
                        break;
                    case 7:
                        textView.setText(TarihHesaplayici.DAY_DONKEY+" "+context.getString(R.string.txt_day_2));
                        break;
                    case 8:
                        textView.setText(TarihHesaplayici.DAY_CAMEL+" "+context.getString(R.string.txt_day_2));
                        break;
                }
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
            case 3: //Tüm türler-Diğer yok.
                switch (tur_kodu){
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
                        Glide.with(context).load(R.drawable.icon_horse).into(imgView);
                        break;
                    case 7:
                        Glide.with(context).load(R.drawable.icon_donkey).into(imgView);
                        break;
                    case 8:
                        Glide.with(context).load(R.drawable.icon_camel).into(imgView);
                        break;
                }
        }

    }


}
