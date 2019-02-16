package com.eb.new_line_seller.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//录卡记录
public class RecordMeal  {

    Map<String, List<MealEntity>> list;

    public Map<String, List<MealEntity>> getList() {
        return list;
    }

    public void setList(Map<String, List<MealEntity>> list) {
        this.list = list;
    }
}
