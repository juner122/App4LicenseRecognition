package com.juner.mvp.bean;

public class ShopImage {


    int id;
    int shopId;
    String shopImage;
    int type;
    int status;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
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

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ShopImage{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", shopImage='" + shopImage + '\'' +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
