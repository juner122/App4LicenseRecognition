package com.eb.geaiche.bean;

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
