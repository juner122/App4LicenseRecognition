package com.eb.geaiche.coupon;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.bean.CouponRecode;


import java.util.List;

import butterknife.BindView;



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


            String id = adapter.getData().get(position).getId();
            toActivity(CouponPostActivity.class, "id", id);


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

