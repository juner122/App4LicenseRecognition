package com.frank.plate.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class QueryByCarEntity implements Parcelable {


    private OrderInfoEntity orderInfo;
    private List<OrderGoodEntity> orderGoods;
    private List<CarEntity> carList;
    private UserEntity user;


    public OrderInfoEntity getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoEntity orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<OrderGoodEntity> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(List<OrderGoodEntity> orderGoods) {
        this.orderGoods = orderGoods;
    }

    public List<CarEntity> getCarList() {
        return carList;
    }

    public void setCarList(List<CarEntity> carList) {
        this.carList = carList;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "QueryByCarEntity{" +
                "orderInfo=" + orderInfo +
                ", orderGoods=" + orderGoods +
                ", carList=" + carList +
                ", user=" + user +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.orderInfo, flags);
        dest.writeList(this.orderGoods);
        dest.writeList(this.carList);
        dest.writeParcelable(this.user, flags);
    }

    public QueryByCarEntity() {
    }

    protected QueryByCarEntity(Parcel in) {
        this.orderInfo = in.readParcelable(OrderInfoEntity.class.getClassLoader());
        this.orderGoods = new ArrayList<OrderGoodEntity>();
        in.readList(this.orderGoods, OrderGoodEntity.class.getClassLoader());
        this.carList = new ArrayList<CarEntity>();
        in.readList(this.carList, CarEntity.class.getClassLoader());
        this.user = in.readParcelable(UserEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<QueryByCarEntity> CREATOR = new Parcelable.Creator<QueryByCarEntity>() {
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
