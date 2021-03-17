package com.abcd.hayvandogumtakibi2.Misc;

import android.os.Bundle;

import androidx.annotation.Nullable;

public class ActivityInteractor {

    private static ActivityInteractor activityInteractor=null;
    private ActivityInteractorCallback activityInteractorCallback;

    private ActivityInteractor(){}

    public static ActivityInteractor getInstance(){
        if(activityInteractor==null){
            activityInteractor=new ActivityInteractor();
        }
        return activityInteractor;
    }

    public interface ActivityInteractorCallback{
        void onSomethingsChanged(@Nullable Bundle whatChanged);
    }

    public void setActivityInteractorCallback(ActivityInteractorCallback activityInteractorCallback){
        this.activityInteractorCallback=activityInteractorCallback;
    }

    public void changeSomething(@Nullable Bundle bundle){
        if(activityInteractorCallback!=null){
            activityInteractorCallback.onSomethingsChanged(bundle);
        }
    }

}
