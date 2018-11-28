package com.frank.plate.bean;

public class ActivityEntity {


    BasePage<ActivityEntityItem> page;

    public BasePage<ActivityEntityItem> getPage() {
        return page;
    }

    public void setPage(BasePage<ActivityEntityItem> page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "ActivityEntity{" +
                "page=" + page +
                '}';
    }
}
