package com.abcd.hayvandogumtakibi2;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class PrimaryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String POLICIY_URL = "https://sametbozkurt0.blogspot.com/2019/09/samet-bozkurt-built-animal-pregnancy.html";
    private static final int PERMISSION_REQ_CODE = 21323;
    SQLiteDatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    FloatingActionButton btn_add;
    CoordinatorLayout coordinatorLayout;
    private static final String INTENT_ACTION= "SET_AN_ALARM" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SQLiteDatabaseHelper(PrimaryActivity.this);
        databaseHelper=new SQLiteDatabaseHelper(PrimaryActivity.this);
        hayvanVerilerArrayList=databaseHelper.getAllData();
        izinler();
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
            if(hayvanVerilerArrayList.size()==0){
                Snackbar.make(coordinatorLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
            }
            else{
                startActivity(new Intent(PrimaryActivity.this,ActivityKayitAra.class));
            }
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_critics:
                if(hayvanVerilerArrayList.size()==0){
                    Snackbar.make(coordinatorLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                    break;
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityKritikler.class));
                    break;
                }
            case R.id.nav_edit:
                if(hayvanVerilerArrayList.size()==0){
                    Snackbar.make(coordinatorLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                    break;
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityKayitDuzenle.class));
                    break;
                }
            case R.id.nav_search:
                if(hayvanVerilerArrayList.size()==0){
                    Snackbar.make(coordinatorLayout,getString(R.string.kayit_yok_uyari2),Snackbar.LENGTH_LONG).show();
                    break;
                }
                else{
                    startActivity(new Intent(PrimaryActivity.this,ActivityKayitAra.class));
                    break;
                }
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
                Button cancel=dialog.findViewById(R.id.btn_cancel);
                txt_version.setText(new StringBuilder(PrimaryActivity.this.getString(R.string.app_version)).append(ver));
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.nav_policy:
                startActivity(new Intent(Intent.ACTION_VIEW).setData(
                        Uri.parse(POLICIY_URL)));
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void dosya_kontrol(ArrayList<HayvanVeriler> hayvanVeriler){
        if(hayvanVeriler.size()==0){
            setContentView(R.layout.activity_primary_msg);
            coordinatorLayout=findViewById(R.id.main_layout);
            toolbar = findViewById(R.id.toolbar);
            btn_add = findViewById(R.id.create);
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
                    startActivity(new Intent(PrimaryActivity.this,ActivityDogumKayit.class));
                }
            });
        }
        else{
            setContentView(R.layout.activity_primary);
            coordinatorLayout=findViewById(R.id.main_layout);
            toolbar = findViewById(R.id.toolbar);
            databaseHelper=new SQLiteDatabaseHelper(PrimaryActivity.this);
            btn_add = findViewById(R.id.create);
            drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            hayvanVerilerArrayList=databaseHelper.getAllData();
            setSupportActionBar(toolbar);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(PrimaryActivity.this,ActivityDogumKayit.class));
                }
            });
            navigationView.setNavigationItemSelectedListener(this);
            toggle = new ActionBarDrawerToggle(PrimaryActivity.this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            recyclerView=findViewById(R.id.recyclerView);
            KayitlarAdapter kayitlarAdapter =new KayitlarAdapter(PrimaryActivity.this,hayvanVeriler);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(PrimaryActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(kayitlarAdapter);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                sendBroadcast(new Intent(PrimaryActivity.this,TarihKontrol.class).setAction(INTENT_ACTION));
            }
        }
    }
    public void izinler(){
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1){
            if(ContextCompat.checkSelfPermission(PrimaryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED||ContextCompat.checkSelfPermission(PrimaryActivity.this,Manifest.permission.CAMERA)==
                    PackageManager.PERMISSION_DENIED){
                setContentView(R.layout.layout_req_perms);
                Button btn_allow=findViewById(R.id.allow);
                btn_allow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        izin_kontrol();
                    }
                });
            }
            else{
                dosya_kontrol(hayvanVerilerArrayList);
            }
        }
        else{
            dosya_kontrol(hayvanVerilerArrayList);
        }
    }
    public void izin_kontrol(){
        if(ContextCompat.checkSelfPermission(PrimaryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(PrimaryActivity.this,Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            //Kamera ve depolama izinlerini ister.
            ActivityCompat.requestPermissions(PrimaryActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, PERMISSION_REQ_CODE);
        }
        else if(ContextCompat.checkSelfPermission(PrimaryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(PrimaryActivity.this,Manifest.permission.CAMERA)==
                PackageManager.PERMISSION_GRANTED){
            //Sadece depolama izni ister.
            ActivityCompat.requestPermissions(PrimaryActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        }
        else if(ContextCompat.checkSelfPermission(PrimaryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(PrimaryActivity.this,Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            //Sadece kamera izni ister.
            ActivityCompat.requestPermissions(PrimaryActivity.this,new String[]{Manifest.permission.CAMERA}, PERMISSION_REQ_CODE);
        }
        else{
            dosya_kontrol(hayvanVerilerArrayList);
            //İzinler tamamdır\O/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==
                PackageManager.PERMISSION_GRANTED){
            dosya_kontrol(hayvanVerilerArrayList);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}