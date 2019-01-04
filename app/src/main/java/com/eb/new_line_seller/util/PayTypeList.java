package com.eb.new_line_seller.util;


import com.eb.new_line_seller.bean.OffLinePayType;

import java.util.ArrayList;
import java.util.List;

public class PayTypeList {


    public static List<OffLinePayType> getList() {

        List<OffLinePayType> olpy = new ArrayList<>();

        olpy.add(new OffLinePayType("套卡核销", 32));
        olpy.add(new OffLinePayType("嗨卡", 31));
        olpy.add(new OffLinePayType("收钱吧", 25));
        olpy.add(new OffLinePayType("现金收款", 22));
        olpy.add(new OffLinePayType("掌贝收款", 21));
        olpy.add(new OffLinePayType("团购收款", 23));
        olpy.add(new OffLinePayType("旧商城", 24));
        olpy.add(new OffLinePayType("车行易", 26));
        olpy.add(new OffLinePayType("巨会养车", 27));
        olpy.add(new OffLinePayType("停洗欢", 28));
        olpy.add(new OffLinePayType("百汇通", 29));
        olpy.add(new OffLinePayType("A洗车", 30));


        return olpy;
    }


}
