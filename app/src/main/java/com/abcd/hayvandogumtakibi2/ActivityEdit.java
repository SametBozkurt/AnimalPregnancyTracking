package com.abcd.hayvandogumtakibi2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

public class ActivityEdit extends AppCompatActivity implements CalendarTools {

    private static final int GALLERY_REQ_CODE=12321;
    private static final int TAKING_PHOTO_REQ_CODE = 12322;
    private boolean boolTarih=true;
    private int kayit_id,petCode,calisma_sayaci=0;
    TextInputEditText txt_isim, txt_kupe_no,dollenme_tarihi, dogum_tarihi;
    Button kaydet,iptal;
    Spinner tur_sec;
    RelativeLayout main_Layout;
    SQLiteDatabaseHelper databaseHelper;
    String secilen_tur="",gorsel_ad,gorsel_adres;
    final Calendar takvim=Calendar.getInstance();
    Calendar takvim2=Calendar.getInstance();
    Date date1=new Date(),date2=new Date();
    Bundle data_bundle;
    TextInputLayout textInputLayout;
    ImageView photo;
    final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        data_bundle=getIntent().getExtras();
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
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        degerleri_yerlestir();
    }

    void degerleri_yerlestir(){
        kayit_id=data_bundle.getInt("kayit_id");
        petCode=data_bundle.getInt("isPet");
        date1.setTime(Long.parseLong(String.valueOf(data_bundle.getCharSequence("kayit_tarih1"))));
        takvim.setTime(date1);
        date2.setTime(Long.parseLong(String.valueOf(data_bundle.getCharSequence("kayit_tarih2"))));
        takvim2.setTime(date2);
        ArrayAdapter<String> spinnerAdapter;
        switch(petCode){
            case 0:
                spinnerAdapter=new ArrayAdapter<>(this,R.layout.spinner_text,getResources().getStringArray(R.array.animal_list));
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_text);
                tur_sec.setAdapter(spinnerAdapter);
                break;
            case 1:
                spinnerAdapter=new ArrayAdapter<>(this,R.layout.spinner_text,getResources().getStringArray(R.array.animal_list_pet));
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_text);
                tur_sec.setAdapter(spinnerAdapter);
                break;
            case 2:
                spinnerAdapter=new ArrayAdapter<>(this,R.layout.spinner_text,getResources().getStringArray(R.array.animal_list_barn));
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_text);
                tur_sec.setAdapter(spinnerAdapter);
                break;
        }
        txt_isim.setText(data_bundle.getCharSequence("kayit_isim"));
        txt_kupe_no.setText(data_bundle.getCharSequence("kayit_kupe_no"));
        dollenme_tarihi.setText(dateFormat.format(date1));
        dogum_tarihi.setText(dateFormat.format(date2));
        secilen_tur= (String)data_bundle.getCharSequence("kayit_tur");
        tur_sec.setSelection(Integer.parseInt((String)data_bundle.getCharSequence("kayit_tur")));
        main_Layout.post(new Runnable() {
            @Override
            public void run() {
                gorsel_ad=(String)data_bundle.getCharSequence("kayit_gorsel_isim");
                if(gorsel_ad.length()!=0){
                    gorsel_adres=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),gorsel_ad).getAbsolutePath();
                    Glide.with(ActivityEdit.this).load(Uri.fromFile(new File(gorsel_adres))).into(photo);
                }
                else{
                    Glide.with(ActivityEdit.this).load(R.drawable.icon_photo_add).into(photo);
                }
            }
        });
        tur_sec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*
                Activity her başladığında, yani onCreate() metodu çalıştığında, Spinner nesnesine/view'ine ait
                onItemSelected metodu da çalışmakta ve oto_tarih_hesapla() metodu çalıştırılarak elle seçilmiş dogum
                tarihlerinde sorunlara yol açmakta, bu yüzden calisma_sayaci ile onItemSelected() metodunun Activity
                içinde kaç kez çalıştığı bilgisi tutulmaktadır.
                */
                if(calisma_sayaci>0){
                    secilen_tur=String.valueOf(position);
                    switch (petCode){
                        case 0://Önceki sürümler için
                            if(position!=6){
                                textInputLayout.setHelperText(getString(R.string.date_input_helper_text_2));
                                oto_tarih_hesapla(date1);
                            }
                            else{
                                textInputLayout.setHelperText("");
                            }
                            break;
                        case 1: case 2: //Evcil & çiftlik hayvan ise
                            if(position!=3){
                                textInputLayout.setHelperText(getString(R.string.date_input_helper_text_2));
                                oto_tarih_hesapla(date1);
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
                DatePickerDialog dialog=new DatePickerDialog(ActivityEdit.this, R.style.PickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        takvim.set(year,month,dayOfMonth);
                        date1=takvim.getTime();
                        dollenme_tarihi.setText(dateFormat.format(date1));
                        boolTarih=true;
                        switch (petCode){
                            case 0://Önceki sürümler için
                                if(!secilen_tur.equals("6")){
                                    oto_tarih_hesapla(date1);
                                }
                                break;
                            case 1: case 2: //Evcil kodu //Besi kodu
                                if(!secilen_tur.equals("3")){
                                    oto_tarih_hesapla(date1);
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
                DatePickerDialog dialog=new DatePickerDialog(ActivityEdit.this, R.style.PickerTheme, new DatePickerDialog.OnDateSetListener() {
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
                    databaseHelper.guncelle(kayit_id,txt_isim.getText().toString(),
                            secilen_tur,txt_kupe_no.getText().toString(),String.valueOf(date1.getTime()),
                            String.valueOf(date2.getTime()),gorsel_ad,data_bundle.getInt("dogumGrcklsti"));
                    finish();
                    startActivity(new Intent(ActivityEdit.this,PrimaryActivity.class));
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
                final PopupMenu popupMenu=new PopupMenu(ActivityEdit.this,photo);
                popupMenu.getMenuInflater().inflate(R.menu.popup_photo_picker,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final File photoFile=getImageFile();
                        final Uri photoURI= FileProvider.getUriForFile(ActivityEdit.this,getPackageName(),photoFile);
                        if(item.getItemId()==R.id.camera) {
                            final Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                            startActivityForResult(camera_intent,TAKING_PHOTO_REQ_CODE);
                        }
                        else if(item.getItemId()==R.id.gallery){
                            final Intent gallery_intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(gallery_intent,GALLERY_REQ_CODE);
                        }
                        else if(item.getItemId()==R.id.remove){
                            if(gorsel_ad.length()!=0){
                                new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),gorsel_ad).delete();
                                gorsel_ad="";
                                Glide.with(ActivityEdit.this).load(R.drawable.icon_photo_add).into(photo);
                            }
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK){
            switch(requestCode){
                case GALLERY_REQ_CODE:
                    Glide.with(this).load(data.getData()).into(photo);
                    try {
                        final InputStream openInputStream=getContentResolver().openInputStream(data.getData());
                        final ExifInterface exif=new ExifInterface(openInputStream);
                        dondur(MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData()),
                                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED));
                        openInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case TAKING_PHOTO_REQ_CODE:
                    try {
                        final ExifInterface exif=new ExifInterface(gorsel_adres);
                        dondur(BitmapFactory.decodeFile(gorsel_adres),
                                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File getImageFile(){
        final File picDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        final File imgFile=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ANM" + System.currentTimeMillis() +".jpg");
        if(gorsel_ad.length()!=0){
            new File(picDir,gorsel_ad).delete();
            gorsel_ad="";
        }
        gorsel_adres=imgFile.getAbsolutePath();
        gorsel_ad=imgFile.getName();
        return imgFile;
    }
    private void dondur(Bitmap bitmap, int orientation){
        final Matrix matrix=new Matrix();
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
            final FileOutputStream fileOutputStream=new FileOutputStream(gorsel_adres);
            final int croped_width=bitmap.getWidth()/5;
            final int croped_height=bitmap.getHeight()/5;
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,croped_width,croped_height,true);
            Bitmap.createBitmap(scaledBitmap,0,0,croped_width,croped_height,matrix,true).
                    compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Glide.with(this).load(Uri.fromFile(new File(gorsel_adres))).into(photo);
    }

    @Override
    public void oto_tarih_hesapla(Date date) {
        if(boolTarih){
            takvim2=TarihHesaplayici.get_dogum_tarihi(petCode,secilen_tur,date,getClass().getName());
            date2=takvim2.getTime();
            dogum_tarihi.setText(dateFormat.format(date2));
            Snackbar.make(main_Layout,R.string.otomatik_hesaplandi_bildirim,Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public int get_gun_sayisi(long dogum_tarihi_in_millis) {
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper=null;
        takvim2=null;
        data_bundle=null;
        if(main_Layout!=null){
            main_Layout.removeAllViews();
            main_Layout=null;
        }
    }
}
