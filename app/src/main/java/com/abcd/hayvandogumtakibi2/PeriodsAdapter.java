package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;

public class PeriodsAdapter extends RecyclerView.Adapter<PeriodsAdapter.CustomViewHolder> {

    private final Context context;
    private final ArrayList<String> arrayList_all_species;
    private final boolean isListedViewEnabled;

    public PeriodsAdapter(final Context context, boolean isListed){
        this.context=context;
        arrayList_all_species = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.all_species)));
        this.isListedViewEnabled=isListed;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(isListedViewEnabled){
            view=LayoutInflater.from(context).inflate(R.layout.periods_adapter_list_design,parent,false);
        }
        else{
            view=LayoutInflater.from(context).inflate(R.layout.periods_adapter_tile_design,parent,false);
        }
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        HayvanDuzenleyici.set_text(context,3,position,holder.txt_gun);
        HayvanDuzenleyici.set_img(context,3,position,holder.img_animal);
    }

    @Override
    public int getItemCount() {
        return arrayList_all_species.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView txt_gun;
        ImageView img_animal;

        private CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            txt_gun=itemView.findViewById(R.id.txt_gun);
        }
    }
}
