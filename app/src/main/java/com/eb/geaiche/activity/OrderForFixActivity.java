package com.eb.geaiche.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.FixInfoListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.FixInfoActivity;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixInfoList;

import butterknife.BindView;
import butterknife.OnClick;


//检修单搜索页面
public class OrderForFixActivity extends BaseActivity {


    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.et_key)
    EditText et;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;


    int page = 1;

    String name;//关键字

    FixInfoListAdapter ola;
    @Override
    protected void init() {

    }

    @Override
    protected void setUpView() {

        ola = new FixInfoListAdapter(R.layout.item_fragment2_main, null, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ola);

        ola.setEmptyView(R.layout.order_list_empty_view, recyclerView);
        ola.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                toActivity(FixInfoActivity.class, "id", ((FixInfoEntity) adapter.getData().get(position)).getId());
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
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_search;
    }


    @OnClick({R.id.iv_search, R.id.back})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_search:

                searchData();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void searchData() {
        if (TextUtils.isEmpty(et.getText())) {
            easylayout.refreshComplete();
            easylayout.loadMoreComplete();
            ToastUtils.showToast("请输入搜索内容！");
            return;
        }
        name = et.getText().toString();
        Api().quotationList(name, page).subscribe(new RxSubscribe<FixInfoList>(this, true) {
            @Override
            protected void _onNext(FixInfoList basePage) {

                if(null == basePage.getQuotationList()||basePage.getQuotationList().size()==0){
                    ToastUtils.showToast("没有记录！");
                }


                if (page == 1) {
                    easylayout.refreshComplete();
                    ola.setNewData(basePage.getQuotationList());
                    if (basePage.getQuotationList().size() < Configure.limit_page)
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                } else {
                    easylayout.loadMoreComplete();
                    if (basePage.getQuotationList().size() == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }
                    ola.addData(basePage.getQuotationList());
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

}
