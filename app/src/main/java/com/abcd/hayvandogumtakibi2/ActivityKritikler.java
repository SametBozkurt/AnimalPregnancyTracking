package com.abcd.hayvandogumtakibi2;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class ActivityKritikler extends AppCompatActivity {

    //private static final String BANNER_AD_UNIT_ID = "ca-app-pub-9721232821183013/8246180827";
    private static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";
    private SQLiteDatabaseHelper databaseHelper;
    private ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    private FrameLayout adContainerView;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kritikler);
        final Toolbar toolbar = findViewById(R.id.activity_toolbar);
        adContainerView=findViewById(R.id.ad_view_container);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        hayvanVerilerArrayList=databaseHelper.getKritikOlanlar();
        if(hayvanVerilerArrayList.size()==0){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentYaklasanDogumYok()).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentYaklasanDogumlar()).commit();
        }
        final ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null){
            if(networkInfo.isConnected()){
                adContainerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MobileAds.initialize(ActivityKritikler.this, new OnInitializationCompleteListener() {
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
    protected void onStop() {
        super.onStop();
        databaseHelper=null;
        hayvanVerilerArrayList=null;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        hayvanVerilerArrayList=databaseHelper.getKritikOlanlar();
        if(hayvanVerilerArrayList.size()==0){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentYaklasanDogumYok()).commitAllowingStateLoss();
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentYaklasanDogumlar()).commitAllowingStateLoss();
        }
    }

    @Override
    protected void onDestroy() {
        if (adView != null) { adView.destroy(); }
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
