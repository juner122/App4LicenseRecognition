package com.juner.mvp.bean;


import java.util.List;

//工时分类
public class FixServiceListEntity {

    int id;
    int parentId;
    String serviceName;
    int status;

    List<FixService2item> sonList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<FixService2item> getSonList() {
        return sonList;
    }

    public void setSonList(List<FixService2item> sonList) {
        this.sonList = sonList;
    }
}
