package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DuzenleAdapter extends RecyclerView.Adapter<DuzenleAdapter.CustomViewHolder> {

    private final Context mContext;
    private final ArrayList<DataModel> dataModelArrayList;
    private final SQLiteDatabaseHelper databaseHelper;
    private final int code;
    private final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    private final Date date=new Date();

    DuzenleAdapter(final Context context, int code, @Nullable String selectionClause, @Nullable String orderClause){
        this.mContext=context;
        databaseHelper=SQLiteDatabaseHelper.getInstance(context);
        this.dataModelArrayList=databaseHelper.getAllData(selectionClause,orderClause);
        this.code=code;
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
        holder.textView.setText(new StringBuilder(dataModel.getIsim()));
        switch(code){
            case 0:
                holder.textView.setText(dataModel.getIsim());
                break;
            case 1:
                if(dataModel.getKupe_no()==null||dataModel.getKupe_no().length()==0){
                    holder.textView.setText(mContext.getString(R.string.kupe_no_yok));
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
        final FrameLayout.LayoutParams imageView_layoutParams;
        if(gorselFile.exists()&&gorselFile.isFile()){
            imageView_layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            holder.img_animal.setLayoutParams(imageView_layoutParams);
            holder.textView.setTextColor(Color.WHITE);
            holder.img_animal.setColorFilter(Color.TRANSPARENT);
            Glide.with(mContext).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
        }
        else{
            imageView_layoutParams=new FrameLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.image_size),
                    mContext.getResources().getDimensionPixelSize(R.dimen.image_size));
            imageView_layoutParams.gravity= Gravity.CENTER;
            holder.img_animal.setLayoutParams(imageView_layoutParams);
            holder.textView.setTextColor(Color.parseColor("#37474f"));
            holder.img_animal.setColorFilter(Color.parseColor("#2196F3"));
            HayvanDuzenleyici.set_img(mContext,dataModel.getIs_evcilhayvan(),Integer.parseInt(dataModel.getTur()),holder.img_animal);
        }
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        final TextView textView;
        final FloatingActionButton button_duzenle;
        final FloatingActionButton button_sil;
        final ImageView img_animal;

        CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            textView=itemView.findViewById(R.id.txt_isim);
            button_duzenle=itemView.findViewById(R.id.button_edit);
            button_sil=itemView.findViewById(R.id.button_del);
        }
    }
}
