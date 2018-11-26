package com.frank.plate.bean;

public class GoodsEntity {

    String id;
    String name;
    String goods_sn;
    String brand_id;
    String goods_number;
    String keywords;
    String goods_brief;
    String goods_desc;
    String category_id;
    String primary_pic_url;

    public String getPrimary_pic_url() {
        return primary_pic_url;
    }

    public void setPrimary_pic_url(String primary_pic_url) {
        this.primary_pic_url = primary_pic_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getGoods_brief() {
        return goods_brief;
    }

    public void setGoods_brief(String goods_brief) {
        this.goods_brief = goods_brief;
    }

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    @Override
    public String toString() {
        return "GoodsEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", goods_sn='" + goods_sn + '\'' +
                ", brand_id='" + brand_id + '\'' +
                ", goods_number='" + goods_number + '\'' +
                ", keywords='" + keywords + '\'' +
                ", goods_brief='" + goods_brief + '\'' +
                ", goods_desc='" + goods_desc + '\'' +
                ", category_id='" + category_id + '\'' +
                '}';
    }
}
