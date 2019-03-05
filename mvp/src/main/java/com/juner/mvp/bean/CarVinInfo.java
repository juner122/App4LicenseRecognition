package com.juner.mvp.bean;

import java.math.BigDecimal;

public class CarVinInfo {

    //品牌
    private String brand_name;
    //型号
    private String model_name;

    //配置
    private String sale_name;

    //排放标准
    private String effluent_standard;
    //级别
    private String car_type;
    //车架号
    private String vin;
    //年份
    private String year;

    //指导价
    private BigDecimal guiding_price;
    //排量
    private String output_volume;


    public String getOutput_volume() {
        return output_volume;
    }

    public void setOutput_volume(String output_volume) {
        this.output_volume = output_volume;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getSale_name() {
        return sale_name;
    }

    public void setSale_name(String sale_name) {
        this.sale_name = sale_name;
    }

    public String getEffluent_standard() {
        return effluent_standard;
    }

    public void setEffluent_standard(String effluent_standard) {
        this.effluent_standard = effluent_standard;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
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

    public BigDecimal getGuiding_price() {
        return guiding_price;
    }

    public void setGuiding_price(BigDecimal guiding_price) {
        this.guiding_price = guiding_price;
    }

    @Override
    public String toString() {
        return "CarVinInfo{" +
                "brand_name='" + brand_name + '\'' +
                ", model_name='" + model_name + '\'' +
                ", sale_name='" + sale_name + '\'' +
                ", effluent_standard='" + effluent_standard + '\'' +
                ", car_type='" + car_type + '\'' +
                ", vin='" + vin + '\'' +
                ", year='" + year + '\'' +
                ", guiding_price=" + guiding_price +
                '}';
    }
}
