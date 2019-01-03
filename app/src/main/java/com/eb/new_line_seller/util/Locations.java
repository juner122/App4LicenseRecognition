package com.eb.new_line_seller.util;

import com.eb.new_line_seller.bean.CarNoLocation;

import java.util.ArrayList;
import java.util.List;

public class Locations {

    static String[] strings = {"京", "沪", "津", "渝", "冀", "晋", "蒙", "辽", "吉", "黑", "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "粤", "桂", "琼", "川", "贵", "云", "藏", "陕", "甘", "青", "宁", "新"};

    public static List<CarNoLocation> getLocations() {

        List<CarNoLocation> locations = new ArrayList<>();

        for (String s : strings) {
            locations.add(new CarNoLocation(s));

        }


        return locations;


    }
}
