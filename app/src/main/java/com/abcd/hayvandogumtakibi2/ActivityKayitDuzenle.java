package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityKayitDuzenle extends AppCompatActivity {

    RelativeLayout relativeLayout;
    final Context context=this;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_duzenle);
        relativeLayout=findViewById(R.id.main_layout);
        final ImageView cross=findViewById(R.id.iptal);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initProgressBarAndTask();
    }

    void initProgressBarAndTask(){
        recyclerView=findViewById(R.id.recyclerView);
        final GridLayoutManager layoutManager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(layoutManager);
        final ProgressBar progressBar=new ProgressBar(this);
        progressBar.setIndeterminate(true);
        final RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(mLayoutParams);
        relativeLayout.addView(progressBar);
        recyclerView.animate().alpha(0f).setDuration(200).start();
        recyclerView.setAdapter(null);
        relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                relativeLayout.removeView(progressBar);
                final DuzenleAdapter duzenleAdapter =new DuzenleAdapter(context,"isim ASC");
                recyclerView.setAdapter(duzenleAdapter);
                recyclerView.animate().alpha(1f).setDuration(200).start();
            }
        },600);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initProgressBarAndTask();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        recyclerView.setAdapter(null);
    }

}
