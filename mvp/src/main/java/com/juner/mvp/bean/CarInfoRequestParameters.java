package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
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
    private String lastTime;

    private String saleName;
    //排放标准
    private String effluentStandard;
    //级别
    private String carType;
    //车架号
    private String vin;
    //年份
    private String year;
    //车型
    private String allJson;
    //指导价
    private String guidingPrice;
    //里程数
    private String mileage;

    //排量
    private String outputVolume;

    //发动机号
    private String engineSn;

    public String getEngineSn() {
        return engineSn;
    }

    public void setEngineSn(String engineSn) {
        this.engineSn = engineSn;
    }

    public String getOutputVolume() {
        return outputVolume;
    }

    public void setOutputVolume(String outputVolume) {
        this.outputVolume = outputVolume;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getEffluentStandard() {
        return effluentStandard;
    }

    public void setEffluentStandard(String effluentStandard) {
        this.effluentStandard = effluentStandard;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAllJson() {
        return allJson;
    }

    public void setAllJson(String allJson) {
        this.allJson = allJson;
    }

    public String getGuidingPrice() {
        return guidingPrice;
    }

    public void setGuidingPrice(String guidingPrice) {
        this.guidingPrice = guidingPrice;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

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
        return null == postscript || postscript.equals("") ? "" : postscript;
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


    public CarInfoRequestParameters(int id, String carNo) {
        this.id = id;
        this.carNo = carNo;
        this.setSelected(true);
    }

    public CarInfoRequestParameters() {

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
        dest.writeString(this.lastTime);
        dest.writeString(this.saleName);
        dest.writeString(this.effluentStandard);
        dest.writeString(this.carType);
        dest.writeString(this.vin);
        dest.writeString(this.year);
        dest.writeString(this.allJson);
        dest.writeSerializable(this.guidingPrice);
        dest.writeString(this.mileage);
        dest.writeString(this.outputVolume);
        dest.writeString(this.engineSn);
        dest.writeList(this.imagesList);
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
        this.lastTime = in.readString();
        this.saleName = in.readString();
        this.effluentStandard = in.readString();
        this.carType = in.readString();
        this.vin = in.readString();
        this.year = in.readString();
        this.allJson = in.readString();
        this.guidingPrice = (String) in.readSerializable();
        this.mileage = in.readString();
        this.outputVolume = in.readString();
        this.engineSn = in.readString();
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
