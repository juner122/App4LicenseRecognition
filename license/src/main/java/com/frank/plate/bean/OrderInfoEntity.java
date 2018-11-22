package com.frank.plate.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderInfoEntity implements Parcelable {



    String id;
    String order_sn;
    String user_id;
    String order_status;
    String shipping_status;
    String pay_status;
    String consignee;//李鹏军
    String mobile;
    String order_status_text;//未付款
    String add_time;//


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrder_status_text() {
        return order_status_text;
    }

    public void setOrder_status_text(String order_status_text) {
        this.order_status_text = order_status_text;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    @Override
    public String toString() {
        return "OrderInfoEntity{" +
                "id='" + id + '\'' +
                ", order_sn='" + order_sn + '\'' +
                ", user_id='" + user_id + '\'' +
                ", order_status='" + order_status + '\'' +
                ", shipping_status='" + shipping_status + '\'' +
                ", pay_status='" + pay_status + '\'' +
                ", consignee='" + consignee + '\'' +
                ", mobile='" + mobile + '\'' +
                ", order_status_text='" + order_status_text + '\'' +
                ", add_time='" + add_time + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.order_sn);
        dest.writeString(this.user_id);
        dest.writeString(this.order_status);
        dest.writeString(this.shipping_status);
        dest.writeString(this.pay_status);
        dest.writeString(this.consignee);
        dest.writeString(this.mobile);
        dest.writeString(this.order_status_text);
        dest.writeString(this.add_time);
    }

    public OrderInfoEntity() {
    }

    protected OrderInfoEntity(Parcel in) {
        this.id = in.readString();
        this.order_sn = in.readString();
        this.user_id = in.readString();
        this.order_status = in.readString();
        this.shipping_status = in.readString();
        this.pay_status = in.readString();
        this.consignee = in.readString();
        this.mobile = in.readString();
        this.order_status_text = in.readString();
        this.add_time = in.readString();
    }

    public static final Parcelable.Creator<OrderInfoEntity> CREATOR = new Parcelable.Creator<OrderInfoEntity>() {
        @Override
        public OrderInfoEntity createFromParcel(Parcel source) {
            return new OrderInfoEntity(source);
        }

        @Override
        public OrderInfoEntity[] newArray(int size) {
            return new OrderInfoEntity[size];
        }
    };
}
