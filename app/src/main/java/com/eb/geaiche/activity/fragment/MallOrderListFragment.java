package com.eb.geaiche.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.MallMakeOrderInfoActivity;
import com.eb.geaiche.adapter.FixInfoListAdapter;
import com.eb.geaiche.adapter.MallOrderListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.FixInfoActivity;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixInfoList;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.XgxPurchaseOrderPojo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MallOrderListFragment extends BaseFragment {

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    MallOrderListAdapter adapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    int status;
    int page = 1;//第一页

    public static MallOrderListFragment newInstance(int position) {
        MallOrderListFragment fragmentOne = new MallOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", position);
        //fragment保存参数，传入一个Bundle对象
        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //取出保存的值
            status = getArguments().getInt("status");
        }

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        getData();
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_orderlist;
    }

    @Override
    protected void setUpView() {
        initData();
    }

    private void initData() {
        adapter = new MallOrderListAdapter(null, getContext());


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.order_list_empty_view, recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                toActivity(MallMakeOrderInfoActivity.class, "id", ((FixInfoEntity) adapter.getData().get(position)).getId());
            }
        });
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                page++;
                getData();
            }

            @Override
            public void onRefreshing() {

                page = 1;
                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                getData();

            }
        });


        //子项按钮
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter a, View view, int position) {

                if (adapter.getData().get(position).getPayStatus().equals("1")) {//在线支付
                    prepay(adapter.getData().get(position).getId());
                } else if (adapter.getData().get(position).getShipStatus().equals("2")) {//确认收货


                }


            }
        });

    }


    //获取
    private void getData() {
        Api().purchaseorderList(page, status).subscribe(new RxSubscribe<BasePage<XgxPurchaseOrderPojo>>(mContext, true) {
            @Override
            protected void _onNext(BasePage<XgxPurchaseOrderPojo> basePage) {
                easylayout.refreshComplete();

                if (page == 1) {//不等于1 显示更多
                    easylayout.refreshComplete();
                    adapter.setNewData(basePage.getList());
                    if (basePage.getList().size() < Configure.limit_page)
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                } else {
                    easylayout.loadMoreComplete();
                    if (basePage.getList().size() == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }
                    adapter.addData(basePage.getList());
                }

            }

            @Override
            protected void _onError(String message) {
                easylayout.refreshComplete();
            }
        });
    }


    //prepay
    private void prepay(int orderId) {
        Api().prepay(orderId).subscribe(new RxSubscribe<NullDataEntity>(mContext, true) {
            @Override
            protected void _onNext(NullDataEntity n) {
                //支付成功
                ToastUtils.showToast("支付成功");
            }

            @Override
            protected void _onError(String message) {
                easylayout.refreshComplete();
            }
        });

    }


    @Override
    protected String setTAG() {
        return "OrderListFragment";
    }
}
