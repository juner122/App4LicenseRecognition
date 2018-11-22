package com.frank.plate.activity;


import android.app.DatePickerDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.R;
import com.frank.plate.adapter.BillListAdpter;
import com.frank.plate.api.MySubscriber;
import com.frank.plate.api.SubscribeOnNextListener;
import com.frank.plate.bean.BillEntity;
import com.frank.plate.bean.BillEntityItem;
import com.frank.plate.util.MathUtil;
import com.frank.plate.view.DatePickerDialogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.chad.library.adapter.base.BaseQuickAdapter.ALPHAIN;

public class BillListActivity extends BaseActivity {

    List<BillEntityItem> list = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_money1)
    TextView tv_money1;
    @BindView(R.id.tv_money2)
    TextView tv_money2;
    @BindView(R.id.tv_money3)
    TextView tv_money3;
    @BindView(R.id.tv_money4)
    TextView tv_money4;

    @BindView(R.id.v_date1)
    TextView v_date1;
    @BindView(R.id.v_date2)
    TextView v_date2;

    BillListAdpter adpter;


    int count;
    MySubscriber subscriber;
    DatePickerDialogUtil dialogUti1, dialogUti2;
    int y1, y2, m1, m2, d1, d2;


    @Override
    protected void init() {

        initDate();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adpter = new BillListAdpter(list);
        adpter.openLoadAnimation(ALPHAIN);
        recyclerView.setAdapter(adpter);
        adpter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toActivity(BillListItemInfoActivity.class);
            }
        });


        subscriber = new MySubscriber<>(this, new SubscribeOnNextListener<BillEntity>() {
            @Override
            public void onNext(BillEntity bean) {
                if (bean == null)
                    return;
                list = bean.getPage().getList();
                adpter.addData(list);
                tv_money1.setText(String.format("￥%s", MathUtil.twoDecimal(Double.parseDouble(bean.getDayIn()))));
                tv_money2.setText(String.format("￥%s", MathUtil.twoDecimal(Double.parseDouble(bean.getDayOut()))));
                tv_money3.setText(String.format("￥%s", MathUtil.twoDecimal(Double.parseDouble(bean.getMonthIn()))));
                tv_money4.setText(String.format("￥%s", MathUtil.twoDecimal(Double.parseDouble(bean.getMonthOut()))));
            }
        });


    }


    @Override
    protected void setUpView() {
        tv_title.setText("账单列表");


    }

    @Override
    protected void setUpData() {

        Api().getUserBillList(subscriber, count, 10, "", "");


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_bill_list;
    }

    //当DatePickerDialog关闭时，更新日期显示
    private void updateDate(TextView textView, int year, int month, int dayOfMonth) {
        //在TextView上显示日期
        textView.setText(String.valueOf(year + "-" + (month + 1) + "-" + dayOfMonth));
    }


    private void initDate() {
        Calendar ca = Calendar.getInstance();
        y1 = ca.get(Calendar.YEAR);
        m1 = ca.get(Calendar.MONTH);
        d1 = ca.get(Calendar.DAY_OF_MONTH);

        if (m1 + 1 == 13) {
            y2 = y1 + 1;
            m2 = 1;
        } else {
            y2 = ca.get(Calendar.YEAR);
            m2 = ca.get(Calendar.MONTH) + 1;
        }
        d2 = ca.get(Calendar.DAY_OF_MONTH);

        updateDate(v_date1, y1, m1, d1);
        updateDate(v_date2, y2, m2, d1);

        dialogUti1 = new DatePickerDialogUtil(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y1 = year;
                m1 = month;
                d1 = dayOfMonth;
                updateDate(v_date1, year, month, dayOfMonth);
            }
        });
        dialogUti2 = new DatePickerDialogUtil(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y2 = year;
                m2 = month;
                d2 = dayOfMonth;
                updateDate(v_date2, year, month, dayOfMonth);
            }
        });
    }

    @OnClick({R.id.iv1, R.id.iv2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv1:

                dialogUti1.show(y1, m1, d1);

                break;

            case R.id.iv2:

                dialogUti2.show(y2, m2, d2);

                break;
        }
    }


}
