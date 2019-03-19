package com.eb.geaiche.util;


import com.juner.mvp.bean.OffLinePayType;

import java.util.ArrayList;
import java.util.List;

public class PayTypeList {


    public static List<OffLinePayType> getList() {

        List<OffLinePayType> olpy = new ArrayList<>();

        olpy.add(new OffLinePayType("套卡核销", 21));

        olpy.add(new OffLinePayType("收钱吧", 22));
        olpy.add(new OffLinePayType("挂账", 23));

        olpy.add(new OffLinePayType("现金", 24));
        olpy.add(new OffLinePayType("嗨卡", 25));

        olpy.add(new OffLinePayType("团购", 26));
        olpy.add(new OffLinePayType("旧商城", 27));
        olpy.add(new OffLinePayType("掌贝会员", 28));


        olpy.add(new OffLinePayType("车行易", 29));
        olpy.add(new OffLinePayType("巨会养车", 30));
        olpy.add(new OffLinePayType("停洗欢", 31));
        olpy.add(new OffLinePayType("百汇通", 32));
        olpy.add(new OffLinePayType("A洗车", 33));


        return olpy;
    }


}
