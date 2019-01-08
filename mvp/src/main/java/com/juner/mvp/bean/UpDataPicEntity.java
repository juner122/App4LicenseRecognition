package com.juner.mvp.bean;

public class UpDataPicEntity {

    int id;
    String imageUrl;
    int type;
    int sort;

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
