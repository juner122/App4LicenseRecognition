package com.eb.geaiche.stockControl.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.adapter.StockInListAdapter;
import com.eb.geaiche.stockControl.bean.StockInOrOut;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.NullDataEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.geaiche.stockControl.activity.StockControlActivity.stockCartUtils;

public class StockInActivity extends BaseActivity {


    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.et_remarks)
    EditText et_remarks;

    @BindView(R.id.all_price)
    TextView all_price;

    @BindView(R.id.enter)
    TextView enter;

    StockInListAdapter adapter;

    @OnClick({R.id.enter})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.enter:
                stockInEnter();

                break;
        }

    }

    @Override
    protected void init() {

        tv_title.setText("采购入库");
        setRTitle("继续入库");
    }

    @Override
    protected void setUpView() {
        adapter = new StockInListAdapter(null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        adapter.setNewData(generateData(stockCartUtils.getDataFromLocal()));


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stock_in;
    }

    //确认入库
    private void stockInEnter() {
        Api().inOrOut(getStock()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                finish();
                toActivity(StockInOrOutInfoActivity.class, "stockType", Configure.STOCK_IN);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("入库失败！" + message);
            }
        });
    }

    private StockInOrOut getStock() {
        StockInOrOut stock = new StockInOrOut();

        stock.setType("2");////1出库2入库


        return stock;
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
                        gs.setGoodsTitle(list.get(i).getGoodsTitle());
                        lv0.addSubItem(gs);

                    }
                }
            }
            res.add(lv0);
        }
        return res;
    }
}
