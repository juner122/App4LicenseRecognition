package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

//检修单配件 查找到的
public class FixPartsSeek implements Parcelable {
    int id;
    int selected;//选择 0 ,1,2

    int number;
    String name;
    String retailPrice;
    String listPicUrl;
    String goodsDesc;//描述

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getListPicUrl() {
        return listPicUrl;
    }

    public void setListPicUrl(String listPicUrl) {
        this.listPicUrl = listPicUrl;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.selected);
        dest.writeInt(this.number);
        dest.writeString(this.name);
        dest.writeString(this.retailPrice);
        dest.writeString(this.listPicUrl);
        dest.writeString(this.goodsDesc);
    }

    public FixPartsSeek() {
    }

    protected FixPartsSeek(Parcel in) {
        this.id = in.readInt();
        this.selected = in.readInt();
        this.number = in.readInt();
        this.name = in.readString();
        this.retailPrice = in.readString();
        this.listPicUrl = in.readString();
        this.goodsDesc = in.readString();
    }

    public static final Parcelable.Creator<FixPartsSeek> CREATOR = new Parcelable.Creator<FixPartsSeek>() {
        @Override
        public FixPartsSeek createFromParcel(Parcel source) {
            return new FixPartsSeek(source);
        }

        @Override
        public FixPartsSeek[] newArray(int size) {
            return new FixPartsSeek[size];
        }
    };
}
