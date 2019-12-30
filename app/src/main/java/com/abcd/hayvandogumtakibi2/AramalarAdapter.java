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

    private static final String TUR_0="0"; //INEK
    private static final String TUR_1="1"; //KOYUN
    private static final String TUR_2="2";//KECI
    private static final String TUR_3="3"; //KEDI
    private static final String TUR_4="4"; //KOPEK
    private static final String TUR_5="5";//HAMSTER
    private static final String TUR_6="6";//DIGER
    private ArrayList<HayvanVeriler> hayvanVeriler;
    private Context context;


    public AramalarAdapter(Context context, ArrayList<HayvanVeriler> hayvanVerilerArrayList){
        this.context=context;
        this.hayvanVeriler=hayvanVerilerArrayList;
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
        if(hayvanVeriler1.getFotograf_isim().length()!=0){
            Glide.with(context)
                    .load(Uri.fromFile(new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler1.getFotograf_isim()))).
                    into(holder.img_animal);
            switch(hayvanVeriler1.getTur()){
                case TUR_0:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getString(R.string.tur_0));
                    break;
                case TUR_1:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getString(R.string.tur_1));
                    break;
                case TUR_2:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getString(R.string.tur_2));
                    break;
                case TUR_3:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getString(R.string.tur_3));
                    break;
                case TUR_4:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getString(R.string.tur_4));
                    break;
                case TUR_5:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getText(R.string.tur_5));
                    break;
                case TUR_6:
                    holder.txt_tur.setText(new StringBuilder(context.getString(R.string.listView_tur))
                            .append(context.getString(R.string.tur_6)));
                    break;
            }
        }
        else{
            switch(hayvanVeriler1.getTur()){
                case TUR_0:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getString(R.string.tur_0));
                    Glide.with(context).load(R.mipmap.cow).into(holder.img_animal);
                    break;
                case TUR_1:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getString(R.string.tur_1));
                    Glide.with(context).load(R.mipmap.sheep).into(holder.img_animal);
                    break;
                case TUR_2:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getString(R.string.tur_2));
                    Glide.with(context).load(R.mipmap.goat).into(holder.img_animal);
                    break;
                case TUR_3:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getString(R.string.tur_3));
                    Glide.with(context).load(R.mipmap.cat).into(holder.img_animal);
                    break;
                case TUR_4:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getString(R.string.tur_4));
                    Glide.with(context).load(R.mipmap.dog).into(holder.img_animal);
                    break;
                case TUR_5:
                    holder.txt_tur.setText(context.getString(R.string.listView_tur)+context.getText(R.string.tur_5));
                    Glide.with(context).load(R.mipmap.hamster).into(holder.img_animal);
                    break;
                case TUR_6:
                    holder.txt_tur.setText(new StringBuilder(context.getString(R.string.listView_tur))
                            .append(context.getString(R.string.tur_6)));
                    Glide.with(context).load(R.mipmap.interrogation_mark).into(holder.img_animal);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return hayvanVeriler.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

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
