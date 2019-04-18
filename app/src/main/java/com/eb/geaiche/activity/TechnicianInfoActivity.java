package com.eb.geaiche.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.OrderOfTchnicianAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.OrderInfoEntity;
import com.juner.mvp.bean.Technician;
import com.juner.mvp.bean.TechnicianInfo;

import java.util.ArrayList;
import java.util.List;

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
    int id;

    @OnClick({R.id.iv_edit, R.id.ll_record, R.id.tv_title_r, R.id.phone})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_edit:
                Intent intent = new Intent(this, StaffInfoFixActivity.class);
                intent.putExtra("sysUser", sysUser);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;

            case R.id.ll_record:
                toActivity(WorkOrderListActivity.class, "id", id);
                break;
            case R.id.tv_title_r:
                toActivity(WorkOrderListActivity.class, "id", id);
                break;

            case R.id.phone:
                if (TextUtils.isEmpty(phone.getText()))
                    return;
                callPhone(phone.getText().toString());
                break;
        }
    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    protected void init() {
        tv_title.setText("员工详情");
//        showRView("历史记录");
        adapter = new OrderOfTchnicianAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setEmptyView(R.layout.order_list_empty_view, recyclerView);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {

                toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, adapter.getData().get(position).getId());

            }
        });

    }

    @Override
    protected void setUpView() {
    }

    @Override
    protected void setUpData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sysuserDetail();
    }

    private void sysuserDetail() {
        id = getIntent().getIntExtra("id", -1);
        Api().sysuserDetail(id).subscribe(new RxSubscribe<TechnicianInfo>(this, true) {
            @Override
            protected void _onNext(TechnicianInfo t) {


                tv_price.setText(MathUtil.twoDecimal(t.getMoney()) + "元");
                tv_name.setText(t.getSysUser().getUsername());
                phone.setText(t.getSysUser().getMobile());
                tv_shopName.setText(t.getShop().getShopName());
                tv_address.setText(t.getShop().getAddress());
                tv_name_s.setText(t.getSysUser().getUsername().substring(0, 1));
                tv_type.setText(t.getSysUser().getRoleName());

                List<OrderInfoEntity> data = new ArrayList<>();
                data.addAll(t.getOrderList());
                data.addAll(t.getSaleList());


                adapter.setNewData(data);
                sysUser = t.getSysUser();
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
            }
        });
    }
}
