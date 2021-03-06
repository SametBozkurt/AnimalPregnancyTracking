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
                holder.textView.setText(new StringBuilder(mHayvanVeriler.getIsim()));
                break;
            case 1:
                if(mHayvanVeriler.getKupe_no()==null||mHayvanVeriler.getKupe_no().length()==0){
                    holder.textView.setText(new StringBuilder(context.getString(R.string.kupe_no_yok)));
                }
                else{
                    holder.textView.setText(new StringBuilder(mHayvanVeriler.getKupe_no()));
                }
                break;
            case 2:
                holder.textView.setText(new StringBuilder(mHayvanVeriler.getTohumlama_tarihi()));
                break;
            case 3:
                holder.textView.setText(new StringBuilder(mHayvanVeriler.getDogum_tarihi()));
                break;
        }
        if(mHayvanVeriler.getFotograf_isim().length()!=0){
            File gorselFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),mHayvanVeriler.getFotograf_isim());
            Glide.with(context).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
        }
        else if(mHayvanVeriler.getFotograf_isim()==null||mHayvanVeriler.getFotograf_isim().length()==0){
            hayvanDuzenleyici.set_img(mHayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(mHayvanVeriler.getTur()),holder.img_animal);
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

        TextView textView;
        ImageView img_animal;

        private CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            textView=itemView.findViewById(R.id.text);
        }
    }
}
