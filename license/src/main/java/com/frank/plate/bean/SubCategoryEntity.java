package com.frank.plate.bean;


/**
 * {
 * "id": 1001020,
 * "name": "邓禄普",
 * "list_pic_url": "http://qiniu.xgxshop.com/xgx/20181123/120347159b68b3.jpg",
 * "app_list_pic_url": "http://qiniu.xgxshop.com/xgx/20181123/120347159b68b3.jpg"
 * }
 */
public class SubCategoryEntity {

    String id;
    String name;
    String app_list_pic_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApp_list_pic_url() {
        return app_list_pic_url;
    }

    public void setApp_list_pic_url(String app_list_pic_url) {
        this.app_list_pic_url = app_list_pic_url;
    }

    @Override
    public String toString() {
        return "SubCategoryEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", app_list_pic_url='" + app_list_pic_url + '\'' +
                '}';
    }
}
