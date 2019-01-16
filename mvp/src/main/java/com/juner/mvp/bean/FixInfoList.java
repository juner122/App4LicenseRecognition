package com.juner.mvp.bean;


import java.util.List;

/**
 * 维修单
 */
public class FixInfoList {

    List<FixInfoEntity> quotationList;

    public List<FixInfoEntity> getQuotationList() {
        return quotationList;
    }

    public void setQuotationList(List<FixInfoEntity> quotationList) {
        this.quotationList = quotationList;
    }
}
