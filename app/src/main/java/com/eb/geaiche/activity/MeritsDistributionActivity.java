package com.eb.geaiche.activity;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MeeitsAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfo;
import com.juner.mvp.bean.StaffPerformance;
import com.juner.mvp.bean.Technician;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.juner.mvp.Configure.ORDERINFOID;
import static com.juner.mvp.Configure.brand;

//绩效分配员工页面
public class MeritsDistributionActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;


    @BindView(R.id.ll_button_view)
    View ll_button_view;

    @BindView(R.id.price)
    TextView price;

    MeeitsAdapter adapter;

    List<Technician> sysUserList;//技师列表
    boolean isShow;//true查看，f分配
    int orderId;//订单id;

    @OnClick({R.id.enter, R.id.reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter:
                setDeduction();
                break;

            case R.id.reset:

                break;

        }
    }


    @Override
    protected void init() {
        isShow = getIntent().getBooleanExtra("view_type", false);
        tv_title.setText("绩效分配");


    }


    @Override
    protected void setUpView() {
        adapter = new MeeitsAdapter(null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


    }

    @Override
    protected void setUpData() {


        if (!isShow) {
            ll_button_view.setVisibility(View.VISIBLE);
            orderInfo();
        } else {
            ll_button_view.setVisibility(View.GONE);
            orderDeduction();
        }
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_merits_distribution;
    }


    //查看 员工绩效分配
    private void orderDeduction() {

        Api().getOrderDeduction(getIntent().getIntExtra(ORDERINFOID, 0)).subscribe(new RxSubscribe<List<Technician>>(this, true) {
            @Override
            protected void _onNext(List<Technician> s) {
                adapter.setNewData(s);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("查看绩效分配失败" + message);
                finish();
            }
        });
    }


    //设置员工绩效分配
    private void setDeduction() {

        Api().setDeduction(getSysUserList()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity n) {
                ToastUtils.showToast("分配绩效分配成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("分配绩效分配失败！" + message);
            }
        });
    }


    //查看 订单详情
    private void orderInfo() {

        Api().orderDetail(getIntent().getIntExtra(ORDERINFOID, 0)).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo oi) {
                sysUserList = oi.getOrderInfo().getSysUserList();
                orderId = oi.getOrderInfo().getId();
                price.setText(String.format("提成总额:￥%s", oi.getOrderInfo().getOrder_price()));

                for (Technician t : sysUserList) {
                    t.setDeductionBase(String.valueOf(oi.getOrderInfo().getOrder_price()));
                }

                adapter.setNewData(sysUserList);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("订单详情获取失败！" + message);
                finish();
            }
        });
    }


    private List<Technician> getSysUserList() {

        List<Technician> list = adapter.getData();

        for(Technician t:list){
            t.setOrderId(orderId);
            t.setSysuserId(t.getUserId());
            t.setUsername(t.getNickName());
        }




        return list;

    }

}
