package com.abcd.hayvandogumtakibi2;

import android.app.DatePickerDialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

public class ActivityDogumKayit extends AppCompatActivity {

    private static final int GALLERY_REQ_CODE=12321;
    private static final int TAKING_PHOTO_REQ_CODE = 12322;
    private boolean boolTarih=false;
    private RelativeLayout main_Layout;
    private SQLiteDatabaseHelper dbYoneticisi;
    private EditText edit_isim,edit_kupe_no,btn_tarih_dogum,btn_tarih_dollenme;
    private Button kaydet,iptal;
    private ImageView photo;
    private Spinner spinner_turler;
    private Calendar gecerli_takvim;
    private String gorsel_adres;
    private String secilen_tur="0";
    private String gorsel_ad="";
    private Date date;
    private TextInputLayout textInputLayout;
    private int gun1,ay1,yil1,gun2,ay2,yil2,_isPet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogum_kayit);
        dbYoneticisi=new SQLiteDatabaseHelper(ActivityDogumKayit.this);
        photo=findViewById(R.id.add_photo);
        edit_isim=findViewById(R.id.isim);
        edit_kupe_no=findViewById(R.id.kupe_no);
        spinner_turler=findViewById(R.id.spinner);
        kaydet=findViewById(R.id.kaydet);
        iptal=findViewById(R.id.iptal);
        btn_tarih_dollenme=findViewById(R.id.dollenme_tarihi);
        btn_tarih_dogum=findViewById(R.id.dogum_tarihi);
        main_Layout=findViewById(R.id.ana_katman);
        textInputLayout=findViewById(R.id.input_layout_tarih2);
        textInputLayout.setHelperText(getString(R.string.date_input_helper_text_2));
        gecerli_takvim=Calendar.getInstance();
        gun1=gecerli_takvim.get(Calendar.DAY_OF_MONTH);
        ay1=gecerli_takvim.get(Calendar.MONTH);
        yil1=gecerli_takvim.get(Calendar.YEAR);
        gun2=gecerli_takvim.get(Calendar.DAY_OF_MONTH);
        ay2=gecerli_takvim.get(Calendar.MONTH);
        yil2=gecerli_takvim.get(Calendar.YEAR);
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
                            new OtoTarihHesaplayici(main_Layout,boolTarih,btn_tarih_dogum,secilen_tur,date,ActivityDogumKayit.this, _isPet);
                        }
                        else{
                            textInputLayout.setHelperText("");
                        }
                        break;
                    case 2: //Besi hayvanı ise
                        if(position!=3){
                            textInputLayout.setHelperText(getString(R.string.date_input_helper_text_2));
                            new OtoTarihHesaplayici(main_Layout,boolTarih,btn_tarih_dogum,secilen_tur,date,ActivityDogumKayit.this, _isPet);
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
                final DatePickerDialog datePickerDialog=new DatePickerDialog(ActivityDogumKayit.this,R.style.PickerTheme,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        gun1=i2;
                        ay1=i1;
                        yil1=i;
                        gecerli_takvim.set(i,i1,i2);
                        date=gecerli_takvim.getTime();
                        i1+=1; //i2-->GÜN i1-->AY i-->YIL
                        btn_tarih_dollenme.setText(i2+"/"+i1+"/"+i);
                        boolTarih=true;
                        switch (_isPet){
                            case 1: //Evcil hayvan ise
                                if(!secilen_tur.equals("3")){
                                    new OtoTarihHesaplayici(main_Layout,boolTarih,btn_tarih_dogum,secilen_tur,date,ActivityDogumKayit.this,
                                            _isPet);
                                    gun2=new TarihHesaplayici(btn_tarih_dogum.getText().toString()).get_tarih_bilgileri().get(Calendar.DAY_OF_MONTH);
                                    ay2=new TarihHesaplayici(btn_tarih_dogum.getText().toString()).get_tarih_bilgileri().get(Calendar.MONTH);
                                    yil2=new TarihHesaplayici(btn_tarih_dogum.getText().toString()).get_tarih_bilgileri().get(Calendar.YEAR);
                                }
                                break;
                            case 2: //Besi hayvanı ise
                                if(!secilen_tur.equals("3")){
                                    new OtoTarihHesaplayici(main_Layout,boolTarih,btn_tarih_dogum,secilen_tur,date,ActivityDogumKayit.this,
                                            _isPet);
                                    gun2=new TarihHesaplayici(btn_tarih_dogum.getText().toString()).get_tarih_bilgileri().get(Calendar.DAY_OF_MONTH);
                                    ay2=new TarihHesaplayici(btn_tarih_dogum.getText().toString()).get_tarih_bilgileri().get(Calendar.MONTH);
                                    yil2=new TarihHesaplayici(btn_tarih_dogum.getText().toString()).get_tarih_bilgileri().get(Calendar.YEAR);
                                }
                                break;
                        }
                    }
                },yil1,ay1,gun1);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        btn_tarih_dogum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog=new DatePickerDialog(ActivityDogumKayit.this,R.style.PickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        gun2=i2;
                        yil2=i;
                        ay2=i1;
                        i1+=1;
                        btn_tarih_dogum.setText(i2+"/"+i1+"/"+i);
                    }
                },yil2,ay2,gun2);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
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
                if(gorsel_ad.length()!=0)
                    new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),gorsel_ad).delete();
                onBackPressed();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(ActivityDogumKayit.this,photo);
                popupMenu.getMenuInflater().inflate(R.menu.popup_photo_picker,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        File photoFile=getImageFile();
                        Uri photoURI= FileProvider.getUriForFile(ActivityDogumKayit.this,getPackageName(),photoFile);
                        if(item.getItemId()==R.id.camera) {
                            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                            startActivityForResult(camera_intent,TAKING_PHOTO_REQ_CODE);
                        }
                        else if(item.getItemId()==R.id.gallery){
                            Intent gallery_intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(gallery_intent,GALLERY_REQ_CODE);
                        }
                        else if(item.getItemId()==R.id.remove){
                            if(gorsel_ad.length()!=0)
                                new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),gorsel_ad).delete();
                                gorsel_ad="";
                                Glide.with(ActivityDogumKayit.this).load(R.mipmap.icon_photo_add).into(photo);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(gorsel_ad.length()!=0)
            new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),gorsel_ad).delete();
        super.onBackPressed();
    }

    private void kayit_gir(View snackbar_view){
        Bundle data_pack=getIntent().getExtras();
        if (edit_isim.length()==0){
            Snackbar.make(snackbar_view,getString(R.string.deger_yok_uyari),Snackbar.LENGTH_SHORT).show();
        }
        else if(btn_tarih_dogum.length()==0){
            Snackbar.make(snackbar_view,getString(R.string.deger_yok_uyari),Snackbar.LENGTH_SHORT).show();
        }
        else{
            HayvanVeriler hayvanVeriler=new HayvanVeriler(0,edit_isim.getText().toString(),secilen_tur,edit_kupe_no.getText().toString(),
                    btn_tarih_dollenme.getText().toString(),btn_tarih_dogum.getText().toString(),gorsel_ad,_isPet);
            dbYoneticisi.veri_yaz(hayvanVeriler);
            startActivity(new Intent(ActivityDogumKayit.this,PrimaryActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK){
            switch(requestCode){
                case GALLERY_REQ_CODE:
                    Glide.with(this).load(data.getData()).into(photo);
                    try {
                        InputStream openInputStream=getContentResolver().openInputStream(data.getData());
                        ExifInterface exif=new ExifInterface(openInputStream);
                        dondur(MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData()),
                                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED),false);
                        openInputStream.close();

                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case TAKING_PHOTO_REQ_CODE:
                    try {
                        ExifInterface exif=new ExifInterface(gorsel_adres);
                        dondur(BitmapFactory.decodeFile(gorsel_adres),
                                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED),true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public File getImageFile() {
        StringBuilder dosya_adi=new StringBuilder("ANM_").append(System.currentTimeMillis());
        File picDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imgFile=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),dosya_adi+".jpg");
        if(gorsel_ad.length()!=0){
            new File(picDir,gorsel_ad).delete();
            gorsel_ad="";
            gorsel_adres=imgFile.getAbsolutePath();
            gorsel_ad=imgFile.getName();
            return imgFile;
        }
        else{
            gorsel_adres=imgFile.getAbsolutePath();
            gorsel_ad=imgFile.getName();
            return imgFile;
        }
    }
    private void dondur(Bitmap bitmap,int orientation,boolean from_camera){
        Matrix matrix=new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
        }
        try {
            FileOutputStream fileOutputStream=null;
            if(from_camera){
                fileOutputStream=new FileOutputStream(gorsel_adres);
            }
            else{
                fileOutputStream=new FileOutputStream(getImageFile());
            }
            Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true)
                    .compress(Bitmap.CompressFormat.JPEG,40,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Glide.with(this).load(Uri.fromFile(new File(gorsel_adres))).into(photo);
    }
}
