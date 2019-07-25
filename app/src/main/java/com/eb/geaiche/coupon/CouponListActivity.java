package com.eb.geaiche.coupon;


import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Coupon2;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CouponListActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    CouponAdapter couponAdapter;

    @OnClick({R.id.new_coupon, R.id.tv_title_r})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.new_coupon:
                toActivity(CouponNewActivity.class,"view_type",1);

                break;
            case R.id.tv_title_r:
                toActivity(CouponPostRecordActivity.class);
                break;


        }
    }

    @Override
    protected void init() {
        tv_title.setText("优惠劵管理");
        setRTitle("派发记录");
    }

    @Override
    protected void setUpView() {
        couponAdapter = new CouponAdapter(null, this);

        rv.setLayoutManager(new LinearLayoutManager(this));

        couponAdapter.setEmptyView(R.layout.order_list_empty_view_c, rv);

        couponAdapter.setOnItemClickListener((adapter, view, position) -> {

            //查看优惠劵信息

            toActivity(CouponNewActivity.class,"id",couponAdapter.getData().get(position).getId());


        });
        couponAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            //派发
            toCouponPostActivity(couponAdapter.getData().get(position));
        });


        rv.setAdapter(couponAdapter);

    }

    private void toCouponPostActivity(Coupon2 coupon) {

        Intent intent = new Intent(this, CouponPostActivity.class);
        intent.putExtra("view_type", 1);
        intent.putExtra("Coupon", coupon);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Api().shopCouponList().subscribe(new RxSubscribe<List<Coupon2>>(this, true) {
            @Override
            protected void _onNext(List<Coupon2> coupon2s) {
                couponAdapter.setNewData(coupon2s);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("获取失败！" + message);
            }
        });
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_coupon_list;
    }
}
