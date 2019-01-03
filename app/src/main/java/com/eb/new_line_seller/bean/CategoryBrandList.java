package com.eb.new_line_seller.bean;

import java.util.List;

public class CategoryBrandList {



    List<GoodsEntity> goodList;
    List<Category> categoryList;

    public List<GoodsEntity> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<GoodsEntity> goodList) {
        this.goodList = goodList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public String toString() {
        return "CategoryBrandList{" +
                "goodList=" + goodList +
                ", categoryList=" + categoryList +
                '}';
    }
}
