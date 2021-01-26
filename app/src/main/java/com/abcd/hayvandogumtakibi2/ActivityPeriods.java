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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class ActivityPeriods extends AppCompatActivity {

    //private static final String BANNER_AD_UNIT_ID = "ca-app-pub-9721232821183013/8246180827";
    private static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";
    private final Context context=this;
    private AdView adView;
    private FrameLayout adContainerView;
    private RelativeLayout relativeLayout;
    private boolean listModeEnabled;
    RecyclerView recyclerView;
    RelativeLayout.LayoutParams mLayoutParams;
    PeriodsAdapter periodsAdapter;
    GridLayoutManager layoutManager;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periods);
        listModeEnabled=PreferencesHolder.getIsListedViewEnabled(context);
        relativeLayout=findViewById(R.id.parent_layout);
        adContainerView=findViewById(R.id.ad_view_container);
        recyclerView=findViewById(R.id.recyclerView);
        final Button edidPeriods = findViewById(R.id.edit_periods);
        final ImageView cross = findViewById(R.id.iptal);
        final ImageView imgListMode=findViewById(R.id.listMode);
        if(listModeEnabled){
            imgListMode.setImageResource(R.drawable.ic_view_all);
        }
        else{
            imgListMode.setImageResource(R.drawable.ic_tile);
        }
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
                initProgressBarAndTask();
                PreferencesHolder.setIsListedViewEnabled(context,listModeEnabled);
            }
        });
        edidPeriods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPeriodsBottomSheet editPeriodsBottomSheet=new EditPeriodsBottomSheet();
                editPeriodsBottomSheet.show(getSupportFragmentManager(),null);
            }
        });
        initProgressBarAndTask();
        initAdsTask();
    }

    private void initProgressBarAndTask(){
        taskPrePostOnUI();
        doAsyncTaskAndPost();
    }

    private void taskPrePostOnUI(){
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar=new ProgressBar(context);
                progressBar.setIndeterminate(true);
                if(mLayoutParams==null){
                    mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                }
                progressBar.setLayoutParams(mLayoutParams);
                relativeLayout.addView(progressBar);
                recyclerView.setAlpha(0f);
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
                periodsAdapter=new PeriodsAdapter(context,listModeEnabled);
                if(listModeEnabled){
                    layoutManager=new GridLayoutManager(context,2);
                }
                else{
                    layoutManager=new GridLayoutManager(context,3);
                }
                try {
                    Thread.sleep(500);
                    Message message=new Message();
                    message.obj="InitializeUIProcess";
                    asyncHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        asyncHandler.post(runnable);
    }

    private void taskPostOnUI(){
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(periodsAdapter);
                relativeLayout.removeView(progressBar);
                recyclerView.animate().alpha(1f).setDuration(200).start();
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

    private void loadBanner() {
        adView = new AdView(context);
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
        return AdSize.getLandscapeAnchoredAdaptiveBannerAdSize(context,adWidth);
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
            relativeLayout.removeAllViews();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (adView != null) { adView.pause(); }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) { adView.resume(); }
    }

}