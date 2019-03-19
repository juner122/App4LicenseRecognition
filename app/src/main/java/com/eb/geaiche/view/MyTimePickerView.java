package com.eb.geaiche.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;


import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.eb.geaiche.R;


import java.util.Calendar;

public class MyTimePickerView {


    private TimePickerView t;
    Context context;

    public MyTimePickerView(Context context) {

        this.context = context;

    }

    public void show(View v) {
        t.show(v);

    }

    public void init(Calendar nowDate, OnTimeSelectListener listener) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();


        int year = nowDate.get(Calendar.YEAR);
        int month = nowDate.get(Calendar.MONTH);
        int day = nowDate.get(Calendar.DAY_OF_MONTH);


        //正确设置方式 原因：注意事有说明
        nowDate.set(year, month, day);
        endDate.set(year + 10, month, day);
        startDate.set(year - 10, month, day);


        t = new TimePickerBuilder(context, listener).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setDate(nowDate)
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setTitleBgColor(context.getResources().getColor(R.color.appColor))//标题背景颜色 Night mode
                .build();

    }


}
