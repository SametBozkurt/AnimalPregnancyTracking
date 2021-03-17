package com.abcd.hayvandogumtakibi2.Activity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abcd.hayvandogumtakibi2.Fragment.FragmentYaklasanDogumYok;
import com.abcd.hayvandogumtakibi2.Fragment.FragmentYaklasanDogumlar;
import com.abcd.hayvandogumtakibi2.Misc.ActivityInteractor;
import com.abcd.hayvandogumtakibi2.Misc.DataModel;
import com.abcd.hayvandogumtakibi2.Misc.HayvanDuzenleyici;
import com.abcd.hayvandogumtakibi2.Misc.ListModeCallback;
import com.abcd.hayvandogumtakibi2.Misc.PreferencesHolder;
import com.abcd.hayvandogumtakibi2.Misc.SQLiteDatabaseHelper;
import com.abcd.hayvandogumtakibi2.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class ActivityKritikler extends AppCompatActivity {

    private static final String BANNER_AD_UNIT_ID = "ca-app-pub-9721232821183013/8246180827";
    private static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";
    private SQLiteDatabaseHelper databaseHelper;
    private ArrayList<DataModel> dataModelArrayList;
    private FrameLayout adContainerView, fragment_container;
    private AdView adView;
    private ImageView imgListMode;
    private RelativeLayout relativeLayout;
    private boolean listModeEnabled;
    private ListModeCallback listModeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!HayvanDuzenleyici.isLoaded) {
            HayvanDuzenleyici.load(this);
        }
        setContentView(R.layout.activity_kritikler);
        relativeLayout=findViewById(R.id.parent);
        adContainerView=findViewById(R.id.ad_view_container);
        final ImageView cross=findViewById(R.id.iptal);
        imgListMode=findViewById(R.id.listMode);
        fragment_container=findViewById(R.id.fragment_container);
        doAsyncTaskAndPost();
        doAsyncAdsTask();
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgListMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listModeEnabled){
                    listModeEnabled=false;
                    imgListMode.setImageResource(R.drawable.ic_tile);
                }
                else{
                    listModeEnabled=true;
                    imgListMode.setImageResource(R.drawable.ic_view_all);
                }
                if(listModeCallback!=null){
                    listModeCallback.onListModeChanged(listModeEnabled);
                }
                PreferencesHolder.setIsListedViewEnabled(ActivityKritikler.this,listModeEnabled);
            }
        });
        ActivityInteractor.getInstance().setActivityInteractorCallback(new ActivityInteractor.ActivityInteractorCallback() {
            @Override
            public void onSomethingsChanged(@Nullable Bundle whatChanged) {
                /*Olası bir gerçekleşen doğum işaretlemesine karşı aktiviteyi sürekli yenilemek gereklidir. Daha önce
                * bunun için onRestart metodunda bazı işlemler yapılıyordu ve bu zamanla belleğin şişmesine ve işlemcinin sürekli çalışır durumda
                * olmasına neden oluyordu. Ancak ActivityInteractor sınıfı bünyesindeki callback metodu sayesinde bahsedilen gereksiz yenilemeye
                * son veriyor. Çünkü artık sadece bir değişim olduğunda yenileme yapılacak, çoğunlukla gereksiz olan işlemlere gerek kalmayacak.
                * */
                dataModelArrayList=databaseHelper.getKritikOlanlar(null,30);
                initFragment();
            }
        });
    }

    private void doAsyncTaskAndPost(){
        HandlerThread handlerThread=new HandlerThread("AsyncTasks");
        handlerThread.start();
        final Handler asyncHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                taskPostOnUI();
            }
        };
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                databaseHelper=SQLiteDatabaseHelper.getInstance(ActivityKritikler.this);
                dataModelArrayList=databaseHelper.getKritikOlanlar(null,30);
                Message message=new Message();
                message.obj="InitializeUIProcess";
                asyncHandler.sendMessage(message);
            }
        };
        asyncHandler.post(runnable);
    }

    private void taskPostOnUI(){
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(dataModelArrayList==null||dataModelArrayList.isEmpty()){
                    imgListMode.setVisibility(View.INVISIBLE);
                }
                else{
                    listModeEnabled= PreferencesHolder.getIsListedViewEnabled(ActivityKritikler.this);
                    if(listModeEnabled){
                        imgListMode.setImageResource(R.drawable.ic_view_all);
                    }
                    else{
                        imgListMode.setImageResource(R.drawable.ic_tile);
                    }
                }
                initFragment();
            }
        });
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
                        loadBanner();
                    }
                });
            }
        };
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager=(ConnectivityManager)ActivityKritikler.this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if(networkInfo!=null){
                    if(networkInfo.isConnected()){
                        MobileAds.initialize(ActivityKritikler.this, new OnInitializationCompleteListener() {
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
        };
        asyncHandler.post(runnable);
    }

    private void initFragment(){
        fragment_container.removeAllViews();
        if(dataModelArrayList==null||dataModelArrayList.isEmpty()){
            FragmentYaklasanDogumYok fragmentYaklasanDogumYok=new FragmentYaklasanDogumYok();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentYaklasanDogumYok).commitAllowingStateLoss();
        }
        else{
            FragmentYaklasanDogumlar fragmentYaklasanDogumlar=new FragmentYaklasanDogumlar();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentYaklasanDogumlar).commitAllowingStateLoss();
        }
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
            databaseHelper=null;
            adView.destroy();
            adContainerView.removeAllViews();
            relativeLayout.removeAllViews();
            fragment_container.removeAllViews();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adView != null) { adView.pause(); }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) { adView.resume(); }
    }

    private void loadBanner() {
        adContainerView.removeAllViews();
        adView = new AdView(this);
        adView.setAdUnitId(BANNER_TEST_ID);
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

    public void setListModeCallback(ListModeCallback listModeCallback){
        this.listModeCallback=listModeCallback;
    }

}
