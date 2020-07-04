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
    private final HayvanDuzenleyici hayvanDuzenleyici;

    PeriodsAdapter(Context context){
        this.context=context;
        arrayList_all_species = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.all_species)));
        hayvanDuzenleyici=new HayvanDuzenleyici(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.periods_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        hayvanDuzenleyici.set_text(3,position,holder.txt_gun);
        hayvanDuzenleyici.set_img(3,position,holder.img_animal);
    }

    @Override
    public int getItemCount() {
        return arrayList_all_species.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        final TextView txt_gun;
        final ImageView img_animal;

        private CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            txt_gun=itemView.findViewById(R.id.txt_gun);
        }
    }
}
