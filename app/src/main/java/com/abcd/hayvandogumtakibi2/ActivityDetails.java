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
import android.widget.Button;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityDetails extends AppCompatActivity {

    private Boolean isOtherFieldsShown=false;
    private DataModel dataModel;
    private static final long DAY_IN_MILLIS = 1000*60*60*24;
    int dogum_gerceklesti=0;
    private AdView adView;
    private TextView txtMarkIt;
    private FrameLayout adContainerView, other_fields_container;
    private LinearLayout linearLayout;
    private RelativeLayout parent_layout;
    private int screen_width=0;
    SQLiteDatabaseHelper databaseHelper;
    //private static final String BANNER_AD_UNIT_ID = "ca-app-pub-9721232821183013/8246180827";
    private static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        parent_layout=findViewById(R.id.parent_layout);
        adContainerView=findViewById(R.id.ad_view_container);
        other_fields_container=findViewById(R.id.diger_detaylar_container);
        linearLayout=findViewById(R.id.linear_layout);
        txtMarkIt=findViewById(R.id.text_mark);
        final LinearLayout layout_mark = findViewById(R.id.mark_layout);
        final ImageView imageView = findViewById(R.id.img_hayvan);
        final ImageView cross = findViewById(R.id.iptal);
        final TextView txt_isim = findViewById(R.id.txt_isim);
        final TextView txt_kupe_no = findViewById(R.id.txt_kupe_no);
        final TextView txt_tur = findViewById(R.id.txt_tur);
        final TextView txt_tarih1 = findViewById(R.id.txt_tarih1);
        final TextView txt_tarih2 = findViewById(R.id.txt_tarih2);
        final TextView txt_tarih2_title = findViewById(R.id.txt_tarih2_title);
        final TextView txt_kalan = findViewById(R.id.txt_kalan_gun);
        final ImageView icon_edit = findViewById(R.id.btn_edit);
        final TextView txtOtherFields=findViewById(R.id.txt_other_details);
        final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        final Bundle bundle = getIntent().getExtras();
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        final Date date_dollenme=new Date(), date_dogum=new Date();
        dataModel=databaseHelper.getDataById(bundle.getInt("ID"));
        dogum_gerceklesti=dataModel.getDogum_grcklsti();
        date_dollenme.setTime(Long.parseLong(dataModel.getTohumlama_tarihi()));
        date_dogum.setTime(Long.parseLong(dataModel.getDogum_tarihi()));
        parent_layout.post(new Runnable() {
            @Override
            public void run() {
                if(dogum_gerceklesti==1){
                    txtMarkIt.setText(getString(R.string.happened_birth));
                }
                final File gorselFile=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),dataModel.getFotograf_isim());
                if(gorselFile.exists()&&gorselFile.isFile()){
                    Glide.with(ActivityDetails.this).load(Uri.fromFile(gorselFile)).into(imageView);
                }
                else{
                    HayvanDuzenleyici.set_img(ActivityDetails.this,dataModel.getIs_evcilhayvan(),Integer.parseInt(dataModel.getTur()), imageView);
                }
            }
        });
        txt_isim.setText(dataModel.getIsim());
        if(dataModel.getKupe_no()==null||dataModel.getKupe_no().length()==0){
            txt_kupe_no.setText(new StringBuilder(getString(R.string.kupe_no_yok)));
        }
        else{
            txt_kupe_no.setText(new StringBuilder(dataModel.getKupe_no()));
        }
        txt_tarih1.setText(dateFormat.format(date_dollenme));
        HayvanDuzenleyici.set_text(this,dataModel.getIs_evcilhayvan(),Integer.parseInt(dataModel.getTur()), txt_tur);
        parent_layout.post(new Runnable() {
            @Override
            public void run() {
                if(dataModel.getDogum_tarihi()==null||dataModel.getDogum_tarihi().isEmpty()){
                    txt_tarih2.setText(getString(R.string.text_NA));
                    txt_kalan.setText(getString(R.string.text_NA));
                }
                else if(dataModel.getDogum_grcklsti()==1){
                    txt_kalan.setText(getString(R.string.text_NA));
                    txt_tarih2_title.setText(getString(R.string.hint_dateOfBreeding));
                    txt_tarih2.setText(dateFormat.format(date_dogum));
                }
                else{
                    txt_tarih2.setText(dateFormat.format(date_dogum));
                    if(get_gun_sayisi(Long.parseLong(dataModel.getDogum_tarihi()))>=0){
                        txt_kalan.setText(new StringBuilder(String.valueOf(get_gun_sayisi(Long.parseLong(dataModel.getDogum_tarihi())))));
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
                    veri_paketi.putInt("kayit_id",dataModel.getId());
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
                final File gorselFile=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),dataModel.getFotograf_isim());
                if(gorselFile.exists()&&gorselFile.isFile()){
                    show_image(gorselFile.getAbsolutePath());
                }
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        layout_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dogum_gerceklesti==0){
                    final Dialog dialog_warn=new Dialog(ActivityDetails.this,R.style.ImageDialogStyle);
                    dialog_warn.setContentView(R.layout.dialog_dogrulama_dgm_grcklsti);
                    final Button btn_ok=dialog_warn.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final long tahmini_dogum_tarihi=Long.parseLong(dataModel.getDogum_tarihi());
                            databaseHelper.isaretle_dogum_gerceklesti(dataModel.getId(),System.currentTimeMillis(),tahmini_dogum_tarihi);
                            dogum_gerceklesti=1;
                            txtMarkIt.setText(getString(R.string.happened_birth));
                            dialog_warn.dismiss();
                        }
                    });
                   dialog_warn.show();
                }
            }
        });
        txtOtherFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOtherFieldsShown){
                    isOtherFieldsShown=true;
                    final boolean a=dataModel.getIs_evcilhayvan()==2 && Integer.parseInt(dataModel.getTur())==0;
                    final boolean b=dataModel.getIs_evcilhayvan()==0 && Integer.parseInt(dataModel.getTur())==0;
                    final LayoutInflater inflater=LayoutInflater.from(ActivityDetails.this);
                    other_fields_container.setAlpha(0f);
                    View view;
                    if(a||b){
                        view=inflater.inflate(R.layout.lyt_dgr_dtylar2,other_fields_container,false);
                        final TextView txt_kizidirma_tarihi=view.findViewById(R.id.txt_tarih3);
                        final TextView txt_kuruya_alma=view.findViewById(R.id.txt_tarih4);
                        final TextView txt_sperma=view.findViewById(R.id.txt_sperma);
                        final long date_in_millis=Long.parseLong(dataModel.getDogum_tarihi());
                        final Date date = new Date();
                        TarihHesaplayici tarihHesaplayici=new TarihHesaplayici(ActivityDetails.this);
                        date.setTime(TarihHesaplayici.get_kizdirma_tarihi(date_in_millis));
                        txt_kizidirma_tarihi.setText(dateFormat.format(date));
                        date.setTime(tarihHesaplayici.get_kuruya_alma_tarihi(date_in_millis));
                        txt_kuruya_alma.setText(dateFormat.format(date));
                        if(dataModel.getSperma_kullanilan()==null||dataModel.getSperma_kullanilan().isEmpty()){
                            txt_sperma.setText(getString(R.string.kupe_no_yok));
                        }
                        else{
                            txt_sperma.setText(dataModel.getSperma_kullanilan());
                        }
                    }
                    else{
                        view=inflater.inflate(R.layout.lyt_dgr_dtylar1,other_fields_container,false);
                        final TextView txt_sperma=view.findViewById(R.id.txt_sperma);
                        if(dataModel.getSperma_kullanilan()==null||dataModel.getSperma_kullanilan().isEmpty()){
                            txt_sperma.setText(getString(R.string.kupe_no_yok));
                        }
                        else{
                            txt_sperma.setText(dataModel.getSperma_kullanilan());
                        }
                    }
                    other_fields_container.addView(view);
                    other_fields_container.animate().alpha(1f).setDuration(200).start();
                }
            }
        });
        adContainerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                final ConnectivityManager connectivityManager=(ConnectivityManager)ActivityDetails.this.getSystemService(CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if(networkInfo!=null){
                    if(networkInfo.isConnected()){
                        MobileAds.initialize(ActivityDetails.this, new OnInitializationCompleteListener() {
                            @Override
                            public void onInitializationComplete(InitializationStatus initializationStatus) {}
                        });
                        loadBanner();
                    }
                }
            }
        },500);
        parent_layout.post(new Runnable() {
            @Override
            public void run() {
                get_width_pixels();
            }
        });
    }

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
        final ImageView img_animal=dialog.findViewById(R.id.image_animal);
        final FloatingActionButton fab_close=dialog.findViewById(R.id.fab_close);
        img_animal.setMinimumHeight(screen_width*3/4);  //Bu işlem dialog boyutunu yatay piksel sayısının
        img_animal.setMaxHeight(screen_width*4/5);      //%75-%80'i boyutunda tutarak dialog boyutunu stabil tutar.
        img_animal.setMinimumWidth(screen_width*3/4);
        img_animal.setMaxWidth(screen_width*4/5);
        Glide.with(this).load(Uri.fromFile(new File(photo_address))).into(img_animal);
        fab_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    void get_width_pixels(){
        final Display display = getWindowManager().getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        screen_width=displayMetrics.widthPixels;
    }

}
