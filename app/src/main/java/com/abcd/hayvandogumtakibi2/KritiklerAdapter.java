package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.net.Uri;
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
    private Calendar takvim=Calendar.getInstance();
    private SimpleDateFormat date_formatter=new SimpleDateFormat("dd/MM/yyyy");
    private Date bugun,dogum;
    private HayvanDuzenleyici hayvanDuzenleyici;

    KritiklerAdapter(Context context, ArrayList<HayvanVeriler> hayvanVerilerArrayList1){
        this.context=context;
        this.hayvanVerilerArrayList=hayvanVerilerArrayList1;
        hayvanDuzenleyici=new HayvanDuzenleyici(context);
        degerler();
    }

    private void degerler(){
        String date_bugun=takvim.get(Calendar.DAY_OF_MONTH)+"/"+(takvim.get(Calendar.MONTH)+1)+"/"+takvim.get(Calendar.YEAR);
        try {
            bugun=date_formatter.parse(date_bugun);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.kritikler_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        HayvanVeriler hayvanVeriler=hayvanVerilerArrayList.get(position);
        holder.txt_isim.setText(new StringBuilder(hayvanVeriler.getIsim()));
        if(hayvanVeriler.getKupe_no().length()==0){
            holder.txt_kupe_no.setText(new StringBuilder(context.getString(R.string.listview_kupe_no)).append(context.getString(R.string.kupe_no_yok)));
        }
        else{
            holder.txt_kupe_no.setText(new StringBuilder(context.getString(R.string.listview_kupe_no)).append(hayvanVeriler.getKupe_no()));
        }
        hayvanDuzenleyici.set_text(hayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler.getTur()),holder.txt_tur);
        holder.txt_tarih1.setText(new StringBuilder(context.getString(R.string.listView_tarih1)).append(hayvanVeriler.getTohumlama_tarihi()));
        holder.txt_tarih2.setText(new StringBuilder(context.getString(R.string.listView_tarih2)).append(hayvanVeriler.getDogum_tarihi()));
        String date_dogum=hayvanVeriler.getDogum_tarihi();
        try {
            dogum=date_formatter.parse(date_dogum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long fark_ms=dogum.getTime()-bugun.getTime();
        long gun_sayisi=(fark_ms/(1000*60*60*24));
        if(gun_sayisi<0){
            holder.txt_kalan.setText(new StringBuilder(context.getString(R.string.ozel_listview_kalan_gun_asim)));
            holder.txt_durum.setText(new StringBuilder(context.getString(R.string.listview_durum_tehlikeli)));
        }
        else if(gun_sayisi>0&&gun_sayisi<30){
            holder.txt_kalan.setText(new StringBuilder(context.getString(R.string.ozel_listview_kalan_gun)).append(gun_sayisi));
            holder.txt_durum.setText(new StringBuilder(context.getString(R.string.listview_durum_kritik)));
        }
        else if(gun_sayisi==0){
            holder.txt_kalan.setText(new StringBuilder(context.getString(R.string.ozel_listview_kalan_gun)).append(gun_sayisi));
            holder.txt_durum.setText(new StringBuilder(context.getString(R.string.listview_dogum_uyari_cok_kritik)));
        }
        if(hayvanVeriler.getFotograf_isim().length()!=0){
            Glide.with(context)
                    .load(Uri.fromFile(new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler.getFotograf_isim()))).
                    into(holder.img_animal);
        }
        else{
            hayvanDuzenleyici.set_img(hayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler.getTur()),holder.img_animal);
        }
    }

    @Override
    public int getItemCount() {
        return hayvanVerilerArrayList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView txt_tur,txt_isim,txt_tarih1,txt_tarih2,txt_durum,txt_kalan,txt_kupe_no;
        ImageView img_animal;

        CustomViewHolder(View itemView) {
            super(itemView);
            txt_tur=itemView.findViewById(R.id.txt_tur);
            txt_isim=itemView.findViewById(R.id.txt_isim);
            txt_tarih1=itemView.findViewById(R.id.txt_tarih1);
            txt_tarih2=itemView.findViewById(R.id.txt_tarih2);
            txt_durum=itemView.findViewById(R.id.txt_durum);
            txt_kalan=itemView.findViewById(R.id.txt_kalan_gun);
            txt_kupe_no=itemView.findViewById(R.id.txt_kupe_no);
            img_animal=itemView.findViewById(R.id.img_hayvan);
        }
    }
}
