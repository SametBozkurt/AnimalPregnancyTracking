package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
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

public class AramalarAdapter extends RecyclerView.Adapter<AramalarAdapter.CustomViewHolder> {

    private final ArrayList<DataModel> dataModel;
    private final Context context;
    private final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    private final Date date=new Date();
    private final String aranan,selection;

    AramalarAdapter(Context context, ArrayList<DataModel> dataModelArrayList, @NonNull String selection,@NonNull String aranan){
        this.context=context;
        this.dataModel=dataModelArrayList;
        this.aranan=aranan;
        this.selection=selection;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view=LayoutInflater.from(context).inflate(R.layout.arama_sonuclari_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final DataModel dataModel1=dataModel.get(position);
        //final String replacedWith = "<font color='red'>" + aranan + "</font>";
        final String replacedWith = "<span style='background-color:#00FEFE'>" + aranan + "</span>";
        final String modifiedString;
        if(selection.contentEquals(SQLiteDatabaseHelper.SUTUN_1)){
            modifiedString = dataModel1.getIsim().replaceAll(aranan,replacedWith);
            holder.txt_isim.setText(Html.fromHtml(modifiedString));
            if(dataModel1.getKupe_no().length()==0){
                holder.txt_kupe_no.setText(context.getString(R.string.kupe_no_yok));
            }
            else{
                holder.txt_kupe_no.setText(dataModel1.getKupe_no());
            }
        }
        else {
            if(dataModel1.getKupe_no().length()==0){
                holder.txt_kupe_no.setText(context.getString(R.string.kupe_no_yok));
            }
            else{
                modifiedString = dataModel1.getKupe_no().replaceAll(aranan,replacedWith);
                holder.txt_kupe_no.setText(Html.fromHtml(modifiedString));
            }
            holder.txt_isim.setText(dataModel1.getIsim());
        }
        date.setTime(Long.parseLong(dataModel1.getTohumlama_tarihi()));
        holder.txt_tarih1.setText(dateFormat.format(date));
        date.setTime(Long.parseLong(dataModel1.getDogum_tarihi()));
        holder.txt_tarih2.setText(dateFormat.format(date));
        HayvanDuzenleyici.set_text(context,dataModel1.getIs_evcilhayvan(),Integer.parseInt(dataModel1.getTur()),holder.txt_tur);
        final File gorselFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),dataModel1.getFotograf_isim());
        if(gorselFile.exists()&&gorselFile.isFile()){
            Glide.with(context).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
        }
        else{
            HayvanDuzenleyici.set_img(context,dataModel1.getIs_evcilhayvan(),Integer.parseInt(dataModel1.getTur()),holder.img_animal);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle data=new Bundle();
                data.putInt("ID",dataModel1.getId());
                Intent intent=new Intent(context,ActivityDetails.class);
                intent.putExtras(data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }


    static class CustomViewHolder extends RecyclerView.ViewHolder {

        final TextView txt_tur;
        final TextView txt_isim;
        final TextView txt_tarih1;
        final TextView txt_tarih2;
        final TextView txt_kupe_no;
        final ImageView img_animal;

        private CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            txt_tur=itemView.findViewById(R.id.txt_tur);
            txt_isim=itemView.findViewById(R.id.txt_isim);
            txt_tarih1=itemView.findViewById(R.id.txt_tarih1);
            txt_tarih2=itemView.findViewById(R.id.txt_tarih2);
            txt_kupe_no=itemView.findViewById(R.id.txt_kupe_no);
        }
    }
}
