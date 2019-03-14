package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuBut implements Parcelable {
    String name;
    String perms;
    int orderNum;
    String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

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

    public MenuBut() {
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
        dest.writeString(this.icon);
    }

    protected MenuBut(Parcel in) {
        this.name = in.readString();
        this.perms = in.readString();
        this.orderNum = in.readInt();
        this.icon = in.readString();
    }

    public static final Creator<MenuBut> CREATOR = new Creator<MenuBut>() {
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
