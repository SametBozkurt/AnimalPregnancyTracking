package com.abcd.hayvandogumtakibi2.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abcd.hayvandogumtakibi2.Misc.DataModel;
import com.abcd.hayvandogumtakibi2.Misc.HayvanDuzenleyici;
import com.abcd.hayvandogumtakibi2.R;
import com.abcd.hayvandogumtakibi2.Misc.SQLiteDatabaseHelper;
import com.abcd.hayvandogumtakibi2.Misc.TarihHesaplayici;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

    private static final String BANNER_AD_UNIT_ID = "ca-app-pub-9721232821183013/8246180827";
    private static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";
    private Boolean isOtherFieldsShown=false,hasInternetConnection=false;
    private DataModel dataModel;
    private static final long DAY_IN_MILLIS = 1000*60*60*24;
    private int dogum_gerceklesti=0;
    private AdView adView;
    private TextView txtMarkIt;
    private FrameLayout adContainerView, other_fields_container;
    private LinearLayout linearLayout;
    private RelativeLayout parent_layout;
    private int screen_width=0;
    private SQLiteDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        parent_layout=findViewById(R.id.parent_layout);
        adContainerView=findViewById(R.id.ad_view_container);
        other_fields_container=findViewById(R.id.diger_detaylar_container);
        linearLayout=findViewById(R.id.linear_layout);
        txtMarkIt=findViewById(R.id.text_mark);
        LinearLayout layout_mark = findViewById(R.id.mark_layout);
        final ImageView imageView = findViewById(R.id.img_hayvan);
        ImageView cross = findViewById(R.id.iptal);
        TextView txt_isim = findViewById(R.id.txt_isim);
        TextView txt_kupe_no = findViewById(R.id.txt_kupe_no);
        TextView txt_tur = findViewById(R.id.txt_tur);
        TextView txt_tarih1 = findViewById(R.id.txt_tarih1);
        final TextView txt_tarih2 = findViewById(R.id.txt_tarih2);
        final TextView txt_tarih2_title = findViewById(R.id.txt_tarih2_title);
        final TextView txt_kalan = findViewById(R.id.txt_kalan_gun);
        ImageView icon_edit = findViewById(R.id.btn_edit);
        TextView txtOtherFields=findViewById(R.id.txt_other_details);
        final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        Bundle bundle = getIntent().getExtras();
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
                    imageView.setColorFilter(Color.TRANSPARENT);
                    imageView.setScaleX(1.0f);
                    imageView.setScaleY(1.0f);
                    Glide.with(ActivityDetails.this).load(Uri.fromFile(gorselFile)).apply(RequestOptions.circleCropTransform()).into(imageView);
                }
                else{
                    imageView.setColorFilter(Color.parseColor("#37474f"));
                    imageView.setScaleX(.75f);
                    imageView.setScaleY(.75f);
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
                    Intent data=new Intent(ActivityDetails.this,ActivityEdit.class);
                    Bundle veri_paketi=new Bundle();
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
                File gorselFile=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),dataModel.getFotograf_isim());
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
                    Button btn_ok=dialog_warn.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long tahmini_dogum_tarihi=Long.parseLong(dataModel.getDogum_tarihi());
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
                    boolean a=dataModel.getIs_evcilhayvan()==2 && Integer.parseInt(dataModel.getTur())==0;
                    boolean b=dataModel.getIs_evcilhayvan()==0 && Integer.parseInt(dataModel.getTur())==0;
                    LayoutInflater inflater=LayoutInflater.from(ActivityDetails.this);
                    other_fields_container.setAlpha(0f);
                    View view;
                    if(a||b){
                        view=inflater.inflate(R.layout.lyt_dgr_dtylar2,other_fields_container,false);
                        TextView txt_kizidirma_tarihi=view.findViewById(R.id.txt_tarih3);
                        TextView txt_kuruya_alma=view.findViewById(R.id.txt_tarih4);
                        TextView txt_sperma=view.findViewById(R.id.txt_sperma);
                        long date_in_millis=Long.parseLong(dataModel.getDogum_tarihi());
                        Date date = new Date();
                        TarihHesaplayici tarihHesaplayici=TarihHesaplayici.getInstance(ActivityDetails.this);
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
                        TextView txt_sperma=view.findViewById(R.id.txt_sperma);
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
        doAsyncAdsTask();
    }

    private void doAsyncAdsTask(){
        HandlerThread handlerThread=new HandlerThread("AsyncAdsTasks");
        handlerThread.start();
        final Handler asyncHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(hasInternetConnection){
                            loadBanner();
                        }
                    }
                });
            }
        };
        asyncHandler.post(new Runnable() {
            @Override
            public void run() {
                get_width_pixels();
                ConnectivityManager connectivityManager=(ConnectivityManager)ActivityDetails.this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if(networkInfo!=null){
                    hasInternetConnection=networkInfo.isConnected();
                    if(networkInfo.isConnected()){
                        MobileAds.initialize(ActivityDetails.this, new OnInitializationCompleteListener() {
                            @Override
                            public void onInitializationComplete(InitializationStatus initializationStatus) {}
                        });
                        try {
                            Thread.sleep(500);
                            Message message=new Message();
                            message.obj="InitializeUIProcess";
                            asyncHandler.sendMessage(message);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private int get_gun_sayisi(long dogum_tarihi_in_millis) {
        long gun=(dogum_tarihi_in_millis-System.currentTimeMillis())/DAY_IN_MILLIS;
        return (int)gun;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
            adContainerView.removeAllViews();
            linearLayout.removeAllViews();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adView != null) {
            adView.pause();
        }
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
        adView.setAdSize(getAdSize());
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = outMetrics.density;
        float adWidthPixels = adContainerView.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getLandscapeAnchoredAdaptiveBannerAdSize(this,adWidth);
    }

    private void show_image(String photo_address){
        final Dialog dialog = new Dialog(this,R.style.ImageDialogStyle);
        dialog.setContentView(R.layout.image_dialog_layout);
        ImageView img_animal=dialog.findViewById(R.id.image_animal);
        FloatingActionButton fab_close=dialog.findViewById(R.id.fab_close);
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

    private void get_width_pixels(){
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        screen_width=displayMetrics.widthPixels;
    }

}
