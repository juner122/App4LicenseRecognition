package com.frank.plate.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MyMultipleItem implements MultiItemEntity {
    public static final int FIRST_TYPE = 0;
    public static final int SECOND_TYPE = 1;

    private int itemType;
    private MealEntity data;

    public MyMultipleItem(int itemType, MealEntity data) {
        this.itemType = itemType;
        this.data = data;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public MealEntity getData() {
        return data;
    }
}
