package com.abcd.hayvandogumtakibi2;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import com.google.android.material.snackbar.Snackbar;
import java.util.Date;

public class OtoTarihHesaplayici {

    private String tur;
    private Context mContext;
    private EditText tahmini_dogum_tarihi;
    private View view;
    private Date date1;

    public OtoTarihHesaplayici(View v, Boolean booleanTur, Boolean booleanTarih, EditText editText, String isim_tur, Date d, Context context){
        view=v;
        tahmini_dogum_tarihi=editText;
        tur=isim_tur;
        mContext=context;
        date1=d;
        otomatik_hesapla(booleanTur,booleanTarih);
    }

    public void otomatik_hesapla(Boolean b1, Boolean b2){
        if(b1&&b2){
            TarihHesaplayici tarihHesaplayici=new TarihHesaplayici(tur,date1);
            tahmini_dogum_tarihi.setText(tarihHesaplayici.getTarih());
            Snackbar.make(view,mContext.getString(R.string.otomatik_hesaplandi_bildirim),Snackbar.LENGTH_SHORT).show();
        }
    }

}
