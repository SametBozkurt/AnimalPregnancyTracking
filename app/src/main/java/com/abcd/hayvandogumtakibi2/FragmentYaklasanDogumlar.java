package com.abcd.hayvandogumtakibi2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class FragmentYaklasanDogumlar extends Fragment {

    Context context;
    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;
    int selection_code=0, selectedRadioButtonFilter=R.id.radio_button_isim, selectedRadioButtonOrder=R.id.radio_button_first;
    String table_name=SQLiteDatabaseHelper.SUTUN_1, orderBy=null;
    BottomSheetDialog bottomSheetDialog;
    RadioGroup radioGroupFilter,radioGroupOrder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_kritikler,container,false);
        if(container!=null){
            container.clearDisappearingChildren();
        }
        recyclerView=view.findViewById(R.id.recyclerView);
        coordinatorLayout=view.findViewById(R.id.parent_layout);
        final Button btn_filter=view.findViewById(R.id.btn_filter);
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetDialog!=null){
                    radioGroupFilter.check(selectedRadioButtonFilter);
                    radioGroupOrder.check(selectedRadioButtonOrder);
                    bottomSheetDialog.show();
                }
            }
        });
        initProgressBarAndTask();
        bottomSheetDialog=new BottomSheetDialog(context,R.style.FilterDialogTheme);
        initFilterMenu();
        return view;
    }

    void initProgressBarAndTask(){
        final ProgressBar progressBar=new ProgressBar(context);
        progressBar.setIndeterminate(true);
        final CoordinatorLayout.LayoutParams mLayoutParams=new CoordinatorLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity=Gravity.CENTER;
        progressBar.setLayoutParams(mLayoutParams);
        coordinatorLayout.addView(progressBar);
        recyclerView.setAlpha(0f);
        coordinatorLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                final KritiklerAdapter kritiklerAdapter=new KritiklerAdapter(context,selection_code,orderBy);
                recyclerView.setAdapter(kritiklerAdapter);
                coordinatorLayout.removeView(progressBar);
                recyclerView.animate().alpha(1f).setDuration(200).start();
            }
        },600);
    }

    void initFilterMenu(){
        @SuppressLint("InflateParams")
        final View view = LayoutInflater.from(context).inflate(R.layout.layout_filter_and_sort,null);
        bottomSheetDialog.setContentView(view);
        final Button buttonApply=view.findViewById(R.id.btn_apply), buttonReset=view.findViewById(R.id.btn_reset);
        radioGroupFilter=view.findViewById(R.id.radio_group_filter);
        radioGroupOrder=view.findViewById(R.id.radio_group_order);
        final SwitchMaterial switchMaterial=view.findViewById(R.id.switch_show_happeneds);
        switchMaterial.setVisibility(View.GONE);
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
                selectedRadioButtonFilter=checkedId;
            }
        });
        radioGroupOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                orderBy=null;
                if(checkedId==R.id.radio_button_first){
                    orderBy="id ASC";
                }
                else if(checkedId==R.id.radio_button_last){
                    orderBy="id DESC";
                }
                else if(checkedId==R.id.radio_button_AtoZ){
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
                bottomSheetDialog.dismiss();
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupOrder.check(R.id.radio_button_first);
                selection_code=0;
                selectedRadioButtonFilter=R.id.radio_button_isim;
                table_name=SQLiteDatabaseHelper.SUTUN_1;
                orderBy=null;
                initProgressBarAndTask();
                bottomSheetDialog.dismiss();
            }
        });
    }

}
