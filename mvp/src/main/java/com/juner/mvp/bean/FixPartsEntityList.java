package com.juner.mvp.bean;



import java.util.List;

//检修单配件
public class FixPartsEntityList {

    List<FixPartsSeek> projectList;

    public List<FixPartsSeek> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<FixPartsSeek> projectList) {
        this.projectList = projectList;
    }


}
