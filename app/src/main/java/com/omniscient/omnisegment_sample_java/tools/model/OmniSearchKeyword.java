package com.omniscient.omnisegment_sample_java.tools.model;

import com.google.gson.annotations.SerializedName;

public class OmniSearchKeyword {
    @SerializedName("search_string")
    public String mKeyword;

    public OmniSearchKeyword(String keyword) {
        this.mKeyword = keyword;
    }
}
