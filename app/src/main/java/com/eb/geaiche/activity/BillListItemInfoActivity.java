package com.eb.geaiche.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.adapter.SimpleServiceInfoAdpter;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.SimpleGoodInfoAdpter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.OrderInfo;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.String2Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.juner.mvp.Configure.Goods_TYPE_4;

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
    @BindView(R.id.rv2)
    RecyclerView rv2;


    private SimpleGoodInfoAdpter adpter1;
    private SimpleGoodInfoAdpter adpter2;

    @Override
    protected void init() {
        tv_title.setText("账单详情");
        adpter1 = new SimpleGoodInfoAdpter(null, false);
        adpter2 = new SimpleGoodInfoAdpter(null, false);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adpter1);

        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setAdapter(adpter2);

    }

    @Override
    protected void setUpView() {
        String s = getIntent().getStringExtra(Configure.order_on);

        Api().orderDetail(s).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo o) {

                adpter1.setNewData(getProductList(o.getOrderInfo().getGoodsList(),Configure.Goods_TYPE_4));
                adpter2.setNewData(getProductList(o.getOrderInfo().getGoodsList(),Configure.Goods_TYPE_3));

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
                tv_price3.append(MathUtil.twoDecimal(o.getOrderInfo().getActual_price()));

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


    public List<GoodsEntity> getProductList(List<GoodsEntity> list,int type) {

        List<GoodsEntity> carts = new ArrayList<>();
        for (GoodsEntity c : list) {
            if (c.getType() == type)
                carts.add(c);
        }
        return carts;
    }

}
