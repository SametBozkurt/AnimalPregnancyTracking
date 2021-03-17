package com.abcd.hayvandogumtakibi2.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abcd.hayvandogumtakibi2.Activity.ActivityKritikler;
import com.abcd.hayvandogumtakibi2.Adapter.KritiklerAdapter;
import com.abcd.hayvandogumtakibi2.Misc.ListModeCallback;
import com.abcd.hayvandogumtakibi2.Misc.PreferencesHolder;
import com.abcd.hayvandogumtakibi2.Misc.SQLiteDatabaseHelper;
import com.abcd.hayvandogumtakibi2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;

import static android.os.Looper.getMainLooper;

public class FragmentYaklasanDogumlar extends Fragment {

    private Context context;
    private int selection_code=0, selectedRadioButtonFilter= R.id.radio_button_isim, selectedRadioButtonOrder=R.id.radio_button_first;
    private String table_name= SQLiteDatabaseHelper.SUTUN_1, orderBy=null;
    private BottomSheetDialog bottomSheetDialog;
    private RadioGroup radioGroupFilter,radioGroupOrder;
    private CoordinatorLayout.LayoutParams mLayoutParams;
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;
    private KritiklerAdapter kritiklerAdapter;
    private boolean listModeEnabled;

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
        Button btn_filter=view.findViewById(R.id.btn_filter);
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
        //noinspection ConstantConditions
        ((ActivityKritikler)getActivity()).setListModeCallback(new ListModeCallback() {
            @Override
            public void onListModeChanged(boolean b) {
                //Bu callback fonksiyon ile listeleme modu değiştiğinde Fragment yenilemeye gerek kalmayacak.
                listModeEnabled=b;
                initProgressBarAndTask();
            }
        });
        initProgressBarAndTask();
        bottomSheetDialog=new BottomSheetDialog(context,R.style.FilterDialogTheme);
        initFilterMenu();
        return view;
    }

    private void initProgressBarAndTask(){
        taskPrePostOnUI();
        doAsyncTaskAndPost();
    }

    private void taskPrePostOnUI(){
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar=new ProgressBar(context);
                progressBar.setIndeterminate(true);
                if(mLayoutParams==null){
                    mLayoutParams=new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT,
                            CoordinatorLayout.LayoutParams.WRAP_CONTENT);
                    mLayoutParams.gravity=Gravity.CENTER;
                }
                progressBar.setLayoutParams(mLayoutParams);
                coordinatorLayout.addView(progressBar);
                recyclerView.setAlpha(0f);
            }
        });
    }

    private void doAsyncTaskAndPost(){
        HandlerThread handlerThread=new HandlerThread("AsyncTasks");
        handlerThread.start();
        final Handler asyncHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                taskPostOnUI();
            }
        };
        asyncHandler.post(new Runnable() {
            @Override
            public void run() {
                listModeEnabled= PreferencesHolder.getIsListedViewEnabled(context);
                try {
                    Thread.sleep(400);
                    kritiklerAdapter=new KritiklerAdapter(context,selection_code,orderBy,listModeEnabled);
                    Message message=new Message();
                    message.obj="InitializeUIProcess";
                    asyncHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void taskPostOnUI(){
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                GridLayoutManager gridLayoutManager;
                if(listModeEnabled){
                    gridLayoutManager=new GridLayoutManager(context,2);
                }
                else{
                    gridLayoutManager=new GridLayoutManager(context,3);
                }
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(kritiklerAdapter);
                try {
                    Thread.sleep(200);
                    coordinatorLayout.removeView(progressBar);
                    recyclerView.animate().alpha(1f).setDuration(200).start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initFilterMenu(){
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
