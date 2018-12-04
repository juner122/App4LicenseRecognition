package com.frank.plate.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderInfo implements Parcelable {

    ShopEntity shop;

    OrderInfoEntity orderInfo;




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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.shop, flags);
        dest.writeParcelable(this.orderInfo, flags);
    }

    public OrderInfo() {
    }

    protected OrderInfo(Parcel in) {
        this.shop = in.readParcelable(ShopEntity.class.getClassLoader());
        this.orderInfo = in.readParcelable(OrderInfoEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<OrderInfo> CREATOR = new Parcelable.Creator<OrderInfo>() {
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
