package com.frank.plate.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AutoModel implements Parcelable {

    int id;
    int brandId;
    String typeName;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return typeName;
    }

    public void setName(String name) {
        this.typeName = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.brandId);
        dest.writeString(this.typeName);
    }

    public AutoModel() {
    }

    protected AutoModel(Parcel in) {
        this.id = in.readInt();
        this.brandId = in.readInt();
        this.typeName = in.readString();
    }

    public static final Parcelable.Creator<AutoModel> CREATOR = new Parcelable.Creator<AutoModel>() {
        @Override
        public AutoModel createFromParcel(Parcel source) {
            return new AutoModel(source);
        }

        @Override
        public AutoModel[] newArray(int size) {
            return new AutoModel[size];
        }
    };
}
