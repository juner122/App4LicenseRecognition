package com.frank.plate.bean;

public class ActivityEntityItem {


    String id;
    String activityName;
    String activityType;
    String activityImage;
    String activityEnd;
    String activityStart;
    String activityExplain;
    String activityPrice;

    public String getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(String activityPrice) {
        this.activityPrice = activityPrice;
    }

    public String getActivityExplain() {
        return activityExplain;
    }

    public void setActivityExplain(String activityExplain) {
        this.activityExplain = activityExplain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(String activityImage) {
        this.activityImage = activityImage;
    }

    public String getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(String activityEnd) {
        this.activityEnd = activityEnd;
    }

    public String getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(String activityStart) {
        this.activityStart = activityStart;
    }

    @Override
    public String toString() {
        return "ActivityEntityItem{" +
                "id='" + id + '\'' +
                ", activityName='" + activityName + '\'' +
                ", activityType='" + activityType + '\'' +
                ", activityImage='" + activityImage + '\'' +
                ", activityEnd='" + activityEnd + '\'' +
                ", activityStart='" + activityStart + '\'' +
                '}';
    }
}
