package com.abcd.hayvandogumtakibi2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ActivityAppInfo extends AppCompatActivity {

    final String appVer=BuildConfig.VERSION_NAME;
    ConstraintLayout constraintLayout;
    LinearLayout layout_policy, layout_vote, layout_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_info_layout);
        constraintLayout=findViewById(R.id.parent_layout);
        layout_policy = findViewById(R.id.lyt_policy);
        layout_vote = findViewById(R.id.lyt_vote);
        layout_version = findViewById(R.id.lyt_version);
        final TextView textView_version = findViewById(R.id.txt_version);
        layout_policy.setAlpha(0f);
        layout_vote.setAlpha(0f);
        layout_version.setAlpha(0f);
        layout_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent=new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.APP_POLICY)));
                startActivity(intent);
            }
        });
        layout_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent=new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.APP_URL)));
                startActivity(intent);
            }
        });
        final StringBuilder ver=new StringBuilder();
        ver.append(getString(R.string.version)).append(" ").append(appVer);
        textView_version.setText(ver);
        animate_containers();
    }

    void animate_containers(){
        constraintLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_version.animate().alpha(1f).setDuration(200).start();
            }
        },200);
        constraintLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_policy.animate().alpha(1f).setDuration(200).start();
            }
        },400);
        constraintLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_vote.animate().alpha(1f).setDuration(200).start();
            }
        },600);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}