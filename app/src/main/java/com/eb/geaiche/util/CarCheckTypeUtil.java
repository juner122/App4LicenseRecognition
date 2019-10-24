package com.eb.geaiche.util;

public class CarCheckTypeUtil {

    public static  String getType(int type) {
        switch (type) {

            case 1://发动机
                return "发动机";

            case 2://刹车系统
                return "刹车系统";

            case 3://底盘系统
                return "底盘系统";

            case 4://转向系统
                return "转向系统";

            case 5://供电系统
                return "供电系统";

            case 6://车身附件
                return "车身附件";

            case 7://灯光照明
                return "灯光照明";
        }

        return "未知";
    }

}
