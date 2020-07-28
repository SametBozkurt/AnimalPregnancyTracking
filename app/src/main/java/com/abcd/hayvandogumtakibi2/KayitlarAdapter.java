package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class KayitlarAdapter extends RecyclerView.Adapter<KayitlarAdapter.CustomViewHolder> {

    private final ArrayList<HayvanVeriler> hayvanVeriler;
    private final Context context;
    private final int code;
    private final DateFormat dateFormat;
    private final Date date;

    KayitlarAdapter(Context context,int code){
        this.context=context;
        this.hayvanVeriler=SQLiteDatabaseHelper.getInstance(context).getSimpleData();
        this.code=code;
        dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.getDefault());
        date=new Date();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.kayitlar_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final HayvanVeriler mHayvanVeriler=hayvanVeriler.get(position);
        switch(code){
            case 0:
                holder.textView.setText(mHayvanVeriler.getIsim());
                break;
            case 1:
                if(mHayvanVeriler.getKupe_no()==null||mHayvanVeriler.getKupe_no().length()==0){
                    holder.textView.setText(context.getString(R.string.kupe_no_yok));
                }
                else{
                    holder.textView.setText(mHayvanVeriler.getKupe_no());
                }
                break;
            case 2:
                date.setTime(Long.parseLong(mHayvanVeriler.getTohumlama_tarihi()));
                holder.textView.setText(dateFormat.format(date));
                break;
            case 3:
                date.setTime(Long.parseLong(mHayvanVeriler.getDogum_tarihi()));
                holder.textView.setText(dateFormat.format(date));
                break;
        }
        if(mHayvanVeriler.getFotograf_isim().length()!=0){
            File gorselFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),mHayvanVeriler.getFotograf_isim());
            Glide.with(context).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
        }
        else if(mHayvanVeriler.getFotograf_isim()==null||mHayvanVeriler.getFotograf_isim().length()==0){
            HayvanDuzenleyici.set_img(context,mHayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(mHayvanVeriler.getTur()),holder.img_animal);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data=new Bundle();
                data.putInt("ID",mHayvanVeriler.getId());
                Intent intent=new Intent(context,ActivityDetails.class);
                intent.putExtras(data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hayvanVeriler.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        final TextView textView;
        final ImageView img_animal;

        private CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            textView=itemView.findViewById(R.id.text);
        }
    }
}