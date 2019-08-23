package com.juner.mvp.bean;

public class Plate {

    String license;
    int bFakePlate;//是否是车牌 1是，0否

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getbFakePlate() {
        return bFakePlate;
    }

    public void setbFakePlate(int bFakePlate) {
        this.bFakePlate = bFakePlate;
    }
}
