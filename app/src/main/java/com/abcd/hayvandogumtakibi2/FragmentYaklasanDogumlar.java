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

public class FragmentYaklasanDogumlar extends Fragment {

    Context context;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    int selection_code=0, selectedRadioButtonFilter=R.id.radio_button_isim, selectedRadioButtonOrder=R.id.radio_button_AtoZ;
    String table_name="isim", orderBy=table_name+" ASC";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_kritikler,container,false);
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
        recyclerView.setAlpha(0f);
        relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                final KritiklerAdapter kritiklerAdapter=new KritiklerAdapter(context,selection_code,orderBy);
                recyclerView.setAdapter(kritiklerAdapter);
                relativeLayout.removeView(progressBar);
                recyclerView.animate().alpha(1f).setDuration(200).start();
            }
        },600);
    }

    void openFilterMenu(){
        final Dialog dialog = new Dialog(context,R.style.DialogStyleTest);
        dialog.setContentView(R.layout.layout_filter_and_sort);
        final Button buttonApply=dialog.findViewById(R.id.btn_apply);
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
                    table_name="isim";
                }
                else if(checkedId==R.id.radio_button_id){
                    selection_code=1;
                    table_name="kupe_no";
                }
                else if(checkedId==R.id.radio_button_date1){
                    selection_code=2;
                    table_name="tohumlama_tarihi";
                }
                else if(checkedId==R.id.radio_button_date2){
                    selection_code=3;
                    table_name="dogum_tarihi";
                }
                selectedRadioButtonFilter=checkedId;
            }
        });
        radioGroupOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_button_AtoZ){
                    orderBy=table_name+" ASC";
                }
                else if(checkedId==R.id.radio_button_ZtoA){
                    orderBy=table_name+" DESC";
                }
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
        dialog.show();
    }

}
