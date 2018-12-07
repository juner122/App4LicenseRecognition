package com.frank.plate.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class QueryByCarEntity implements Parcelable {


    private List<OrderInfoEntity> orders;
    private List<CarEntity> carList;
    private UserEntity member;

    public List<OrderInfoEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderInfoEntity> orders) {
        this.orders = orders;
    }

    public List<CarEntity> getCarList() {
        return carList;
    }

    public void setCarList(List<CarEntity> carList) {
        this.carList = carList;
    }

    public UserEntity getMember() {
        return member;
    }

    public void setMember(UserEntity member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "QueryByCarEntity{" +
                "orders=" + orders +
                ", carList=" + carList +
                ", member=" + member +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.orders);
        dest.writeTypedList(this.carList);
        dest.writeParcelable(this.member, flags);
    }

    public QueryByCarEntity() {
    }

    protected QueryByCarEntity(Parcel in) {
        this.orders = in.createTypedArrayList(OrderInfoEntity.CREATOR);
        this.carList = in.createTypedArrayList(CarEntity.CREATOR);
        this.member = in.readParcelable(UserEntity.class.getClassLoader());
    }

    public static final Creator<QueryByCarEntity> CREATOR = new Creator<QueryByCarEntity>() {
        @Override
        public QueryByCarEntity createFromParcel(Parcel source) {
            return new QueryByCarEntity(source);
        }

        @Override
        public QueryByCarEntity[] newArray(int size) {
            return new QueryByCarEntity[size];
        }
    };
}
