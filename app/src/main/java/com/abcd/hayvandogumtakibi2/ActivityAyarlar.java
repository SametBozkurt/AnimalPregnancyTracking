package com.abcd.hayvandogumtakibi2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class ActivityAyarlar extends AppCompatActivity {

    final Context context=this;
    private static final int ALARM_REQ_CODE = 1233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);
        final SwitchMaterial switchNotifications=findViewById(R.id.switch_notifications);
        switchNotifications.setChecked(PreferencesHolder.getIsIncomingBirthNotEnabled(context));
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PreferencesHolder.setIsIncomingBirthNotEnabled(context,true);
                    final String INTENT_ACTION= "SET_AN_ALARM" ;
                    sendBroadcast(new Intent(context, AlarmLauncher.class).setAction(INTENT_ACTION));
                    Toast.makeText(context,"Alarm set",Toast.LENGTH_SHORT).show();
                }
                else{
                    PreferencesHolder.setIsIncomingBirthNotEnabled(context,false);
                    final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    final Intent intent = new Intent(getApplicationContext(), AlarmLauncher.class);
                    final PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), ALARM_REQ_CODE, intent, 0);
                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(context,"Alarm canceled",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}