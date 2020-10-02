package com.abcd.hayvandogumtakibi2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentGerceklesenDogumYok extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container!=null){
            container.clearDisappearingChildren();
        }
        return inflater.inflate(R.layout.fragment_gerceklesen_dogum_yok,container,false);
    }
}
