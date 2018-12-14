package com.frank.plate.adapter;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.frank.plate.bean.MealEntity;

public class MealLevel0 extends AbstractExpandableItem<MealEntity> {
    @Override
    public int getLevel() {
        return 0;
    }
}
