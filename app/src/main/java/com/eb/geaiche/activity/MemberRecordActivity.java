package com.eb.geaiche.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.FixInfoListAdapter;
import com.eb.geaiche.adapter.OrderList2Adapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.FixInfoActivity;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixInfoList;
import com.juner.mvp.bean.Member;
import com.juner.mvp.bean.MemberEntity;
import com.juner.mvp.bean.MemberOrder;
import com.juner.mvp.bean.OrderInfoEntity;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MemberRecordActivity extends BaseActivity {

    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.easylayout1)
    EasyRefreshLayout easylayout1;

    @BindView(R.id.rv2)
    RecyclerView rv2;

    @BindView(R.id.easylayout2)
    EasyRefreshLayout easylayout2;


    OrderList2Adapter adapter2;//   订单
    FixInfoListAdapter fixAdapter;//检修单


    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"订单历史", "检修单历史"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void init() {
        tv_title.setText("消费记录");

    }

    @Override
    protected void setUpView() {
        adapter2 = new OrderList2Adapter(null);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adapter2);


        fixAdapter = new FixInfoListAdapter(R.layout.item_fragment2_main, null, this.getBaseContext());
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setAdapter(fixAdapter);

        adapter2.setEmptyView(R.layout.order_list_empty_view, rv1);
        fixAdapter.setEmptyView(R.layout.fix_list_empty_view, rv2);
        adapter2.setOnItemClickListener((adapter, view, position) -> {
            OrderInfoEntity o = (OrderInfoEntity) adapter.getData().get(position);
            toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, o.getId());

        });

        fixAdapter.setOnItemClickListener((adapter, view, position) -> toActivity(FixInfoActivity.class, "id", ((FixInfoEntity) adapter.getData().get(position)).getId()));


        for (int i = 0; i < mTitles.length; i++) {

            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                if (position == 0) {
                    rv1.setVisibility(View.VISIBLE);
                    rv2.setVisibility(View.GONE);
                } else {
                    rv2.setVisibility(View.VISIBLE);
                    rv1.setVisibility(View.GONE);
                }

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        easylayout1.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                getList(1);
            }

            @Override
            public void onRefreshing() {

                easylayout1.setLoadMoreModel(LoadModel.COMMON_MODEL);
                getList(0);
            }
        });

        easylayout2.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                getList2(1);
            }

            @Override
            public void onRefreshing() {

                easylayout2.setLoadMoreModel(LoadModel.COMMON_MODEL);
                getList2(0);
            }
        });
    }


    @Override
    protected void setUpData() {

        getList(0);
        getList2(0);
    }


    int page_p = 1;
    int page_f = 1;

    //美容单
    private void getList(final int type) {
        if (type == 0)
            page_p = 1;
        else
            page_p++;

        Api().orderList(page_p, getIntent().getStringExtra(Configure.user_id)).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(this, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> infoEntityBasePage) {

                if (type == 0) {
                    easylayout1.refreshComplete();
                    if (infoEntityBasePage.getList().size() > 0)
                        adapter2.setNewData(infoEntityBasePage.getList());

                    if (infoEntityBasePage.getList().size() < Configure.limit_page)//少于每页个数，不用加载更多
                        easylayout1.setLoadMoreModel(LoadModel.NONE);
                } else {
                    easylayout1.loadMoreComplete();
                    if (infoEntityBasePage.getList().size() == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout1.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }
                    adapter2.addData(infoEntityBasePage.getList());
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    //检修单
    private void getList2(final int type) {
        if (type == 0)
            page_f = 1;
        else
            page_f++;

        Api().quotationList(page_f, getIntent().getStringExtra(Configure.user_id)).subscribe(new RxSubscribe<FixInfoList>(this, true) {
            @Override
            protected void _onNext(FixInfoList infoList) {

                if (type == 0) {
                    easylayout2.refreshComplete();
                    if (infoList.getQuotationList().size() > 0)
                        fixAdapter.setNewData(infoList.getQuotationList());

                    if (infoList.getQuotationList().size() < Configure.limit_page)//少于每页个数，不用加载更多
                        easylayout2.setLoadMoreModel(LoadModel.NONE);
                } else {
                    easylayout2.loadMoreComplete();
                    if (infoList.getQuotationList().size() == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout2.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }

                    fixAdapter.addData(infoList.getQuotationList());
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_member_record;
    }
}
