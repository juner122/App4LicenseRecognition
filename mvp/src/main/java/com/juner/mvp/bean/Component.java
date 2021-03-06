package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

//	零件pojo类
public class Component implements Parcelable {

    //第二级分类主键id
    private int categoryId;
    //商品名
    private String name;
    //描述，备注
    private String goodsDesc;
    //价格
    private String retailPrice;
    private int openStatus;

    public int getOpenStatus() {

        return openStatus;
    }

    public void setOpenStatus(int openStatus) {
        this.openStatus = openStatus;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Component() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.categoryId);
        dest.writeString(this.name);
        dest.writeString(this.goodsDesc);
        dest.writeString(this.retailPrice);
        dest.writeInt(this.openStatus);
    }

    protected Component(Parcel in) {
        this.categoryId = in.readInt();
        this.name = in.readString();
        this.goodsDesc = in.readString();
        this.retailPrice = in.readString();
        this.openStatus = in.readInt();
    }

    public static final Creator<Component> CREATOR = new Creator<Component>() {
        @Override
        public Component createFromParcel(Parcel source) {
            return new Component(source);
        }

        @Override
        public Component[] newArray(int size) {
            return new Component[size];
        }
    };
}
