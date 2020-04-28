package com.omniscient.omnisegment_sample_java;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.omniscient.omnisegment_java.OmniAnalytics;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

    }

    @Override
    protected void onStart() {
        super.onStart();
        OmniAnalytics.getInstance(this).setScreenName("ThirdActivityScreen", "ThirdActivityScreenClass");
    }
}
