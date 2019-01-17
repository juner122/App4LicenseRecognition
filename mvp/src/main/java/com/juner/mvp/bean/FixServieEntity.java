package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

//检修单工时服务
public class FixServieEntity {
    List<FixServie> projectList;

    public List<FixServie> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<FixServie> projectList) {
        this.projectList = projectList;
    }
}
