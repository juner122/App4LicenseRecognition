package com.eb.geaiche.stockControl.bean;

import java.util.List;

public class StockInOrOut {

    String id;
    String shopId;
    String userId;
    String orderId;
    String orderSn;//订单编号
    String type;//1出库2入库
    String totalPrice;//总价
    String addTime;
    String status;
    String remarks;//备注

    List<StockGoods> stockGoodsList;


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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<StockGoods> getStockGoodsList() {
        return stockGoodsList;
    }

    public void setStockGoodsList(List<StockGoods> stockGoodsList) {
        this.stockGoodsList = stockGoodsList;
    }
}
