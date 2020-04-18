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
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.ArrayList;

public class KayitlarAdapter extends RecyclerView.Adapter<KayitlarAdapter.CustomViewHolder> {

    private ArrayList<HayvanVeriler> hayvanVeriler;
    private Context context;
    private HayvanDuzenleyici hayvanDuzenleyici;
    private int code;

    KayitlarAdapter(Context context, ArrayList<HayvanVeriler> hayvanVerilerArrayList,int code){
        this.context=context;
        this.hayvanVeriler=hayvanVerilerArrayList;
        this.code=code;
        hayvanDuzenleyici=new HayvanDuzenleyici(context);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.kayitlar_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final HayvanVeriler hayvanVeriler1=hayvanVeriler.get(position);
        switch(code){
            case 0:
                holder.textView.setText(new StringBuilder(hayvanVeriler1.getIsim()));
                break;
            case 1:
                if(hayvanVeriler1.getKupe_no()==null||hayvanVeriler1.getKupe_no().length()==0){
                    holder.textView.setText(new StringBuilder(context.getString(R.string.kupe_no_yok)));
                }
                else{
                    holder.textView.setText(new StringBuilder(hayvanVeriler1.getKupe_no()));
                }
                break;
            case 2:
                holder.textView.setText(new StringBuilder(hayvanVeriler1.getTohumlama_tarihi()));
                break;
            case 3:
                holder.textView.setText(new StringBuilder(hayvanVeriler1.getDogum_tarihi()));
                break;
        }
        if(hayvanVeriler1.getFotograf_isim().length()!=0){
            File gorselFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler1.getFotograf_isim());
            Glide.with(context).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
        }
        else if(hayvanVeriler1.getFotograf_isim()==null||hayvanVeriler1.getFotograf_isim().length()==0){
            hayvanDuzenleyici.set_img(hayvanVeriler1.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler1.getTur()),holder.img_animal);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data=new Bundle();
                data.putInt("ID",hayvanVeriler1.getId());
                data.putString("isim",hayvanVeriler1.getIsim());
                data.putString("tur",hayvanVeriler1.getTur());
                data.putString("kupe_no",hayvanVeriler1.getKupe_no());
                data.putString("tohumlama_tarihi",hayvanVeriler1.getTohumlama_tarihi());
                data.putString("dogum_tarihi",hayvanVeriler1.getDogum_tarihi());
                data.putString("fotograf_isim",hayvanVeriler1.getFotograf_isim());
                data.putInt("is_evcilhayvan",hayvanVeriler1.getIs_evcilhayvan());
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

        TextView textView;
        ImageView img_animal;

        private CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            textView=itemView.findViewById(R.id.text);
        }
    }
}
