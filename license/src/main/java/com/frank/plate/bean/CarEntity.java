package com.frank.plate.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CarEntity extends SelectedBean implements Parcelable {

    int id;
    String userId;
    String carNo;
    String carModel;
    String postscript;
    String brand;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {

        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    @Override
    public String toString() {
        return "CarEntity{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", carNo='" + carNo + '\'' +
                ", carModel='" + carModel + '\'' +
                ", postscript='" + postscript + '\'' +
                '}';
    }

    public CarEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.carNo);
        dest.writeString(this.carModel);
        dest.writeString(this.postscript);
        dest.writeString(this.brand);
        dest.writeString(this.name);
    }

    protected CarEntity(Parcel in) {
        this.id = in.readInt();
        this.userId = in.readString();
        this.carNo = in.readString();
        this.carModel = in.readString();
        this.postscript = in.readString();
        this.brand = in.readString();
        this.name = in.readString();
    }

    public static final Creator<CarEntity> CREATOR = new Creator<CarEntity>() {
        @Override
        public CarEntity createFromParcel(Parcel source) {
            return new CarEntity(source);
        }

        @Override
        public CarEntity[] newArray(int size) {
            return new CarEntity[size];
        }
    };
}
