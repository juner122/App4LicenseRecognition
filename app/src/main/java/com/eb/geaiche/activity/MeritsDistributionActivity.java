package com.eb.geaiche.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MeeitsAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfo;
import com.juner.mvp.bean.StaffPerformance;
import com.juner.mvp.bean.Technician;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    boolean isShow;//true查看，分配
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
        adapter = new MeeitsAdapter(null, this, isShow);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


    }

    //计算提成额
    private String calculation(String percentage, String deductionBase) {
        String deduction;//提成额
        if (null == percentage)
            percentage = "0.0";


        BigDecimal db = new BigDecimal(deductionBase);//基数
        BigDecimal per = new BigDecimal(percentage);//百分比
        deduction = (db.multiply(per)).divide(new BigDecimal(100), 2, RoundingMode.UP).toString();


        return deduction;
    }

    @Override
    protected void setUpData() {


        if (!isShow) {//分配绩效
            ll_button_view.setVisibility(View.VISIBLE);
            orderDeduction();
        } else {//查看绩效
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
//
//                if (!isShow) {
//                    adapter.setNewData(sysUserList);
//                } else
                if (s.size() == 0) {
                    ToastUtils.showToast("该订单暂未分配员工！");
                    return;
                }
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

        for (Technician t : list) {
//            t.setOrderId(orderId);
//            t.setSysuserId(t.getUserId());
//            t.setUserName(t.getNickName());
        }


        return list;

    }

}
