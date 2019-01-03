package com.eb.new_line_seller.bean;

import java.util.List;
import java.util.Map;

public class Meal {


    Map<String ,List<MealEntity>> list;


    public Map<String, List<MealEntity>> getList() {
        return list;
    }

    public void setList(Map<String, List<MealEntity>> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "list=" + list +
                '}';
    }
}
