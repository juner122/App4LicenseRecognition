package com.eb.geaiche.activity;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.CouponWriteRecordAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Coupon;

import java.util.List;

import butterknife.BindView;

public class CouponWriteRecordActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    CouponWriteRecordAdapter adapter;

    @Override
    protected void init() {

        tv_title.setText("核销记录");

    }

    @Override
    protected void setUpView() {

        adapter = new CouponWriteRecordAdapter(null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

    }

    @Override
    protected void setUpData() {
        Api().convertCouponLog().subscribe(new RxSubscribe<List<Coupon>>(this, true) {
            @Override
            protected void _onNext(List<Coupon> coupons) {

                if (null == coupons || coupons.size() == 0) {
                    ToastUtils.showToast("暂无记录！");
                    return;
                }

                adapter.setNewData(coupons);
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("查询记录失败！" + message);
            }
        });

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_coupon_write_record;
    }
}
