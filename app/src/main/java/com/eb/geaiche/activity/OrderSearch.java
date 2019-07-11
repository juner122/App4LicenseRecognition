package com.eb.geaiche.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.OrderListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.MyTimePickerView;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.OrderInfo;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.geaiche.util.DateUtil.getFormatedDateTime;

public class OrderSearch extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.et_key)
    EditText et;

    OrderListAdapter ola;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    MyTimePickerView pvTimeStart, pvTimeEnd;
    Calendar startShowDate = Calendar.getInstance();
    Calendar endShowDate = Calendar.getInstance();

    @BindView(R.id.v_date1)
    TextView v_date1;
    @BindView(R.id.v_date2)
    TextView v_date2;

    int page = 1;

    String name;//关键字
    boolean isdate;

    @Override
    protected void init() {
        ola = new OrderListAdapter(R.layout.item_fragment2_main, null, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ola);
        ola.setEmptyView(R.layout.order_list_empty_view, recyclerView);


        ola.setOnItemClickListener((adapter, view, position) -> {
            if (((OrderInfoEntity) adapter.getData().get(position)).isSelected()) {
                ((OrderInfoEntity) adapter.getData().get(position)).setSelected(false);

            } else {
                for (OrderInfoEntity o : (List<OrderInfoEntity>) adapter.getData()) {
                    o.setSelected(false);
                }
                ((OrderInfoEntity) adapter.getData().get(position)).setSelected(true);
            }
            ola.notifyDataSetChanged();
        });

        ola.setOnItemChildClickListener((adapter, view, position) -> {

            switch (view.getId()) {
                case R.id.button_show_details://查看订单

                    toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, ((OrderInfoEntity) adapter.getData().get(position)).getId());

                    break;
                case R.id.button_action://动作按钮

                    orderDetail(((OrderInfoEntity) adapter.getData().get(position)).getId());

                    break;
            }
        });

        easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                page++;
                searchData();
            }

            @Override
            public void onRefreshing() {

                page = 1;
                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                searchData();

            }
        });
    }

    @Override
    protected void setUpView() {
        pvTimeStart = new MyTimePickerView(this);
        pvTimeEnd = new MyTimePickerView(this);

        startShowDate.set(startShowDate.get(Calendar.YEAR), startShowDate.get(Calendar.MONTH), 1);
        endShowDate.set(startShowDate.get(Calendar.YEAR), startShowDate.get(Calendar.MONTH), 31);

        v_date1.setText(getFormatedDateTime(startShowDate.getTime()));
        v_date2.setText(getFormatedDateTime(endShowDate.getTime()));


        pvTimeStart.init(startShowDate, (date, v) -> {
            ((TextView) v).setText(getFormatedDateTime(date));
            startShowDate.setTime(date);
            isdate = true;//设置时间后
            searchData();
        });

        pvTimeEnd.init(endShowDate, (date, v) -> {

            ((TextView) v).setText(getFormatedDateTime(date));
            endShowDate.setTime(date);
            isdate = true;//设置时间后
            searchData();
        });

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_search;
    }


    @OnClick({R.id.iv_search, R.id.back, R.id.v_date1, R.id.v_date2})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_search:

                searchData();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.v_date1:

                pvTimeStart.show(v);
                break;

            case R.id.v_date2:

                pvTimeEnd.show(v);

                break;
        }
    }

    private void searchData() {
//        if (TextUtils.isEmpty(et.getText())) {
//            easylayout.refreshComplete();
//            easylayout.loadMoreComplete();
//            ToastUtils.showToast("请输入搜索内容！");
//            return;
//        }
        name = et.getText().toString();
        Api().orderList(startShowDate.getTime(), endShowDate.getTime(), isdate, name, page).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(this, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {
                if (null == basePage.getList() || basePage.getList().size() == 0) {
                    ToastUtils.showToast("没有记录！");
                }


                if (page == 1) {
                    easylayout.refreshComplete();
                    ola.setNewData(basePage.getList());
                    if (basePage.getList().size() < Configure.limit_page)
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                } else {
                    easylayout.loadMoreComplete();
                    if (basePage.getList().size() == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }
                    ola.addData(basePage.getList());
                }
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
            }
        });
    }

    //查询订单
    private void orderDetail(int id) {

        Api().orderDetail(id).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo orderInfo) {
                int order_staus = orderInfo.getOrderInfo().getOrder_status();
                int pay_staus = orderInfo.getOrderInfo().getPay_status();

                if (order_staus == 0)//未服务
                    if (pay_staus == 2)
                        sendOrderInfo(MakeOrderSuccessActivity.class, orderInfo);
                    else
                        toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, orderInfo.getOrderInfo().getId());
                else if (order_staus == 1) {//服务中
                    if (pay_staus == 2)
                        toActivity(OrderDoneActivity.class, Configure.ORDERINFOID, orderInfo.getOrderInfo().getId());
                    else
                        sendOrderInfo(OrderPayActivity.class, orderInfo);
                } else

                    ToastUtils.showToast("订单已完成");

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("查找订单失败");
            }
        });

    }
}
