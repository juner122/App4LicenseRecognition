package com.frank.plate.bean;

public class BillEntityItem {


    String id;
    String userId;
    String per;
    String aft;
    String balance;
    String type;
    String status;
    String createTime;
    String orderSn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public String getAft() {
        return aft;
    }

    public void setAft(String aft) {
        this.aft = aft;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    @Override
    public String toString() {
        return "BillEntityItem{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", per='" + per + '\'' +
                ", aft='" + aft + '\'' +
                ", balance='" + balance + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", createTime='" + createTime + '\'' +
                ", orderSn='" + orderSn + '\'' +
                '}';
    }
}
