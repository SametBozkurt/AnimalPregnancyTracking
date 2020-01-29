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
    private int ispet;

    public OtoTarihHesaplayici(View v, Boolean booleanTur, Boolean booleanTarih, EditText editText, String isim_tur,
                               Date d, Context context, int isPet){
        view=v;
        tahmini_dogum_tarihi=editText;
        tur=isim_tur;
        mContext=context;
        date1=d;
        ispet=isPet;
        otomatik_hesapla(booleanTur,booleanTarih,mContext);
    }

    public void otomatik_hesapla(Boolean b1, Boolean b2,Context c){
        if(b1&&b2){
            TarihHesaplayici tarihHesaplayici=new TarihHesaplayici(ispet,tur,date1,c);
            tahmini_dogum_tarihi.setText(tarihHesaplayici.getTarih());
            Snackbar.make(view,mContext.getString(R.string.otomatik_hesaplandi_bildirim),Snackbar.LENGTH_SHORT).show();
        }
    }

}
