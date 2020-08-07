package com.abcd.hayvandogumtakibi2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class ActivityPeriods extends AppCompatActivity {

    //private static final String BANNER_AD_UNIT_ID = "ca-app-pub-9721232821183013/8246180827";
    private static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";
    AdView adView;
    FrameLayout adContainerView;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periods);
        final Toolbar toolbar=findViewById(R.id.toolbar);
        relativeLayout=findViewById(R.id.parent_layout);
        final RecyclerView recyclerView=findViewById(R.id.recyclerView);
        adContainerView=findViewById(R.id.ad_view_container);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                final GridLayoutManager gridLayoutManager=new GridLayoutManager(ActivityPeriods.this,3);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(new PeriodsAdapter(ActivityPeriods.this));
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ConnectivityManager connectivityManager=(ConnectivityManager)ActivityPeriods.this.getSystemService(CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if(networkInfo!=null){
                    if(networkInfo.isConnected()){
                        adContainerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MobileAds.initialize(ActivityPeriods.this, new OnInitializationCompleteListener() {
                                    @Override
                                    public void onInitializationComplete(InitializationStatus initializationStatus) {}
                                });
                                loadBanner();
                            }
                        },500);
                    }
                }
            }
        }).start();
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
