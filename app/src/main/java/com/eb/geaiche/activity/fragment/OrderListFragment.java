package com.eb.geaiche.activity.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.stockControl.activity.StockOutActivity;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.eb.geaiche.view.ConfirmDialogOrderDelete;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.MakeOrderSuccessActivity;
import com.eb.geaiche.activity.OrderDoneActivity;
import com.eb.geaiche.activity.OrderInfoActivity;
import com.eb.geaiche.activity.OrderPayActivity;
import com.eb.geaiche.adapter.OrderListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfo;
import com.juner.mvp.bean.OrderInfoEntity;
import com.eb.geaiche.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.eb.geaiche.activity.OrderListActivity.view_intent;

public class OrderListFragment extends BaseFragment {

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    List<OrderInfoEntity> list = new ArrayList<>();
    OrderListAdapter ola;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    int position;

    public static OrderListFragment newInstance(int position) {
        OrderListFragment fragmentOne = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        //fragment保存参数，传入一个Bundle对象
        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //取出保存的值
            position = getArguments().getInt("position");
        }

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_orderlist;
    }

    @Override
    protected void setUpView() {


        initData();
        getData();

    }


    private void initData() {
        ola = new OrderListAdapter(R.layout.item_fragment2_main, list, getContext());


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ola);
        ola.setEmptyView(R.layout.order_list_empty_view, recyclerView);


        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                page++;
                loadMoreData();
            }

            @Override
            public void onRefreshing() {

                page = 1;
                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                getData();

            }
        });

        ola.setOnItemClickListener((adapter, view, position) -> {
            if (view_intent == 0) {//正常打开子项
                if (list.get(position).isSelected()) {
                    list.get(position).setSelected(false);

                } else {
                    for (OrderInfoEntity o : list) {
                        o.setSelected(false);
                    }
                    list.get(position).setSelected(true);
                }
                ola.notifyDataSetChanged();
            } else {//返回出库页面
                Intent intent = new Intent(getActivity(), StockOutActivity.class);
                intent.putExtra(Configure.ORDERINFOID, list.get(position).getId());
                intent.putExtra(Configure.ORDERINFOSN, list.get(position).getOrder_sn());
                getActivity().startActivity(intent);
            }

        });


        ola.setOnItemChildClickListener((adapter, view, position) -> {

            switch (view.getId()) {
                case R.id.button_show_details://查看订单

                    toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, list.get(position).getId());

                    break;
                case R.id.button_action://动作按钮
                    int os = list.get(position).getOrder_status();

                    if (os == 2) {
                        ToastUtils.showToast("当前订单不能取消！");
                        return;
                    }

                    final ConfirmDialogOrderDelete dialogCanlce = new ConfirmDialogOrderDelete(getActivity());
                    dialogCanlce.show();
                    dialogCanlce.setClicklistener(postscript -> {
                        dialogCanlce.dismiss();


                        deleteOrder(list.get(position).getId(), postscript);

                    });

                    break;
            }
        });


    }

    private void getData() {

        Log.e("订单列表++++", "getData()");

        Api().orderList(position, 1).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(mContext, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {


                easylayout.refreshComplete();
                list.clear();
                list = basePage.getList();
                ola.setNewData(list);

                if (list.size() < Configure.limit_page)
                    easylayout.setLoadMoreModel(LoadModel.NONE);
            }

            @Override
            protected void _onError(String message) {
                easylayout.refreshComplete();
            }
        });
    }

    int page = 1;//第一页

    private void loadMoreData() {
        Api().orderList(position, page).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(mContext, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {

                easylayout.loadMoreComplete();
                if (basePage.getList().size() == 0) {
                    ToastUtils.showToast("没有更多了！");

                    easylayout.setLoadMoreModel(LoadModel.NONE);
                    return;
                }

                list.addAll(basePage.getList());
                ola.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                easylayout.loadMoreComplete();
            }
        });
    }


    /**
     * 删除订单
     *
     * @param id         订单id
     * @param postscript 订单删除原因
     */

    private void deleteOrder(int id, String postscript) {

        Api().orderDelete(id, postscript).subscribe(new RxSubscribe<NullDataEntity>(getActivity(), true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                ToastUtils.showToast("取消成功！");
                getData();//刷新列表
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("取消失败！" + message);
            }
        });


    }

    @Override
    protected String setTAG() {
        return "OrderListFragment";
    }
}
