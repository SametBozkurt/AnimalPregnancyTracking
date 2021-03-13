package com.abcd.hayvandogumtakibi2.Misc;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcd.hayvandogumtakibi2.R;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Arrays;

public class HayvanDuzenleyici {

    private static TarihHesaplayici tarihHesaplayici;
    private static ArrayList<String> arrayList_all;
    private static ArrayList<String> arrayList_pet;
    private static ArrayList<String> arrayList_barn;

    public static void load(final Context context){
        tarihHesaplayici = TarihHesaplayici.getInstance(context);
        arrayList_all = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list)));
        arrayList_pet = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list_pet)));
        arrayList_barn = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.animal_list_barn)));
    }

    public static void set_text(final Context context, final int isPet, final int tur_kodu, final TextView textView){
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
            case 3: //Tüm türler-Diğer yok-PeriodsAdapter içidir.
                switch (tur_kodu){
                    case 0:
                        textView.setText(new StringBuilder(String.valueOf(tarihHesaplayici.DAY_COW))
                                .append(" ").append(context.getString(R.string.txt_day_2)));
                        break;
                    case 1:
                        textView.setText(new StringBuilder(String.valueOf(tarihHesaplayici.DAY_SHEEP))
                                .append(" ").append(context.getString(R.string.txt_day_2)));
                        break;
                    case 2:
                        textView.setText(new StringBuilder(String.valueOf(tarihHesaplayici.DAY_GOAT))
                                .append(" ").append(context.getString(R.string.txt_day_2)));
                        break;
                    case 3:
                        textView.setText(new StringBuilder(String.valueOf(tarihHesaplayici.DAY_CAT))
                                .append(" ").append(context.getString(R.string.txt_day_2)));
                        break;
                    case 4:
                        textView.setText(new StringBuilder(String.valueOf(tarihHesaplayici.DAY_DOG))
                                .append(" ").append(context.getString(R.string.txt_day_2)));
                        break;
                    case 5:
                        textView.setText(new StringBuilder(String.valueOf(tarihHesaplayici.DAY_HAMSTER))
                                .append(" ").append(context.getString(R.string.txt_day_2)));
                        break;
                    case 6:
                        textView.setText(new StringBuilder(String.valueOf(tarihHesaplayici.DAY_HORSE))
                                .append(" ").append(context.getString(R.string.txt_day_2)));
                        break;
                    case 7:
                        textView.setText(new StringBuilder(String.valueOf(tarihHesaplayici.DAY_DONKEY))
                                .append(" ").append(context.getString(R.string.txt_day_2)));
                        break;
                    case 8:
                        textView.setText(new StringBuilder(String.valueOf(tarihHesaplayici.DAY_CAMEL))
                                .append(" ").append(context.getString(R.string.txt_day_2)));
                        break;
                    case 9:
                        textView.setText(new StringBuilder(String.valueOf(tarihHesaplayici.DAY_PIG))
                                .append(" ").append(context.getString(R.string.txt_day_2)));
                        break;
                }
                break;
        }
    }
    public static void set_img(final Context context, final int isPet, final int tur_kodu, final ImageView imgView){
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
                    case 7:
                        Glide.with(context).load(R.mipmap.horse).into(imgView);
                        break;
                    case 8:
                        Glide.with(context).load(R.mipmap.donkey).into(imgView);
                        break;
                    case 9:
                        Glide.with(context).load(R.mipmap.camel).into(imgView);
                        break;
                    case 10:
                        Glide.with(context).load(R.mipmap.pig).into(imgView);
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
                    case 4:
                        Glide.with(context).load(R.mipmap.horse).into(imgView);
                        break;
                    case 5:
                        Glide.with(context).load(R.mipmap.donkey).into(imgView);
                        break;
                    case 6:
                        Glide.with(context).load(R.mipmap.camel).into(imgView);
                        break;
                    case 7:
                        Glide.with(context).load(R.mipmap.pig).into(imgView);
                        break;
                }
                break;
            case 3: //Tüm türler-Diğer yok-PeriodsAdapter içidir.
                switch (tur_kodu){
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
                        Glide.with(context).load(R.mipmap.horse).into(imgView);
                        break;
                    case 7:
                        Glide.with(context).load(R.mipmap.donkey).into(imgView);
                        break;
                    case 8:
                        Glide.with(context).load(R.mipmap.camel).into(imgView);
                        break;
                    case 9:
                        Glide.with(context).load(R.mipmap.pig).into(imgView);
                        break;
                }
        }

    }

}