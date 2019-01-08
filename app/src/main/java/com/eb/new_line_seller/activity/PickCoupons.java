package com.eb.new_line_seller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.CouponaAdapter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.bean.Coupon;

import java.util.List;

import butterknife.BindView;

public class PickCoupons extends BaseActivity {


    String order_price;
    int user_id;
    CouponaAdapter adapter;

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.tv1)
    View tv1;


    List<Coupon> cos;
    int position_pick = -1;

    @Override
    protected void init() {
        tv_title.setText("选择优惠券");

        order_price = getIntent().getStringExtra("order_price");
        user_id = getIntent().getIntExtra(Configure.user_id, 0);


        adapter = new CouponaAdapter(cos);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (cos.get(position).isSelected()) {
                    cos.get(position).setSelected(false);

                    tv1.setBackgroundResource(R.drawable.button_background_g);
                    tv1.setOnClickListener(null);

                } else {

                    for (Coupon c : cos) {
                        c.setSelected(false);
                    }
                    cos.get(position).setSelected(true);

                    tv1.setBackgroundResource(R.drawable.button_background_c);
                    tv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(PickCoupons.this, OrderPayActivity.class);
                            Bundle b = new Bundle();
                            b.putParcelable("Coupon", cos.get(position_pick));
                            b.putInt("code", 110);
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });

                    position_pick = position;
                }

                adapter.notifyDataSetChanged();

            }
        });

        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter.setEmptyView(R.layout.order_list_empty_view_c,rv);
        rv.setAdapter(adapter);
    }

    @Override
    protected void setUpView() {


        Api().couponList(order_price, user_id).subscribe(new RxSubscribe<List<Coupon>>(this, true) {
            @Override
            protected void _onNext(List<Coupon> coupons) {

                cos = coupons;
                adapter.setNewData(cos);

            }

            @Override
            protected void _onError(String message) {

            }
        });


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_user_pick_coupons;
    }


}
