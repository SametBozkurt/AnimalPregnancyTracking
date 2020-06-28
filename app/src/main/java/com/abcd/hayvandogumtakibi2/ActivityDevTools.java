package com.abcd.hayvandogumtakibi2;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import java.util.Calendar;
import java.util.Random;

public class ActivityDevTools extends AppCompatActivity {

    private static final long one_day_in_millis = 1000*60*60*24;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,PrimaryActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_tools);
        final long today_in_millis=Calendar.getInstance().getTimeInMillis();
        final int[] sayac = {2};
        final Button btn=findViewById(R.id.kayit_olustur);
        SeekBar seekBar=findViewById(R.id.seekbar);
        btn.setText(new StringBuilder(getString(R.string.button_multiple_creation)).append(": ").append(sayac[0]));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(ActivityDevTools.this);
                progressDialog.setTitle("İşlem Gerçekleştiriliyor");
                progressDialog.setMessage("Lütfen bekleyiniz.");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabaseHelper sqLiteDatabaseHelper=new SQLiteDatabaseHelper(ActivityDevTools.this);
                        int id=0;
                        int dgm_grcklsti=0;
                        for(int i=0;i<sayac[0];i++){
                            String isim="test"+ i;
                            String tur=String.valueOf(new Random().nextInt(4));
                            String kupe_no="0000"+ i;
                            String tarih1=String.valueOf(today_in_millis-(one_day_in_millis*(27+i)));
                            String tarih2=String.valueOf(today_in_millis+(one_day_in_millis*(27+i)));
                            int petCode=new Random().nextInt(2)+1;
                            sqLiteDatabaseHelper.veri_yaz(new HayvanVeriler(id,isim,tur,kupe_no,tarih1,tarih2,null,petCode,dgm_grcklsti));
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }).start();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sayac[0] = progress;
                btn.setText(new StringBuilder(getString(R.string.button_multiple_creation)).append(": ").append(sayac[0]));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}