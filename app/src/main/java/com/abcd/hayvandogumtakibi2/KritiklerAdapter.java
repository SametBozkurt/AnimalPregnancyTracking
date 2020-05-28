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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class KritiklerAdapter extends RecyclerView.Adapter<KritiklerAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    private SimpleDateFormat date_formatter=new SimpleDateFormat("dd/MM/yyyy");


    KritiklerAdapter(Context context, ArrayList<HayvanVeriler> hayvanVerilerArrayList1){
        this.context=context;
        this.hayvanVerilerArrayList=hayvanVerilerArrayList1;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.kritikler_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final HayvanVeriler hayvanVeriler=hayvanVerilerArrayList.get(position);
        HayvanDuzenleyici hayvanDuzenleyici=new HayvanDuzenleyici(context);
        holder.txt_isim.setText(new StringBuilder(hayvanVeriler.getIsim()));
        if(hayvanVeriler.getFotograf_isim().length()!=0){
            File gorselFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler.getFotograf_isim());
            Glide.with(context).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
        }
        else if(hayvanVeriler.getFotograf_isim()==null||hayvanVeriler.getFotograf_isim().length()==0){
            hayvanDuzenleyici.set_img(hayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler.getTur()),holder.img_animal);
        }
        if(hayvanVeriler.getDogum_tarihi().length()!=0){
            if(get_gun_sayisi(hayvanVeriler.getDogum_tarihi())>=0){
                holder.txt_durum.setText(new StringBuilder(String.valueOf(get_gun_sayisi(hayvanVeriler.getDogum_tarihi())))
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

        TextView txt_isim, txt_durum;
        ImageView img_animal;

        CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            txt_isim=itemView.findViewById(R.id.txt_isim);
            txt_durum=itemView.findViewById(R.id.txt_durum);
        }
    }

    private int get_gun_sayisi(String date){
        Calendar takvim= Calendar.getInstance();
        int gun,ay,yil;
        Date bugun = null,dogum = null;
        gun=takvim.get(Calendar.DAY_OF_MONTH);
        ay=takvim.get(Calendar.MONTH)+1;
        yil=takvim.get(Calendar.YEAR);
        String date_bugun=gun+"/"+ay+"/"+yil;
        try {
            bugun=date_formatter.parse(date_bugun);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            dogum=date_formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long fark_ms=dogum.getTime()-bugun.getTime();
        long gun_sayisi=(fark_ms/(1000*60*60*24));
        return (int)gun_sayisi;
    }

}
