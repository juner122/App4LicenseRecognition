package com.eb.new_line_seller.bean;


//订单列表ITEM
public class OrderListItemEntity {


    String plateNumber;
    String orderNumber;
    String date;
    String orderState;
    String money;

    public OrderListItemEntity(String plateNumber, String orderNumber, String date, String orderState, String money) {
        this.plateNumber = plateNumber;
        this.orderNumber = orderNumber;
        this.date = date;
        this.orderState = orderState;
        this.money = money;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
