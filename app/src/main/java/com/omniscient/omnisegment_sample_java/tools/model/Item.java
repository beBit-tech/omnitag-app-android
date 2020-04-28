package com.omniscient.omnisegment_sample_java.tools.model;

import java.io.Serializable;

public abstract class Item implements Serializable {
    private String title;
    public abstract void collect();

    public Item(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
