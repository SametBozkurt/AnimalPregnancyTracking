package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.util.ArrayList;

public class DuzenleAdapter extends RecyclerView.Adapter<DuzenleAdapter.CustomViewHolder> {

    private final Context mContext;
    private final ArrayList<DataModel> dataModelArrayList;
    private final SQLiteDatabaseHelper databaseHelper;

    DuzenleAdapter(Context context,@Nullable String orderClause){
        this.mContext=context;
        databaseHelper=SQLiteDatabaseHelper.getInstance(context);
        this.dataModelArrayList=databaseHelper.getAllData(orderClause);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.duzenle_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        final DataModel dataModel=dataModelArrayList.get(position);
        holder.txt_isim.setText(new StringBuilder(dataModel.getIsim()));
        holder.button_duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataModel.getDogum_grcklsti()==0){
                    final Intent data=new Intent(mContext,ActivityEdit.class);
                    final Bundle veri_paketi=new Bundle();
                    veri_paketi.putInt("kayit_id",dataModel.getId());
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
                databaseHelper.girdiSil(dataModel.getId());
                dataModelArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,dataModelArrayList.size());
            }
        });
        final File gorselFile=new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),dataModel.getFotograf_isim());
        if(gorselFile.exists()&&gorselFile.isFile()){
            holder.txt_isim.setTextColor(Color.WHITE);
            holder.img_animal.setColorFilter(Color.TRANSPARENT);
            Glide.with(mContext).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
        }
        else{
            holder.txt_isim.setTextColor(Color.parseColor("#37474f"));
            holder.img_animal.setColorFilter(Color.parseColor("#2196F3"));
            HayvanDuzenleyici.set_img(mContext,dataModel.getIs_evcilhayvan(),Integer.parseInt(dataModel.getTur()),holder.img_animal);
        }
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        final TextView txt_isim;
        final FloatingActionButton button_duzenle;
        final FloatingActionButton button_sil;
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
