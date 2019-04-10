package com.eb.geaiche.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.RecordMealListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.bean.MealEntity;
import com.eb.geaiche.bean.MealEntity2;
import com.eb.geaiche.bean.MealL0Entity;
import com.eb.geaiche.bean.RecordMeal;
import com.eb.geaiche.util.ToastUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

//录卡记录
public class CustomRecordsActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.et_key)
    EditText et;

    RecordMealListAdapter adapter;

    List<MultiItemEntity> list;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    int page = 1;//第一页

    @OnClick({R.id.iv_search, R.id.back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                if (TextUtils.isEmpty(et.getText())) {
                    ToastUtils.showToast("搜索内容不能为空！");
                    return;
                }
                queryConnectAct(et.getText().toString());
                break;

            case R.id.back:
                finish();
                break;
        }

    }

    @Override
    protected void init() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordMealListAdapter(null);
        rv.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MealL0Entity m = (MealL0Entity) adapter.getData().get(position);
                if (m.isExpanded()) {//是否打开
                    adapter.collapse(position);

                } else {
                    int size = adapter.getData().size();
                    int e = 0;
                    for (int i = 0; i < size; i++) {
                        if (adapter.getData().get(i) instanceof MealL0Entity && ((MealL0Entity) adapter.getData().get(i)).isExpanded()) {
                            if (position > i) //点击比当前展开项后面的项才查找
                                e = ((MealL0Entity) adapter.getData().get(i)).getSubItems().size();//先查找出展开项的子项数量
                            break;
                        }
                    }
                    //收起所有子项
                    for (int i = 0; i < size; i++) {
                        adapter.collapse(i);
                    }
                    adapter.expand(position - e);//
                }
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
                queryConnectAct("");

            }
        });


    }

    @Override
    protected void setUpView() {


    }

    @Override
    protected void setUpData() {

        queryConnectAct("");
    }


    private void queryConnectAct(String name) {
        Api().queryConnectAct(name).subscribe(new RxSubscribe<RecordMeal>(this, true) {
            @Override
            protected void _onNext(RecordMeal recordMeal) {
                easylayout.refreshComplete();
                list = generateData(recordMeal.getList());
                adapter.setNewData(list);

                if (recordMeal.getList().size() == 0)
                    ToastUtils.showToast("数据为空！");

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
            }
        });
    }

    private void loadMoreData() {

        Api().queryConnectAct(page).subscribe(new RxSubscribe<RecordMeal>(this, true) {
            @Override
            protected void _onNext(RecordMeal recordMeal) {

                easylayout.loadMoreComplete();
                if (recordMeal.getList().size() == 0) {
                    ToastUtils.showToast("没有更多了！");

                    easylayout.setLoadMoreModel(LoadModel.NONE);
                    return;
                }

                list.addAll(generateData(recordMeal.getList()));
                adapter.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                easylayout.loadMoreComplete();
                easylayout.setLoadMoreModel(LoadModel.NONE);
                ToastUtils.showToast(message);
            }
        });


    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_search;
    }

    private List<MultiItemEntity> generateData(Map<String, List<MealEntity>> map) {

        List<MultiItemEntity> res = new ArrayList<>();

        for (List<MealEntity> list : map.values()) {

            MealL0Entity lv0 = new MealL0Entity(list.get(0).getActivityName(), list.get(0).getActivitySn());
            lv0.setMobile(list.get(0).getMobile());
            lv0.setEndTime(Long.parseLong(list.get(0).getEndTime()));
            lv0.setCarNo(list.get(0).getCarNo());
            for (MealEntity entity : list) {
                lv0.addSubItem(entity);
            }
            res.add(lv0);

        }
        return res;
    }

}
