package com.abcd.hayvandogumtakibi2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

public class ActivityKayitAra extends AppCompatActivity {

    TextInputEditText inputAranacak;
    RadioGroup myRadioGroup;
    RadioButton radioButtonIsimler;
    SQLiteDatabaseHelper databaseHelper;
    ArrayList<HayvanVeriler> hayvanVerilerArrayList;
    RelativeLayout layout_sonuclar;
    RecyclerView recyclerView;
    TextView textViewUyari;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ara);
        inputAranacak=findViewById(R.id.bul);
        myRadioGroup=findViewById(R.id.radio_group);
        radioButtonIsimler=findViewById(R.id.radio_button_isim);
        layout_sonuclar=findViewById(R.id.layout_sonuclar);
        recyclerView=new RecyclerView(ActivityKayitAra.this);
        textViewUyari=new TextView(ActivityKayitAra.this);
        img=new ImageView(ActivityKayitAra.this);
        databaseHelper=SQLiteDatabaseHelper.getInstance(this);
        inputAranacak.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(myRadioGroup.getCheckedRadioButtonId()==radioButtonIsimler.getId()){
                    hayvanVerilerArrayList=databaseHelper.getAramaSonuclari(true,String.valueOf(charSequence));
                    if(String.valueOf(charSequence).equals("")){
                        layout_sonuclar.removeView(recyclerView);
                        layout_sonuclar.removeView(img);
                        layout_sonuclar.removeView(textViewUyari);
                    }
                    else{
                        if(hayvanVerilerArrayList.size()==0){
                            layout_sonuclar.removeView(recyclerView);
                            layout_sonuclar.removeView(img);
                            layout_sonuclar.removeView(textViewUyari);
                            RelativeLayout.LayoutParams imageLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            imageLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            imageLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            img.setLayoutParams(imageLayoutParams);
                            img.setImageResource(R.mipmap.cross_line);
                            img.setId(R.id.image_cross_line);
                            textViewUyari.setText(getString(R.string.no_matches));
                            textViewUyari.setTextAppearance(ActivityKayitAra.this,R.style.NoMatchesStyle);
                            RelativeLayout.LayoutParams textLayoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            textLayoutParams.addRule(RelativeLayout.BELOW,R.id.image_cross_line);
                            textLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            textViewUyari.setLayoutParams(textLayoutParams);
                            layout_sonuclar.addView(img);
                            layout_sonuclar.addView(textViewUyari);
                        }
                        else{
                            layout_sonuclar.removeView(recyclerView);
                            layout_sonuclar.removeView(img);
                            layout_sonuclar.removeView(textViewUyari);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivityKayitAra.this);
                            RelativeLayout.LayoutParams recyclerParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.MATCH_PARENT);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutParams(recyclerParams);
                            recyclerView.setAdapter(new AramalarAdapter(ActivityKayitAra.this,hayvanVerilerArrayList));
                            layout_sonuclar.addView(recyclerView);
                        }
                    }
                }
                else{
                    hayvanVerilerArrayList=databaseHelper.getAramaSonuclari(false,String.valueOf(charSequence));
                    if(String.valueOf(charSequence).equals("")){
                        layout_sonuclar.removeView(recyclerView);
                        layout_sonuclar.removeView(img);
                        layout_sonuclar.removeView(textViewUyari);
                    }
                    else{
                        if(hayvanVerilerArrayList.size()==0){
                            layout_sonuclar.removeView(recyclerView);
                            layout_sonuclar.removeView(img);
                            layout_sonuclar.removeView(textViewUyari);
                            RelativeLayout.LayoutParams imageLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            imageLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            imageLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            img.setLayoutParams(imageLayoutParams);
                            img.setImageResource(R.mipmap.cross_line);
                            img.setId(R.id.image_cross_line);
                            textViewUyari.setText(getString(R.string.no_matches));
                            textViewUyari.setTextAppearance(ActivityKayitAra.this,R.style.NoMatchesStyle);
                            RelativeLayout.LayoutParams textLayoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            textLayoutParams.addRule(RelativeLayout.BELOW,R.id.image_cross_line);
                            textLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            textViewUyari.setLayoutParams(textLayoutParams);
                            layout_sonuclar.addView(img);
                            layout_sonuclar.addView(textViewUyari);
                        }
                        else{
                            layout_sonuclar.removeView(recyclerView);
                            layout_sonuclar.removeView(img);
                            layout_sonuclar.removeView(textViewUyari);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivityKayitAra.this);
                            RelativeLayout.LayoutParams recyclerParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.MATCH_PARENT);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutParams(recyclerParams);
                            recyclerView.setAdapter(new AramalarAdapter(ActivityKayitAra.this,hayvanVerilerArrayList));
                            layout_sonuclar.addView(recyclerView);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
