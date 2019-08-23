package com.juner.mvp.bean;

import java.util.List;

public class PlateInfo {
    String createPicTime;//识别时间

    List<Plate> pPlate;

    public List<Plate> getpPlate() {
        return pPlate;
    }

    public void setpPlate(List<Plate> pPlate) {
        this.pPlate = pPlate;
    }

    public String getCreatePicTime() {
        return createPicTime;
    }

    public void setCreatePicTime(String createPicTime) {
        this.createPicTime = createPicTime;
    }
}
