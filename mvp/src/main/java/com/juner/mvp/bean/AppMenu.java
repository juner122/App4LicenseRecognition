package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AppMenu implements Parcelable {


    String name;
    String perms;

    List<MenuBut> list;

    String androidInfo;

    public String getAndroidInfo() {
        return androidInfo;
    }

    public void setAndroidInfo(String androidInfo) {
        this.androidInfo = androidInfo;
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

    public List<MenuBut> getList() {
        return list;
    }

    public void setList(List<MenuBut> list) {
        this.list = list;
    }

    public AppMenu() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.perms);
        dest.writeTypedList(this.list);
        dest.writeString(this.androidInfo);
    }

    protected AppMenu(Parcel in) {
        this.name = in.readString();
        this.perms = in.readString();
        this.list = in.createTypedArrayList(MenuBut.CREATOR);
        this.androidInfo = in.readString();
    }

    public static final Creator<AppMenu> CREATOR = new Creator<AppMenu>() {
        @Override
        public AppMenu createFromParcel(Parcel source) {
            return new AppMenu(source);
        }

        @Override
        public AppMenu[] newArray(int size) {
            return new AppMenu[size];
        }
    };
}
