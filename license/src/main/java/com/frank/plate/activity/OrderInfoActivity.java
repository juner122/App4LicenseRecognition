package com.frank.plate.activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.frank.plate.R;
import com.frank.plate.bean.QueryByCarEntity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单详情 分4种情况     已预约未付款   待服务，服务中，已完成 后三种动态都已付款
 */
public class OrderInfoActivity extends BaseActivity {
    private boolean isFixOrder;//是否正在修改订单

    String order_status;
    public static final String order_status_text1 = "已预约";
    public static final String order_status_text2 = "待服务";
    public static final String order_status_text3 = "服务中";
    public static final String order_status_text4 = "已完成";

    @BindView(R.id.tv_order_sn)
    TextView tv_order_sn;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_consignee)
    TextView tv_consignee;

    @BindView(R.id.tv_pick_technician)
    ImageButton tv_pick_technician;

    @BindView(R.id.tv_pick_date)
    ImageButton tv_pick_date;

    @BindView(R.id.tv_fix_order)
    TextView tv_fix_order;

    @BindView(R.id.but_product_list)
    ImageButton but_product_list;
    @BindView(R.id.but_meal_list)
    ImageButton but_meal_list;

    @OnClick({R.id.tv_fix_order, R.id.tv_enter_order, R.id.but_meal_list, R.id.but_product_list, R.id.tv_pick_technician, R.id.tv_pick_date, R.id.tv_car_info})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_fix_order:
                if (!isFixOrder)
                    onFixOrder();
                else
                    onFixOrderDone();
                break;

            case R.id.tv_enter_order:

                break;
            case R.id.but_meal_list:
                break;

            case R.id.but_product_list://选择商品
                break;

            case R.id.tv_pick_technician://选择技师
                break;
            case R.id.tv_pick_date://选择时间
                break;
            case R.id.tv_car_info://车信息
                break;
        }


    }


    private QueryByCarEntity queryByCarEntity;


    @Override
    protected void init() {

        queryByCarEntity = getIntent().getParcelableExtra(MemberInfoInputActivity.key);
        order_status = queryByCarEntity.getOrderInfo().getOrder_status();

        if (order_status.equals("1")) {//已预约
            setRTitle(order_status_text1);
        }
        if (order_status.equals("2")) {
            setRTitle(order_status_text2);

        }
        tv_order_sn.append(queryByCarEntity.getOrderInfo().getOrder_sn());
        tv_mobile.setText(queryByCarEntity.getOrderInfo().getMobile());
        tv_consignee.setText(queryByCarEntity.getOrderInfo().getConsignee());


    }


    //修改订单  显示控件
    private void onFixOrder() {
        tv_fix_order.setText("保存修改");
        but_product_list.setVisibility(View.VISIBLE);
        but_meal_list.setVisibility(View.VISIBLE);
        tv_pick_technician.setVisibility(View.VISIBLE);
        tv_pick_date.setVisibility(View.VISIBLE);
        isFixOrder = true;
    }

    //保存修改  隐藏控件
    private void onFixOrderDone() {
        tv_fix_order.setText("修改下单");
        but_product_list.setVisibility(View.INVISIBLE);
        but_meal_list.setVisibility(View.INVISIBLE);
        tv_pick_technician.setVisibility(View.INVISIBLE);
        tv_pick_date.setVisibility(View.INVISIBLE);
        isFixOrder = false;
    }


    @Override
    protected void setUpView() {
        tv_title.setText("订单详情");

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_info;
    }
}
