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

public class GerceklesenlerAdapter extends RecyclerView.Adapter<GerceklesenlerAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    private HayvanDuzenleyici hayvanDuzenleyici;

    GerceklesenlerAdapter(Context context, ArrayList<HayvanVeriler> hayvanVerilerArrayList){
        this.context=context;
        this.hayvanVerilerArrayList=hayvanVerilerArrayList;
        this.hayvanDuzenleyici=new HayvanDuzenleyici(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.kayitlar_adapter,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        HayvanVeriler mHayvanVeriler=hayvanVerilerArrayList.get(position);
        holder.textView.setText(mHayvanVeriler.getIsim());
        if(mHayvanVeriler.getFotograf_isim().length()!=0){
            File gorselFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),mHayvanVeriler.getFotograf_isim());
            Glide.with(context).load(Uri.fromFile(gorselFile)).into(holder.img_animal);
        }
        else if(mHayvanVeriler.getFotograf_isim()==null||mHayvanVeriler.getFotograf_isim().length()==0){
            hayvanDuzenleyici.set_img(mHayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(mHayvanVeriler.getTur()),holder.img_animal);
        }
    }

    @Override
    public int getItemCount() {
        return hayvanVerilerArrayList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView img_animal;

        CustomViewHolder(View itemView) {
            super(itemView);
            img_animal=itemView.findViewById(R.id.img_hayvan);
            textView=itemView.findViewById(R.id.text);
        }
    }

}
