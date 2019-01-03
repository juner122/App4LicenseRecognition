package com.eb.new_line_seller.bean;

import java.util.List;

public class BillEntity {

    String betwInt;
    String dayOut;
    String betwOut;
    String dayIn;


    List<BillEntityItem> list;

    public String getMonthIn() {
        if (null == betwInt) return "0";
        return betwInt;
    }

    public void setMonthIn(String monthIn) {
        this.betwInt = monthIn;
    }

    public String getDayOut() {
        if (null == dayOut) return "0";
        return dayOut;
    }

    public void setDayOut(String dayOut) {
        this.dayOut = dayOut;
    }

    public String getMonthOut() {
        if (null == betwOut) return "0";
        return betwOut;
    }

    public void setMonthOut(String monthOut) {
        this.betwOut = monthOut;
    }

    public String getDayIn() {
        if (null == dayIn) return "0";
        return dayIn;
    }

    public void setDayIn(String dayIn) {
        this.dayIn = dayIn;
    }

    public List<BillEntityItem> getList() {
        return list;
    }

    public void setList(List<BillEntityItem> list) {
        this.list = list;
    }
}
