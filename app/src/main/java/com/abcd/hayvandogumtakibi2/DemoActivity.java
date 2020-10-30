package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DemoActivity extends AppCompatActivity {

    final Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        final Thread copcuThread = new Thread(new Runnable() {
            @Override
            public void run() {
                clean_redundants();
            }
        });
        copcuThread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(context, PrimaryActivity.class));
            }
        },1000);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    void clean_redundants(){
        final SQLiteDatabaseHelper sqLiteDatabaseHelper=SQLiteDatabaseHelper.getInstance(context);
        final ArrayList<DataModel> dataModelArrayList=sqLiteDatabaseHelper.getSimpleData(null,null);
        final File dizin=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
        final File[] fileList = dizin.listFiles();
        final ArrayList<String> files_in_db=new ArrayList<>();
        for(int x=0;x<dataModelArrayList.size();x++){
            files_in_db.add(dataModelArrayList.get(x).getFotograf_isim());
        }
        for (File file : fileList) {
            if (!files_in_db.contains(file.getName())) {
                new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), file.getName()).delete();
            }
        }
        try {
            final File caches = new File(context.getCacheDir().getAbsolutePath()+"/image_manager_disk_cache");
            if (caches != null && caches.isDirectory()) {
                long totalCacheSize=0;
                //final long dirSizeThresold_100KB=100*1024;
                final long dirSizeThresold_100MB=100*1024*1024;
                final File[] cacheFileArray = caches.listFiles();
                for(File file:cacheFileArray){
                    totalCacheSize=totalCacheSize+file.length();
                }
                if(totalCacheSize>dirSizeThresold_100MB){
                    Glide.get(context).clearDiskCache();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
