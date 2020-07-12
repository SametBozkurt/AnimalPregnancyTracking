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
import java.util.Calendar;

public class KritiklerAdapter extends RecyclerView.Adapter<KritiklerAdapter.CustomViewHolder> {

    private final Context context;
    private final ArrayList<HayvanVeriler> hayvanVerilerArrayList;


    KritiklerAdapter(Context context, ArrayList<HayvanVeriler> hayvanVerilerArrayList1){
        this.context=context;
        this.hayvanVerilerArrayList=hayvanVerilerArrayList1;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.kritikler_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final HayvanVeriler hayvanVeriler=hayvanVerilerArrayList.get(position);
        holder.txt_isim.setText(new StringBuilder(hayvanVeriler.getIsim()));
        if(hayvanVeriler.getFotograf_isim().length()!=0){
            File gorselFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler.getFotograf_isim());
            Glide.with(context).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
        }
        else if(hayvanVeriler.getFotograf_isim()==null||hayvanVeriler.getFotograf_isim().length()==0){
            HayvanDuzenleyici.set_img(context,hayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler.getTur()),holder.img_animal);
        }
        if(hayvanVeriler.getDogum_tarihi().length()!=0){
            if(get_gun_sayisi(Long.parseLong(hayvanVeriler.getDogum_tarihi()))>=0){
                holder.txt_durum.setText(new StringBuilder(String.valueOf(get_gun_sayisi(Long.parseLong(hayvanVeriler.getDogum_tarihi()))))
                        .append(" ")
                        .append(context.getString(R.string.txt_day_2)));
            }
            else{
                holder.txt_durum.setText(new StringBuilder(context.getString(R.string.text_NA)).append(" ").append(context.getString(R.string.txt_day_2)));
            }
        }
        else if(hayvanVeriler.getDogum_tarihi()==null||hayvanVeriler.getDogum_tarihi().length()==0){
            holder.txt_durum.setText(new StringBuilder(context.getString(R.string.text_NA)).append(" ").append(context.getString(R.string.txt_day_2)));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data=new Bundle();
                data.putInt("ID",hayvanVeriler.getId());
                Intent intent=new Intent(context,ActivityDetails.class);
                intent.putExtras(data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hayvanVerilerArrayList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        final TextView txt_isim;
        final TextView txt_durum;
        final ImageView img_animal;

        CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            txt_isim=itemView.findViewById(R.id.txt_isim);
            txt_durum=itemView.findViewById(R.id.txt_durum);
        }
    }

    private int get_gun_sayisi(long dogum_tarihi_in_millis){
        long gun=(dogum_tarihi_in_millis-Calendar.getInstance().getTimeInMillis())/(1000*60*60*24);
        return (int)gun;
    }

}
