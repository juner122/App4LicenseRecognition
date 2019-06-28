package com.eb.geaiche.stockControl.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Supplier implements Parcelable {

    private  String id;
    private String name;
    //联系电话
    private String mobile;
    //联系人
    private String linkman;
    //地址
    private String address;
    //主营业务
    private String operation;
    //
    private String addTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.linkman);
        dest.writeString(this.address);
        dest.writeString(this.operation);
        dest.writeString(this.addTime);
    }

    public Supplier() {
    }

    protected Supplier(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.mobile = in.readString();
        this.linkman = in.readString();
        this.address = in.readString();
        this.operation = in.readString();
        this.addTime = in.readString();
    }

    public static final Parcelable.Creator<Supplier> CREATOR = new Parcelable.Creator<Supplier>() {
        @Override
        public Supplier createFromParcel(Parcel source) {
            return new Supplier(source);
        }

        @Override
        public Supplier[] newArray(int size) {
            return new Supplier[size];
        }
    };
}
