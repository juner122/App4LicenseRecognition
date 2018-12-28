package com.frank.plate.bean;


public class OffLinePayType {
    String type_string;
    int pay_type;

    public void setType_string(String type_string) {
        this.type_string = type_string;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public OffLinePayType(String type_string, int pay_type) {
        this.type_string = type_string;
        this.pay_type = pay_type;
    }

    public String getType_string() {
        return type_string;
    }

}
