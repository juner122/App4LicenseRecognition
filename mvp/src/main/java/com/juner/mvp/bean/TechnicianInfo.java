package com.juner.mvp.bean;

import java.util.List;

public class TechnicianInfo {

    String money;
    Technician sysUser;
    List<OrderInfoEntity> orderList;
    List<OrderInfoEntity> saleList;

    ShopEntity shop;

    public ShopEntity getShop() {
        return shop;
    }

    public void setShop(ShopEntity shop) {
        this.shop = shop;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Technician getSysUser() {
        return sysUser;
    }

    public void setSysUser(Technician sysUser) {
        this.sysUser = sysUser;
    }

    public List<OrderInfoEntity> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderInfoEntity> orderList) {
        this.orderList = orderList;
    }

    public List<OrderInfoEntity> getSaleList() {
        return saleList;
    }

    public void setSaleList(List<OrderInfoEntity> saleList) {
        this.saleList = saleList;
    }
}
