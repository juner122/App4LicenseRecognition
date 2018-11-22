package com.frank.plate.bean;

public class BillEntity {

    String monthIn;
    String dayOut;
    String monthOut;
    String dayIn;


    BasePage<BillEntityItem> page;

    public String getMonthIn() {
        return monthIn;
    }

    public void setMonthIn(String monthIn) {
        this.monthIn = monthIn;
    }

    public String getDayOut() {
        return dayOut;
    }

    public void setDayOut(String dayOut) {
        this.dayOut = dayOut;
    }

    public String getMonthOut() {
        return monthOut;
    }

    public void setMonthOut(String monthOut) {
        this.monthOut = monthOut;
    }

    public String getDayIn() {
        return dayIn;
    }

    public void setDayIn(String dayIn) {
        this.dayIn = dayIn;
    }

    public BasePage<BillEntityItem> getPage() {
        return page;
    }

    public void setPage(BasePage<BillEntityItem> page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "BillEntity{" +
                "monthIn='" + monthIn + '\'' +
                ", dayOut='" + dayOut + '\'' +
                ", monthOut='" + monthOut + '\'' +
                ", dayIn='" + dayIn + '\'' +
                ", page=" + page +
                '}';
    }
}
