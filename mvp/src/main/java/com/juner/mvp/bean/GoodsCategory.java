package com.juner.mvp.bean;


//商品大分类
public class GoodsCategory {

    int id;
    int categoryId;
    String categoryTitle;
    String categoryAppLogo;

    public GoodsCategory(String name) {
        this.categoryTitle = name;
    }

    public String getSrc() {
        return categoryAppLogo;
    }

    public void setSrc(String src) {
        this.categoryAppLogo = src;
    }

    public String getName() {

        return categoryTitle;
    }

    public void setName(String name) {
        this.categoryTitle = name;
    }
}
