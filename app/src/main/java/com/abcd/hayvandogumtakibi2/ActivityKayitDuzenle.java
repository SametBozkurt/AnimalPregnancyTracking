package com.abcd.hayvandogumtakibi2;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import java.util.ArrayList;

public class ActivityKayitDuzenle extends AppCompatActivity {

    //private static final String BANNER_AD_UNIT_ID = "ca-app-pub-9721232821183013/8246180827";
    private static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111";
    RecyclerView recyclerView;
    SQLiteDatabaseHelper databaseHelper;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    Toolbar toolbar;
    AdView adView;
    FrameLayout adContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_duzenle);
        toolbar=findViewById(R.id.activity_toolbar);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        adContainerView=findViewById(R.id.ad_view_container);
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        final ProgressDialog[] dialog = {new ProgressDialog(this)};
        dialog[0].setTitle(R.string.dialog_title1);
        dialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog[0].setCancelable(false);
        dialog[0].show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        hayvanVerilerArrayList=databaseHelper.getAllData();
                        final DuzenleAdapter duzenleAdapter =new DuzenleAdapter(ActivityKayitDuzenle.this,hayvanVerilerArrayList);
                        recyclerView.setAdapter(duzenleAdapter);
                        dialog[0].dismiss();
                        dialog[0] =null;
                    }
                });
            }
        }).start();
        final ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null){
            if(networkInfo.isConnected()){
                adContainerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MobileAds.initialize(ActivityKayitDuzenle.this, new OnInitializationCompleteListener() {
                            @Override
                            public void onInitializationComplete(InitializationStatus initializationStatus) {

                            }
                        });
                        loadBanner();
                    }
                },500);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        final ProgressDialog[] dialog = {new ProgressDialog(this)};
        dialog[0].setTitle(R.string.dialog_title1);
        dialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog[0].setCancelable(false);
        dialog[0].show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        hayvanVerilerArrayList=databaseHelper.getAllData();
                        final DuzenleAdapter duzenleAdapter =new DuzenleAdapter(ActivityKayitDuzenle.this,hayvanVerilerArrayList);
                        recyclerView.setAdapter(duzenleAdapter);
                        dialog[0].dismiss();
                        dialog[0] =null;
                    }
                });
            }
        }).start();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hayvanVerilerArrayList=null;
        recyclerView.setAdapter(null);
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
}
