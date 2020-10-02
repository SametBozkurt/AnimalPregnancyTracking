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

    private final ArrayList<DataModel> dataModelArrayList;
    private final Context context;
    private final int code;
    private final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.getDefault());
    private final Date date=new Date();

    KayitlarAdapter(Context context,ArrayList<DataModel> arrayList,int code){
        this.context=context;
        this.dataModelArrayList=arrayList;
        this.code=code;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view=LayoutInflater.from(context).inflate(R.layout.kayitlar_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
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
        final File gorselFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),dataModel.getFotograf_isim());
        if(gorselFile.exists()&&gorselFile.isFile()){
            Glide.with(context).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
        }
        else{
            HayvanDuzenleyici.set_img(context,dataModel.getIs_evcilhayvan(),Integer.parseInt(dataModel.getTur()),holder.img_animal);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle data=new Bundle();
                data.putInt("ID",dataModel.getId());
                Intent intent=new Intent(context,ActivityDetails.class);
                intent.putExtras(data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
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