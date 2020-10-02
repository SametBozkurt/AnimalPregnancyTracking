package com.abcd.hayvandogumtakibi2;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
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

    private static final String INTERSTITIAL_TEST_ID = "ca-app-pub-3940256099942544/1033173712";
    //private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-9721232821183013/5088109999";
    int database_size;
    InterstitialAd mInterstitialAd = new InterstitialAd(this);
    AdRequest adRequest;
    final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(this);
    RelativeLayout relativeLayout;
    FrameLayout fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_primary);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        fragment_container=findViewById(R.id.fragment_container);
        relativeLayout=findViewById(R.id.main_layout);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(PrimaryActivity.this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N && database_size!=0){
            final String INTENT_ACTION= "SET_AN_ALARM" ;
            PrimaryActivity.this.sendBroadcast(new Intent(PrimaryActivity.this,TarihKontrol.class).setAction(INTENT_ACTION));
        }
        relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                final ConnectivityManager connectivityManager=(ConnectivityManager)PrimaryActivity.this.getSystemService(CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if(networkInfo!=null){
                    if(networkInfo.isConnected()){
                        MobileAds.initialize(PrimaryActivity.this, new OnInitializationCompleteListener() {
                            @Override
                            public void onInitializationComplete(InitializationStatus initializationStatus) {}
                        });
                        show_ads();
                    }
                }
            }
        },500);
        setProgressBarIndeterminateVisibility(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragment_container.removeAllViews();
        database_size=databaseHelper.getSize();
        if(database_size==0){
            final FragmentKayitYok fragmentKayitYok=new FragmentKayitYok();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentKayitYok).commitAllowingStateLoss();
        }
        else{
            final FragmentKayitlar fragmentKayitlar=new FragmentKayitlar();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentKayitlar).commitAllowingStateLoss();
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
                Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
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
        final Intent i=new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        final int id = item.getItemId();
        switch (id){
            case R.id.nav_critics:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityKritikler.class));
                }
                break;
            case R.id.nav_edit:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityKayitDuzenle.class));
                }
                break;
            case R.id.nav_search:
                if(database_size==0){
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
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
                    final PackageInfo packageInfo=getPackageManager().getPackageInfo(PrimaryActivity.this.getPackageName(),0);
                    ver=packageInfo.versionName;
                }
                catch(PackageManager.NameNotFoundException e){
                    e.printStackTrace();
                }
                final Dialog dialog=new Dialog(PrimaryActivity.this,R.style.AppInfoDialogStyle);
                dialog.setContentView(R.layout.app_info_layout);
                final TextView txt_version=dialog.findViewById(R.id.app_version);
                final Button vote=dialog.findViewById(R.id.btn_cancel);
                txt_version.setText(new StringBuilder(PrimaryActivity.this.getString(R.string.app_version)).append(ver));
                vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent=new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.APP_URL)));
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
                    Snackbar.make(relativeLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                }
                else {
                    startActivity(new Intent(this,ActivityGerceklesenler.class));
                }
                break;
        }
        return true;
    }


    private void show_ads(){
        mInterstitialAd.setAdUnitId(INTERSTITIAL_TEST_ID);
        adRequest=new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
            @Override
            public void onAdClosed() {
                mInterstitialAd.setAdListener(null);
                adRequest=null;
                mInterstitialAd=null;
            }
        });
    }
}

