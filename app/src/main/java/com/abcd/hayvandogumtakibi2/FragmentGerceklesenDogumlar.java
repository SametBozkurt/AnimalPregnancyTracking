package com.abcd.hayvandogumtakibi2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class FragmentGerceklesenDogumlar extends Fragment {

    Context context;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    int selection_code=0, selectedRadioButtonFilter=0, selectedRadioButtonOrder=R.id.radio_button_AtoZ;
    String table_name=SQLiteDatabaseHelper.SUTUN_0, orderKey=" ASC", orderBy=table_name+orderKey;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_gerceklesen_dogumlar,container,false);
        if(container!=null){
            container.clearDisappearingChildren();
        }
        recyclerView=view.findViewById(R.id.recyclerView);
        relativeLayout=view.findViewById(R.id.parent_layout);
        final Button btn_filter=view.findViewById(R.id.btn_filter);
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterMenu();
            }
        });
        initProgressBarAndTask();
        return view;
    }

    void initProgressBarAndTask(){
        final ProgressBar progressBar=new ProgressBar(context);
        progressBar.setIndeterminate(true);
        final RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(mLayoutParams);
        relativeLayout.addView(progressBar);
        recyclerView.animate().alpha(0f).setDuration(200).start();
        recyclerView.setAdapter(null);
        relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                final KayitlarAdapter kayitlarAdapter=new KayitlarAdapter(context,selection_code,"dogum_grcklsti=1",orderBy);
                recyclerView.setAdapter(kayitlarAdapter);
                relativeLayout.removeView(progressBar);
                recyclerView.animate().alpha(1f).setDuration(200).start();
            }
        },600);
    }

    void openFilterMenu(){
        final Dialog dialog = new Dialog(context,R.style.DialogStyleTest);
        dialog.setContentView(R.layout.layout_filter_and_sort);
        final Button buttonApply=dialog.findViewById(R.id.btn_apply), buttonReset=dialog.findViewById(R.id.btn_reset);
        final RadioGroup radioGroupFilter=dialog.findViewById(R.id.radio_group_filter);
        final RadioGroup radioGroupOrder=dialog.findViewById(R.id.radio_group_order);
        final SwitchMaterial switchMaterial=dialog.findViewById(R.id.switch_show_happeneds);
        switchMaterial.setVisibility(View.GONE);
        radioGroupFilter.check(selectedRadioButtonFilter);
        radioGroupOrder.check(selectedRadioButtonOrder);
        radioGroupFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_button_isim){
                    selection_code=0;
                    table_name=SQLiteDatabaseHelper.SUTUN_1;
                }
                else if(checkedId==R.id.radio_button_kupe_no){
                    selection_code=1;
                    table_name=SQLiteDatabaseHelper.SUTUN_3;
                }
                else if(checkedId==R.id.radio_button_date1){
                    selection_code=2;
                    table_name=SQLiteDatabaseHelper.SUTUN_4;
                }
                else if(checkedId==R.id.radio_button_date2){
                    selection_code=3;
                    table_name=SQLiteDatabaseHelper.SUTUN_5;
                }
                orderBy=table_name+orderKey;
                selectedRadioButtonFilter=checkedId;
            }
        });
        radioGroupOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_button_AtoZ){
                    orderKey=" ASC";
                }
                else if(checkedId==R.id.radio_button_ZtoA){
                    orderKey=" DESC";
                }
                orderBy=table_name+orderKey;
                selectedRadioButtonOrder=checkedId;
            }
        });
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initProgressBarAndTask();
                dialog.dismiss();
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupOrder.check(R.id.radio_button_AtoZ);
                selection_code=0;
                selectedRadioButtonFilter=0;
                table_name=SQLiteDatabaseHelper.SUTUN_0;
                orderKey=" ASC";
                orderBy=table_name+orderKey;
                initProgressBarAndTask();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
