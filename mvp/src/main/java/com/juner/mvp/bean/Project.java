package com.juner.mvp.bean;

//自定义商品
public class Project {

    //名
    private String goodsTitle;
    //3服务4零件
    private Integer type;
    String price;  //价格

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
