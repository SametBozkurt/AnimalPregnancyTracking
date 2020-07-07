package com.abcd.hayvandogumtakibi2;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class PrimaryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String ADMOB_TEST_ID1 = "ca-app-pub-3940256099942544/1033173712";
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-9721232821183013/5088109999";
    int database_size;
    byte ad_show_counter=0;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    InterstitialAd mInterstitialAd = new InterstitialAd(this);
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(PrimaryActivity.this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        database_size=SQLiteDatabaseHelper.getInstance(this).getSize();
        dosya_kontrol();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        if(ad_show_counter==0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    show_ads();
                }
            }, 6000);
            ad_show_counter+=1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id=item.getItemId();
        if(item_id==R.id.kayit_bul){
            if(database_size==0){
                Snackbar.make(findViewById(R.id.relativeLayout),getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
            }
            else{
                startActivity(new Intent(PrimaryActivity.this,ActivityKayitAra.class));
            }
        }
        else if(item_id==R.id.dev_tools){
            startActivity(new Intent(PrimaryActivity.this,ActivityDevTools.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent i=new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_critics:
                if(database_size==0){
                    Snackbar.make(findViewById(R.id.relativeLayout),getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityKritikler.class));
                }
                break;
            case R.id.nav_edit:
                if(database_size==0){
                    Snackbar.make(findViewById(R.id.relativeLayout),getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityKayitDuzenle.class));
                }
                break;
            case R.id.nav_search:
                if(database_size==0){
                    Snackbar.make(findViewById(R.id.relativeLayout),getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityKayitAra.class));
                }
                break;
            case R.id.nav_calculator:
                startActivity(new Intent(PrimaryActivity.this,ActivityTarihHesapla.class));
                break;
            case R.id.nav_info:
                String ver="";
                try{
                    PackageInfo packageInfo=getPackageManager().getPackageInfo(PrimaryActivity.this.getPackageName(),0);
                    ver=packageInfo.versionName;
                }
                catch(PackageManager.NameNotFoundException e){
                    e.printStackTrace();
                }
                final Dialog dialog=new Dialog(PrimaryActivity.this,R.style.AppInfoDialogStyle);
                dialog.setContentView(R.layout.app_info_layout);
                TextView txt_version=dialog.findViewById(R.id.app_version);
                Button vote=dialog.findViewById(R.id.btn_cancel);
                txt_version.setText(new StringBuilder(PrimaryActivity.this.getString(R.string.app_version)).append(ver));
                vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.APP_URL)));
                        startActivity(intent);
                    }
                });
                dialog.show();
                break;
            case R.id.nav_policy:
                startActivity(new Intent(Intent.ACTION_VIEW).setData(
                        Uri.parse(getString(R.string.POLICY_URL))));
                break;
            case R.id.nav_period:
                startActivity(new Intent(PrimaryActivity.this,ActivityPeriods.class));
                break;
            case R.id.nav_happened:
                if(database_size==0){
                    Snackbar.make(findViewById(R.id.relativeLayout),getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else {
                    startActivity(new Intent(this,ActivityGerceklesenler.class));
                }
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void dosya_kontrol() {
        if(database_size==0){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentKayitYok(this)).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new FragmentKayitlar(this)).commit();
        }
    }

    private void show_ads(){
        mInterstitialAd.setAdUnitId(ADMOB_TEST_ID1);
        adRequest=new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(PrimaryActivity.this,"Ad Yüklendi.",Toast.LENGTH_SHORT).show();
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                //Toast.makeText(PrimaryActivity.this,"Ad Yüklenemedi, Kod: "+errorCode,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdOpened() {
                //Log.e("DURUM","Ad Açıldı");
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                //Log.e("DURUM","Ad Tıklandı");
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                //Log.e("DURUM","Kullanıcı Adden ayrıldı");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                //Log.e("DURUM","Ad Kapandı");
                mInterstitialAd.setAdListener(null);
                adRequest=null;
                mInterstitialAd=null;
            }
        });
    }

}