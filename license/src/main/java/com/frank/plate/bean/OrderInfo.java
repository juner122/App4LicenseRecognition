package com.frank.plate.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OrderInfo implements Parcelable {

    ShopEntity shop;

    OrderInfoEntity orderInfo;


    Technician receptionist;



    public Technician getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(Technician receptionist) {
        this.receptionist = receptionist;
    }

    public ShopEntity getShop() {
        return shop;
    }

    public void setShop(ShopEntity shop) {
        this.shop = shop;
    }

    public OrderInfoEntity getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoEntity orderInfo) {
        this.orderInfo = orderInfo;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "shop=" + shop +
                ", orderInfo=" + orderInfo +
                '}';
    }

    public OrderInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.shop, flags);
        dest.writeParcelable(this.orderInfo, flags);
        dest.writeParcelable(this.receptionist, flags);
    }

    protected OrderInfo(Parcel in) {
        this.shop = in.readParcelable(ShopEntity.class.getClassLoader());
        this.orderInfo = in.readParcelable(OrderInfoEntity.class.getClassLoader());
        this.receptionist = in.readParcelable(Technician.class.getClassLoader());
    }

    public static final Creator<OrderInfo> CREATOR = new Creator<OrderInfo>() {
        @Override
        public OrderInfo createFromParcel(Parcel source) {
            return new OrderInfo(source);
        }

        @Override
        public OrderInfo[] newArray(int size) {
            return new OrderInfo[size];
        }
    };
}
