package com.eb.geaiche.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.OrderNewsListAdapter;
import com.eb.geaiche.adapter.decoration.SimplePaddingDecoration;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.FixInfoActivity;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderNews;

import net.grandcentrix.tray.AppPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderNewsListActivity extends BaseActivity {
    OrderNewsListAdapter adapter;

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    int page = 1;//第一页

    @Override
    protected void init() {

        tv_title.setText("消息");
        showRView("全部已读");
    }

    @OnClick({R.id.tv_title_r})
    public void onClick(View v) {


        //标记全部已读
        Api().updateRead(0).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nulld) {


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    @Override
    protected void setUpView() {
        adapter = new OrderNewsListAdapter(null, this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setEmptyView(R.layout.order_list_empty_view_m, rv);
        rv.addItemDecoration(new SimplePaddingDecoration(this));//分割线

        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                pushlogList(1);
            }

            @Override
            public void onRefreshing() {

                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                pushlogList(0);

            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderNews orderNews = (OrderNews) adapter.getData().get(position);
                updateRead(orderNews);


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        pushlogList(0);
    }

    @Override
    protected void setUpData() {
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_news_list;
    }


    //获取列表
    private void pushlogList(final int type) {
        if (type == 0)
            page = 1;
        else
            page++;

        Api().pushlogList(page).subscribe(new RxSubscribe<List<OrderNews>>(this, true) {
            @Override
            protected void _onNext(List<OrderNews> orderNews) {

                if (type == 0) {
                    adapter.setNewData(orderNews);
                    easylayout.refreshComplete();

                } else
                    loadMoreData(orderNews);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    //加载更多
    private void loadMoreData(List<OrderNews> ml) {
        easylayout.loadMoreComplete();
        if (ml.size() == 0) {
            ToastUtils.showToast("没有更多了！");
            easylayout.setLoadMoreModel(LoadModel.NONE);
            return;
        }
        adapter.addData(ml);
        adapter.notifyDataSetChanged();

    }

    //标为已读
    private void updateRead(final OrderNews orderNews) {

        Api().updateRead(orderNews.getId()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nulld) {

                if (orderNews.getType() == 2) {//检修单
                    toActivity(FixInfoActivity.class, "id", orderNews.getValueId());
                } else {
                    toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, orderNews.getValueId());
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }
}
