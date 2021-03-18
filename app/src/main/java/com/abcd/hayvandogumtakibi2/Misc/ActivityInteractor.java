package com.abcd.hayvandogumtakibi2.Misc;

import android.os.Bundle;

import androidx.annotation.Nullable;

public class ActivityInteractor {

    private static ActivityInteractor activityInteractor=null;
    private ActivityKritiklerCallback activityKritiklerCallback;
    private PrimaryActivityCallback primaryActivityCallback;

    private ActivityInteractor(){}

    public static ActivityInteractor getInstance(){
        if(activityInteractor==null){
            activityInteractor=new ActivityInteractor();
        }
        return activityInteractor;
    }

    public interface ActivityKritiklerCallback{
        void onSomethingsChanged(@Nullable Bundle whatChanged);
    }

    public interface PrimaryActivityCallback{
        void onSomethingsChanged(@Nullable Bundle whatChanged);
    }

    public void setActivityKritiklerCallback(ActivityKritiklerCallback activityKritiklerCallback){
        this.activityKritiklerCallback=activityKritiklerCallback;
    }

    public void setPrimaryActivityCallback(PrimaryActivityCallback primaryActivityCallback){
        this.primaryActivityCallback=primaryActivityCallback;
    }

    public void notifyActivityKritikler(@Nullable Bundle bundle){
        if(activityKritiklerCallback!=null){
            activityKritiklerCallback.onSomethingsChanged(bundle);
        }
    }

    public void notifyPrimaryActivity(@Nullable Bundle bundle){
        if(primaryActivityCallback!=null){
            primaryActivityCallback.onSomethingsChanged(bundle);
        }
    }

}
