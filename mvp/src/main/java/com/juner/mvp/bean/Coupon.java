package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class Coupon extends SelectedBean implements Parcelable {

    int id;
    int user_coupon_id;
    String name;
    double type_money;
    double min_amount;
    String use_end_date;
    int coupon_status;  //1可用，2已用，3过期
    String coupon_number;
    String used_time;
    String convert_time;//核销时间
    String username;
    String mobile;
    String stock;

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getConvert_time() {
        return convert_time;
    }

    public void setConvert_time(String convert_time) {
        this.convert_time = convert_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsed_time() {
        return used_time;
    }

    public void setUsed_time(String used_time) {
        this.used_time = used_time;
    }

    public String getCoupon_number() {
        return coupon_number;
    }

    public void setCoupon_number(String coupon_number) {
        this.coupon_number = coupon_number;
    }

    public double getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(double min_amount) {
        this.min_amount = min_amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_coupon_id() {
        return user_coupon_id;
    }

    public void setUser_coupon_id(int user_coupon_id) {
        this.user_coupon_id = user_coupon_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getType_money() {
        return type_money;
    }

    public void setType_money(double type_money) {
        this.type_money = type_money;
    }

    public String getUse_end_date() {
        return use_end_date;
    }

    public void setUse_end_date(String use_end_date) {
        this.use_end_date = use_end_date;
    }

    public int getCoupon_status() {
        return coupon_status;
    }

    public void setCoupon_status(int coupon_status) {
        this.coupon_status = coupon_status;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", user_coupon_id=" + user_coupon_id +
                ", name='" + name + '\'' +
                ", type_money='" + type_money + '\'' +
                ", use_end_date='" + use_end_date + '\'' +
                ", coupon_status=" + coupon_status +
                '}';
    }

    public Coupon() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.user_coupon_id);
        dest.writeString(this.name);
        dest.writeDouble(this.type_money);
        dest.writeDouble(this.min_amount);
        dest.writeString(this.use_end_date);
        dest.writeInt(this.coupon_status);
        dest.writeString(this.coupon_number);
        dest.writeString(this.used_time);
        dest.writeString(this.convert_time);
        dest.writeString(this.username);
        dest.writeString(this.mobile);
    }

    protected Coupon(Parcel in) {
        this.id = in.readInt();
        this.user_coupon_id = in.readInt();
        this.name = in.readString();
        this.type_money = in.readDouble();
        this.min_amount = in.readDouble();
        this.use_end_date = in.readString();
        this.coupon_status = in.readInt();
        this.coupon_number = in.readString();
        this.used_time = in.readString();
        this.convert_time = in.readString();
        this.username = in.readString();
        this.mobile = in.readString();
    }

    public static final Creator<Coupon> CREATOR = new Creator<Coupon>() {
        @Override
        public Coupon createFromParcel(Parcel source) {
            return new Coupon(source);
        }

        @Override
        public Coupon[] newArray(int size) {
            return new Coupon[size];
        }
    };
}
