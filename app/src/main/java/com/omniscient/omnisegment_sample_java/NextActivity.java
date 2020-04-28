package com.omniscient.omnisegment_sample_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.omniscient.omnisegment_java.OmniAnalytics;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
    }

    @Override
    protected void onStart() {
        super.onStart();
        OmniAnalytics.getInstance(this).setScreenName("NextActivityScreen", "NextActivityScreenClass");
    }

    public void eventClick(View view) {
//        OmniAnalytics.getInstance(this).logEvent("NextActivityEventClick",null);
        startActivity(new Intent(this,ThirdActivity.class));
    }
}
