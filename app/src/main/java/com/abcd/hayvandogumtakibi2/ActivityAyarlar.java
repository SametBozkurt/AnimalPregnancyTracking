package com.abcd.hayvandogumtakibi2;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class ActivityAyarlar extends AppCompatActivity {

    final Context context=this;
    private static final int ALARM_REQ_CODE = 1233;
    private static final String ALARM_INTENT = "SET_AN_ALARM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);
        final TextView notificationHour=findViewById(R.id.text_hour);
        final TextView notifyRange=findViewById(R.id.text_range);
        final SwitchMaterial switchNotifications=findViewById(R.id.switch_notifications);
        final Button changeHour=findViewById(R.id.change_hour);
        final Button changeRange=findViewById(R.id.change_the_range);
        switchNotifications.setChecked(PreferencesHolder.getIsIncomingBirthNotEnabled(context));
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PreferencesHolder.setIsIncomingBirthNotEnabled(context,true);
                    sendBroadcast(new Intent(context, AlarmLauncher.class).setAction(ALARM_INTENT));
                    Toast.makeText(context,"Alarm set",Toast.LENGTH_SHORT).show();
                }
                else{
                    PreferencesHolder.setIsIncomingBirthNotEnabled(context,false);
                    final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    final Intent intent = new Intent(getApplicationContext(), AlarmLauncher.class);
                    final PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), ALARM_REQ_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(context,"Alarm canceled",Toast.LENGTH_SHORT).show();
                }
            }
        });
        notificationHour.setText(new StringBuilder(getString(R.string.text_daily_not_hour))
                .append(" ")
                .append(PreferencesHolder.getAlarmHour(context)));
        notifyRange.setText(new StringBuilder(getString(R.string.text_notify_range))
                .append(" ")
                .append(PreferencesHolder.getDayRange(context)));
        changeHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] selectedHour = {PreferencesHolder.getAlarmHour(context)};
                final Dialog timepickerDialog=new Dialog(context);
                timepickerDialog.setContentView(R.layout.layout_timepicker_dialog);
                final TimePicker timePicker=timepickerDialog.findViewById(R.id.timePicker);
                final Button btnPositive=timepickerDialog.findViewById(R.id.btn_ok);
                final Button btnNegative=timepickerDialog.findViewById(R.id.btn_cancel);
                timePicker.setIs24HourView(true);
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    timePicker.setHour(selectedHour[0]);
                    timePicker.setMinute(30);
                }
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        selectedHour[0]=hourOfDay;
                    }
                });
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notificationHour.setText(new StringBuilder(getString(R.string.text_daily_not_hour))
                                .append(" ")
                                .append(selectedHour[0]));
                        PreferencesHolder.setAlarmHour(context,selectedHour[0]);
                        timepickerDialog.dismiss();
                    }
                });
                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timepickerDialog.dismiss();
                    }
                });
                timepickerDialog.show();
            }
        });
        changeRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] daysLeft = {PreferencesHolder.getDayRange(context)};
                final Dialog seekbarDialog=new Dialog(context);
                seekbarDialog.setContentView(R.layout.layout_seekbar_dialog);
                final SeekBar seekBar=seekbarDialog.findViewById(R.id.seekbar);
                final Button btnPositive=seekbarDialog.findViewById(R.id.btn_ok);
                final Button btnNegative=seekbarDialog.findViewById(R.id.btn_cancel);
                final TextView text_daysLeft=seekbarDialog.findViewById(R.id.days_left);
                seekBar.setProgress(daysLeft[0]);
                text_daysLeft.setText(String.valueOf(daysLeft[0]));
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        daysLeft[0]=progress;
                        text_daysLeft.setText(String.valueOf(progress));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyRange.setText(new StringBuilder(getString(R.string.text_notify_range))
                                .append(" ")
                                .append(daysLeft[0]));
                        PreferencesHolder.setDayRange(context,daysLeft[0]);
                        seekbarDialog.dismiss();
                    }
                });
                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        seekbarDialog.dismiss();
                    }
                });
                seekbarDialog.show();
            }
        });
    }
}