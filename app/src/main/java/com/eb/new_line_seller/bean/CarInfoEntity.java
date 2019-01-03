package com.eb.new_line_seller.bean;

import java.util.List;

public class CarInfoEntity {

    String car_id;

    UserCarCondition userCarCondition;


    List<Pic> picList;


    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public UserCarCondition getUserCarCondition() {
        return userCarCondition;
    }

    public void setUserCarCondition(UserCarCondition userCarCondition) {
        this.userCarCondition = userCarCondition;
    }

    public List<Pic> getPicList() {
        return picList;
    }

    public void setPicList(List<Pic> picList) {
        this.picList = picList;
    }

    @Override
    public String toString() {
        return "CarInfoEntity{" +
                "car_id='" + car_id + '\'' +
                ", userCarCondition=" + userCarCondition +
                ", picList=" + picList +
                '}';
    }

    public class UserCarCondition {


        String carModel;
        String carNo;
        String userId;

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "UserCarCondition{" +
                    "carModel='" + carModel + '\'' +
                    ", carNo='" + carNo + '\'' +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }

    class Pic {

        String imageUrl;
        String type;


        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Pic{" +
                    "imageUrl='" + imageUrl + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

}
