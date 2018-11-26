package com.frank.plate.bean;

import java.util.List;

public class GoodsListEntity {


    List<GoodsEntity> goodsList;

    public List<GoodsEntity> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsEntity> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return "GoodsListEntity{" +
                "goodsList=" + goodsList +
                '}';
    }
}
