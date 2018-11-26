package com.frank.plate.bean;

import java.util.List;

public class Category {

    String id;
    String name;
    String keywords;
    String sort_order;

    List<SubCategoryEntity> subCategoryList;

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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public List<SubCategoryEntity> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<SubCategoryEntity> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    @Override
    public String toString() {
        return "CategoryList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", keywords='" + keywords + '\'' +
                ", sort_order='" + sort_order + '\'' +
                ", subCategoryList=" + subCategoryList +
                '}';
    }
}
