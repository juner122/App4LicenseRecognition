package com.eb.geaiche.coupon;


import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;

import com.juner.mvp.bean.Coupon2;
import com.juner.mvp.bean.CouponRecode;

import net.grandcentrix.tray.AppPreferences;

import java.util.List;

import butterknife.BindView;

import static com.juner.mvp.Configure.shop_id;

public class CouponPostRecordActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;
    PostRecordAdapter adapter;


    @Override
    protected void init() {
        tv_title.setText("派发记录");
    }

    @Override
    protected void setUpView() {
        adapter = new PostRecordAdapter(null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {

            CouponRecode cr = adapter.getData().get(position);


            toActivity(CouponPostActivity.class, cr, "CouponRecode");


        });

    }



    @Override
    protected void setUpData() {

        Api().couponPostRecode().subscribe(new RxSubscribe<List<CouponRecode>>(this, true) {
            @Override
            protected void _onNext(List<CouponRecode> recodes) {
                adapter.setNewData(recodes);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("获取记录失败！" + message);
            }
        });

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_coupon_post_record;
    }
}

