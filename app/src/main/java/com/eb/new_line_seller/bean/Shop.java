package com.eb.new_line_seller.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Shop implements Parcelable {


    ShopEntity shop;

    public ShopEntity getShop() {
        return shop;
    }

    public void setShop(ShopEntity shop) {
        this.shop = shop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.shop, flags);
    }

    public Shop() {
    }

    protected Shop(Parcel in) {
        this.shop = in.readParcelable(ShopEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel source) {
            return new Shop(source);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };
}
