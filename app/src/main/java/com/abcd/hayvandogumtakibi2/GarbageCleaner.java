package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class GarbageCleaner {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected static void clean_redundants(final Context context){
        try{
            final SQLiteDatabaseHelper sqLiteDatabaseHelper=SQLiteDatabaseHelper.getInstance(context);
            final ArrayList<DataModel> dataModelArrayList=sqLiteDatabaseHelper.getAllData(null,null);
            final File dizin=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
            final File[] fileList = dizin.listFiles();
            final ArrayList<String> files_in_db=new ArrayList<>();
            for(int x=0;x<dataModelArrayList.size();x++){
                files_in_db.add(dataModelArrayList.get(x).getFotograf_isim());
            }
            if(fileList!=null){
                for(File file:fileList){
                    if (!files_in_db.contains(file.getName())) {
                        new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), file.getName()).delete();
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected static void clean_caches(final Context context){
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
