package com.juner.mvp.bean;

import java.util.List;

public class SaveUserAndCarEntity {


    int user_id;
    String user_name;
    String car_id;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    List<CarInfoRequestParameters> carList;

    public List<CarInfoRequestParameters> getCarList() {
        return carList;
    }

    public void setCarList(List<CarInfoRequestParameters> carList) {
        this.carList = carList;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
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
