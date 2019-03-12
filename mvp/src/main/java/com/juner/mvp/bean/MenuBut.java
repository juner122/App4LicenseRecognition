package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuBut implements Parcelable {
    String name;
    String perms;
    int orderNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.perms);
        dest.writeInt(this.orderNum);
    }

    public MenuBut() {
    }

    protected MenuBut(Parcel in) {
        this.name = in.readString();
        this.perms = in.readString();
        this.orderNum = in.readInt();
    }

    public static final Parcelable.Creator<MenuBut> CREATOR = new Parcelable.Creator<MenuBut>() {
        @Override
        public MenuBut createFromParcel(Parcel source) {
            return new MenuBut(source);
        }

        @Override
        public MenuBut[] newArray(int size) {
            return new MenuBut[size];
        }
    };
}
