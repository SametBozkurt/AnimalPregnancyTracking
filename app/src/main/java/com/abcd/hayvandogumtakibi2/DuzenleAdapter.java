package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.ArrayList;

public class DuzenleAdapter extends RecyclerView.Adapter<DuzenleAdapter.CustomViewHolder> {

    Context mContext;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    SQLiteDatabaseHelper databaseHelper;
    HayvanDuzenleyici hayvanDuzenleyici;

    public DuzenleAdapter(Context context, ArrayList<HayvanVeriler> arrayList){
        this.mContext=context;
        this.hayvanVerilerArrayList=arrayList;
        databaseHelper=new SQLiteDatabaseHelper(context);
        hayvanDuzenleyici=new HayvanDuzenleyici(context);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.duzenle_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        final HayvanVeriler hayvanVeriler=hayvanVerilerArrayList.get(position);
        holder.txt_isim.setText(new StringBuilder(hayvanVeriler.getIsim()));
        if(hayvanVeriler.getKupe_no().length()==0){
            holder.txt_kupe_no.setText(new StringBuilder(mContext.getString(R.string.listview_kupe_no)).append(mContext.getString(R.string.kupe_no_yok)));
        }
        else{
            holder.txt_kupe_no.setText(new StringBuilder(mContext.getString(R.string.listview_kupe_no)).append(hayvanVeriler.getKupe_no()));
        }
        hayvanDuzenleyici.set_text(hayvanVeriler.getIs_evcilhayvan(),Integer.valueOf(hayvanVeriler.getTur()),holder.txt_tur);
        holder.txt_tarih1.setText(new StringBuilder(mContext.getString(R.string.listView_tarih1)).append(hayvanVeriler.getTohumlama_tarihi()));
        holder.txt_tarih2.setText(new StringBuilder(mContext.getString(R.string.listView_tarih2)).append(hayvanVeriler.getDogum_tarihi()));
        holder.button_duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data=new Intent(mContext,ActivityEdit.class);
                Bundle veri_paketi=new Bundle();
                veri_paketi.putInt("kayit_id",hayvanVeriler.getId());
                veri_paketi.putCharSequence("kayit_isim",hayvanVeriler.getIsim());
                veri_paketi.putCharSequence("kayit_kupe_no",hayvanVeriler.getKupe_no());
                veri_paketi.putCharSequence("kayit_tur",hayvanVeriler.getTur());
                veri_paketi.putCharSequence("kayit_tarih1",hayvanVeriler.getTohumlama_tarihi());
                veri_paketi.putCharSequence("kayit_tarih2",hayvanVeriler.getDogum_tarihi());
                veri_paketi.putCharSequence("kayit_gorsel_isim",hayvanVeriler.getFotograf_isim());
                veri_paketi.putInt("isPet",hayvanVeriler.getIs_evcilhayvan());
                data.putExtras(veri_paketi);
                mContext.startActivity(data);

            }
        });
        holder.button_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.girdiSil(hayvanVeriler.getId());
                hayvanVerilerArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,hayvanVerilerArrayList.size());
            }
        });
        if(hayvanVeriler.getFotograf_isim().length()!=0){
            Glide.with(mContext).load(Uri.fromFile(new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler.getFotograf_isim()))).into(holder.img_animal);
        }
        else{
            hayvanDuzenleyici.set_img(hayvanVeriler.getIs_evcilhayvan(),Integer.valueOf(hayvanVeriler.getTur()),holder.img_animal);
        }
    }

    @Override
    public int getItemCount() {
        return hayvanVerilerArrayList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView txt_tur,txt_isim,txt_tarih1,txt_tarih2,txt_kupe_no;
        Button button_duzenle,button_sil;
        ImageView img_animal;

        public CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            txt_tur=itemView.findViewById(R.id.txt_tur);
            txt_isim=itemView.findViewById(R.id.txt_isim);
            txt_tarih1=itemView.findViewById(R.id.txt_tarih1);
            txt_tarih2=itemView.findViewById(R.id.txt_tarih2);
            txt_kupe_no=itemView.findViewById(R.id.txt_kupe_no);
            button_duzenle=itemView.findViewById(R.id.button_edit);
            button_sil=itemView.findViewById(R.id.button_del);
        }
    }
}
