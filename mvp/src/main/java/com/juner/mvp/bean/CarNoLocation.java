package com.juner.mvp.bean;

public class CarNoLocation extends SelectedBean {

    String location;

    public CarNoLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
