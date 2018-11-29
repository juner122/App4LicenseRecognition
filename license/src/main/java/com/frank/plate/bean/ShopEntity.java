package com.frank.plate.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ShopEntity implements Parcelable {

    String id;
    String name;
    String address;
    String shopName;
    String phone;
    String status;
    String detail;
    String createTime;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ShopEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", shopName='" + shopName + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", detail='" + detail + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.shopName);
        dest.writeString(this.phone);
        dest.writeString(this.status);
        dest.writeString(this.detail);
        dest.writeString(this.createTime);
    }

    public ShopEntity() {
    }

    protected ShopEntity(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.shopName = in.readString();
        this.phone = in.readString();
        this.status = in.readString();
        this.detail = in.readString();
        this.createTime = in.readString();
    }

    public static final Parcelable.Creator<ShopEntity> CREATOR = new Parcelable.Creator<ShopEntity>() {
        @Override
        public ShopEntity createFromParcel(Parcel source) {
            return new ShopEntity(source);
        }

        @Override
        public ShopEntity[] newArray(int size) {
            return new ShopEntity[size];
        }
    };
}
