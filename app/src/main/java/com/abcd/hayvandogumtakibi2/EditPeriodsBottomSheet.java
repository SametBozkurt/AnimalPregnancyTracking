package com.abcd.hayvandogumtakibi2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditPeriodsBottomSheet extends BottomSheetDialogFragment {

    EditText periodCow, periodSheep, periodGoat, periodCat, periodDog, periodHamster, periodHorse, periodDonkey, periodCamel;
    Handler dataBinder=new Handler();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final PeriodsHolder periodsHolder=PeriodsHolder.getInstance(getContext());
        final BottomSheetDialog bottomSheetDialog=(BottomSheetDialog)super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ConstraintLayout constraintLayout = bottomSheetDialog.findViewById(R.id.parent);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(constraintLayout);
                ViewGroup.LayoutParams layoutParams = constraintLayout.getLayoutParams();
                int windowHeight = getWindowHeight();
                if (layoutParams != null) {
                    layoutParams.height = windowHeight;
                }
                constraintLayout.setLayoutParams(layoutParams);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        View view=View.inflate(getContext(), R.layout.dialog_edit_periods, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        Button save_periods=view.findViewById(R.id.save_periods), resetPeriods=view.findViewById(R.id.btn_reset);
        final CheckBox checkBoxOptions=view.findViewById(R.id.checkbox_option);
        periodCow=view.findViewById(R.id.period_cow);
        periodSheep=view.findViewById(R.id.period_sheep);
        periodGoat=view.findViewById(R.id.period_goat);
        periodCat=view.findViewById(R.id.period_cat);
        periodDog=view.findViewById(R.id.period_dog);
        periodHamster=view.findViewById(R.id.period_hamster);
        periodHorse=view.findViewById(R.id.period_horse);
        periodDonkey=view.findViewById(R.id.period_donkey);
        periodCamel=view.findViewById(R.id.period_camel);
        dataBinder.postDelayed(new Runnable() {
            @Override
            public void run() {
                periodCow.setText(String.valueOf(periodsHolder.getPeriodCow()));
                periodSheep.setText(String.valueOf(periodsHolder.getPeriodSheep()));
                periodGoat.setText(String.valueOf(periodsHolder.getPeriodGoat()));
                periodCat.setText(String.valueOf(periodsHolder.getPeriodCat()));
                periodDog.setText(String.valueOf(periodsHolder.getPeriodDog()));
                periodHamster.setText(String.valueOf(periodsHolder.getPeriodHamster()));
                periodHorse.setText(String.valueOf(periodsHolder.getPeriodHorse()));
                periodDonkey.setText(String.valueOf(periodsHolder.getPeriodDonkey()));
                periodCamel.setText(String.valueOf(periodsHolder.getPeriodCamel()));
            }
        },100);
        save_periods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle_periods=new Bundle();
                bundle_periods.putString("cow",periodCow.getText().toString());
                bundle_periods.putString("sheep",periodSheep.getText().toString());
                bundle_periods.putString("goat",periodGoat.getText().toString());
                bundle_periods.putString("cat",periodCat.getText().toString());
                bundle_periods.putString("dog",periodDog.getText().toString());
                bundle_periods.putString("hamster",periodHamster.getText().toString());
                bundle_periods.putString("horse",periodHorse.getText().toString());
                bundle_periods.putString("donkey",periodDonkey.getText().toString());
                bundle_periods.putString("camel",periodCamel.getText().toString());
                saveAllInputs(periodsHolder,bundle_periods,checkBoxOptions.isChecked());
                bottomSheetDialog.dismiss();
            }
        });
        resetPeriods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle_periods=new Bundle();
                bundle_periods.putString("cow","283");
                bundle_periods.putString("sheep","152");
                bundle_periods.putString("goat","150");
                bundle_periods.putString("cat","65");
                bundle_periods.putString("dog","64");
                bundle_periods.putString("hamster","16");
                bundle_periods.putString("horse","335");
                bundle_periods.putString("donkey","365");
                bundle_periods.putString("camel","390");
                saveAllInputs(periodsHolder,bundle_periods,checkBoxOptions.isChecked());
                bottomSheetDialog.dismiss();
            }
        });
        return bottomSheetDialog;
    }

    private void saveAllInputs(final PeriodsHolder periodsHolder, final Bundle bundle, final boolean recalculateDates){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                periodsHolder.setPeriodCow(bundle.getString("cow"));
                periodsHolder.setPeriodSheep(bundle.getString("sheep"));
                periodsHolder.setPeriodGoat(bundle.getString("goat"));
                periodsHolder.setPeriodCat(bundle.getString("cat"));
                periodsHolder.setPeriodDog(bundle.getString("dog"));
                periodsHolder.setPeriodHamster(bundle.getString("hamster"));
                periodsHolder.setPeriodHorse(bundle.getString("horse"));
                periodsHolder.setPeriodDonkey(bundle.getString("donkey"));
                periodsHolder.setPeriodCamel(bundle.getString("camel"));
                if(recalculateDates){
                    SQLiteDatabaseHelper sqLiteDatabaseHelper=SQLiteDatabaseHelper.getInstance(getContext());
                    sqLiteDatabaseHelper.recalculateEstBirthDates(getContext());
                }
            }
        });
        thread.start();
    }

    private int getWindowHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }



}
