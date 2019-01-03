package com.eb.new_line_seller.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eb.new_line_seller.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.SimpleGoodInfoAdpter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.bean.OrderInfo;
import com.eb.new_line_seller.util.MathUtil;
import com.eb.new_line_seller.util.String2Utils;

import butterknife.BindView;

public class BillListItemInfoActivity extends BaseActivity {

    private static final String TAG = "BillListItemInfo";
    @BindView(R.id.tv_price1)
    TextView tv_price1;

    @BindView(R.id.tv_price2)
    TextView tv_price2;
    @BindView(R.id.tv_price3)
    TextView tv_price3;

    @BindView(R.id.tv_price4)
    TextView tv_price4;

    @BindView(R.id.tv_order_sn)
    TextView tv_order_sn;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.tv3)
    TextView tv3;

    @BindView(R.id.rv1)
    RecyclerView rv1;


    private SimpleGoodInfoAdpter adpter1;


    @Override
    protected void init() {
        tv_title.setText("账单详情");
        adpter1 = new SimpleGoodInfoAdpter(null, false);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adpter1);

    }

    @Override
    protected void setUpView() {
        String s = getIntent().getStringExtra(Configure.order_on);

        Api().orderDetail(s).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo o) {

                adpter1.setNewData(o.getOrderInfo().getGoodsList());
                tv_order_sn.append(o.getOrderInfo().getOrder_sn());
                tv2.append(o.getOrderInfo().getAdd_time());

                if (null == o.getOrderInfo().getConfirm_time())
                    tv3.setVisibility(View.GONE);
                else
                    tv3.append(o.getOrderInfo().getConfirm_time());

                double goodsPrice = String2Utils.getOrderGoodsPrice(o.getOrderInfo().getGoodsList());
                double goodsPrice2 = String2Utils.getOrderServicePrice(o.getOrderInfo().getSkillList());

                tv_price1.append(MathUtil.twoDecimal(goodsPrice));
                tv_price2.append(MathUtil.twoDecimal(goodsPrice2));
                tv_price3.append(MathUtil.twoDecimal(o.getOrderInfo().getOrder_price()));

                if (o.getOrderInfo().getPay_type() == 11) {
                    tv_price4.append(MathUtil.twoDecimal(o.getOrderInfo().getOrder_price()));

                } else {
                    tv_price4.append("0.00");

                }


            }

            @Override
            protected void _onError(String message) {
                Log.e(TAG, message);
            }
        });

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_bill_info;
    }
}
