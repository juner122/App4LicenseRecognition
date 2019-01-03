package com.eb.new_line_seller.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.BillListAdpter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.bean.BillEntity;
import com.eb.new_line_seller.bean.BillEntityItem;
import com.eb.new_line_seller.util.MathUtil;
import com.eb.new_line_seller.util.ToastUtils;
import com.eb.new_line_seller.view.MyTimePickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.chad.library.adapter.base.BaseQuickAdapter.ALPHAIN;
import static com.eb.new_line_seller.util.DateUtil.getFormatedDateTime;

public class BillListActivity extends BaseActivity {

    private static final String TAG = "BillListActivity";
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

    @BindView(R.id.ll1)
    View ll1;
    @BindView(R.id.ll2)
    View ll2;

    @BindView(R.id.v_date1)
    TextView v_date1;
    @BindView(R.id.v_date2)
    TextView v_date2;

    BillListAdpter adpter;
    int count;
    int isShowAll;//是否显示收入和支出

    Calendar startShowDate = Calendar.getInstance();
    Calendar endShowDate = Calendar.getInstance();

    @Override
    protected void init() {

        isShowAll = getIntent().getIntExtra("isShowAll", -1);

        if (isShowAll == 0) {
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
        } else {
            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);

        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adpter = new BillListAdpter(list);
        adpter.openLoadAnimation(ALPHAIN);
        adpter.setEmptyView(R.layout.order_list_empty_view, recyclerView);
        recyclerView.setAdapter(adpter);
        adpter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {



                toActivity(BillListItemInfoActivity.class, Configure.order_on, list.get(position).getOrderSn());
            }
        });

    }


    @Override
    protected void setUpView() {
        tv_title.setText("账单列表");


        pvTimeStart = new MyTimePickerView(this);
        pvTimeEnd = new MyTimePickerView(this);

        startShowDate.set(startShowDate.get(Calendar.YEAR), startShowDate.get(Calendar.MONTH), startShowDate.get(Calendar.DAY_OF_MONTH));
        endShowDate.set(startShowDate.get(Calendar.YEAR), endShowDate.get(Calendar.MONTH) + 1, endShowDate.get(Calendar.DAY_OF_MONTH));

        v_date1.setText(getFormatedDateTime(startShowDate.getTime()));
        v_date2.setText(getFormatedDateTime(endShowDate.getTime()));


        pvTimeStart.init(startShowDate, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                ((TextView) v).setText(getFormatedDateTime(date));

                startShowDate.setTime(date);
                setUpDateData();

            }
        });
        pvTimeEnd.init(endShowDate, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {

                ((TextView) v).setText(getFormatedDateTime(date));
                endShowDate.setTime(date);
                setUpDateData();
            }
        });


    }


    @Override
    protected void setUpData() {

        Api().getUserBillList(isShowAll).subscribe(new RxSubscribe<BillEntity>(this, true) {
            @Override
            protected void _onNext(BillEntity bean) {
                if (bean == null)
                    return;
                list = bean.getList();
                adpter.setNewData(list);
                tv_money1.setText(String.format("%s", MathUtil.twoDecimal(Double.parseDouble(bean.getDayIn()))));
                tv_money2.setText(String.format("%s", MathUtil.twoDecimal(Double.parseDouble(bean.getDayOut()))));
                tv_money3.setText(String.format("%s", MathUtil.twoDecimal(Double.parseDouble(bean.getMonthIn()))));
                tv_money4.setText(String.format("%s", MathUtil.twoDecimal(Double.parseDouble(bean.getMonthOut()))));
            }

            @Override
            protected void _onError(String message) {

                Log.d(TAG, message);

                ToastUtils.showToast("查找账单列表失败！ " + message);
            }
        });


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_bill_list;
    }


    MyTimePickerView pvTimeStart, pvTimeEnd;

    @OnClick({R.id.v_date1, R.id.v_date2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_date1:

                pvTimeStart.show(v);
                break;

            case R.id.v_date2:

                pvTimeEnd.show(v);

                break;
        }

    }

    private void setUpDateData() {
        Api().getUserBillList(startShowDate.getTime(), endShowDate.getTime(), isShowAll).subscribe(new RxSubscribe<BillEntity>(this, true) {
            @Override
            protected void _onNext(BillEntity bean) {
                list = bean.getList();
                adpter.setNewData(list);
            }

            @Override
            protected void _onError(String message) {

                Log.d(TAG, message);
                ToastUtils.showToast("查找账单失败:" + message);
            }
        });

    }

}
