package com.abcd.hayvandogumtakibi2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.abcd.hayvandogumtakibi2.Activity.ActivityDetails;
import com.abcd.hayvandogumtakibi2.Misc.DataModel;
import com.abcd.hayvandogumtakibi2.Misc.HayvanDuzenleyici;
import com.abcd.hayvandogumtakibi2.Misc.PreferencesHolder;
import com.abcd.hayvandogumtakibi2.Misc.SQLiteDatabaseHelper;
import com.abcd.hayvandogumtakibi2.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class KritiklerAdapter extends RecyclerView.Adapter<KritiklerAdapter.CustomViewHolder> {

    private final Context context;
    private final ArrayList<DataModel> dataModelArrayList;
    private final int code;
    private final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    private final Date date=new Date();
    private boolean isListedViewEnabled=false;

    public KritiklerAdapter(final Context context,int code, @Nullable String orderClause, boolean isListed){
        this.context=context;
        this.dataModelArrayList= SQLiteDatabaseHelper.getInstance(context).getKritikOlanlar(orderClause,30);
        this.code=code;
        this.isListedViewEnabled=isListed;
    }

    public KritiklerAdapter(final Context context){
        this.context=context;
        this.dataModelArrayList=SQLiteDatabaseHelper.getInstance(context).getEnYakinDogumlar();
        this.code=0;
        //Bu constructor en yakın doğumlar için oluşturuldu.
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view;
        if(isListedViewEnabled){
            view=LayoutInflater.from(context).inflate(R.layout.kritikler_adapter_list_design,parent,false);
        }
        else{
            view=LayoutInflater.from(context).inflate(R.layout.kritikler_adapter_tile_design,parent,false);
        }
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, PreferencesHolder.getCardTextSize(context));
        holder.txt_durum.setTextSize(TypedValue.COMPLEX_UNIT_SP,PreferencesHolder.getCardTextSize(context));
        final DataModel dataModel=dataModelArrayList.get(position);
        holder.textView.setText(new StringBuilder(dataModel.getIsim()));
        switch(code){
            case 0:
                holder.textView.setText(dataModel.getIsim());
                break;
            case 1:
                if(dataModel.getKupe_no()==null||dataModel.getKupe_no().length()==0){
                    holder.textView.setText(context.getString(R.string.kupe_no_yok));
                }
                else{
                    holder.textView.setText(dataModel.getKupe_no());
                }
                break;
            case 2:
                date.setTime(Long.parseLong(dataModel.getTohumlama_tarihi()));
                holder.textView.setText(dateFormat.format(date));
                break;
            case 3:
                date.setTime(Long.parseLong(dataModel.getDogum_tarihi()));
                holder.textView.setText(dateFormat.format(date));
                break;
        }
        HayvanDuzenleyici.set_text(context,dataModel.getIs_evcilhayvan(),Integer.parseInt(dataModel.getTur()),holder.textViewTur);
        final File gorselFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),dataModel.getFotograf_isim());
        if(gorselFile.exists()&&gorselFile.isFile()){
            holder.img_photo.setScaleX(1.0f);
            holder.img_photo.setScaleY(1.0f);
            holder.img_photo.setColorFilter(Color.TRANSPARENT);
            Glide.with(context).load(Uri.fromFile(gorselFile)).into(holder.img_photo);
        }
        else{
            holder.img_photo.setScaleX(0.5f);
            holder.img_photo.setScaleY(0.5f);
            holder.img_photo.setColorFilter(Color.parseColor("#2979ff"));
            HayvanDuzenleyici.set_img(context,dataModel.getIs_evcilhayvan(),Integer.parseInt(dataModel.getTur()),holder.img_photo);
        }
        if(!dataModel.getDogum_tarihi().isEmpty()){
            if(get_gun_sayisi(Long.parseLong(dataModel.getDogum_tarihi()))>=0){
                holder.txt_durum.setText(new StringBuilder(String.valueOf(get_gun_sayisi(Long.parseLong(dataModel.getDogum_tarihi()))))
                        .append(" ")
                        .append(context.getString(R.string.txt_day_2)));
            }
            else{
                holder.txt_durum.setText(new StringBuilder(context.getString(R.string.text_NA)).append(" ").append(context.getString(R.string.txt_day_2)));
            }
        }
        else if(dataModel.getDogum_tarihi()==null||dataModel.getDogum_tarihi().length()==0){
            holder.txt_durum.setText(new StringBuilder(context.getString(R.string.text_NA)).append(" ").append(context.getString(R.string.txt_day_2)));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data=new Bundle();
                data.putInt("ID",dataModel.getId());
                Intent intent=new Intent(context, ActivityDetails.class);
                intent.putExtras(data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        final TextView textView;
        final TextView txt_durum;
        final TextView textViewTur;
        final ImageView img_photo;
        final ImageView img_clock;

        public CustomViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.txt_isim);
            txt_durum=itemView.findViewById(R.id.txt_durum);
            img_photo=itemView.findViewById(R.id.img_hayvan);
            img_clock=itemView.findViewById(R.id.img_clock);
            textViewTur=itemView.findViewById(R.id.txt_tur);
        }
    }

    private int get_gun_sayisi(long dogum_tarihi_in_millis){
        long gun=(dogum_tarihi_in_millis-System.currentTimeMillis())/(1000*60*60*24);
        return (int)gun;
    }

}
