package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class QueryByCarEntity implements Parcelable {


    private List<OrderInfoEntity> orders;
    private List<CarInfoRequestParameters> carList;
    private List<UserEntity> users;

    public List<OrderInfoEntity> getOrders() {

        return orders;
    }

    public void setOrders(List<OrderInfoEntity> orders) {
        this.orders = orders;
    }

    public List<CarInfoRequestParameters> getCarList() {
        return carList;
    }

    public void setCarList(List<CarInfoRequestParameters> carList) {
        this.carList = carList;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.orders);
        dest.writeTypedList(this.carList);
        dest.writeTypedList(this.users);
    }

    public QueryByCarEntity() {
    }

    protected QueryByCarEntity(Parcel in) {
        this.orders = in.createTypedArrayList(OrderInfoEntity.CREATOR);
        this.carList = in.createTypedArrayList(CarInfoRequestParameters.CREATOR);
        this.users = in.createTypedArrayList(UserEntity.CREATOR);
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
