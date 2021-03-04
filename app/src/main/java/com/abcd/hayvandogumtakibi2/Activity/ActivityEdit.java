package com.abcd.hayvandogumtakibi2.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.abcd.hayvandogumtakibi2.Misc.DataModel;
import com.abcd.hayvandogumtakibi2.Misc.GarbageCleaner;
import com.abcd.hayvandogumtakibi2.R;
import com.abcd.hayvandogumtakibi2.Misc.SQLiteDatabaseHelper;
import com.abcd.hayvandogumtakibi2.Misc.TarihHesaplayici;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityEdit extends AppCompatActivity {

    private static final int GALLERY_REQ_CODE = 12321;
    private static final int CAMERA_REQ_CODE = 12322;
    private static final int PERMISSION_REQ_CODE = 21323;
    private String secilen_tur="",img_name="", sperma_name="";
    private boolean boolTarih=true, otherFieldsIsShown=false;
    private int kayit_id,petCode,calisma_sayaci=0;
    private final Context context=this;
    private final Calendar takvim=Calendar.getInstance(), takvim2=Calendar.getInstance();
    private Date date1=new Date(), date2=new Date();
    private final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    private DataModel dataModel;
    private SQLiteDatabaseHelper databaseHelper;
    private RelativeLayout main_Layout;
    private TextInputLayout textInputLayout;
    private TextInputEditText txt_isim, txt_kupe_no,dollenme_tarihi, dogum_tarihi;
    private Button kaydet;
    private Spinner tur_sec;
    private ImageView photo,iptal;
    private TarihHesaplayici tarihHesaplayici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        txt_isim=findViewById(R.id.isim);
        txt_kupe_no=findViewById(R.id.kupe_no);
        dollenme_tarihi= findViewById(R.id.dollenme_tarihi);
        dogum_tarihi= findViewById(R.id.dogum_tarihi);
        kaydet=findViewById(R.id.kaydet);
        iptal=findViewById(R.id.iptal);
        tur_sec=findViewById(R.id.spinner);
        main_Layout=findViewById(R.id.ana_katman);
        textInputLayout=findViewById(R.id.input_layout_tarih2);
        photo=findViewById(R.id.add_photo);
        final TextView txtOtherFields=findViewById(R.id.txt_other_details);
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        tarihHesaplayici=new TarihHesaplayici(this);
        dataModel=databaseHelper.getDataById(getIntent().getExtras().getInt("kayit_id"));
        degerleri_yerlestir();
        txtOtherFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!otherFieldsIsShown){
                    final LayoutInflater inflater=LayoutInflater.from(context);
                    final FrameLayout container=findViewById(R.id.other_fields_container);
                    container.setAlpha(0f);
                    final View view=inflater.inflate(R.layout.kayitlar_dgr_dtylr,container,false);
                    final EditText edit_sperma=view.findViewById(R.id.sperma_name);
                    edit_sperma.setText(sperma_name);
                    edit_sperma.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            sperma_name=s.toString();
                        }

                        @Override
                        public void afterTextChanged(Editable s) {}
                    });
                    container.addView(view);
                    container.animate().alpha(1f).setDuration(200).start();
                    otherFieldsIsShown=true;
                }
            }
        });
    }

    private void degerleri_yerlestir(){
        kayit_id=dataModel.getId();
        petCode=dataModel.getIs_evcilhayvan();
        date1.setTime(Long.parseLong(String.valueOf(dataModel.getTohumlama_tarihi())));
        takvim.setTime(date1);
        date2.setTime(Long.parseLong(String.valueOf(dataModel.getDogum_tarihi())));
        takvim2.setTime(date2);
        final ArrayAdapter<String> spinnerAdapter;
        switch(petCode){
            case 0:
                spinnerAdapter=new ArrayAdapter<>(context,R.layout.spinner_text,getResources().getStringArray(R.array.animal_list));
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_text);
                tur_sec.setAdapter(spinnerAdapter);
                break;
            case 1:
                spinnerAdapter=new ArrayAdapter<>(context,R.layout.spinner_text,getResources().getStringArray(R.array.animal_list_pet));
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_text);
                tur_sec.setAdapter(spinnerAdapter);
                break;
            case 2:
                spinnerAdapter=new ArrayAdapter<>(context,R.layout.spinner_text,getResources().getStringArray(R.array.animal_list_barn));
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_text);
                tur_sec.setAdapter(spinnerAdapter);
                break;
        }
        txt_isim.setText(dataModel.getIsim());
        txt_kupe_no.setText(dataModel.getKupe_no());
        dollenme_tarihi.setText(dateFormat.format(date1));
        dogum_tarihi.setText(dateFormat.format(date2));
        secilen_tur=dataModel.getTur();
        tur_sec.setSelection(Integer.parseInt(dataModel.getTur()));
        img_name=dataModel.getFotograf_isim();
        sperma_name=dataModel.getSperma_kullanilan();
        main_Layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                final File f=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),img_name);
                if(f.exists()&&f.isFile()){
                    Glide.with(context)
                            .load(Uri.fromFile(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),img_name)))
                            .apply(RequestOptions.circleCropTransform()).into(photo);
                }
                else{
                    Glide.with(context).load(R.drawable.icon_photo_add).into(photo);
                }
            }
        },100);
        tur_sec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(calisma_sayaci>0){
                    secilen_tur=String.valueOf(position);
                    switch (petCode){
                        case 0://Önceki sürümler için
                            if(position!=6){
                                textInputLayout.setHelperText(getString(R.string.date_input_helper_text_2));
                                if(boolTarih){
                                    tarihHesaplayici.dogum_tarihi_hesapla(petCode,secilen_tur,date1,getClass().getName());
                                }
                            }
                            else{
                                textInputLayout.setHelperText("");
                            }
                            break;
                        case 1: case 2: //Evcil & çiftlik hayvan ise
                            if(position!=3){
                                textInputLayout.setHelperText(getString(R.string.date_input_helper_text_2));
                                if(boolTarih){
                                    tarihHesaplayici.dogum_tarihi_hesapla(petCode,secilen_tur,date1,getClass().getName());
                                }
                            }
                            else{
                                textInputLayout.setHelperText("");
                            }
                            break;
                    }
                }
                else{
                    calisma_sayaci+=1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dollenme_tarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog dialog=new DatePickerDialog(context, R.style.PickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        takvim.set(year,month,dayOfMonth);
                        date1=takvim.getTime();
                        dollenme_tarihi.setText(dateFormat.format(date1));
                        boolTarih=true;
                        switch (petCode){
                            case 0://Önceki sürümler için
                                if(!secilen_tur.equals("6")){
                                    tarihHesaplayici.dogum_tarihi_hesapla(petCode,secilen_tur,date1,getClass().getName());
                                }
                                break;
                            case 1: case 2: //Evcil kodu //Besi kodu
                                if(!secilen_tur.equals("3")){
                                    tarihHesaplayici.dogum_tarihi_hesapla(petCode,secilen_tur,date1,getClass().getName());
                                }
                                break;
                        }
                    }
                },takvim.get(Calendar.YEAR),takvim.get(Calendar.MONTH),takvim.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        dogum_tarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog dialog=new DatePickerDialog(context, R.style.PickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        takvim2.set(year,month,dayOfMonth);
                        date2=takvim2.getTime();
                        dogum_tarihi.setText(dateFormat.format(date2));
                    }
                },takvim2.get(Calendar.YEAR),takvim2.get(Calendar.MONTH),takvim2.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_isim.length()==0){
                    Snackbar.make(v,getString(R.string.deger_yok_uyari),Snackbar.LENGTH_SHORT).show();
                }
                else if(dogum_tarihi.length()==0){
                    Snackbar.make(v,getString(R.string.deger_yok_uyari),Snackbar.LENGTH_SHORT).show();
                }
                else{
                    final File f=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),img_name);
                    final DataModel model;
                    if(f.exists()&&f.isFile()){
                        model=new DataModel(kayit_id,txt_isim.getText().toString(),
                                secilen_tur,txt_kupe_no.getText().toString(),String.valueOf(date1.getTime()),
                                String.valueOf(date2.getTime()),img_name,0,dataModel.getDogum_grcklsti(),sperma_name);
                    }
                    else{
                        model=new DataModel(kayit_id,txt_isim.getText().toString(),
                                secilen_tur,txt_kupe_no.getText().toString(),String.valueOf(date1.getTime()),
                                String.valueOf(date2.getTime()),"",0,dataModel.getDogum_grcklsti(),sperma_name);
                    }
                    databaseHelper.guncelle(model);
                    finish();
                    startActivity(new Intent(context,PrimaryActivity.class));
                }
            }
        });
        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu=new PopupMenu(context,photo);
                popupMenu.getMenuInflater().inflate(R.menu.popup_photo_picker,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.camera) {
                            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1){
                                if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){
                                    ActivityCompat.requestPermissions(ActivityEdit.this,new String[]{Manifest.permission.CAMERA},PERMISSION_REQ_CODE);
                                }
                                else{
                                    open_cam();
                                }
                            }
                            else{
                                open_cam();
                            }
                        }
                        else if(item.getItemId()==R.id.gallery){
                            final Intent gallery_intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            gallery_intent.setType("image/*");
                            gallery_intent.putExtra(Intent.EXTRA_MIME_TYPES,new String[]{"image/jpeg","image/png","image/jpg"});
                            gallery_intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(gallery_intent,GALLERY_REQ_CODE);
                        }
                        else if(item.getItemId()==R.id.remove){
                            final File f=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),img_name);
                            if(f.exists()&&f.isFile()){
                                f.delete();
                            }
                            Glide.with(context).load(R.drawable.icon_photo_add).into(photo);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        tarihHesaplayici.setDateChangeListener(new TarihHesaplayici.DateChangeListener() {
            @Override
            public void onNewDateCalculated(Date dateCalculated) {
                date2=dateCalculated;
                dogum_tarihi.setText(dateFormat.format(dateCalculated));
                Snackbar.make(main_Layout,R.string.otomatik_hesaplandi_bildirim,Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            switch (requestCode){
                case GALLERY_REQ_CODE:
                    if(resultCode==RESULT_OK){
                        launchImageCrop(data.getData());
                    }
                    break;
                case CAMERA_REQ_CODE:
                    if(resultCode==RESULT_OK){
                        final File file=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),img_name);
                        launchImageCrop(Uri.fromFile(file));
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    final CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if(resultCode==RESULT_OK){
                        Glide.with(context).load(result.getUri()).apply(RequestOptions.circleCropTransform()).into(photo);
                        try {
                            final Bitmap photoBitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),result.getUri());
                            save_photo(photoBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                GarbageCleaner.clean_redundants(context);
            }
        });
        thread.start();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper=null;
        if(main_Layout!=null){
            main_Layout.removeAllViews();
            main_Layout=null;
        }
    }

    private void launchImageCrop(final Uri uri){
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle(getString(R.string.crop_text))
                .start(this);
    }

    private File getImageFile() {
        final File imgFile=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ANM_" + System.currentTimeMillis() +".jpg");
        final File eski_dosya=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),img_name);
        if(eski_dosya.exists()&&eski_dosya.isFile()){
            eski_dosya.delete();
        }
        img_name=imgFile.getName();
        return imgFile;
    }

    private void save_photo(final Bitmap bitmap){
        int croped_width=bitmap.getWidth();
        int croped_height=bitmap.getHeight();
        while(croped_width>1000){
            final double target_resolution=croped_width/1.1;
            croped_width=(int)target_resolution;
            croped_height=(int)target_resolution;
            /*Bu işlemle kaydedilecek fotoğraf adım adım küçültülerek piksel sayısı 1000'in altında
            ve olabildiğince 1000'e yakın tutularak fotoğrafın netliği çok bozulmadan depolamanın ve belleğin şişmesi önlenir.*/
        }
        try{
            final FileOutputStream fileOutputStream=new FileOutputStream(getImageFile());
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,croped_width,croped_height,true);
            Bitmap.createBitmap(scaledBitmap,0,0,croped_width,croped_height,null,true).
                    compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void open_cam(){
        final Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera_intent.resolveActivity(getPackageManager())!=null) {
            final File photoFile=getImageFile();
            if (photoFile != null) {
                final Uri photoURI= FileProvider.getUriForFile(context,getPackageName(),photoFile);
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(camera_intent, CAMERA_REQ_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            open_cam();
        }
    }

}
