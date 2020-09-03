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
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.util.ArrayList;

public class DuzenleAdapter extends RecyclerView.Adapter<DuzenleAdapter.CustomViewHolder> {

    private final Context mContext;
    private final ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    private final SQLiteDatabaseHelper databaseHelper;

    DuzenleAdapter(Context context, ArrayList<HayvanVeriler> arrayList){
        this.mContext=context;
        this.hayvanVerilerArrayList=arrayList;
        databaseHelper=SQLiteDatabaseHelper.getInstance(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.duzenle_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        final HayvanVeriler hayvanVeriler=hayvanVerilerArrayList.get(position);
        holder.txt_isim.setText(new StringBuilder(hayvanVeriler.getIsim()));
        holder.button_duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hayvanVeriler.getDogum_grcklsti()==0){
                    final Intent data=new Intent(mContext,ActivityEdit.class);
                    final Bundle veri_paketi=new Bundle();
                    veri_paketi.putInt("kayit_id",hayvanVeriler.getId());
                    veri_paketi.putCharSequence("kayit_isim",hayvanVeriler.getIsim());
                    veri_paketi.putCharSequence("kayit_kupe_no",hayvanVeriler.getKupe_no());
                    veri_paketi.putCharSequence("kayit_tur",hayvanVeriler.getTur());
                    veri_paketi.putCharSequence("kayit_tarih1",hayvanVeriler.getTohumlama_tarihi());
                    veri_paketi.putCharSequence("kayit_tarih2",hayvanVeriler.getDogum_tarihi());
                    veri_paketi.putCharSequence("kayit_gorsel_isim",hayvanVeriler.getFotograf_isim());
                    veri_paketi.putInt("isPet",hayvanVeriler.getIs_evcilhayvan());
                    veri_paketi.putInt("dogumGrcklsti",hayvanVeriler.getDogum_grcklsti());
                    data.putExtras(veri_paketi);
                    mContext.startActivity(data);
                }
                else{
                    Snackbar.make(holder.itemView,R.string.edit_blocked_msg,4000).show();
                }
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
        final File gorselFile=new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler.getFotograf_isim());
        if(gorselFile.exists()&&gorselFile.isFile()){
            Glide.with(mContext).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
        }
        else{
            HayvanDuzenleyici.set_img(mContext,hayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler.getTur()),holder.img_animal);
        }
    }

    @Override
    public int getItemCount() {
        return hayvanVerilerArrayList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        final TextView txt_isim;
        final Button button_duzenle;
        final Button button_sil;
        final ImageView img_animal;

        CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            txt_isim=itemView.findViewById(R.id.txt_isim);
            button_duzenle=itemView.findViewById(R.id.button_edit);
            button_sil=itemView.findViewById(R.id.button_del);
        }
    }
}
