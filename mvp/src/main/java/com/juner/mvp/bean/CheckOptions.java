package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

//检修项目
public class CheckOptions implements Parcelable {

    int id;
    int type;//分类
    String name;
    String describe;//描述
    //1选中 0未选中
    private Integer selected = 1;

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeString(this.describe);
        dest.writeValue(this.selected);
    }

    public CheckOptions() {
    }


    public CheckOptions(int type) {
        this.type = type;
    }


    protected CheckOptions(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.name = in.readString();
        this.describe = in.readString();
        this.selected = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<CheckOptions> CREATOR = new Parcelable.Creator<CheckOptions>() {
        @Override
        public CheckOptions createFromParcel(Parcel source) {
            return new CheckOptions(source);
        }

        @Override
        public CheckOptions[] newArray(int size) {
            return new CheckOptions[size];
        }
    };


}
