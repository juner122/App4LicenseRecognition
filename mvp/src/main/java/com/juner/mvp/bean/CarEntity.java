package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class CarEntity extends SelectedBean implements Parcelable {

    int id;
    String userId;
    String carNo;
    String carModel;
    String postscript;
    String brand;//品牌
    String name;//型号

    private Integer brandId;
    //
    private Integer nameId;

    private String saleName;//配置


    //排放标准
    private String effluentStandard;
    //级别
    private String carType;
    //车架号
    private String vin;
    //年份
    private String year;

    private String allJson;
    //指导价
    private BigDecimal guidingPrice;
    //里程数
    private Integer mileage;

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getNameId() {
        return nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId = nameId;
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

    public BigDecimal getGuidingPrice() {
        return guidingPrice;
    }

    public void setGuidingPrice(BigDecimal guidingPrice) {
        this.guidingPrice = guidingPrice;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
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
        dest.writeValue(this.brandId);
        dest.writeValue(this.nameId);
        dest.writeString(this.saleName);
        dest.writeString(this.effluentStandard);
        dest.writeString(this.carType);
        dest.writeString(this.vin);
        dest.writeString(this.year);
        dest.writeString(this.allJson);
        dest.writeSerializable(this.guidingPrice);
        dest.writeValue(this.mileage);
    }

    public CarEntity() {
    }

    protected CarEntity(Parcel in) {
        this.id = in.readInt();
        this.userId = in.readString();
        this.carNo = in.readString();
        this.carModel = in.readString();
        this.postscript = in.readString();
        this.brand = in.readString();
        this.name = in.readString();
        this.brandId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.nameId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.saleName = in.readString();
        this.effluentStandard = in.readString();
        this.carType = in.readString();
        this.vin = in.readString();
        this.year = in.readString();
        this.allJson = in.readString();
        this.guidingPrice = (BigDecimal) in.readSerializable();
        this.mileage = (Integer) in.readValue(Integer.class.getClassLoader());
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
