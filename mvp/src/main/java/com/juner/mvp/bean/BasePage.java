package com.juner.mvp.bean;

import java.util.List;

public class BasePage<T> {

    int totalCount;
    int pageSize;
    int totalPage;
    int currPage;
    int dayTotal;
    int monthTotal;


    List<T> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getMonthTotal() {
        return monthTotal;
    }

    public void setMonthTotal(int monthTotal) {
        this.monthTotal = monthTotal;
    }

    public int getDayTotal() {

        return dayTotal;
    }

    public void setDayTotal(int dayTotal) {
        this.dayTotal = dayTotal;
    }

    @Override
    public String toString() {
        return "BasePage{" +
                "totalCount=" + totalCount +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", currPage=" + currPage +
                ", list=" + list +
                '}';
    }
}
