package com.eb.new_line_seller.bean;

public class BillEntityItem {


    String id;
    String userId;
    String per;
    String aft;
    String balance;
    int type; //1是提现，2消费， 3 线上收入 4 线下收入
    int status;
    String createTime;
    String orderSn;
    int pay_type;

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public String getTypeString() {


        if (type == 1)
            return "提现";
        if (type == 2)
            return "消费";
        if (type == 3)
            return "线上收入";
        if (type == 4)
            return "线下收入";
        return "-";

    }

    public String getPayTypeString() {


        if (pay_type == 1)
            return "提现";
        if (pay_type == 2)
            return "消费";
        if (pay_type == 3)
            return "线上收入";
        if (pay_type == 4)
            return "线下收入";
        return "-";

    }

    public void setStatus(int status) {
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
