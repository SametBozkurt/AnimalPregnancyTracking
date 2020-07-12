package com.abcd.hayvandogumtakibi2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FragmentYaklasanDogumlar extends Fragment {

    Context context;
    RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_kritikler,container,false);
        recyclerView=view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        final ProgressDialog[] dialog = {new ProgressDialog(context)};
        dialog[0].setTitle(R.string.dialog_title1);
        dialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog[0].setCancelable(false);
        dialog[0].show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(context);
                        final ArrayList<HayvanVeriler> hayvanVerilerArrayList=databaseHelper.getKritikOlanlar();
                        recyclerView.setAdapter(new KritiklerAdapter(context, hayvanVerilerArrayList));
                        dialog[0].dismiss();
                        dialog[0] =null;
                    }
                });
            }
        }).start();
        return view;
    }

}
