package com.frank.plate.bean;

import java.util.List;

public class SaveUserAndCarEntity {


    String user_id;
    String car_id;

    List<CarEntity> carList;

    public List<CarEntity> getCarList() {
        return carList;
    }

    public void setCarList(List<CarEntity> carList) {
        this.carList = carList;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    @Override
    public String toString() {
        return "SaveUserAndCarEntity{" +
                "user_id='" + user_id + '\'' +
                ", car_id='" + car_id + '\'' +
                ", carList=" + carList +
                '}';
    }
}
