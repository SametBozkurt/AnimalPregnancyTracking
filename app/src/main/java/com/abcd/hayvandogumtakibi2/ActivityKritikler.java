package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
    private RelativeLayout relativeLayout;
    final Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kritikler);
        relativeLayout=findViewById(R.id.parent);
        adContainerView=findViewById(R.id.ad_view_container);
        final ImageView cross=findViewById(R.id.iptal);
        fragment_container=findViewById(R.id.fragment_container);
        databaseHelper=SQLiteDatabaseHelper.getInstance(context);
        dataModelArrayList=databaseHelper.getKritikOlanlar();
        adContainerView.postDelayed(new Runnable() {
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
                        loadBanner();
                    }
                }
            }
        },500);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
        dataModelArrayList=databaseHelper.getKritikOlanlar();
        if(dataModelArrayList.size()==0){
            final FragmentYaklasanDogumYok fragmentYaklasanDogumYok=new FragmentYaklasanDogumYok();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentYaklasanDogumYok).commitAllowingStateLoss();
        }
        else{
            final FragmentYaklasanDogumlar fragmentYaklasanDogumlar=new FragmentYaklasanDogumlar();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentYaklasanDogumlar).commitAllowingStateLoss();
        }
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            databaseHelper=null;
            adView.destroy();
            adContainerView.removeAllViews();
            relativeLayout.removeAllViews();
            fragment_container.removeAllViews();
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

}
