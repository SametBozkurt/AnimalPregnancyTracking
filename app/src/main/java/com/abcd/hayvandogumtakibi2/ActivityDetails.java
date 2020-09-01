package com.abcd.hayvandogumtakibi2;

import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityDetails extends AppCompatActivity implements CalendarTools {

    private Boolean isOtherDatesShown=false;
    private HayvanVeriler hayvanVeriler;
    private static final long DAY_IN_MILLIS = 1000*60*60*24;
    int dogum_gerceklesti=0;
    private AdView adView;
    private FrameLayout adContainerView, info_container;
    private LinearLayout linearLayout;
    private RelativeLayout parent_layout;
    private int screen_width=0;
    //private static final String BANNER_AD_UNIT_ID = "ca-app-pub-9721232821183013/8246180827";
    private static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        parent_layout=findViewById(R.id.parent_layout);
        adContainerView=findViewById(R.id.ad_view_container);
        info_container=findViewById(R.id.info_container);
        linearLayout=findViewById(R.id.linear_layout);
        final ImageView imageView = findViewById(R.id.img_hayvan);
        final TextView txt_isim = findViewById(R.id.txt_isim);
        final TextView txt_kupe_no = findViewById(R.id.txt_kupe_no);
        final TextView txt_tur = findViewById(R.id.txt_tur);
        final TextView txt_tarih1 = findViewById(R.id.txt_tarih1);
        final TextView txt_tarih2 = findViewById(R.id.txt_tarih2);
        final TextView txt_kalan = findViewById(R.id.txt_kalan_gun);
        final ImageView icon_edit = findViewById(R.id.btn_edit);
        final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        final Bundle bundle = getIntent().getExtras();
        final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        final Date date_dollenme=new Date(), date_dogum=new Date();
        hayvanVeriler=databaseHelper.getDataById(bundle.getInt("ID"));
        dogum_gerceklesti=hayvanVeriler.getDogum_grcklsti();
        date_dollenme.setTime(Long.parseLong(hayvanVeriler.getTohumlama_tarihi()));
        date_dogum.setTime(Long.parseLong(hayvanVeriler.getDogum_tarihi()));
        parent_layout.post(new Runnable() {
            @Override
            public void run() {
                if(!hayvanVeriler.getFotograf_isim().isEmpty()){
                    final File gorselFile=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler.getFotograf_isim());
                    Glide.with(ActivityDetails.this).load(Uri.fromFile(gorselFile)).into(imageView);
                }
                else if(hayvanVeriler.getFotograf_isim()==null||hayvanVeriler.getFotograf_isim().length()==0){
                    HayvanDuzenleyici.set_img(ActivityDetails.this,hayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler.getTur()), imageView);
                }
            }
        });
        txt_isim.setText(hayvanVeriler.getIsim());
        if(hayvanVeriler.getKupe_no()==null||hayvanVeriler.getKupe_no().length()==0){
            txt_kupe_no.setText(new StringBuilder(getString(R.string.kupe_no_yok)));
        }
        else{
            txt_kupe_no.setText(new StringBuilder(hayvanVeriler.getKupe_no()));
        }
        txt_tarih1.setText(dateFormat.format(date_dollenme));
        HayvanDuzenleyici.set_text(this,hayvanVeriler.getIs_evcilhayvan(),Integer.parseInt(hayvanVeriler.getTur()), txt_tur);
        parent_layout.post(new Runnable() {
            @Override
            public void run() {
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
            }
        });
        icon_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dogum_gerceklesti==0){
                    final Intent data=new Intent(ActivityDetails.this,ActivityEdit.class);
                    final Bundle veri_paketi=new Bundle();
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
                    Snackbar.make(parent_layout,R.string.edit_blocked_msg,5000).show();
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hayvanVeriler.getFotograf_isim().isEmpty()){
                    final File gorselFile=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),hayvanVeriler.getFotograf_isim());
                    show_image(gorselFile.getAbsolutePath());
                }
            }
        });
        if(get_gun_sayisi(Long.parseLong(hayvanVeriler.getDogum_tarihi()))<0){
            if(hayvanVeriler.getDogum_grcklsti()==0){
                final Snackbar mySnackbar = Snackbar.make(parent_layout,R.string.dogum_gerceklesti_uyarı, 8000);
                mySnackbar.setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseHelper.isaretle_dogum_gerceklesti(hayvanVeriler.getId());
                        dogum_gerceklesti=1;
                    }
                });
                mySnackbar.setActionTextColor(getResources().getColor(R.color.action_color));
                mySnackbar.show();
            }
        }
        final ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null){
            if(networkInfo.isConnected()){
                adContainerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MobileAds.initialize(ActivityDetails.this, new OnInitializationCompleteListener() {
                            @Override
                            public void onInitializationComplete(InitializationStatus initializationStatus) {}
                        });
                        loadBanner();
                    }
                },500);
            }
        }
        parent_layout.post(new Runnable() {
            @Override
            public void run() {
                if(hayvanVeriler.getDogum_tarihi()!=null||!hayvanVeriler.getDogum_tarihi().isEmpty()){
                    final boolean a=hayvanVeriler.getIs_evcilhayvan()==2 && Integer.parseInt(hayvanVeriler.getTur())==0;
                    final boolean b=hayvanVeriler.getIs_evcilhayvan()==0 && Integer.parseInt(hayvanVeriler.getTur())==0;
                    if(a||b){
                        final LayoutInflater inflater=LayoutInflater.from(ActivityDetails.this);
                        final View view=inflater.inflate(R.layout.layout_diger_detaylar,info_container,false);
                        final TextView textView=view.findViewById(R.id.textView);
                        final FrameLayout frameLayout=view.findViewById(R.id.diger_tarihler_container);
                        frameLayout.setAlpha(0f);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!isOtherDatesShown){
                                    frameLayout.animate().alpha(1f).setDuration(200).start();
                                    final LayoutInflater inflater=LayoutInflater.from(ActivityDetails.this);
                                    final View view1=inflater.inflate(R.layout.layout_kizdirma_tarihi,frameLayout,false);
                                    final TextView txt_kizidirma_tarihi=view1.findViewById(R.id.txt_tarih3);
                                    final TextView txt_kuruya_alma=view1.findViewById(R.id.txt_tarih4);
                                    final long date_in_millis=Long.parseLong(hayvanVeriler.getDogum_tarihi());
                                    final Date date = new Date();
                                    date.setTime(TarihHesaplayici.get_kizdirma_tarihi(date_in_millis));
                                    txt_kizidirma_tarihi.setText(dateFormat.format(date));
                                    date.setTime(TarihHesaplayici.get_kuruya_alma_tarihi(date_in_millis));
                                    txt_kuruya_alma.setText(dateFormat.format(date));
                                    frameLayout.addView(view1);
                                    textView.setText(getString(R.string.hide_other_details));
                                    frameLayout.setAlpha(1f);
                                    isOtherDatesShown=true;
                                }
                                else{
                                    frameLayout.animate().alpha(0f).setDuration(200).start();
                                    frameLayout.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            frameLayout.removeAllViews();
                                        }
                                    },210);
                                    textView.setText(getString(R.string.show_other_details));
                                    isOtherDatesShown=false;
                                }
                            }
                        });
                        info_container.addView(view);
                    }
                }
            }
        });
        parent_layout.post(new Runnable() {
            @Override
            public void run() {
                get_width_pixels();
            }
        });
    }

    @Override
    public void oto_tarih_hesapla(Date date) {}

    @Override
    public int get_gun_sayisi(long dogum_tarihi_in_millis) {
        final long gun=(dogum_tarihi_in_millis-System.currentTimeMillis())/DAY_IN_MILLIS;
        return (int)gun;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
            adContainerView.removeAllViews();
            linearLayout.removeAllViews();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    private void loadBanner() {
        adView = new AdView(this);
        adView.setAdUnitId(BANNER_TEST_ID);
        adContainerView.removeAllViews();
        adContainerView.addView(adView);
        final AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        final Display display = getWindowManager().getDefaultDisplay();
        final DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        final float density = outMetrics.density;
        float adWidthPixels = adContainerView.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }
        final int adWidth = (int) (adWidthPixels / density);
        return AdSize.getLandscapeAnchoredAdaptiveBannerAdSize(this,adWidth);
    }

    void show_image(String photo_address){
        final Dialog dialog = new Dialog(this,R.style.ImageDialogStyle);
        dialog.setContentView(R.layout.image_dialog_layout);
        dialog.setCanceledOnTouchOutside(true);
        final ImageView img_animal=dialog.findViewById(R.id.image_animal);
        img_animal.setMinimumHeight(screen_width*3/4);  //Bu işlem dialog boyutunu yatay piksel sayısının
        img_animal.setMaxHeight(screen_width*4/5);      //%75-%80'i boyutunda tutarak dialog boyutunu stabil tutar.
        img_animal.setMinimumWidth(screen_width*3/4);
        img_animal.setMaxWidth(screen_width*4/5);
        Glide.with(this).load(Uri.fromFile(new File(photo_address))).into(img_animal);
        dialog.show();
    }

    void get_width_pixels(){
        final Display display = getWindowManager().getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        screen_width=displayMetrics.widthPixels;
    }

}
