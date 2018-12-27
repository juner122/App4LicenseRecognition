package com.frank.plate.bean;

public class ActivityEntityItem {


    int id;
    String activityName;
    String activityType;
    String activityImage;
    String activityEnd;
    String activityStart;
    String activityExplain;
    String activityPrice;
    String joinNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getActivityExplain() {
        return activityExplain;
    }

    public void setActivityExplain(String activityExplain) {
        this.activityExplain = activityExplain;
    }

    public String getActivityPrice() {
        return null == activityPrice || activityPrice.equals("") ? "0.00" : activityPrice;
    }

    public void setActivityPrice(String activityPrice) {
        this.activityPrice = activityPrice;
    }

    public String getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(String joinNum) {
        this.joinNum = joinNum;
    }


}
