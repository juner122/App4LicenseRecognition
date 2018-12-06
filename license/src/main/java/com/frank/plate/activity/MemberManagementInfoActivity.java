package com.frank.plate.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.adapter.OrderList2Adapter;
import com.frank.plate.adapter.SimpleCarInfoAdpter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.MemberOrder;

import butterknife.BindView;

public class MemberManagementInfoActivity extends BaseActivity {
    private static final String TAG = "MemberManagementInfo";
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.rv2)
    RecyclerView rv2;

    SimpleCarInfoAdpter adpter1;

    OrderList2Adapter adapter2;

    @Override
    protected void init() {
        tv_title.setText("会员管理");
        adpter1 = new SimpleCarInfoAdpter(null);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adpter1);

        adapter2 = new OrderList2Adapter(null);
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setAdapter(adapter2);
    }

    @Override
    protected void setUpView() {
        int user_id = getIntent().getIntExtra(Configure.user_id, 0);

        Api().memberOrderList(user_id).subscribe(new RxSubscribe<MemberOrder>(this, true) {
            @Override
            protected void _onNext(MemberOrder memberOrder) {
                phone.setText(memberOrder.getMember().getMobile());
                name.setText(memberOrder.getMember().getUsername());
                adpter1.setNewData(memberOrder.getCarList());
                adapter2.setNewData(memberOrder.getOrderList());
            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG,message);
            }
        });

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_member_management_member_info;
    }
}
