package com.eb.new_line_seller.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


//添加车况 传参方式为一个对象
public class CarInfoRequestParameters extends SelectedBean implements Parcelable {

    private int id;
    private String userId;
    private String carNo;
    private int brandId;
    private String brand;
    private int nameId;
    private String name;
    private String postscript;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    List<UpDataPicEntity> imagesList;

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

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public String getName() {
        return null != name ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostscript() {
        return null == postscript || postscript.equals("") ? "暂无备注" : postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public List<UpDataPicEntity> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<UpDataPicEntity> imagesList) {
        this.imagesList = imagesList;
    }


    @Override
    public String toString() {
        return "CarInfoRequestParameters{" +
                "userId='" + userId + '\'' +
                ", carNo='" + carNo + '\'' +
                ", brandId='" + brandId + '\'' +
                ", brand='" + brand + '\'' +
                ", nameId='" + nameId + '\'' +
                ", name='" + name + '\'' +
                ", postscript='" + postscript + '\'' +
                ", id='" + id + '\'' +
                ", imagesList=" + imagesList +
                '}';
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
        dest.writeInt(this.brandId);
        dest.writeString(this.brand);
        dest.writeInt(this.nameId);
        dest.writeString(this.name);
        dest.writeString(this.postscript);
        dest.writeList(this.imagesList);
    }

    public CarInfoRequestParameters() {
    }

    protected CarInfoRequestParameters(Parcel in) {
        this.id = in.readInt();
        this.userId = in.readString();
        this.carNo = in.readString();
        this.brandId = in.readInt();
        this.brand = in.readString();
        this.nameId = in.readInt();
        this.name = in.readString();
        this.postscript = in.readString();
        this.imagesList = new ArrayList<UpDataPicEntity>();
        in.readList(this.imagesList, UpDataPicEntity.class.getClassLoader());
    }

    public static final Creator<CarInfoRequestParameters> CREATOR = new Creator<CarInfoRequestParameters>() {
        @Override
        public CarInfoRequestParameters createFromParcel(Parcel source) {
            return new CarInfoRequestParameters(source);
        }

        @Override
        public CarInfoRequestParameters[] newArray(int size) {
            return new CarInfoRequestParameters[size];
        }
    };
}
