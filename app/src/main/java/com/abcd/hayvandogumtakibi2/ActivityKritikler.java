package com.abcd.hayvandogumtakibi2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityKritikler extends AppCompatActivity {

    SQLiteDatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        dosya_kontrol();
    }

    private void dosya_kontrol(){
        hayvanVerilerArrayList=databaseHelper.getKritikOlanlar();
        if(hayvanVerilerArrayList.size()==0){
            setContentView(R.layout.yaklasan_dogum_yok);
        }
        else{
            setContentView(R.layout.activity_kritikler);
            toolbar=findViewById(R.id.activity_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            recyclerView=findViewById(R.id.recyclerView);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(ActivityKritikler.this,3);
            recyclerView.setLayoutManager(gridLayoutManager);
            final ProgressDialog dialog=new ProgressDialog(this);
            dialog.setTitle(R.string.dialog_title1);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
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
                            recyclerView.setAdapter(new KritiklerAdapter(ActivityKritikler.this, hayvanVerilerArrayList));
                            dialog.dismiss();
                        }
                    });
                }
            }).start();
        }
    }
}
