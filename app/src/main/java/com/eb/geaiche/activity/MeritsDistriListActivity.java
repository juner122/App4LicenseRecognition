package com.eb.geaiche.activity;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.OrderList3Adapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.MemberEntity;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MeritsDistriListActivity extends BaseActivity {


    @BindView(R.id.num1)
    TextView num1;//订单总额
    @BindView(R.id.num2)
    TextView num2;//提成总额

    @BindView(R.id.rv)
    RecyclerView rv1;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout1;


    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"已分配", "未分配 "};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    OrderList3Adapter ola;

    Context context;

    String status;

    @Override
    protected void init() {
        tv_title.setText("订单绩效");
        context = this;
    }

    @Override
    protected void setUpView() {
        ola = new OrderList3Adapter(null, this);

        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(ola);

        ola.setEmptyView(R.layout.order_list_empty_view, rv1);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                if (position == 0) {
                    status = "1";
                } else {
                    status = "0";
                }
                orderStatusList();
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        ola.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.button_show_details://查看详情

                    toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, ((OrderInfoEntity) adapter.getData().get(position)).getId());

                    break;
                case R.id.button_action://分配绩效

                    int id = ola.getData().get(position).getId();
                    String deduction = ola.getData().get(position).getDeduction_status();

                    boolean isShow = null != deduction && deduction.equals("1");


                    Intent intent = new Intent(context, MeritsDistributionActivity.class);
                    intent.putExtra(Configure.ORDERINFOID, id);
                    intent.putExtra("view_type", isShow);
                    context.startActivity(intent);


                    break;
            }
        });

        ola.setOnItemClickListener((adapter, view, position) -> {
            if (((OrderInfoEntity) adapter.getData().get(position)).isSelected()) {
                ((OrderInfoEntity) adapter.getData().get(position)).setSelected(false);

            } else {
                for (OrderInfoEntity o : (List<OrderInfoEntity>) adapter.getData()) {
                    o.setSelected(false);
                }
                ((OrderInfoEntity) adapter.getData().get(position)).setSelected(true);
            }
            adapter.notifyDataSetChanged();
        });

        easylayout1.setLoadMoreModel(LoadModel.COMMON_MODEL);
        easylayout1.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                page++;
                orderStatusList();
            }

            @Override
            public void onRefreshing() {

                page = 1;

                orderStatusList();

            }
        });
    }

    int page = 1;

    @Override
    protected void setUpData() {
        status = "1";
        orderStatusList();

    }


    //是否分配过业绩，1是0否
    private void orderStatusList() {

        Api().orderStatusList(status, page).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(this, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> ib) {
                num1.setText(ib.getSaleMoney());
                if (null != ib.getDeductionSum())
                    num2.setText(ib.getDeductionSum());


                if (page == 1) {
                    refreshing(ib.getList());
                } else {
                    loadMoreData(ib.getList());
                }


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("查询失败！" + message);
            }
        });
    }

    private void refreshing(List<OrderInfoEntity> ml) {
        easylayout1.refreshComplete();

        ola.setNewData(ml);

        if (ml.size() < Configure.limit_page)//少于每页个数，不用加载更多
            easylayout1.setLoadMoreModel(LoadModel.NONE);
    }

    //加载更多
    private void loadMoreData(List<OrderInfoEntity> ml) {
        easylayout1.loadMoreComplete();
        if (ml.size() == 0) {
            ToastUtils.showToast("没有更多了！");
            easylayout1.setLoadMoreModel(LoadModel.NONE);
            return;
        }

        ola.addData(ml);
        ola.notifyDataSetChanged();
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_merits_distri_list;
    }
}
