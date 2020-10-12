package com.abcd.hayvandogumtakibi2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentGerceklesenDogumlar extends Fragment {

    Context context;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    ProgressBar mProgressBar;

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
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        mProgressBar=new ProgressBar(context);
        final RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setLayoutParams(mLayoutParams);
        mProgressBar.setIndeterminate(true);
        relativeLayout.addView(mProgressBar);
        new Task1().execute();
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    class Task1 extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerView.animate().alpha(0f).setDuration(200).start();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            relativeLayout.removeView(mProgressBar);
            final SQLiteDatabaseHelper databaseHelper=SQLiteDatabaseHelper.getInstance(context);
            final ArrayList<DataModel> dataModelArrayList=databaseHelper.getSimpleData("dogum_grcklsti=1",null);
            final KayitlarAdapter kayitlarAdapter=new KayitlarAdapter(context,dataModelArrayList,0);
            recyclerView.setAdapter(kayitlarAdapter);
            recyclerView.animate().alpha(1f).setDuration(200).start();
            mProgressBar=null;
        }
    }

}
