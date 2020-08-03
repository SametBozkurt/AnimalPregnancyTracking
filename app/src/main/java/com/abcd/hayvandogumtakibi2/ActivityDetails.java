package com.abcd.hayvandogumtakibi2;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

    private HayvanVeriler hayvanVeriler;
    private static final long DAY_IN_MILLIS = 1000*60*60*24;
    private AdView adView;
    private FrameLayout adContainerView;
    //private static final String BANNER_AD_UNIT_ID = "ca-app-pub-9721232821183013/8246180827";
    private static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        final ConstraintLayout parent_layout=findViewById(R.id.parent_layout);
        final ImageView imageView = findViewById(R.id.img_hayvan);
        final TextView txt_isim = findViewById(R.id.txt_isim);
        final TextView txt_kupe_no = findViewById(R.id.txt_kupe_no);
        final TextView txt_tur = findViewById(R.id.txt_tur);
        final TextView txt_tarih1 = findViewById(R.id.txt_tarih1);
        final TextView txt_tarih2 = findViewById(R.id.txt_tarih2);
        final TextView txt_kalan = findViewById(R.id.txt_kalan_gun);
        final ImageView icon_edit = findViewById(R.id.btn_edit);
        adContainerView=findViewById(R.id.ad_view_container);
        final Bundle bundle = getIntent().getExtras();
        final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        hayvanVeriler=databaseHelper.getDataById(bundle.getInt("ID"));
        final Date date_dollenme=new Date(), date_dogum=new Date();
        date_dollenme.setTime(Long.parseLong(hayvanVeriler.getTohumlama_tarihi()));
        date_dogum.setTime(Long.parseLong(hayvanVeriler.getDogum_tarihi()));
        parent_layout.post(new Runnable() {
            @Override
            public void run() {
                if(hayvanVeriler.getFotograf_isim().length()!=0){
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
                if(hayvanVeriler.getDogum_grcklsti()==0){
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
                    Snackbar.make(findViewById(R.id.main_layout),R.string.edit_blocked_msg,5000).show();
                }
            }
        });
        if(get_gun_sayisi(Long.parseLong(hayvanVeriler.getDogum_tarihi()))<0){
            if(hayvanVeriler.getDogum_grcklsti()==0){
                final Snackbar mySnackbar = Snackbar.make(findViewById(R.id.main_layout),R.string.dogum_gerceklesti_uyarı, 8000);
                mySnackbar.setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseHelper.isaretle_dogum_gerceklesti(hayvanVeriler.getId());
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
        return AdSize.getCurrentOrientationBannerAdSizeWithWidth(this, adWidth);
    }

}
