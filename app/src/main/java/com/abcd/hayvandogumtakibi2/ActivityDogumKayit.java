package com.abcd.hayvandogumtakibi2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;

public class ActivityDogumKayit extends AppCompatActivity implements CalendarTools{

    private static final int GALLERY_REQ_CODE=12321;
    private static final int CAMERA_REQ_CODE = 12322;
    private int _isPet;
    private boolean boolTarih=false, otherFieldsIsShown=false;
    private final Calendar gecerli_takvim=Calendar.getInstance();
    private Calendar hesaplanan_tarih=Calendar.getInstance();
    private final SQLiteDatabaseHelper dbYoneticisi=SQLiteDatabaseHelper.getInstance(this);
    private final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    private Date date_dollenme, date_dogum;
    private RelativeLayout main_Layout;
    private EditText edit_isim,edit_kupe_no,btn_tarih_dogum,btn_tarih_dollenme;
    private ImageView photo, iptal;
    private Spinner spinner_turler;
    private TextInputLayout textInputLayout;
    private String secilen_tur="0";
    private String gorsel_ad="";
    private String sperma_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogum_kayit);
        photo=findViewById(R.id.add_photo);
        edit_isim=findViewById(R.id.isim);
        edit_kupe_no=findViewById(R.id.kupe_no);
        spinner_turler=findViewById(R.id.spinner);
        final Button kaydet=findViewById(R.id.kaydet);
        iptal=findViewById(R.id.iptal);
        btn_tarih_dollenme=findViewById(R.id.dollenme_tarihi);
        btn_tarih_dogum=findViewById(R.id.dogum_tarihi);
        main_Layout=findViewById(R.id.ana_katman);
        textInputLayout=findViewById(R.id.input_layout_tarih2);
        final TextView txtOtherFields=findViewById(R.id.txt_other_details);
        textInputLayout.setHelperText(getString(R.string.date_input_helper_text_2));
        date_dollenme=gecerli_takvim.getTime();
        _isPet=getIntent().getExtras().getInt("isPet");
        ArrayAdapter<String> spinner_adapter;
        switch (_isPet){
            case 1: //Evcil hayvan ise
                spinner_adapter=new ArrayAdapter<>(this,R.layout.spinner_text,
                        getResources().getStringArray(R.array.animal_list_pet));
                spinner_adapter.setDropDownViewResource(R.layout.spinner_text);
                spinner_turler.setAdapter(spinner_adapter);
                break;
            case 2: //Besi hayvanı ise
                spinner_adapter=new ArrayAdapter<>(this,R.layout.spinner_text,
                        getResources().getStringArray(R.array.animal_list_barn));
                spinner_adapter.setDropDownViewResource(R.layout.spinner_text);
                spinner_turler.setAdapter(spinner_adapter);
                break;
        }
        spinner_turler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilen_tur=String.valueOf(position);
                switch (_isPet){
                    case 1: //Evcil hayvan ise
                        if(position!=3){
                            textInputLayout.setHelperText(getString(R.string.date_input_helper_text_2));
                            oto_tarih_hesapla(date_dollenme);
                        }
                        else{
                            textInputLayout.setHelperText("");
                        }
                        break;
                    case 2: //Besi hayvanı ise
                        if(position!=3){
                            textInputLayout.setHelperText(getString(R.string.date_input_helper_text_2));
                            oto_tarih_hesapla(date_dollenme);
                        }
                        else{
                            textInputLayout.setHelperText("");
                        }
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_tarih_dollenme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog dialog=new DatePickerDialog(ActivityDogumKayit.this, R.style.PickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        gecerli_takvim.set(year,month,dayOfMonth);
                        date_dollenme=gecerli_takvim.getTime();
                        btn_tarih_dollenme.setText(dateFormat.format(date_dollenme));
                        boolTarih=true;
                        switch (_isPet){
                            case 1: //Evcil hayvan ise
                                if(!secilen_tur.equals("3")){
                                    oto_tarih_hesapla(date_dollenme);
                                }
                                break;
                            case 2: //Besi hayvanı ise
                                if(!secilen_tur.equals("3")){
                                    oto_tarih_hesapla(date_dollenme);
                                }
                                break;
                        }
                    }
                },gecerli_takvim.get(Calendar.YEAR),gecerli_takvim.get(Calendar.MONTH),gecerli_takvim.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        btn_tarih_dogum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog dialog = new DatePickerDialog(ActivityDogumKayit.this, R.style.PickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        hesaplanan_tarih.set(year,month,dayOfMonth);
                        date_dogum=hesaplanan_tarih.getTime();
                        btn_tarih_dogum.setText(dateFormat.format(date_dogum));
                    }
                },hesaplanan_tarih.get(Calendar.YEAR),hesaplanan_tarih.get(Calendar.MONTH),hesaplanan_tarih.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayit_gir(v);
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
                final PopupMenu popupMenu=new PopupMenu(ActivityDogumKayit.this,photo);
                popupMenu.getMenuInflater().inflate(R.menu.popup_photo_picker,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.camera) {
                            final Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (camera_intent.resolveActivity(getPackageManager())!=null) {
                                final File photoFile=getImageFile();
                                if (photoFile != null) {
                                    final Uri photoURI= FileProvider.getUriForFile(ActivityDogumKayit.this,getPackageName(),photoFile);
                                    camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(camera_intent, CAMERA_REQ_CODE);
                                }
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
                            final File f=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),gorsel_ad);
                            final boolean img_exists=f.exists();
                            if(img_exists){
                                f.delete();
                            }
                            Glide.with(ActivityDogumKayit.this).load(R.drawable.icon_photo_add).into(photo);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        txtOtherFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!otherFieldsIsShown){
                    final LayoutInflater inflater=LayoutInflater.from(ActivityDogumKayit.this);
                    final FrameLayout container=findViewById(R.id.other_fields_container);
                    container.setAlpha(0f);
                    final View view=inflater.inflate(R.layout.kayitlar_dgr_dtylr,container,false);
                    final EditText edit_sperma=view.findViewById(R.id.sperma_name);
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
                        final File file=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),gorsel_ad);
                        launchImageCrop(Uri.fromFile(file));
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    final CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if(resultCode==RESULT_OK){
                        Glide.with(this).load(result.getUri()).into(photo);
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
        if(gorsel_ad.length()!=0) {
            new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), gorsel_ad).delete();
        }
        finish();
    }

    private void kayit_gir(View snackbar_view){
        if (edit_isim.length()==0||btn_tarih_dollenme.length()==0||btn_tarih_dogum.length()==0){
            Snackbar.make(snackbar_view,getString(R.string.deger_yok_uyari),Snackbar.LENGTH_SHORT).show();
        }
        else{
            final File f=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),gorsel_ad);
            DataModel dataModel;
            if(f.exists()&&f.isFile()){
                dataModel=new DataModel(0,edit_isim.getText().toString(),secilen_tur,edit_kupe_no.getText().toString(),
                        String.valueOf(date_dollenme.getTime()),String.valueOf(date_dogum.getTime()),gorsel_ad,_isPet,0,sperma_name);
            }
            else{
                dataModel=new DataModel(0,edit_isim.getText().toString(),secilen_tur,edit_kupe_no.getText().toString(),
                        String.valueOf(date_dollenme.getTime()),String.valueOf(date_dogum.getTime()),"",_isPet,0,sperma_name);
            }
            dbYoneticisi.veri_yaz(dataModel);
            finish();
        }
    }

    @Override
    public void oto_tarih_hesapla(Date date) {
        if(boolTarih){
            hesaplanan_tarih=TarihHesaplayici.get_dogum_tarihi(_isPet,secilen_tur,date,getClass().getName());
            date_dogum=hesaplanan_tarih.getTime();
            btn_tarih_dogum.setText(dateFormat.format(date_dogum));
            Snackbar.make(main_Layout,R.string.otomatik_hesaplandi_bildirim,Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public int get_gun_sayisi(long dogum_tarihi_in_millis) {
        return 0;
    }

    private void launchImageCrop(Uri uri){
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle(getString(R.string.crop_text))
                .start(this);
    }

    public File getImageFile() {
        final File imgFile=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ANM_" + System.currentTimeMillis() +".jpg");
        final File eski_dosya=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), gorsel_ad);
        if(eski_dosya.exists()&&eski_dosya.isFile()){
            eski_dosya.delete();
        }
        gorsel_ad=imgFile.getName();
        return imgFile;
    }

    private void save_photo(@NonNull Bitmap bitmap){
        int cropped_width=bitmap.getWidth();
        int cropped_height=bitmap.getHeight();
        while(cropped_width>1000){
            final double x=cropped_width/1.1;
            cropped_width=(int)x;
            cropped_height=(int)x;
            /**Bu işlemle kaydedilecek fotoğraf adım adım küçültülerek piksel sayısı 1000'in altında
            ve olabildiğince 1000'e yakın tutularak fotoğrafın netliği çok bozulmadan depolamanın ve belleğin şişmesi önlenir.*/
        }
        try{
            final FileOutputStream fileOutputStream=new FileOutputStream(getImageFile());
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,cropped_width,cropped_height,true);
            Bitmap.createBitmap(scaledBitmap,0,0,cropped_width,cropped_height,null,true).
                    compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
