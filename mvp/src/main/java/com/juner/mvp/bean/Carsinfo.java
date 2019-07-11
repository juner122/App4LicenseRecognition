package com.juner.mvp.bean;

import java.util.List;

public class Carsinfo {

    String type;

    List<License> items;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<License> getItems() {
        return items;
    }

    public void setItems(List<License> items) {
        this.items = items;
    }

}
