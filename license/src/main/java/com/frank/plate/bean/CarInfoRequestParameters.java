package com.frank.plate.bean;

import java.util.List;


//添加车况 传参方式为一个对象
public class CarInfoRequestParameters {


    private String userId;
    private String carNo;
    private int brandId;
    private String brand;
    private int nameId;
    private String name;
    private String postscript;
    private int id;

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
        return postscript;
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


}
