package com.abcd.hayvandogumtakibi2;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PrimaryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String pref_key = "dates_converted";
    private static final String pref_file = "preferences";
    boolean is_opened = false;
    private int database_size;
    private TextView txt_pet,txt_barn;
    private SQLiteDatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private FloatingActionButton btn_add,btn_pet,btn_barn;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private SharedPreferences preferences;
    private ArrayList<HayvanVeriler> hayvanVerilerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        izin_kontrol();
        super.onCreate(savedInstanceState);
        fab_open= AnimationUtils.loadAnimation(this,R.anim.fab_on);
        fab_close=AnimationUtils.loadAnimation(this,R.anim.fab_off);
        fab_clock=AnimationUtils.loadAnimation(this,R.anim.rotation_clock);
        fab_anticlock=AnimationUtils.loadAnimation(this,R.anim.rotation_anticlock);
        new SQLiteDatabaseHelper(PrimaryActivity.this);
        databaseHelper=new SQLiteDatabaseHelper(PrimaryActivity.this);
        database_size=databaseHelper.getSize();
        preferences = this.getSharedPreferences(pref_file,MODE_PRIVATE);
        dosya_kontrol();
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
                    ver+=packageInfo.versionName;
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
        Toolbar toolbar;
        DrawerLayout drawer;
        ActionBarDrawerToggle toggle;
        NavigationView navigationView;
        if(database_size==0){
            setContentView(R.layout.activity_primary_msg);
            toolbar = findViewById(R.id.toolbar);
            btn_add = findViewById(R.id.create);
            btn_pet = findViewById(R.id.fab_pet);
            btn_barn = findViewById(R.id.fab_barn);
            txt_pet = findViewById(R.id.text_pet);
            txt_barn = findViewById(R.id.text_barn);
            setSupportActionBar(toolbar);
            drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            toggle = new ActionBarDrawerToggle(PrimaryActivity.this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(is_opened){
                        txt_pet.startAnimation(fab_close);
                        btn_pet.startAnimation(fab_close);
                        txt_barn.startAnimation(fab_close);
                        btn_barn.startAnimation(fab_close);
                        btn_add.startAnimation(fab_anticlock);
                        btn_pet.setClickable(false);
                        btn_barn.setClickable(false);
                        is_opened=false;
                    }
                    else{
                        txt_pet.startAnimation(fab_open);
                        btn_pet.startAnimation(fab_open);
                        txt_barn.startAnimation(fab_open);
                        btn_barn.startAnimation(fab_open);
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
                    PrimaryActivity.this.startActivity(bundle_intent);
                }
            });
            btn_barn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent bundle_intent=new Intent(PrimaryActivity.this,ActivityDogumKayit.class);
                    Bundle datas=new Bundle();
                    datas.putInt("isPet",2);
                    bundle_intent.putExtras(datas);
                    PrimaryActivity.this.startActivity(bundle_intent);
                }
            });
        }
        else{
            setContentView(R.layout.activity_primary);
            toolbar = findViewById(R.id.toolbar);
            databaseHelper=new SQLiteDatabaseHelper(PrimaryActivity.this);
            btn_add = findViewById(R.id.create);
            btn_pet = findViewById(R.id.fab_pet);
            btn_barn = findViewById(R.id.fab_barn);
            txt_pet = findViewById(R.id.text_pet);
            txt_barn = findViewById(R.id.text_barn);
            Spinner goruntuleme_kategorisi = findViewById(R.id.kategori);
            ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(this,R.layout.spinner_kategori,
                    getResources().getStringArray(R.array.goruntuleme_kategorileri));
            spinner_adapter.setDropDownViewResource(R.layout.spinner_kategori);
            drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            setSupportActionBar(toolbar);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(is_opened){
                        txt_pet.startAnimation(fab_close);
                        btn_pet.startAnimation(fab_close);
                        txt_barn.startAnimation(fab_close);
                        btn_barn.startAnimation(fab_close);
                        btn_add.startAnimation(fab_anticlock);
                        btn_pet.setClickable(false);
                        btn_barn.setClickable(false);
                        is_opened=false;
                    }
                    else{
                        txt_pet.startAnimation(fab_open);
                        btn_pet.startAnimation(fab_open);
                        txt_barn.startAnimation(fab_open);
                        btn_barn.startAnimation(fab_open);
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
                    PrimaryActivity.this.startActivity(bundle_intent);
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
                    PrimaryActivity.this.startActivity(bundle_intent);
                    //Besi hayvanı ise 2
                }
            });
            hayvanVerilerArrayList = databaseHelper.getSimpleData();
            recyclerView=findViewById(R.id.recyclerView);
            GridLayoutManager layoutManager=new GridLayoutManager(PrimaryActivity.this,3);
            recyclerView.setLayoutManager(layoutManager);
            goruntuleme_kategorisi.setAdapter(spinner_adapter);
            goruntuleme_kategorisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                    recyclerView.setAdapter(new KayitlarAdapter(PrimaryActivity.this,hayvanVerilerArrayList,position));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            navigationView.setNavigationItemSelectedListener(this);
            toggle = new ActionBarDrawerToggle(PrimaryActivity.this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            tarih_donustur();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                String INTENT_ACTION= "SET_AN_ALARM" ;
                sendBroadcast(new Intent(PrimaryActivity.this,TarihKontrol.class).setAction(INTENT_ACTION));
            }
        }
    }

    private void izin_kontrol() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            startActivity(new Intent(this,ActivityPermission.class));
        }
    }

    private void tarih_donustur() {
        if(!preferences.contains(pref_key)){
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt(pref_key,1);
            editor.apply();
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle(R.string.dialog_title1);
            progressDialog.setMessage(getString(R.string.dialog_msg1));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                    int sayac=0;
                    Date date_dollenme = new Date(),date_dogum = new Date();
                    while(sayac<hayvanVerilerArrayList.size()) {
                        try {
                            date_dollenme.setTime(simpleDateFormat.parse(hayvanVerilerArrayList.get(sayac).getTohumlama_tarihi()).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date_dollenme.setTime(Long.parseLong(hayvanVerilerArrayList.get(sayac).getTohumlama_tarihi()));
                        }
                        try {
                            date_dogum.setTime(simpleDateFormat.parse(hayvanVerilerArrayList.get(sayac).getDogum_tarihi()).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date_dogum.setTime(Long.parseLong(hayvanVerilerArrayList.get(sayac).getDogum_tarihi()));
                        }
                        databaseHelper.tarih_donustur(hayvanVerilerArrayList.get(sayac).getId(),
                                String.valueOf(date_dollenme.getTime()),
                                String.valueOf(date_dogum.getTime()));
                        sayac++;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                    hayvanVerilerArrayList=databaseHelper.getSimpleData();
                }
            }).start();
        }
    }
}