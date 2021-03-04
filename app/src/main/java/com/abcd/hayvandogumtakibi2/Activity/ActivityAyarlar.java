package com.abcd.hayvandogumtakibi2.Activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.abcd.hayvandogumtakibi2.Misc.AlarmLauncher;
import com.abcd.hayvandogumtakibi2.Misc.PreferencesHolder;
import com.abcd.hayvandogumtakibi2.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ActivityAyarlar extends AppCompatActivity {

    final Context context=this;
    private static final int ALARM_REQ_CODE = 1233;
    private static final String ALARM_INTENT = "SET_AN_ALARM";
    Button changeHour, changeRange;
    TextView notificationHour, notifyRange, textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);
        notificationHour=findViewById(R.id.text_hour);
        notifyRange=findViewById(R.id.text_range);
        textSize=findViewById(R.id.text_size);
        final Button changeTextSize=findViewById(R.id.change_textSize);
        final SwitchMaterial switchNotifications=findViewById(R.id.switch_notifications);
        final SwitchMaterial switchCleaner=findViewById(R.id.switch_cleaner);
        changeHour=findViewById(R.id.change_hour);
        changeRange=findViewById(R.id.change_the_range);
        switchNotifications.setChecked(PreferencesHolder.getIsIncomingBirthNotEnabled(context));
        switchCleaner.setChecked(PreferencesHolder.getIsCacheCleanerEnabled(context));
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PreferencesHolder.setIsIncomingBirthNotEnabled(context,true);
                    sendBroadcast(new Intent(context, AlarmLauncher.class).setAction(ALARM_INTENT));
                    enableNotElements();
                }
                else{
                    PreferencesHolder.setIsIncomingBirthNotEnabled(context,false);
                    final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    final Intent intent = new Intent(getApplicationContext(), AlarmLauncher.class);
                    final PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), ALARM_REQ_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    disableNotElements();
                }
            }
        });
        switchCleaner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesHolder.setIsCacheCleanerEnabled(context, isChecked);
            }
        });
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
                        sendBroadcast(new Intent(context, AlarmLauncher.class).setAction(ALARM_INTENT));
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
        changeTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final float[] selectedSize = {14.0f};
                final Dialog textSizeDialog=new Dialog(context);
                textSizeDialog.setContentView(R.layout.layout_textsize_dialog);
                final RadioGroup radioGroup=textSizeDialog.findViewById(R.id.radio_group_textSize);
                final Button btnPositive=textSizeDialog.findViewById(R.id.btn_ok);
                final Button btnNegative=textSizeDialog.findViewById(R.id.btn_cancel);
                final TextView textView=textSizeDialog.findViewById(R.id.title_text);
                textView.setText(new StringBuilder(getString(R.string.text_textSize))
                        .append(" ")
                        .append((int)PreferencesHolder.getCardTextSize(context)));
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId==R.id.sizeLarge){
                            selectedSize[0] =18.0f;
                        }
                        else if(checkedId==R.id.sizeMedium){
                            selectedSize[0] =14.0f;
                        }
                        else if(checkedId==R.id.sizeSmall){
                            selectedSize[0] =12.0f;
                        }
                        textView.setText(new StringBuilder(getString(R.string.text_textSize))
                                .append(" ")
                                .append((int)selectedSize[0]));
                    }
                });
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textSize.setText(new StringBuilder(getString(R.string.text_textSize))
                                .append(" ")
                                .append((int)selectedSize[0]));
                        PreferencesHolder.setCardTextSize(context,selectedSize[0]);
                        textSizeDialog.dismiss();
                    }
                });
                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textSizeDialog.dismiss();
                    }
                });
                textSizeDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!PreferencesHolder.getIsIncomingBirthNotEnabled(context)){
            disableNotElements();
        }
        notificationHour.setText(new StringBuilder(getString(R.string.text_daily_not_hour))
                .append(" ")
                .append(PreferencesHolder.getAlarmHour(context)));
        notifyRange.setText(new StringBuilder(getString(R.string.text_notify_range))
                .append(" ")
                .append(PreferencesHolder.getDayRange(context)));
        textSize.setText(new StringBuilder(getString(R.string.text_textSize))
                .append(" ")
                .append((int)PreferencesHolder.getCardTextSize(context)));
    }

    protected void disableNotElements(){
        changeHour.setEnabled(false);
        changeRange.setEnabled(false);
        changeHour.setTextColor(Color.parseColor("#bdbdbd"));
        changeRange.setTextColor(Color.parseColor("#bdbdbd"));
    }
    protected void enableNotElements(){
        changeHour.setEnabled(true);
        changeRange.setEnabled(true);
        changeHour.setTextColor(Color.parseColor("#138EE9"));
        changeRange.setTextColor(Color.parseColor("#138EE9"));
    }

}