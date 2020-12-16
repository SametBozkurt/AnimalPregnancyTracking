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
    private static final String THREAD_CLEANER_NAME = "CleanerThread";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        final Thread copcuThread = new Thread(new Runnable() {
            @Override
            public void run() {
                clean_redundants();
                clean_caches();
            }
        });
        copcuThread.setName(THREAD_CLEANER_NAME);
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
        try{
            final SQLiteDatabaseHelper sqLiteDatabaseHelper=SQLiteDatabaseHelper.getInstance(context);
            final ArrayList<DataModel> dataModelArrayList=sqLiteDatabaseHelper.getAllData(null,null);
            final File dizin=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
            final File[] fileList = dizin.listFiles();
            final ArrayList<String> files_in_db=new ArrayList<>();
            for(int x=0;x<dataModelArrayList.size();x++){
                files_in_db.add(dataModelArrayList.get(x).getFotograf_isim());
            }
            if(fileList!=null){
                for(File file:fileList){
                    if (!files_in_db.contains(file.getName())) {
                        new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), file.getName()).delete();
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    void clean_caches(){
        try {
            final String DIR_CACHE_GLIDE = "/image_manager_disk_cache";
            final File cachesDir = context.getCacheDir();
            final File cachesGlideDir = new File(context.getCacheDir().getAbsolutePath()+DIR_CACHE_GLIDE);
            if (cachesDir != null && cachesDir.isDirectory()) {
                long totalCacheSize=0;
                //final long dirSizeThresold_100KB=100*1024;
                final long dirSizeThresold_50MB=50*1024*1024;
                final File[] fileArray1 = cachesDir.listFiles();
                final File[] fileArray2 = cachesGlideDir.listFiles();
                for(File file:fileArray1){
                    totalCacheSize+=file.length();
                }
                for(File file:fileArray2){
                    totalCacheSize+=file.length();
                }
                //Log.e("CACHE_SIZE",String.valueOf(totalCacheSize));
                if(totalCacheSize>dirSizeThresold_50MB){
                    Glide.get(context).clearDiskCache();
                    for(File file:fileArray1){
                        if(file.isFile()){
                            file.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
