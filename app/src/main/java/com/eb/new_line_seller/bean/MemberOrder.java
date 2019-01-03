package com.eb.new_line_seller.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MemberOrder implements Parcelable {

    MemberEntity member;

    List<OrderInfoEntity> orderList;

    List<CarInfoRequestParameters> carList;

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public List<OrderInfoEntity> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderInfoEntity> orderList) {
        this.orderList = orderList;
    }

    public List<CarInfoRequestParameters> getCarList() {
        return carList;
    }

    public void setCarList(List<CarInfoRequestParameters> carList) {
        this.carList = carList;
    }

    public MemberOrder() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.member, flags);
        dest.writeTypedList(this.orderList);
        dest.writeList(this.carList);
    }

    protected MemberOrder(Parcel in) {
        this.member = in.readParcelable(MemberEntity.class.getClassLoader());
        this.orderList = in.createTypedArrayList(OrderInfoEntity.CREATOR);
        this.carList = new ArrayList<CarInfoRequestParameters>();
        in.readList(this.carList, CarInfoRequestParameters.class.getClassLoader());
    }

    public static final Creator<MemberOrder> CREATOR = new Creator<MemberOrder>() {
        @Override
        public MemberOrder createFromParcel(Parcel source) {
            return new MemberOrder(source);
        }

        @Override
        public MemberOrder[] newArray(int size) {
            return new MemberOrder[size];
        }
    };
}
