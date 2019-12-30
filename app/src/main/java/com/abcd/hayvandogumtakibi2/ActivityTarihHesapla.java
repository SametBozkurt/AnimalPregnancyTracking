package com.abcd.hayvandogumtakibi2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityTarihHesapla extends AppCompatActivity {

    private RelativeLayout main_Layout;
    private EditText btn_tarih_dogum,btn_tarih_dollenme;
    private Calendar gecerli_takvim;
    private Durumlar durumlar;
    private int spinner_sayac,gun1,ay1,yil1;
    private String secilen_tur;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarih_hesapla);
        Spinner spinner_turler=findViewById(R.id.spinner);
        btn_tarih_dollenme=findViewById(R.id.dollenme_tarihi);
        btn_tarih_dogum=findViewById(R.id.dogum_tarihi);
        main_Layout=findViewById(R.id.ana_katman);
        spinner_sayac=0;
        secilen_tur="0";
        durumlar=new Durumlar();
        durumlar.setDurum1(true);
        gecerli_takvim=Calendar.getInstance();
        date=gecerli_takvim.getTime();
        gun1=gecerli_takvim.get(Calendar.DAY_OF_MONTH);
        ay1=gecerli_takvim.get(Calendar.MONTH);
        yil1=gecerli_takvim.get(Calendar.YEAR);
        ArrayList<String> turler_list=new ArrayList<>(6);
        turler_list.add(getString(R.string.tur_0));
        turler_list.add(getString(R.string.tur_1));
        turler_list.add(getString(R.string.tur_2));
        turler_list.add(getString(R.string.tur_3));
        turler_list.add(getString(R.string.tur_4));
        turler_list.add(getString(R.string.tur_5));
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<>(this,R.layout.spinner_text,turler_list);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_text);
        spinner_turler.setAdapter(spinnerAdapter);
        spinner_turler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner_sayac==0){
                    spinner_sayac+=1;
                }
                else{
                    spinner_sayac+=1;
                    secilen_tur=String.valueOf(position);
                    new OtoTarihHesaplayici(main_Layout,durumlar,btn_tarih_dogum,secilen_tur,date,ActivityTarihHesapla.this);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_tarih_dollenme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog=new DatePickerDialog(ActivityTarihHesapla.this,R.style.PickerTheme,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        yil1=i;
                        ay1=i1;
                        gun1=i2;
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(i,i1,i2);
                        date=calendar.getTime();
                        i1+=1; //i2-->GÜN i1-->AY i-->YIL
                        btn_tarih_dollenme.setText(i2+"/"+i1+"/"+i);
                        durumlar.setDurum2(true);
                        new OtoTarihHesaplayici(main_Layout,durumlar,btn_tarih_dogum,secilen_tur,date,ActivityTarihHesapla.this);
                    }
                },yil1,ay1,gun1);
                datePickerDialog.show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
