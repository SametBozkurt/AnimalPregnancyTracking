package com.abcd.hayvandogumtakibi2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentTarihHesaplayici extends BottomSheetDialogFragment {

    private boolean boolTarih=false;
    private EditText btn_tarih_dogum,btn_tarih_dollenme;
    private String secilen_tur;
    private final int petCode=0;
    private Date date_dollenme=new Date();
    private final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    private TarihHesaplayici tarihHesaplayici;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        tarihHesaplayici=new TarihHesaplayici(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_tarih_hesaplayici,container,false);
        final Spinner spinner_turler=view.findViewById(R.id.spinner);
        btn_tarih_dollenme=view.findViewById(R.id.dollenme_tarihi);
        btn_tarih_dogum=view.findViewById(R.id.dogum_tarihi);
        final Calendar gecerli_takvim=Calendar.getInstance();
        final ArrayList<String> _turler_list=new ArrayList<>(9);
        loadArrayElements(_turler_list);
        date_dollenme=gecerli_takvim.getTime();
        final ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<>(context,R.layout.spinner_text,_turler_list);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_text);
        spinner_turler.setAdapter(spinnerAdapter);
        spinner_turler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilen_tur=String.valueOf(position);
                tarihHesaplayici.sendDate();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_tarih_dollenme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog dialog=new DatePickerDialog(context, R.style.PickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        gecerli_takvim.set(year,month,dayOfMonth);
                        date_dollenme=gecerli_takvim.getTime();
                        btn_tarih_dollenme.setText(dateFormat.format(date_dollenme));
                        boolTarih=true;
                        tarihHesaplayici.sendDate();
                    }
                },gecerli_takvim.get(Calendar.YEAR),gecerli_takvim.get(Calendar.MONTH),gecerli_takvim.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        tarihHesaplayici.setDateChangeListener(new TarihHesaplayici.DateChangeListener() {
            @Override
            public void onNewDateSet() {
                if(boolTarih){
                    final Date date_dogum = tarihHesaplayici.get_dogum_tarihi(petCode,secilen_tur,date_dollenme,getClass().getName()).getTime();
                    btn_tarih_dogum.setText(dateFormat.format(date_dogum));
                }
            }
        });
        return view;
    }

    public void loadArrayElements(@NonNull final ArrayList<String> turler_list){
        turler_list.add(getString(R.string.tur_0));
        turler_list.add(getString(R.string.tur_1));
        turler_list.add(getString(R.string.tur_2));
        turler_list.add(getString(R.string.tur_3));
        turler_list.add(getString(R.string.tur_4));
        turler_list.add(getString(R.string.tur_5));
        turler_list.add(getString(R.string.tur_7));
        turler_list.add(getString(R.string.tur_8));
        turler_list.add(getString(R.string.tur_9));
    }

}
