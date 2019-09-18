package com.juner.mvp.bean;


import java.util.List;

//https://onedb.tecalliance.cn/api/vehicles/chinaid  返回对象
public class CarVinInfo2 {
    public List<Vehicles> getVehicles() {
        return vehicles;
    }

    List<Vehicles> vehicles;

    public class Vehicles {


        String brand;//奥迪
        String model;//A8
        String vehicleType;//轿车
        String capacityLitre;//排量
        String engineCode;//发动机号
        String salesName;//配置
        String modelYear;//生产年份

        public String getBrand() {
            return brand;
        }

        public String getModel() {
            return model;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public String getCapacityLitre() {
            return capacityLitre;
        }

        public String getEngineCode() {
            return engineCode;
        }

        public String getSalesName() {
            return salesName;
        }

        public String getModelYear() {
            return modelYear;
        }
    }


}
