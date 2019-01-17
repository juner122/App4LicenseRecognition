package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

//第二级
public class FixParts2item implements Parcelable {
    int id;
    String name;
    String keywords;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.keywords);
    }

    public FixParts2item() {
    }

    protected FixParts2item(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.keywords = in.readString();

    }

    public static final Parcelable.Creator<FixParts2item> CREATOR = new Parcelable.Creator<FixParts2item>() {
        @Override
        public FixParts2item createFromParcel(Parcel source) {
            return new FixParts2item(source);
        }

        @Override
        public FixParts2item[] newArray(int size) {
            return new FixParts2item[size];
        }
    };
}
