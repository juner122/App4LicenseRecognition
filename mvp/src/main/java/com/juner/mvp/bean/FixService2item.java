package com.juner.mvp.bean;


import java.util.List;

//第二级
public class FixService2item {
    int id;
    int parentId;
    String serviceName;
    int status;

    List<FixServie> projectList;//第3级

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

    public List<FixServie> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<FixServie> projectList) {
        this.projectList = projectList;
    }
}
