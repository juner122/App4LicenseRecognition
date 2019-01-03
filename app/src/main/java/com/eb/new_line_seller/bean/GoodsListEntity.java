package com.eb.new_line_seller.bean;


import java.util.List;

public class GoodsListEntity {

    List<GoodsEntity> list;

    public List<GoodsEntity> getGoodsList() {
        return list;
    }

    public void setGoodsList(List<GoodsEntity> goodsList) {
        this.list = goodsList;
    }

    @Override
    public String toString() {
        return "GoodsListEntity{" +
                "goodsList=" + list +
                '}';
    }
}
