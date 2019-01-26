package com.juner.mvp.bean;

import android.support.v4.math.MathUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpDataPicEntity {

    int id;
    String imageUrl;
    int type;
    int sort;
    String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getCarateTime4Date() {

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(createTime)));

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "UpDataPicEntity{" +
                "imageUrl='" + imageUrl + '\'' +
                ", type='" + type + '\'' +
                ", sort='" + sort + '\'' +
                '}';
    }
}
