package com.eb.geaiche;


import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.adapter.OrderList2Adapter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.MemberOrder;

public class TestActivity extends BaseActivity {

    OrderList2Adapter adapter2;//   订单

    @Override
    protected void init() {
        adapter2 = new OrderList2Adapter(null);

    }

    @Override
    protected void setUpView() {
        Api().memberOrderList(115).subscribe(new RxSubscribe<MemberOrder>(this, true) {
            @Override
            protected void _onNext(MemberOrder memberOrder) {
                adapter2.setNewData(memberOrder.getOrderList());
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
        return R.layout.activity_member_management_member_info2;
    }
}
