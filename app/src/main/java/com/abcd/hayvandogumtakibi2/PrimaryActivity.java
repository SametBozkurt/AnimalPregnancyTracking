package com.abcd.hayvandogumtakibi2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class PrimaryActivity extends AppCompatActivity {

    private static final String INTERSTITIAL_TEST_ID = "ca-app-pub-3940256099942544/1033173712";
    //private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-9721232821183013/5088109999";
    int database_size;
    boolean is_opened = false;
    final Context context=this;
    RelativeLayout relativeLayout;
    FrameLayout frameLayout;
    final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(context);
    BottomSheetDialog bottomSheetDialog;
    View view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        relativeLayout=findViewById(R.id.main_layout);
        final ImageView img_menu=findViewById(R.id.img_menu);
        final FloatingActionButton btn_add = findViewById(R.id.create);
        final FloatingActionButton btn_pet = findViewById(R.id.fab_pet);
        final FloatingActionButton btn_barn = findViewById(R.id.fab_barn);
        final TextView txt_pet = findViewById(R.id.text_pet);
        final TextView txt_barn = findViewById(R.id.text_barn);
        frameLayout=findViewById(R.id.no_rec_msg_container);
        final PopupMenu popupMenu=new PopupMenu(context,img_menu);
        database_size=databaseHelper.getSize();
        if(database_size==0){
            final LayoutInflater layoutInflater=LayoutInflater.from(context);
            final View view=layoutInflater.inflate(R.layout.layout_no_record,frameLayout,false);
            frameLayout.addView(view);
        }
        final Animation fab_open= AnimationUtils.loadAnimation(context,R.anim.fab_on);
        final Animation fab_close=AnimationUtils.loadAnimation(context,R.anim.fab_off);
        final Animation fab_clock=AnimationUtils.loadAnimation(context,R.anim.rotation_clock);
        final Animation fab_anticlock=AnimationUtils.loadAnimation(context,R.anim.rotation_anticlock);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N && database_size!=0){
            final String INTENT_ACTION= "SET_AN_ALARM" ;
            sendBroadcast(new Intent(context, AlarmLauncher.class).setAction(INTENT_ACTION));
        }
        new AsyncAdTask(getApplicationContext()).execute();
        popupMenu.getMenuInflater().inflate(R.menu.primary_activity_menu,popupMenu.getMenu());
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        else if(item.getItemId()==R.id.app_info)
                            startActivity(new Intent(context, ActivityAppInfo.class));
                        else if(item.getItemId()==R.id.dev_tools)
                            startActivity(new Intent(context,ActivityDevTools.class));
                        else if(item.getItemId()==R.id.settings)
                            startActivity(new Intent(context,ActivityAyarlar.class));
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
    }
    @SuppressLint("NonConstantResourceId")
    public void viewClick(View view){
        switch (view.getId()){
            case R.id.edit:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(context,ActivityKayitDuzenle.class));
                }
                break;
            case R.id.incoming:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(context,ActivityKritikler.class));
                }
                break;
            case R.id.happened:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(context,ActivityGerceklesenler.class));
                }
                break;
            case R.id.search:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(context,ActivityKayitAra.class));
                }
                break;
            case R.id.periods:
                startActivity(new Intent(context,ActivityPeriods.class));
                break;
            case R.id.all_recs:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(context,ActivityTumKayitlar.class));
                }
                break;
            case R.id.calculator:
                final FragmentTarihHesaplayici fragmentTarihHesaplayici=new FragmentTarihHesaplayici();
                fragmentTarihHesaplayici.show(getSupportFragmentManager(),null);
                break;
            case R.id.summary:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    bottomSheetDialog.show();
                }
                break;
            case R.id.iptal:
                onBackPressed();
                break;
        }
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
        relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                initBottomSheetDialog();
            }
        },300);
    }

    @Override
    public void onBackPressed() {
        finish();
        final Intent i=new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    protected static void show_ads(final Context context1){
        final InterstitialAd mInterstitialAd = new InterstitialAd(context1);
        final AdRequest adRequest=new AdRequest.Builder().build();
        mInterstitialAd.setAdUnitId(INTERSTITIAL_TEST_ID);
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });
    }

    protected void show_enYakinDogumlar(final FrameLayout yakinDogumlarContainer,final BottomSheetDialog bottomSheetDialog){
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(R.layout.lyt_en_yakin_dogumlar,yakinDogumlarContainer,false);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerViewYakinDogumlar);
        final LinearLayout tumunu_goster= view.findViewById(R.id.showAll);
        tumunu_goster.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(context,ActivityKritikler.class));
            }
        });
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        final KritiklerAdapter kritiklerAdapter = new KritiklerAdapter(context);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(kritiklerAdapter);
        yakinDogumlarContainer.addView(view);
    }

    protected void show_sonOlusturulanlar(final FrameLayout sonOlusturulanlarContainer,final BottomSheetDialog bottomSheetDialog){
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(R.layout.lyt_son_olusturulanlar,sonOlusturulanlarContainer,false);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSonOlusturulanlar);
        final LinearLayout tumunu_goster= view.findViewById(R.id.showAll);
        tumunu_goster.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(context,ActivityTumKayitlar.class));
            }
        });
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        final KayitlarAdapter kayitlarAdapter = new KayitlarAdapter(context);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(kayitlarAdapter);
        sonOlusturulanlarContainer.addView(view);
    }

    protected void initBottomSheetDialog(){
        bottomSheetDialog=new BottomSheetDialog(context,R.style.SummaryDialogTheme);
        view1=LayoutInflater.from(context).inflate(R.layout.lyt_dialog_summary,(RelativeLayout)findViewById(R.id.parent_layout));
        bottomSheetDialog.setContentView(view1);
        show_sonOlusturulanlar((FrameLayout)view1.findViewById(R.id.son_olusturulanlar),bottomSheetDialog);
        if(!databaseHelper.getEnYakinDogumlar().isEmpty()){
            show_enYakinDogumlar((FrameLayout)view1.findViewById(R.id.en_yakin_dogumlar),bottomSheetDialog);
        }
    }


    protected static class AsyncAdTask extends AsyncTask<String,Integer,String>{

        @SuppressLint("StaticFieldLeak")
        private final Context mContext;
        private boolean hasInternetConnection=false;

        public AsyncAdTask(final Context context) {
            super();
            this.mContext=context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final ConnectivityManager connectivityManager=(ConnectivityManager)mContext.getSystemService(CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null){
                hasInternetConnection=networkInfo.isConnected();
                if(networkInfo.isConnected()){
                    MobileAds.initialize(mContext, new OnInitializationCompleteListener() {
                        @Override
                        public void onInitializationComplete(InitializationStatus initializationStatus) {}
                    });
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(hasInternetConnection){
                show_ads(mContext);
            }
        }

    }

}

