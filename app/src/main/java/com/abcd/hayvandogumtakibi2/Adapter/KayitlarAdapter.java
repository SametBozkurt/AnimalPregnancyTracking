package com.abcd.hayvandogumtakibi2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class KayitlarAdapter extends RecyclerView.Adapter<KayitlarAdapter.CustomViewHolder> {

    private final ArrayList<DataModel> dataModelArrayList;
    private final Context context;
    private final int code;
    private final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.getDefault());
    private final Date date=new Date();
    private boolean listmode=false;

    public KayitlarAdapter(final Context context, int code, @Nullable String selectionClause, @Nullable String orderClause, boolean listmode){
        this.context=context;
        this.dataModelArrayList= SQLiteDatabaseHelper.getInstance(context).getSimpleData(selectionClause,orderClause);
        this.code=code;
        this.listmode=listmode;
    }

    public KayitlarAdapter(final Context context){
        this.context=context;
        this.dataModelArrayList=SQLiteDatabaseHelper.getInstance(context).getSonOlusturulanlar();
        this.code=0;
        //Bu constructor son oluşturulanlar için oluşturuldu.
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(!listmode){
            view=LayoutInflater.from(context).inflate(R.layout.kayitlar_adapter_tile_design,parent,false);
        }
        else{
            view=LayoutInflater.from(context).inflate(R.layout.kayitlar_adapter_list_design,parent,false);
        }
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, PreferencesHolder.getCardTextSize(context));
        final DataModel dataModel=dataModelArrayList.get(position);
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
        File gorselFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),dataModel.getFotograf_isim());
        if(gorselFile.exists()&&gorselFile.isFile()){
            holder.img_animal.setScaleX(1.0f);
            holder.img_animal.setScaleY(1.0f);
            holder.img_animal.setColorFilter(Color.TRANSPARENT);
            Glide.with(context).load(gorselFile).into(holder.img_animal);
        }
        else{
            holder.img_animal.setScaleX(0.5f);
            holder.img_animal.setScaleY(0.5f);
            holder.img_animal.setColorFilter(Color.parseColor("#2979ff"));
            HayvanDuzenleyici.set_img(context,dataModel.getIs_evcilhayvan(),Integer.parseInt(dataModel.getTur()),holder.img_animal);
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

        final TextView textView, textViewTur;
        final ImageView img_animal;

        private CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            textView=itemView.findViewById(R.id.text);
            textViewTur=itemView.findViewById(R.id.txt_tur);
        }
    }
}