package com.eb.geaiche.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.OrderOfTchnicianAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Technician;
import com.juner.mvp.bean.TechnicianInfo;

import butterknife.BindView;
import butterknife.OnClick;

public class TechnicianInfoActivity extends BaseActivity {
    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_technician_info;
    }

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_name_s)
    TextView tv_name_s;

    @BindView(R.id.tv_type)
    TextView tv_type;

    @BindView(R.id.tv_shopName)
    TextView tv_shopName;

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.phone)
    TextView phone;

    OrderOfTchnicianAdapter adapter;

    Technician sysUser;

    @OnClick({R.id.iv_edit, R.id.ll_record})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_edit:
                toActivity(StaffInfoFixActivity.class, sysUser, "sysUser");

                break;

            case R.id.ll_record:

                break;
        }
    }


    @Override
    protected void init() {
        tv_title.setText("员工详情");
        adapter = new OrderOfTchnicianAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setEmptyView(R.layout.order_list_empty_view, recyclerView);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void setUpView() {
        int id = getIntent().getIntExtra("id", -1);
        Api().sysuserDetail(id).subscribe(new RxSubscribe<TechnicianInfo>(this, true) {
            @Override
            protected void _onNext(TechnicianInfo t) {


                tv_price.setText(MathUtil.twoDecimal(t.getMoney()) + "元");
                tv_name.setText(t.getSysUser().getUsername());
                phone.setText(t.getSysUser().getMobile());
                tv_shopName.setText(t.getShop().getShopName());
                tv_address.setText(t.getShop().getAddress());
                tv_name_s.setText(t.getSysUser().getUsername().substring(0, 1));

                adapter.setNewData(t.getOrderList());
                sysUser = t.getSysUser();
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
            }
        });
    }

    @Override
    protected void setUpData() {

    }


}
