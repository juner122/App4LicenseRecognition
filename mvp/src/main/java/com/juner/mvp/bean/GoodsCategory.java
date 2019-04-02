package com.juner.mvp.bean;


//商品大分类
public class GoodsCategory {

    int id;
    String categoryId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryAppLogo() {
        return categoryAppLogo;
    }

    public void setCategoryAppLogo(String categoryAppLogo) {
        this.categoryAppLogo = categoryAppLogo;
    }
}
