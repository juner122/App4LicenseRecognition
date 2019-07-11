package com.juner.mvp.bean;


import java.util.List;

//http://www.easyepc123.com/interface   Easy EPC 接口功能
public class CarVin2 {

    String code;
    String message;

    Result result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {


        List<CarVinResult> vehicleList;

        public List<CarVinResult> getVehicleList() {
            return vehicleList;
        }

        public void setVehicleList(List<CarVinResult> vehicleList) {
            this.vehicleList = vehicleList;
        }
    }

}
