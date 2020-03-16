package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.net.Uri;
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

public class AramalarAdapter extends RecyclerView.Adapter<AramalarAdapter.CustomViewHolder> {

    private ArrayList<HayvanVeriler> hayvanVeriler;
    private Context context;
    private HayvanDuzenleyici hayvanDuzenleyici;

    AramalarAdapter(Context context, ArrayList<HayvanVeriler> hayvanVerilerArrayList){
        this.context=context;
        this.hayvanVeriler=hayvanVerilerArrayList;
        hayvanDuzenleyici=new HayvanDuzenleyici(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.arama_sonuclari_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        HayvanVeriler hayvanVeriler1=hayvanVeriler.get(position);
        holder.txt_isim.setText(hayvanVeriler1.getIsim());
        holder.txt_kupe_no.setText(context.getString(R.string.listview_kupe_no)+hayvanVeriler1.getKupe_no());
        holder.txt_tarih1.setText(context.getString(R.string.listView_tarih1)+hayvanVeriler1.getTohumlama_tarihi());
        holder.txt_tarih2.setText(context.getString(R.string.listView_tarih2)+hayvanVeriler1.getDogum_tarihi());
        hayvanDuzenleyici.set_text(hayvanVeriler1.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler1.getTur()),holder.txt_tur);
        if(hayvanVeriler1.getFotograf_isim().length()!=0){
            Glide.with(context)
                    .load(Uri.fromFile(new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler1.getFotograf_isim()))).
                    into(holder.img_animal);
        }
        else{
            hayvanDuzenleyici.set_img(hayvanVeriler1.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler1.getTur()),holder.img_animal);
        }
    }

    @Override
    public int getItemCount() {
        return hayvanVeriler.size();
    }


    static class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView txt_tur,txt_isim,txt_tarih1,txt_tarih2,txt_kupe_no;
        ImageView img_animal;

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
