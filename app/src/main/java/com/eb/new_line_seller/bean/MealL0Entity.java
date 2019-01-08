package com.eb.new_line_seller.bean;


import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MealL0Entity extends AbstractExpandableItem<MealEntity> implements MultiItemEntity {
    private boolean isSelected;
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    String activityName;
    int activityId;
    String activitySn;

    public MealL0Entity(String activityName,  String activitySn) {
        this.activityName = activityName;
        this.activitySn = activitySn;
    }

    public String getActivitySn() {
        return activitySn;
    }

    public void setActivitySn(String activitySn) {
        this.activitySn = activitySn;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    @Override
    public String toString() {
        return "MealL0Entity{" +
                "activityName='" + activityName + '\'' +
                ", activityId=" + activityId +
                '}';
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
