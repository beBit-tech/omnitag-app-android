package com.omniscient.omnisegment_sample_java;

import com.omniscient.omnisegment_java.OmniAnalytics;
import com.omniscient.omnisegment_java.OmniApplication;

public class MyApplication extends OmniApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        OmniAnalytics.setTrackingId("OA-fece0556");
        OmniAnalytics.setDEBUG(true);
        OmniAnalytics.getInstance(this);
    }
}
