package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AppMenu implements Parcelable {


    String name;
    String perms;

    List<MenuBut> list;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.perms);
        dest.writeList(this.list);
    }

    public AppMenu() {
    }

    protected AppMenu(Parcel in) {
        this.name = in.readString();
        this.perms = in.readString();
        this.list = new ArrayList<MenuBut>();
        in.readList(this.list, MenuBut.class.getClassLoader());
    }

    public static final Parcelable.Creator<AppMenu> CREATOR = new Parcelable.Creator<AppMenu>() {
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
