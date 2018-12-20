package com.frank.plate.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class AutoBrand implements Comparable<AutoBrand>,Parcelable {

    int id;
    String name;
    String image;
    String type;

    public AutoBrand(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AutoBrand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull AutoBrand autoBrand) {
        if (autoBrand == null) {
            return -1;
        }
        return type.compareTo(autoBrand.getType());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeString(this.type);
    }

    public AutoBrand() {
    }

    protected AutoBrand(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<AutoBrand> CREATOR = new Parcelable.Creator<AutoBrand>() {
        @Override
        public AutoBrand createFromParcel(Parcel source) {
            return new AutoBrand(source);
        }

        @Override
        public AutoBrand[] newArray(int size) {
            return new AutoBrand[size];
        }
    };
}
