package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderGoodEntity implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public OrderGoodEntity() {
    }

    protected OrderGoodEntity(Parcel in) {
    }

    public static final Parcelable.Creator<OrderGoodEntity> CREATOR = new Parcelable.Creator<OrderGoodEntity>() {
        @Override
        public OrderGoodEntity createFromParcel(Parcel source) {
            return new OrderGoodEntity(source);
        }

        @Override
        public OrderGoodEntity[] newArray(int size) {
            return new OrderGoodEntity[size];
        }
    };
}
