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

public class KayitlarAdapter extends RecyclerView.Adapter<KayitlarAdapter.CustomViewHolder> {


    private ArrayList<HayvanVeriler> hayvanVeriler;
    private Context context;
    private Calendar takvim=Calendar.getInstance();
    private SimpleDateFormat date_formatter=new SimpleDateFormat("dd/MM/yyyy");
    private Date bugun,dogum;

    public KayitlarAdapter(Context context, ArrayList<HayvanVeriler> hayvanVerilerArrayList){
        this.context=context;
        this.hayvanVeriler=hayvanVerilerArrayList;
        degerler();
    }

    private void degerler(){
        int gun,ay,yil;
        gun=takvim.get(Calendar.DAY_OF_MONTH);
        ay=takvim.get(Calendar.MONTH)+1;
        yil=takvim.get(Calendar.YEAR);
        String date_bugun=gun+"/"+ay+"/"+yil;
        try {
            bugun=date_formatter.parse(date_bugun);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.kayitlar_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        HayvanVeriler hayvanVeriler1=hayvanVeriler.get(position);
        holder.txt_isim.setText(new StringBuilder(hayvanVeriler1.getIsim()));
        if(hayvanVeriler1.getKupe_no().length()==0){
            holder.txt_kupe_no.setText(new StringBuilder(context.getString(R.string.listview_kupe_no)).append(context.getString(R.string.kupe_no_yok)));
        }
        else{
            holder.txt_kupe_no.setText(new StringBuilder(context.getString(R.string.listview_kupe_no)).append(hayvanVeriler1.getKupe_no()));
        }
        holder.txt_tarih1.setText(new StringBuilder(context.getString(R.string.listView_tarih1)).append(hayvanVeriler1.getTohumlama_tarihi()));
        holder.txt_tarih2.setText(new StringBuilder(context.getString(R.string.listView_tarih2)).append(hayvanVeriler1.getDogum_tarihi()));
        String date_dogum=hayvanVeriler1.getDogum_tarihi();
        try {
            dogum=date_formatter.parse(date_dogum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(hayvanVeriler1.getFotograf_isim().length()!=0){
            File gorselFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler1.getFotograf_isim());
            Glide.with(context).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
            new HayvanDuzenleyici(context).set_text(hayvanVeriler1.getIs_evcilhayvan(),Integer.valueOf(hayvanVeriler1.getTur()),holder.txt_tur);
        }
        else if(hayvanVeriler1.getFotograf_isim()==null||hayvanVeriler1.getFotograf_isim().length()==0){
            new HayvanDuzenleyici(context).set_text(hayvanVeriler1.getIs_evcilhayvan(),Integer.valueOf(hayvanVeriler1.getTur()),holder.txt_tur);
            new HayvanDuzenleyici(context).set_img(hayvanVeriler1.getIs_evcilhayvan(),Integer.valueOf(hayvanVeriler1.getTur()),holder.img_animal);
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
        else{
            holder.txt_kalan.setText(new StringBuilder(context.getString(R.string.ozel_listview_kalan_gun)).append(gun_sayisi));
            holder.txt_durum.setText(new StringBuilder(context.getString(R.string.listview_durum_normal)));
        }
    }

    @Override
    public int getItemCount() {
        return hayvanVeriler.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView txt_tur,txt_isim,txt_tarih1,txt_tarih2,txt_kalan,txt_durum,txt_kupe_no;
        ImageView img_animal;

        private CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            txt_tur=itemView.findViewById(R.id.txt_tur);
            txt_isim=itemView.findViewById(R.id.txt_isim);
            txt_tarih1=itemView.findViewById(R.id.txt_tarih1);
            txt_tarih2=itemView.findViewById(R.id.txt_tarih2);
            txt_kalan=itemView.findViewById(R.id.txt_kalan_gun);
            txt_durum=itemView.findViewById(R.id.txt_durum);
            txt_kupe_no=itemView.findViewById(R.id.txt_kupe_no);
        }
    }
}
