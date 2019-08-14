package com.juner.mvp.bean;

import java.util.List;

public class DisRecordList {

    String sumMoney;//历史总计
    String monthMoney;//月总计

    List<DisRecord> authList;

    public String getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(String sumMoney) {
        this.sumMoney = sumMoney;
    }

    public String getMonthMoney() {
        return monthMoney;
    }

    public void setMonthMoney(String monthMoney) {
        this.monthMoney = monthMoney;
    }

    public List<DisRecord> getAuthList() {
        return authList;
    }

    public void setAuthList(List<DisRecord> authList) {
        this.authList = authList;
    }
}
