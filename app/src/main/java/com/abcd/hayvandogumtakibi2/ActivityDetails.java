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
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityDetails extends AppCompatActivity implements CalendarTools {

    private HayvanVeriler hayvanVeriler;
    private static final long DAY_IN_MILLIS = 1000*60*60*24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final Bundle bundle = getIntent().getExtras();
        hayvanVeriler=SQLiteDatabaseHelper.getInstance(this).getDataById(bundle.getInt("ID"));
        Date date_dollenme, date_dogum;
        date_dollenme=new Date();
        date_dogum=new Date();
        date_dollenme.setTime(Long.parseLong(hayvanVeriler.getTohumlama_tarihi()));
        date_dogum.setTime(Long.parseLong(hayvanVeriler.getDogum_tarihi()));
        DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        ImageView imageView = findViewById(R.id.img_hayvan);
        TextView txt_isim = findViewById(R.id.txt_isim);
        TextView txt_kupe_no = findViewById(R.id.txt_kupe_no);
        TextView txt_tur = findViewById(R.id.txt_tur);
        TextView txt_tarih1 = findViewById(R.id.txt_tarih1);
        TextView txt_tarih2 = findViewById(R.id.txt_tarih2);
        TextView txt_kalan = findViewById(R.id.txt_kalan_gun);
        ImageView icon_edit = findViewById(R.id.btn_edit);
        if(hayvanVeriler.getFotograf_isim().length()!=0){
            File gorselFile=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler.getFotograf_isim());
            Glide.with(this).load(Uri.fromFile(gorselFile)).into(imageView);
        }
        else if(hayvanVeriler.getFotograf_isim()==null||hayvanVeriler.getFotograf_isim().length()==0){
            HayvanDuzenleyici.set_img(this,hayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler.getTur()), imageView);
        }
        txt_isim.setText(hayvanVeriler.getIsim());
        if(hayvanVeriler.getKupe_no()==null||hayvanVeriler.getKupe_no().length()==0){
            txt_kupe_no.setText(new StringBuilder(getString(R.string.kupe_no_yok)));
        }
        else{
            txt_kupe_no.setText(new StringBuilder(hayvanVeriler.getKupe_no()));
        }
        txt_tarih1.setText(dateFormat.format(date_dollenme));
        HayvanDuzenleyici.set_text(this,hayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler.getTur()), txt_tur);
        if(hayvanVeriler.getDogum_tarihi()==null||hayvanVeriler.getDogum_tarihi().length()==0){
            txt_tarih2.setText(getString(R.string.text_NA));
            txt_kalan.setText(getString(R.string.text_NA));
        }
        else{
            txt_tarih2.setText(dateFormat.format(date_dogum));
            if(get_gun_sayisi(Long.parseLong(hayvanVeriler.getDogum_tarihi()))>=0){
                txt_kalan.setText(new StringBuilder(String.valueOf(get_gun_sayisi(Long.parseLong(hayvanVeriler.getDogum_tarihi())))));
            }
            else{
                txt_kalan.setText(getString(R.string.text_NA));
            }
        }
        icon_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hayvanVeriler.getDogum_grcklsti()==0){
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
                else{
                    Snackbar.make(findViewById(R.id.main_layout),R.string.edit_blocked_msg,5000).show();
                }
            }
        });
        if(get_gun_sayisi(Long.parseLong(hayvanVeriler.getDogum_tarihi()))<0){
            if(hayvanVeriler.getDogum_grcklsti()==0){
                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.main_layout),R.string.dogum_gerceklesti_uyarı, 8000);
                mySnackbar.setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteDatabaseHelper.getInstance(ActivityDetails.this).isaretle_dogum_gerceklesti(hayvanVeriler.getId());
                    }
                });
                mySnackbar.setActionTextColor(getResources().getColor(R.color.action_color));
                mySnackbar.show();
            }
        }
    }

    @Override
    public void oto_tarih_hesapla(Date date) {}

    @Override
    public int get_gun_sayisi(long dogum_tarihi_in_millis) {
        long gun=(dogum_tarihi_in_millis-Calendar.getInstance().getTimeInMillis())/DAY_IN_MILLIS;
        return (int)gun;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
