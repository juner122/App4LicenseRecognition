package com.juner.mvp.bean;


import java.util.List;

/**
 * 维修单
 */
public class FixInfoList {

    List<FixInfo> quotationList;

    public List<FixInfo> getQuotationList() {
        return quotationList;
    }

    public void setQuotationList(List<FixInfo> quotationList) {
        this.quotationList = quotationList;
    }
}
