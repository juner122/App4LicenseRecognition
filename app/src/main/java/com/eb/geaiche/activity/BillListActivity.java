package com.eb.geaiche.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.BillListAdpter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.BillEntity;
import com.juner.mvp.bean.BillEntityItem;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.MyTimePickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.chad.library.adapter.base.BaseQuickAdapter.ALPHAIN;
import static com.eb.geaiche.util.DateUtil.getFormatedDateTime;

public class BillListActivity extends BaseActivity {

    private static final String TAG = "BillListActivity";
    List<BillEntityItem> list = new ArrayList<>();
    @BindView(R.id.rv)
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
    int isShowAll;//是否显示收入和支出

    Calendar startShowDate = Calendar.getInstance();
    Calendar endShowDate = Calendar.getInstance();


    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    int page = 1;//第一页
    MyTimePickerView pvTimeStart, pvTimeEnd;
    boolean isdate;

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


        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                getList(1);
            }

            @Override
            public void onRefreshing() {

                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                getList(0);

            }
        });
        getList(0);
    }

    private void getList(final int type) {
        if (type == 0)
            page = 1;
        else
            page++;
        Api().getUserBillList(startShowDate.getTime(), endShowDate.getTime(), isdate, isShowAll, page).subscribe(new RxSubscribe<BillEntity>(this, true) {
            @Override
            protected void _onNext(BillEntity bean) {

                tv_money1.setText(String.format("%s", MathUtil.twoDecimal(Double.parseDouble(bean.getDayIn()))));
                tv_money2.setText(String.format("%s", MathUtil.twoDecimal(Double.parseDouble(bean.getDayOut()))));
                tv_money3.setText(String.format("%s", MathUtil.twoDecimal(Double.parseDouble(bean.getMonthIn()))));
                tv_money4.setText(String.format("%s", MathUtil.twoDecimal(Double.parseDouble(bean.getMonthOut()))));

                if (type == 0)
                    refreshing(bean.getList());
                else
                    loadMoreData(bean.getList());

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("查找账单列表失败！ " + message);
            }
        });
    }

    @Override
    protected void setUpView() {
        tv_title.setText("账单列表");


        pvTimeStart = new MyTimePickerView(this);
        pvTimeEnd = new MyTimePickerView(this);

        startShowDate.set(startShowDate.get(Calendar.YEAR), startShowDate.get(Calendar.MONTH), 1);
        endShowDate.set(startShowDate.get(Calendar.YEAR), startShowDate.get(Calendar.MONTH) , 31);

        v_date1.setText(getFormatedDateTime(startShowDate.getTime()));
        v_date2.setText(getFormatedDateTime(endShowDate.getTime()));


        pvTimeStart.init(startShowDate, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                ((TextView) v).setText(getFormatedDateTime(date));
                startShowDate.setTime(date);
                isdate = true;//设置时间后
                getList(0);
            }
        });

        pvTimeEnd.init(endShowDate, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {

                ((TextView) v).setText(getFormatedDateTime(date));
                endShowDate.setTime(date);
                isdate = true;//设置时间后
                getList(0);
            }
        });


    }


    private void refreshing(List<BillEntityItem> ml) {
        easylayout.refreshComplete();
        list.clear();
        list = ml;
        adpter.setNewData(list);

        if (list.size() < Configure.limit_page)//少于每页个数，不用加载更多
            easylayout.setLoadMoreModel(LoadModel.NONE);
    }


    //加载更多
    private void loadMoreData(List<BillEntityItem> ml) {
        easylayout.loadMoreComplete();
        if (ml.size() == 0) {
            ToastUtils.showToast("没有更多了！");
            easylayout.setLoadMoreModel(LoadModel.NONE);
            return;
        }
        list.addAll(ml);
        adpter.setNewData(list);

    }


    @Override
    protected void setUpData() {


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_bill_list;
    }


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

}
