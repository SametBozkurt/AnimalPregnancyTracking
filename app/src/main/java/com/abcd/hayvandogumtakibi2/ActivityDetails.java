package com.abcd.hayvandogumtakibi2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityDetails extends AppCompatActivity {
    private Date bugun,dogum;
    private SimpleDateFormat date_formatter=new SimpleDateFormat("dd/MM/yyyy");
    private HayvanVeriler hayvanVeriler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle bundle = getIntent().getExtras();
        hayvanVeriler=new SQLiteDatabaseHelper(this).getDataById(bundle.getInt("ID"));
        ImageView imageView = findViewById(R.id.img_hayvan);
        TextView txt_isim = findViewById(R.id.txt_isim);
        TextView txt_kupe_no = findViewById(R.id.txt_kupe_no);
        TextView txt_tur = findViewById(R.id.txt_tur);
        TextView txt_tarih1 = findViewById(R.id.txt_tarih1);
        TextView txt_tarih2 = findViewById(R.id.txt_tarih2);
        TextView txt_kalan = findViewById(R.id.txt_kalan_gun);
        ImageView icon_edit = findViewById(R.id.btn_edit);
        HayvanDuzenleyici hayvanDuzenleyici = new HayvanDuzenleyici(this);
        if(hayvanVeriler.getFotograf_isim().length()!=0){
            File gorselFile=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler.getFotograf_isim());
            Glide.with(this).load(Uri.fromFile(gorselFile)).into(imageView);
        }
        else if(hayvanVeriler.getFotograf_isim()==null||hayvanVeriler.getFotograf_isim().length()==0){
            hayvanDuzenleyici.set_img(hayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler.getTur()), imageView);
        }
        txt_isim.setText(hayvanVeriler.getIsim());
        if(hayvanVeriler.getKupe_no()==null||hayvanVeriler.getKupe_no().length()==0){
            txt_kupe_no.setText(new StringBuilder(getString(R.string.kupe_no_yok)));
        }
        else{
            txt_kupe_no.setText(new StringBuilder(hayvanVeriler.getKupe_no()));
        }
        txt_tarih1.setText(new StringBuilder(hayvanVeriler.getTohumlama_tarihi()));
        hayvanDuzenleyici.set_text(hayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler.getTur()), txt_tur);
        if(hayvanVeriler.getDogum_tarihi()==null||hayvanVeriler.getDogum_tarihi().length()==0){
            txt_tarih2.setText(getString(R.string.text_NA));
            txt_kalan.setText(getString(R.string.text_NA));
        }
        else{
            txt_tarih2.setText(new StringBuilder(hayvanVeriler.getDogum_tarihi()));
            if(get_gun_sayisi()>=0){
                txt_kalan.setText(new StringBuilder(String.valueOf(get_gun_sayisi())));
            }
            else{
                txt_kalan.setText(getString(R.string.text_NA));
            }
        }
        icon_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data=new Intent(ActivityDetails.this,ActivityEdit.class);
                Bundle veri_paketi=new Bundle();
                veri_paketi.putInt("kayit_id",hayvanVeriler.getId());
                veri_paketi.putCharSequence("kayit_isim",hayvanVeriler.getIsim());
                veri_paketi.putCharSequence("kayit_kupe_no",hayvanVeriler.getKupe_no());
                veri_paketi.putCharSequence("kayit_tur",hayvanVeriler.getTur());
                veri_paketi.putCharSequence("kayit_tarih1",hayvanVeriler.getTohumlama_tarihi());
                veri_paketi.putCharSequence("kayit_tarih2",hayvanVeriler.getDogum_tarihi());
                veri_paketi.putCharSequence("kayit_gorsel_isim",hayvanVeriler.getFotograf_isim());
                veri_paketi.putInt("isPet",hayvanVeriler.getIs_evcilhayvan());
                veri_paketi.putInt("dogumGrcklsti",hayvanVeriler.getDogum_grcklsti());
                data.putExtras(veri_paketi);
                startActivity(data);
            }
        });
        if(get_gun_sayisi()<0){
            if(hayvanVeriler.getDogum_grcklsti()==0){
                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.main_layout),R.string.dogum_gerceklesti_uyarı, 8000);
                mySnackbar.setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SQLiteDatabaseHelper(ActivityDetails.this).isaretle_dogum_gerceklesti(hayvanVeriler.getId());
                    }
                });
                mySnackbar.show();
            }
        }
    }

    private int get_gun_sayisi(){
        Calendar takvim= Calendar.getInstance();
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
        try {
            dogum=date_formatter.parse(hayvanVeriler.getDogum_tarihi());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long fark_ms=dogum.getTime()-bugun.getTime();
        long gun_sayisi=(fark_ms/(1000*60*60*24));
        return (int)gun_sayisi;
    }

}
