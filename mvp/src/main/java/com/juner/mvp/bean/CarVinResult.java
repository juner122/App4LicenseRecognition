package com.juner.mvp.bean;

public class CarVinResult {

    //品牌
    String brandName;
    //型号
    String familyName;

    //配置
    private String vehicleName;

    //排放标准
    private String remark;
    //级别
    private String drivenType;
    //车架号
    private String vin;
    //年份
    private String yearPattern;

    //指导价
    private String guiding_price;
    //排量
    private String displacement;

    //发动机号
    private String engineModel;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDrivenType() {
        return drivenType;
    }

    public void setDrivenType(String drivenType) {
        this.drivenType = drivenType;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getYearPattern() {
        return yearPattern;
    }

    public void setYearPattern(String yearPattern) {
        this.yearPattern = yearPattern;
    }

    public String getGuiding_price() {
        return guiding_price;
    }

    public void setGuiding_price(String guiding_price) {
        this.guiding_price = guiding_price;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getEngineModel() {
        return engineModel;
    }

    public void setEngineModel(String engineModel) {
        this.engineModel = engineModel;
    }
}
