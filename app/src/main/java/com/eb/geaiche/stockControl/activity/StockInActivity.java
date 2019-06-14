package com.eb.geaiche.stockControl.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.bean.StockInOrOut;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.NullDataEntity;

import butterknife.BindView;
import butterknife.OnClick;

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

        return stock;
    }

}
