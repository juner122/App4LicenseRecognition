package com.eb.geaiche.stockControl.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.adapter.StockInOrOutRecordAdapter;
import com.eb.geaiche.stockControl.bean.StockInOrOut;
import com.eb.geaiche.util.SoftInputUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.GoodsList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StockInOrOutRecordActivity extends BaseActivity {


    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.easylayout1)
    EasyRefreshLayout easylayout;

    @BindView(R.id.et_key)
    EditText et_key;

    int type = 1;
    int view_type = 0;//页面跳转
    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"出库记录", "入库记录"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    StockInOrOutRecordAdapter adapter;


    @OnClick({R.id.iv_search})
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.iv_search:

                if (!TextUtils.isEmpty(et_key.getText())) {
                    SoftInputUtil.hideSoftInput(this, v);
                    getList(et_key.getText().toString());
                } else
                    ToastUtils.showToast("请输入搜索关键字！");

                break;
        }
    }

    int page = 1;//第一页


    @Override
    protected void init() {
        tv_title.setText("出入库记录");
        type = getIntent().getIntExtra("view_type_v", 1);


        view_type = getIntent().getIntExtra("view_type", 0);
    }

    @Override
    protected void setUpView() {
        adapter = new StockInOrOutRecordAdapter(null, this);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {


            if (view_type == 0) {//查看记录详情

                Intent intent = new Intent(this, StockInOrOutInfoActivity.class);
                intent.putExtra("id", adapter.getData().get(position).getId());
                intent.putExtra("stockType", adapter.getData().get(position).getType());
                startActivity(intent);
            } else if (adapter.getData().get(position).getType().equals("2")) {//入库单出库
                Intent intent = new Intent(this, StockOutActivity.class);
                intent.putExtra("inStockId", adapter.getData().get(position).getId());
                intent.putExtra("inStockSn", adapter.getData().get(position).getOrderSn());

                startActivity(intent);
            }

        });


        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }


        commonTabLayout.setTabData(mTabEntities);

        commonTabLayout.setCurrentTab(type == 1 ? 0 : 1);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                page = 1;
                type = position == 0 ? 1 : 2;
                getList(null);//1出库2入库
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

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
                getList(null);

            }
        });
    }

    @Override
    protected void setUpData() {

        getList(null);

    }

    private void getList(String key) {
        Api().stockInOrOutRecordList(key, type, page).subscribe(new RxSubscribe<List<StockInOrOut>>(this, true) {
            @Override
            protected void _onNext(List<StockInOrOut> stockInOrOuts) {
                easylayout.refreshComplete();
                adapter.setNewData(stockInOrOuts);

                if (stockInOrOuts.size() < Configure.limit_page)
                    easylayout.setLoadMoreModel(LoadModel.NONE);
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("记录获取失败！" + message);
            }
        });
    }

    private void loadMoreData() {
        Api().stockInOrOutRecordList(et_key.getText().toString(), type, page).subscribe(new RxSubscribe<List<StockInOrOut>>(this, true) {
            @Override
            protected void _onNext(List<StockInOrOut> stockInOrOuts) {

                easylayout.loadMoreComplete();
                if (stockInOrOuts.size() == 0) {
                    ToastUtils.showToast("没有更多了！");
                    easylayout.setLoadMoreModel(LoadModel.NONE);
                    return;
                }


                adapter.addData(stockInOrOuts);
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("记录获取失败！" + message);
            }
        });
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_member_record2;
    }
}
