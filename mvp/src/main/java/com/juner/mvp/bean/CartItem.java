package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

//购物车商品
public class CartItem extends SelectedBean implements Parcelable {
    Integer id;
    Integer user_id;
    Integer goods_id;
    String goods_sn;
    Integer product_id;//规格id
    String goods_name;
    String retail_product_price;
    Integer number;
    String goodsStandardTitle;//规格名字
    String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getRetail_product_price() {
        return null == retail_product_price ? "0.00" : retail_product_price;
    }

    public void setRetail_product_price(String retail_product_price) {
        this.retail_product_price = retail_product_price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getGoodsStandardTitle() {
        return goodsStandardTitle;
    }

    public void setGoodsStandardTitle(String goodsStandardTitle) {
        this.goodsStandardTitle = goodsStandardTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.user_id);
        dest.writeValue(this.goods_id);
        dest.writeString(this.goods_sn);
        dest.writeValue(this.product_id);
        dest.writeString(this.goods_name);
        dest.writeString(this.retail_product_price);
        dest.writeValue(this.number);
        dest.writeString(this.goodsStandardTitle);
        dest.writeString(this.image);
    }

    public CartItem() {
    }

    protected CartItem(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.goods_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.goods_sn = in.readString();
        this.product_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.goods_name = in.readString();
        this.retail_product_price = in.readString();
        this.number = (Integer) in.readValue(Integer.class.getClassLoader());
        this.goodsStandardTitle = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<CartItem> CREATOR = new Parcelable.Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel source) {
            return new CartItem(source);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}
