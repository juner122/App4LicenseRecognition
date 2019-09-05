package com.eb.geaiche.stockControl.activity;

import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.adapter.StockControlListAdapter;
import com.eb.geaiche.stockControl.adapter.UnOrderPickStockListAdapter;
import com.eb.geaiche.util.SoftInputUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


//非订单商品出库 选择商品列表
public class UnOrderPickStockActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    UnOrderPickStockListAdapter adapter;

    @Override
    protected void init() {
        tv_title.setText("商品出库");
        setRTitle("出库单");
    }

    @OnClick({R.id.tv_title_r})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_title_r://前往出库单
                toActivity(StockOutActivity.class);

                break;
        }
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {
        adapter = new UnOrderPickStockListAdapter(null, this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_un_order_pick_stock;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData("");
    }

    /**
     * 新商品接口
     *
     * @param key 获取配件数据
     */

    private void getData(String key) {
        Api().xgxshopgoodsList(key, null, null, 1, Configure.Goods_TYPE_4).subscribe(new RxSubscribe<GoodsList>(this, true) {
            @Override
            protected void _onNext(GoodsList goods) {

                adapter.setNewData(generateData(goods.getList()));
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("配件列表获取失败！" + message);
            }
        });
    }

    private List<MultiItemEntity> generateData(List<Goods> list) {
        List<MultiItemEntity> res = new ArrayList<>();
        if (null == list || list.size() == 0) {
            return res;
        }

        for (int i = 0; i < list.size(); i++) {
            Goods lv0 = list.get(i);

            if (null != list.get(i).getXgxGoodsStandardPojoList() && list.get(i).getXgxGoodsStandardPojoList().size() > 0) {
                for (Goods.GoodsStandard gs : list.get(i).getXgxGoodsStandardPojoList()) {
                    if (null != gs) {
                        gs.setGoodsTitle(lv0.getGoodsTitle());
                        gs.setGoodsDetailsPojoList(lv0.getGoodsDetailsPojoList());
                        lv0.addSubItem(gs);
                    }
                }
            }
            res.add(lv0);
        }
        return res;
    }
}
