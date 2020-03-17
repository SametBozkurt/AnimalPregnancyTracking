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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

public class ActivityEdit extends AppCompatActivity {

    private static final int GALLERY_REQ_CODE=12321;
    private static final int TAKING_PHOTO_REQ_CODE = 12322;
    private boolean boolTarih=true;
    private int gun1,ay1,yil1,gun2,ay2,yil2,kayit_id,petCode;
    TextInputEditText txt_isim, txt_kupe_no,dollenme_tarihi, dogum_tarihi;
    Button kaydet,iptal;
    Spinner tur_sec;
    RelativeLayout main_Layout;
    SQLiteDatabaseHelper databaseHelper;
    String secilen_tur,secilen_tarih1,secilen_tarih2,gorsel_ad,gorsel_adres;
    Calendar takvim;
    Date date,date2;
    Bundle data_bundle;
    TextInputLayout textInputLayout;
    ImageView photo;

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
        databaseHelper=new SQLiteDatabaseHelper(ActivityEdit.this);
        takvim=Calendar.getInstance();
        secilen_tur="";
        degerleri_yerlestir();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void degerleri_yerlestir(){
        data_bundle=getIntent().getExtras();
        kayit_id=data_bundle.getInt("kayit_id");
        petCode=data_bundle.getInt("isPet");
        try {
            date=new SimpleDateFormat("dd/MM/yyyy").parse((String)data_bundle.getCharSequence("kayit_tarih1"));
            Calendar mCalendar=Calendar.getInstance();
            mCalendar.setTime(date);
            gun1=mCalendar.get(Calendar.DAY_OF_MONTH);
            ay1=mCalendar.get(Calendar.MONTH);
            yil1=mCalendar.get(Calendar.YEAR);

        } catch (ParseException e) {
            e.printStackTrace();
            gun1=takvim.get(Calendar.DAY_OF_MONTH);
            ay1=takvim.get(Calendar.MONTH);
            yil1=takvim.get(Calendar.YEAR);
        }
        try {
            date2=new SimpleDateFormat("dd/MM/yyyy").parse((String)data_bundle.getCharSequence("kayit_tarih2"));
            Calendar c=Calendar.getInstance();
            c.setTime(date2);
            gun2=c.get(Calendar.DAY_OF_MONTH);
            ay2=c.get(Calendar.MONTH);
            yil2=c.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
            gun2=takvim.get(Calendar.DAY_OF_MONTH);
            ay2=takvim.get(Calendar.MONTH);
            yil2=takvim.get(Calendar.YEAR);
        }
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
        dollenme_tarihi.setText(data_bundle.getCharSequence("kayit_tarih1"));
        dogum_tarihi.setText(data_bundle.getCharSequence("kayit_tarih2"));
        secilen_tur= (String)data_bundle.getCharSequence("kayit_tur");
        tur_sec.setSelection(Integer.valueOf((String)data_bundle.getCharSequence("kayit_tur")));
        secilen_tarih1= (String)data_bundle.getCharSequence("kayit_tarih1");
        secilen_tarih2=(String)data_bundle.getCharSequence("kayit_tarih2");
        gorsel_ad=(String)data_bundle.getCharSequence("kayit_gorsel_isim");
        if(gorsel_ad.length()!=0){
            gorsel_adres=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),gorsel_ad).getAbsolutePath();
            Glide.with(this).load(Uri.fromFile(new File(gorsel_adres))).into(photo);
        }
        else{
            Glide.with(this).load(R.mipmap.icon_photo_add).into(photo);
        }
        tur_sec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilen_tur=String.valueOf(position);
                switch (petCode){
                    case 0://Önceki sürümler için
                        if(position!=6){
                            textInputLayout.setHelperText(getString(R.string.date_input_helper_text_2));
                            new OtoTarihHesaplayici(main_Layout,boolTarih,dogum_tarihi,secilen_tur,date,ActivityEdit.this,
                                    petCode);
                        }
                        else{
                            textInputLayout.setHelperText("");
                        }
                        break;
                    case 1: case 2: //Evcil & çiftlik hayvan ise
                        if(position!=3){
                            textInputLayout.setHelperText(getString(R.string.date_input_helper_text_2));
                            new OtoTarihHesaplayici(main_Layout,boolTarih,dogum_tarihi,secilen_tur,date,ActivityEdit.this,
                                    petCode);
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
        dollenme_tarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog=new DatePickerDialog(ActivityEdit.this,R.style.PickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        gun1=i2;
                        ay1=i1;
                        yil1=i;
                        takvim.set(i,i1,i2);
                        date=takvim.getTime();
                        i1+=1; //i2-->GÜN i1-->AY i-->YIL
                        dollenme_tarihi.setText(i2+"/"+i1+"/"+i);
                        secilen_tarih1=i2+"/"+i1+"/"+i;
                        boolTarih=true;
                        switch (petCode){
                            case 0://Önceki sürümler için
                                if(!secilen_tur.equals("6")){
                                    new OtoTarihHesaplayici(main_Layout,boolTarih,dogum_tarihi,secilen_tur,date,ActivityEdit.this,petCode);
                                    gun2=new TarihHesaplayici(dogum_tarihi.getText().toString()).get_tarih_bilgileri().get(Calendar.DAY_OF_MONTH);
                                    ay2=new TarihHesaplayici(dogum_tarihi.getText().toString()).get_tarih_bilgileri().get(Calendar.MONTH);
                                    yil2=new TarihHesaplayici(dogum_tarihi.getText().toString()).get_tarih_bilgileri().get(Calendar.YEAR);
                                }
                                break;
                            case 1: case 2: //Evcil kodu //Besi kodu
                                if(!secilen_tur.equals("3")){
                                    new OtoTarihHesaplayici(main_Layout,boolTarih,dogum_tarihi,secilen_tur,date,ActivityEdit.this,petCode);
                                    gun2=new TarihHesaplayici(dogum_tarihi.getText().toString()).get_tarih_bilgileri().get(Calendar.DAY_OF_MONTH);
                                    ay2=new TarihHesaplayici(dogum_tarihi.getText().toString()).get_tarih_bilgileri().get(Calendar.MONTH);
                                    yil2=new TarihHesaplayici(dogum_tarihi.getText().toString()).get_tarih_bilgileri().get(Calendar.YEAR);
                                }
                                break;
                        }
                    }
                },yil1,ay1,gun1);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        dogum_tarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog=new DatePickerDialog(ActivityEdit.this,R.style.PickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        gun2=i2;
                        yil2=i;
                        ay2=i1;
                        i1+=1;
                        dogum_tarihi.setText(i2+"/"+i1+"/"+i);
                        secilen_tarih2=i2+"/"+i1+"/"+i;
                    }
                },yil2,ay2,gun2);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
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
                            secilen_tur,txt_kupe_no.getText().toString(),secilen_tarih1,dogum_tarihi.getText().toString(),gorsel_ad);
                    startActivity(new Intent(ActivityEdit.this,ActivityKayitDuzenle.class));
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
                PopupMenu popupMenu=new PopupMenu(ActivityEdit.this,photo);
                popupMenu.getMenuInflater().inflate(R.menu.popup_photo_picker,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        File photoFile=getImageFile();
                        Uri photoURI= FileProvider.getUriForFile(ActivityEdit.this,getPackageName(),photoFile);
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
                            Glide.with(ActivityEdit.this).load(R.mipmap.icon_photo_add).into(photo);
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
                        InputStream openInputStream=getContentResolver().openInputStream(data.getData());
                        ExifInterface exif=new ExifInterface(openInputStream);
                        dondur(MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData()),
                                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED));
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
                                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public File getImageFile(){
        StringBuilder dosya_adi=new StringBuilder("ANM").append(System.currentTimeMillis());
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
    private void dondur(Bitmap bitmap, int orientation){
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
            FileOutputStream fileOutputStream=new FileOutputStream(gorsel_adres);
            Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true)
                    .compress(Bitmap.CompressFormat.JPEG,40,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Glide.with(this).load(bitmap_new).into(photo);
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
