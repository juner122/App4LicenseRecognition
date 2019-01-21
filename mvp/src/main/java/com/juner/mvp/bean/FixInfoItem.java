package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 检修单  工时和配件的父对象
 */
public class FixInfoItem implements Parcelable {

    int id;
    int selected;//选择 0 ,1,2

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public boolean selectde() {

        return selected != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.selected);
    }

    public FixInfoItem() {
    }

    protected FixInfoItem(Parcel in) {
        this.id = in.readInt();
        this.selected = in.readInt();
    }

    public static final Parcelable.Creator<FixInfoItem> CREATOR = new Parcelable.Creator<FixInfoItem>() {
        @Override
        public FixInfoItem createFromParcel(Parcel source) {
            return new FixInfoItem(source);
        }

        @Override
        public FixInfoItem[] newArray(int size) {
            return new FixInfoItem[size];
        }
    };
}
