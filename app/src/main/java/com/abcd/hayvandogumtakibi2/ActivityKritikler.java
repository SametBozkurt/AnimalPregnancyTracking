package com.abcd.hayvandogumtakibi2;

import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class ActivityKritikler extends AppCompatActivity {

    //private static final String BANNER_AD_UNIT_ID = "ca-app-pub-9721232821183013/8246180827";
    private static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";
    private SQLiteDatabaseHelper databaseHelper;
    private ArrayList<DataModel> dataModelArrayList;
    private FrameLayout adContainerView, fragment_container;
    private AdView adView;
    private ImageView imgListMode;
    private RelativeLayout relativeLayout;
    private final Context context=this;
    private boolean listModeEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kritikler);
        relativeLayout=findViewById(R.id.parent);
        adContainerView=findViewById(R.id.ad_view_container);
        final ImageView cross=findViewById(R.id.iptal);
        imgListMode=findViewById(R.id.listMode);
        fragment_container=findViewById(R.id.fragment_container);
        initTask();
        initAdsTask();
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initTask(){
        doAsyncTaskAndPost();
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
                databaseHelper=SQLiteDatabaseHelper.getInstance(context);
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
                if(dataModelArrayList.isEmpty()){
                    imgListMode.setVisibility(View.INVISIBLE);
                }
                else{
                    listModeEnabled=PreferencesHolder.getIsListedViewEnabled(context);
                    if(listModeEnabled){
                        imgListMode.setImageResource(R.drawable.ic_view_all);
                    }
                    else{
                        imgListMode.setImageResource(R.drawable.ic_tile);
                    }
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
                            onStart();
                            PreferencesHolder.setIsListedViewEnabled(context,listModeEnabled);
                        }
                    });
                }
            }
        });
    }

    private void initAdsTask(){
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
                        loadBanner();
                    }
                });
            }
        };
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if(networkInfo!=null){
                    if(networkInfo.isConnected()){
                        MobileAds.initialize(context, new OnInitializationCompleteListener() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataModelArrayList=null;
        fragment_container.removeAllViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragment_container.removeAllViews();
        HandlerThread handlerThread=new HandlerThread("FragmentThread");
        handlerThread.start();
        final Handler asyncHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Handler uiHandler = new Handler(getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(dataModelArrayList.isEmpty()){
                            final FragmentYaklasanDogumYok fragmentYaklasanDogumYok=new FragmentYaklasanDogumYok();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentYaklasanDogumYok).commitAllowingStateLoss();
                        }
                        else{
                            final FragmentYaklasanDogumlar fragmentYaklasanDogumlar=new FragmentYaklasanDogumlar();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentYaklasanDogumlar).commitAllowingStateLoss();
                        }
                    }
                });
            }
        };
        asyncHandler.post(new Runnable() {
            @Override
            public void run() {
                dataModelArrayList=databaseHelper.getKritikOlanlar(null, 30);
                Message message=new Message();
                message.obj="InitializeUIProcess";
                asyncHandler.sendMessage(message);
            }
        });
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
        adView = new AdView(context);
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
        return AdSize.getLandscapeAnchoredAdaptiveBannerAdSize(context,adWidth);
    }

}
