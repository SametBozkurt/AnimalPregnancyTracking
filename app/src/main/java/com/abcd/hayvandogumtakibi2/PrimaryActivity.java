package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class PrimaryActivity extends AppCompatActivity {

    private static final String INTERSTITIAL_TEST_ID = "ca-app-pub-3940256099942544/1033173712";
    //private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-9721232821183013/5088109999";
    int database_size;
    boolean is_opened = false;
    final Context context=this;
    Animation fab_open, fab_close, fab_clock, fab_anticlock;
    InterstitialAd mInterstitialAd = new InterstitialAd(context);
    AdRequest adRequest;
    final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(context);
    RelativeLayout relativeLayout;
    FrameLayout frameLayout, yakinDogumlarContainer;
    LinearLayout lyt_edit,lyt_incoming,lyt_happened,lyt_search,lyt_periods,lyt_all_recs,lyt_calculator,lyt_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_primary);
        relativeLayout=findViewById(R.id.main_layout);
        final ImageView img_menu=findViewById(R.id.img_menu);
        final ImageView cross=findViewById(R.id.iptal);
        final FloatingActionButton btn_add = findViewById(R.id.create);
        final FloatingActionButton btn_pet = findViewById(R.id.fab_pet);
        final FloatingActionButton btn_barn = findViewById(R.id.fab_barn);
        final TextView txt_pet = findViewById(R.id.text_pet);
        final TextView txt_barn = findViewById(R.id.text_barn);
        frameLayout=findViewById(R.id.no_rec_msg_container);
        yakinDogumlarContainer=findViewById(R.id.en_yakin_dogumlar);
        lyt_edit=findViewById(R.id.edit);
        lyt_incoming=findViewById(R.id.incoming);
        lyt_happened=findViewById(R.id.happened);
        lyt_search=findViewById(R.id.search);
        lyt_periods=findViewById(R.id.periods);
        lyt_all_recs=findViewById(R.id.all_recs);
        lyt_calculator=findViewById(R.id.calculator);
        lyt_about=findViewById(R.id.about);
        database_size=databaseHelper.getSize();
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu=new PopupMenu(context,img_menu);
                popupMenu.getMenuInflater().inflate(R.menu.primary_activity_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.kayit_bul) {
                            if(database_size==0){
                                Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                            }
                            else{
                                startActivity(new Intent(context,ActivityKayitAra.class));
                            }
                        }
                        else if(item.getItemId()==R.id.dev_tools){
                            startActivity(new Intent(context,ActivityDevTools.class));
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(is_opened){
                    txt_barn.startAnimation(fab_close);
                    btn_barn.startAnimation(fab_close);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt_pet.startAnimation(fab_close);
                            btn_pet.startAnimation(fab_close);
                        }
                    },200);
                    btn_add.startAnimation(fab_anticlock);
                    btn_pet.setClickable(false);
                    btn_barn.setClickable(false);
                    is_opened=false;
                }
                else{
                    txt_pet.startAnimation(fab_open);
                    btn_pet.startAnimation(fab_open);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt_barn.startAnimation(fab_open);
                            btn_barn.startAnimation(fab_open);
                        }
                    },200);
                    btn_add.startAnimation(fab_clock);
                    btn_pet.setClickable(true);
                    btn_barn.setClickable(true);
                    is_opened = true;
                }
            }
        });
        btn_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent bundle_intent=new Intent(context,ActivityDogumKayit.class);
                final Bundle datas=new Bundle();
                datas.putInt("isPet",1);
                bundle_intent.putExtras(datas);
                startActivity(bundle_intent);
                //Evcil hayvan ise 1
            }
        });
        btn_barn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent bundle_intent=new Intent(context,ActivityDogumKayit.class);
                final Bundle datas=new Bundle();
                datas.putInt("isPet",2);
                bundle_intent.putExtras(datas);
                startActivity(bundle_intent);
                //Besi hayvanı ise 2
            }
        });
        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                lyt_edit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(database_size==0){
                            Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                        }
                        else{
                            startActivity(new Intent(context,ActivityKayitDuzenle.class));
                        }
                    }
                });
                lyt_incoming.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(database_size==0){
                            Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                        }
                        else{
                            startActivity(new Intent(context,ActivityKritikler.class));
                        }
                    }
                });
                lyt_happened.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(database_size==0){
                            Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                        }
                        else{
                            startActivity(new Intent(context,ActivityGerceklesenler.class));
                        }
                    }
                });
                lyt_search.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(database_size==0){
                            Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                        }
                        else{
                            startActivity(new Intent(context,ActivityKayitAra.class));
                        }
                    }
                });
                lyt_periods.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context,ActivityPeriods.class));
                    }
                });
                lyt_all_recs.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(database_size==0){
                            Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                        }
                        else{
                            startActivity(new Intent(context,ActivityTumKayitlar.class));
                        }
                    }
                });
                lyt_calculator.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context,ActivityTarihHesapla.class));
                    }
                });
                lyt_about.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context, ActivityAppInfo.class));
                    }
                });
            }
        });
        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                if(database_size==0){
                    final LayoutInflater layoutInflater=LayoutInflater.from(context);
                    final View view=layoutInflater.inflate(R.layout.layout_no_record,frameLayout,false);
                    frameLayout.addView(view);
                }
            }
        });
        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                fab_open= AnimationUtils.loadAnimation(context,R.anim.fab_on);
                fab_close=AnimationUtils.loadAnimation(context,R.anim.fab_off);
                fab_clock=AnimationUtils.loadAnimation(context,R.anim.rotation_clock);
                fab_anticlock=AnimationUtils.loadAnimation(context,R.anim.rotation_anticlock);
            }
        });
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N && database_size!=0){
            final String INTENT_ACTION= "SET_AN_ALARM" ;
            sendBroadcast(new Intent(context,TarihKontrol.class).setAction(INTENT_ACTION));
        }
        relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                final ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if(networkInfo!=null){
                    if(networkInfo.isConnected()){
                        MobileAds.initialize(context, new OnInitializationCompleteListener() {
                            @Override
                            public void onInitializationComplete(InitializationStatus initializationStatus) {}
                        });
                        show_ads();
                    }
                }
            }
        },500);
        setProgressBarIndeterminateVisibility(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        database_size=databaseHelper.getSize();
        frameLayout.removeAllViews();
        if(database_size==0){
            final LayoutInflater layoutInflater=LayoutInflater.from(context);
            final View view=layoutInflater.inflate(R.layout.layout_no_record,frameLayout,false);
            frameLayout.addView(view);
        }
        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                yakinDogumlarContainer.removeAllViews();
                show_enYakinDogumlar();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        final Intent i=new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void show_ads(){
        mInterstitialAd.setAdUnitId(INTERSTITIAL_TEST_ID);
        adRequest=new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
            @Override
            public void onAdClosed() {
                mInterstitialAd.setAdListener(null);
                adRequest=null;
                mInterstitialAd=null;
            }
        });
    }

    void show_enYakinDogumlar(){
        if(!databaseHelper.getEnYakinDogumlar().isEmpty()){
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            final View view = layoutInflater.inflate(R.layout.lyt_en_yakin_dogumlar,yakinDogumlarContainer,false);
            final RecyclerView recyclerView = view.findViewById(R.id.recyclerViewYakinDogumlar);
            final LinearLayout tumunu_goster= view.findViewById(R.id.showAll);
            tumunu_goster.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context,ActivityKritikler.class));
                }
            });
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            final KritiklerAdapter kritiklerAdapter = new KritiklerAdapter(context);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(kritiklerAdapter);
            yakinDogumlarContainer.addView(view);
        }
    }
}

