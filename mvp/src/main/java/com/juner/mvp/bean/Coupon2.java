package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Coupon2 implements Parcelable {

    String id;
    String dept_id;//门店id
    String superposition;//1可叠加//0不可叠加
    String name;
    String type;///类型1满减券
    String min_amount;//最少使用金额
    String type_money;//减免金额
    String use_end_date;//使用结束时间
    String cycle;//有效周期

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuperposition() {
        return superposition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSuperposition(String superposition) {
        this.superposition = superposition;
    }

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(String min_amount) {
        this.min_amount = min_amount;
    }

    public String getType_money() {
        return type_money;
    }

    public void setType_money(String type_money) {
        this.type_money = type_money;
    }

    public String getUse_end_date() {
        return use_end_date;
    }

    public void setUse_end_date(String use_end_date) {
        this.use_end_date = use_end_date;
    }

    public Coupon2() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.dept_id);
        dest.writeString(this.superposition);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.min_amount);
        dest.writeString(this.type_money);
        dest.writeString(this.use_end_date);
        dest.writeString(this.cycle);
    }

    protected Coupon2(Parcel in) {
        this.id = in.readString();
        this.dept_id = in.readString();
        this.superposition = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.min_amount = in.readString();
        this.type_money = in.readString();
        this.use_end_date = in.readString();
        this.cycle = in.readString();
    }

    public static final Creator<Coupon2> CREATOR = new Creator<Coupon2>() {
        @Override
        public Coupon2 createFromParcel(Parcel source) {
            return new Coupon2(source);
        }

        @Override
        public Coupon2[] newArray(int size) {
            return new Coupon2[size];
        }
    };
}
