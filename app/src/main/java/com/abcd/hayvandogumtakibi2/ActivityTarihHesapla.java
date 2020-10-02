package com.abcd.hayvandogumtakibi2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;

public class ActivityTarihHesapla extends AppCompatActivity implements CalendarTools {

    private boolean boolTarih=false;
    private EditText btn_tarih_dogum,btn_tarih_dollenme;
    private String secilen_tur;
    private NestedScrollView main_layout;
    final int petCode=0;
    private Date date_dollenme=new Date();
    final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarih_hesapla);
        Spinner spinner_turler=findViewById(R.id.spinner);
        btn_tarih_dollenme=findViewById(R.id.dollenme_tarihi);
        btn_tarih_dogum=findViewById(R.id.dogum_tarihi);
        main_layout=findViewById(R.id.ana_katman);
        final ImageView iptal=findViewById(R.id.iptal);
        final Calendar gecerli_takvim=Calendar.getInstance();
        date_dollenme=gecerli_takvim.getTime();
        final ArrayList<String> turler_list=new ArrayList<>(9);
        turler_list.add(getString(R.string.tur_0));
        turler_list.add(getString(R.string.tur_1));
        turler_list.add(getString(R.string.tur_2));
        turler_list.add(getString(R.string.tur_3));
        turler_list.add(getString(R.string.tur_4));
        turler_list.add(getString(R.string.tur_5));
        turler_list.add(getString(R.string.tur_7));
        turler_list.add(getString(R.string.tur_8));
        turler_list.add(getString(R.string.tur_9));
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<>(this,R.layout.spinner_text,turler_list);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_text);
        spinner_turler.setAdapter(spinnerAdapter);
        spinner_turler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilen_tur=String.valueOf(position);
                oto_tarih_hesapla(date_dollenme);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_tarih_dollenme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog dialog=new DatePickerDialog(ActivityTarihHesapla.this, R.style.PickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        gecerli_takvim.set(year,month,dayOfMonth);
                        date_dollenme=gecerli_takvim.getTime();
                        btn_tarih_dollenme.setText(dateFormat.format(date_dollenme));
                        boolTarih=true;
                        oto_tarih_hesapla(date_dollenme);
                    }
                },gecerli_takvim.get(Calendar.YEAR),gecerli_takvim.get(Calendar.MONTH),gecerli_takvim.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void oto_tarih_hesapla(Date date) {
        if(boolTarih){
            final Date date_dogum = TarihHesaplayici.get_dogum_tarihi(petCode,secilen_tur,date,getClass().getName()).getTime();
            btn_tarih_dogum.setText(dateFormat.format(date_dogum));
            Snackbar.make(main_layout,R.string.otomatik_hesaplandi_bildirim,Snackbar.LENGTH_SHORT).show();
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

}
