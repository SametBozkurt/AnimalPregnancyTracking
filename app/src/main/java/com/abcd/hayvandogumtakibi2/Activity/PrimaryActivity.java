package com.abcd.hayvandogumtakibi2.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abcd.hayvandogumtakibi2.Adapter.KayitlarAdapter;
import com.abcd.hayvandogumtakibi2.Adapter.KritiklerAdapter;
import com.abcd.hayvandogumtakibi2.Fragment.FragmentTarihHesaplayici;
import com.abcd.hayvandogumtakibi2.Misc.ActivityInteractor;
import com.abcd.hayvandogumtakibi2.Misc.AlarmLauncher;
import com.abcd.hayvandogumtakibi2.Misc.PreferencesHolder;
import com.abcd.hayvandogumtakibi2.Misc.SQLiteDatabaseHelper;
import com.abcd.hayvandogumtakibi2.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class PrimaryActivity extends AppCompatActivity {

    private static final String INTERSTITIAL_TEST_ID = "ca-app-pub-3940256099942544/1033173712";
    private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-9721232821183013/5088109999";
    private int database_size;
    private boolean is_opened = false, hasInternetConnection=false;
    private RelativeLayout relativeLayout;
    private FrameLayout frameLayout;
    private final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(this);
    private BottomSheetDialog bottomSheetDialog;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        relativeLayout=findViewById(R.id.main_layout);
        ImageView img_menu=findViewById(R.id.img_menu);
        final FloatingActionButton btn_add = findViewById(R.id.create);
        final FloatingActionButton btn_pet = findViewById(R.id.fab_pet);
        final FloatingActionButton btn_barn = findViewById(R.id.fab_barn);
        final TextView txt_pet = findViewById(R.id.text_pet);
        final TextView txt_barn = findViewById(R.id.text_barn);
        frameLayout=findViewById(R.id.no_rec_msg_container);
        final PopupMenu popupMenu=new PopupMenu(this,img_menu);
        final Animation fab_open=AnimationUtils.loadAnimation(this,R.anim.fab_on);
        final Animation fab_close=AnimationUtils.loadAnimation(this,R.anim.fab_off);
        final Animation fab_clock=AnimationUtils.loadAnimation(this,R.anim.rotation_clock);
        final Animation fab_anticlock=AnimationUtils.loadAnimation(this,R.anim.rotation_anticlock);
        popupMenu.getMenuInflater().inflate(R.menu.primary_activity_menu,popupMenu.getMenu());
        initViewTask();
        initBottomSheetDialog();
        doAsyncAdsTask();
        ActivityInteractor.getInstance().setPrimaryActivityCallback(new ActivityInteractor.PrimaryActivityCallback() {
            @Override
            public void onSomethingsChanged(@Nullable Bundle whatChanged) {
                initViewTask();
                initBottomSheetDialog();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N && database_size!=0){
                    String INTENT_ACTION= "SET_AN_ALARM" ;
                    sendBroadcast(new Intent(PrimaryActivity.this, AlarmLauncher.class).setAction(INTENT_ACTION));
                }
            }
        }).start();
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
                                startActivity(new Intent(PrimaryActivity.this,ActivityKayitAra.class));
                            }
                        }
                        else if(item.getItemId()==R.id.app_info)
                            startActivity(new Intent(PrimaryActivity.this, ActivityAppInfo.class));
                        else if(item.getItemId()==R.id.dev_tools)
                            startActivity(new Intent(PrimaryActivity.this,ActivityDevTools.class));
                        else if(item.getItemId()==R.id.settings)
                            startActivity(new Intent(PrimaryActivity.this,ActivityAyarlar.class));
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
                Intent bundle_intent=new Intent(PrimaryActivity.this,ActivityDogumKayit.class);
                Bundle datas=new Bundle();
                datas.putInt("isPet",1);
                bundle_intent.putExtras(datas);
                startActivity(bundle_intent);
                showAd();
                //Evcil hayvan ise 1
            }
        });
        btn_barn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bundle_intent=new Intent(PrimaryActivity.this,ActivityDogumKayit.class);
                Bundle datas=new Bundle();
                datas.putInt("isPet",2);
                bundle_intent.putExtras(datas);
                startActivity(bundle_intent);
                showAd();
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
                    startActivity(new Intent(PrimaryActivity.this,ActivityKayitDuzenle.class));
                }
                showAd();
                break;
            case R.id.incoming:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityKritikler.class));
                }
                showAd();
                break;
            case R.id.happened:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityGerceklesenler.class));
                }
                showAd();
                break;
            case R.id.search:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityKayitAra.class));
                }
                showAd();
                break;
            case R.id.periods:
                startActivity(new Intent(PrimaryActivity.this,ActivityPeriods.class));
                break;
            case R.id.all_recs:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityTumKayitlar.class));
                }
                showAd();
                break;
            case R.id.calculator:
                FragmentTarihHesaplayici fragmentTarihHesaplayici=new FragmentTarihHesaplayici();
                fragmentTarihHesaplayici.setDateCalculatedListener(new FragmentTarihHesaplayici.DateCalculatedListener() {
                    @Override
                    public void onDateCalculated() {
                        Toast.makeText(PrimaryActivity.this,getString(R.string.otomatik_hesaplandi_bildirim),Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void initViewTask(){
        frameLayout.removeAllViews();
        HandlerThread handlerThread=new HandlerThread("HandlerThread");
        handlerThread.start();
        final Handler asyncHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Handler uiHandler = new Handler(getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(database_size==0){
                            LayoutInflater layoutInflater=LayoutInflater.from(PrimaryActivity.this);
                            View view=layoutInflater.inflate(R.layout.layout_no_record,frameLayout,false);
                            frameLayout.addView(view);
                        }
                    }
                });
            }
        };
        asyncHandler.post(new Runnable() {
            @Override
            public void run() {
                database_size=databaseHelper.getSize();
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N && database_size!=0){
                    String INTENT_ACTION= "SET_AN_ALARM" ;
                    sendBroadcast(new Intent(PrimaryActivity.this, AlarmLauncher.class).setAction(INTENT_ACTION));
                }
                Message message=new Message();
                message.obj="InitializeUIProcess";
                asyncHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent i=new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void load_ad(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,INTERSTITIAL_TEST_ID, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        mInterstitialAd = null;
                        Log.e("WARN","onAdDismissedFullScreenContent");
                    }
                });

            }
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }

    private void showAd(){
        if(mInterstitialAd!=null){
            PreferencesHolder.setLastAdShowTime(this,System.currentTimeMillis());
            Log.e("WARN","Time writing....");
            mInterstitialAd.show(PrimaryActivity.this);
        }
    }

    private void show_enYakinDogumlar(final FrameLayout yakinDogumlarContainer,final BottomSheetDialog bottomSheetDialog){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.lyt_en_yakin_dogumlar,yakinDogumlarContainer,false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewYakinDogumlar);
        LinearLayout tumunu_goster= view.findViewById(R.id.showAll);
        tumunu_goster.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(PrimaryActivity.this,ActivityKritikler.class));
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        KritiklerAdapter kritiklerAdapter = new KritiklerAdapter(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(kritiklerAdapter);
        yakinDogumlarContainer.addView(view);
    }

    private void show_sonOlusturulanlar(final FrameLayout sonOlusturulanlarContainer,final BottomSheetDialog bottomSheetDialog){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.lyt_son_olusturulanlar,sonOlusturulanlarContainer,false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSonOlusturulanlar);
        LinearLayout tumunu_goster= view.findViewById(R.id.showAll);
        tumunu_goster.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(PrimaryActivity.this,ActivityTumKayitlar.class));
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        KayitlarAdapter kayitlarAdapter = new KayitlarAdapter(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(kayitlarAdapter);
        sonOlusturulanlarContainer.addView(view);
    }

    private void initBottomSheetDialog(){
        bottomSheetDialog=new BottomSheetDialog(this,R.style.SummaryDialogTheme);
        View view1 = LayoutInflater.from(this).inflate(R.layout.lyt_dialog_summary, (RelativeLayout) findViewById(R.id.parent_layout));
        bottomSheetDialog.setContentView(view1);
        show_sonOlusturulanlar((FrameLayout)view1.findViewById(R.id.son_olusturulanlar),bottomSheetDialog);
        if(!databaseHelper.getEnYakinDogumlar().isEmpty()){
            show_enYakinDogumlar((FrameLayout)view1.findViewById(R.id.en_yakin_dogumlar),bottomSheetDialog);
        }
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
                            load_ad();
                        }
                    }
                });
            }
        };
        asyncHandler.post(new Runnable() {
            @Override
            public void run() {
                final long latestAdShowTime=PreferencesHolder.getLastAdShowTime(PrimaryActivity.this);
                final long period=2*60*60*1000; //2 saat
                final long nextAdShowTime=latestAdShowTime+period;
                if(latestAdShowTime==0||System.currentTimeMillis()>=nextAdShowTime){
                    Log.e("WARN","Preparing for ads");
                    ConnectivityManager connectivityManager=(ConnectivityManager)PrimaryActivity.this.getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                    if(networkInfo!=null){
                        hasInternetConnection=networkInfo.isConnected();
                        if(networkInfo.isConnected()){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            MobileAds.initialize(PrimaryActivity.this, new OnInitializationCompleteListener() {
                                @Override
                                public void onInitializationComplete(InitializationStatus initializationStatus) {}
                            });
                            Message message=new Message();
                            message.obj="InitializeAdProcess";
                            asyncHandler.sendMessage(message);
                        }
                    }
                }
                else{
                    Log.e("WARN","Not preparing for ads");
                }
            }
        });
    }

}