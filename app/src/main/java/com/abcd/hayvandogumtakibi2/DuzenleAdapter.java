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

    private static final String TUR_0="0"; //İNEK
    private static final String TUR_1="1"; //KOYUN
    private static final String TUR_2="2";//KEÇİ
    private static final String TUR_3="3"; //KEDİ
    private static final String TUR_4="4"; //KÖPEK
    private static final String TUR_5="5";//HAMSTER
    private static final String TUR_6="6";//DIGER
    Context mContext;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    SQLiteDatabaseHelper databaseHelper;

    public DuzenleAdapter(Context context, ArrayList<HayvanVeriler> arrayList){
        this.mContext=context;
        this.hayvanVerilerArrayList=arrayList;
        databaseHelper=new SQLiteDatabaseHelper(context);
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
            Glide.with(mContext).
                    load(Uri.fromFile(new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler.getFotograf_isim()))).
                    into(holder.img_animal);
            switch(hayvanVeriler.getTur()){
                case TUR_0:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_0)));
                    break;
                case TUR_1:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_1)));
                    break;
                case TUR_2:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_2)));
                    break;
                case TUR_3:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_3)));
                    break;
                case TUR_4:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_4)));
                    break;
                case TUR_5:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_5)));
                    break;
                case TUR_6:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_6)));
                    break;
            }
        }
        else{
            switch(hayvanVeriler.getTur()){
                case TUR_0:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_0)));
                    Glide.with(mContext).load(R.mipmap.cow).into(holder.img_animal);
                    break;
                case TUR_1:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_1)));
                    Glide.with(mContext).load(R.mipmap.sheep).into(holder.img_animal);
                    break;
                case TUR_2:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_2)));
                    Glide.with(mContext).load(R.mipmap.goat).into(holder.img_animal);
                    break;
                case TUR_3:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_3)));
                    Glide.with(mContext).load(R.mipmap.cat).into(holder.img_animal);
                    break;
                case TUR_4:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_4)));
                    Glide.with(mContext).load(R.mipmap.dog).into(holder.img_animal);
                    break;
                case TUR_5:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_5)));
                    Glide.with(mContext).load(R.mipmap.hamster).into(holder.img_animal);
                    break;
                case TUR_6:
                    holder.txt_tur.setText(new StringBuilder(mContext.getString(R.string.listView_tur))
                            .append(mContext.getString(R.string.tur_6)));
                    Glide.with(mContext).load(R.mipmap.interrogation_mark).into(holder.img_animal);
                    break;
            }
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
