package com.juner.mvp.bean;

public class XgxPurchaseOrderGoodsPojo {
    String id;
    String goodsId;
    String purchaseOrderId;
    String goodsStandardId;
    int number;
    String goodsPrice;
    String goodsTitle;
    String image;
    String goodsStandardTitle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGoodsStandardTitle() {
        return goodsStandardTitle;
    }

    public void setGoodsStandardTitle(String goodsStandardTitle) {
        this.goodsStandardTitle = goodsStandardTitle;
    }

    public String getGoodsStandardPrice() {
        return goodsStandardPrice;
    }

    public void setGoodsStandardPrice(String goodsStandardPrice) {
        this.goodsStandardPrice = goodsStandardPrice;
    }

    String goodsStandardPrice;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsStandardId() {
        return goodsStandardId;
    }

    public void setGoodsStandardId(String goodsStandardId) {
        this.goodsStandardId = goodsStandardId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }
}
