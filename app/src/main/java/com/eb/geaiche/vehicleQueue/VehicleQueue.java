package com.eb.geaiche.vehicleQueue;

public class VehicleQueue {
    String  id;
    String carNo;
    String addTime;
    String shopId;
    String status;//0未接车，1已接车
    String changeTime;//接车时间;
    String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlateNumber() {
        return carNo;
    }

    public void setPlateNumber(String carNo) {
        this.carNo = carNo;
    }

    public String getTime() {
        return addTime;
    }

    public void setTime(String addTime) {
        this.addTime = addTime;
    }
}
