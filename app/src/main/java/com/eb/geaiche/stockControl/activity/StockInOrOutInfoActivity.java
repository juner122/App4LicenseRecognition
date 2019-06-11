package com.eb.geaiche.stockControl.activity;


import android.widget.EditText;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.stockControl.bean.StockInOrOut;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;

import butterknife.BindView;

//入库出库详情
public class StockInOrOutInfoActivity extends BaseActivity {


    int stockType;//库操作类型


    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.title1)
    TextView title1;

    @BindView(R.id.title2)
    TextView title2;


    @Override
    protected void init() {
        stockType = getIntent().getIntExtra("stockType", 1);

        if (stockType == Configure.STOCK_IN) {
            tv_title.setText("入库详情");
            title1.setText("入库人：");
            title2.setText("入库时间：");
        } else {
            tv_title.setText("出库详情");
            title1.setText("出库人：");
            title2.setText("出库时间：");
        }

    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

        Api().stockInfo(getIntent().getIntExtra("id", 1)).subscribe(new RxSubscribe<StockInOrOut>(this, true) {
            @Override
            protected void _onNext(StockInOrOut stockInOrOut) {
                name.setText(stockInOrOut.getUserId());
                time.setText(DateUtil.getFormatedDateTime(stockInOrOut.getAddTime()));

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("详情查询失败！" + message);
            }
        });

    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stock_in_or_out_infoctivity;
    }
}
