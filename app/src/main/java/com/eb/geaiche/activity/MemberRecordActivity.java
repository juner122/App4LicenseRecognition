package com.eb.geaiche.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.FixInfoListAdapter;
import com.eb.geaiche.adapter.OrderList2Adapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.FixInfoActivity;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.MemberOrder;
import com.juner.mvp.bean.OrderInfoEntity;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;

import butterknife.BindView;

public class MemberRecordActivity extends BaseActivity {

    @BindView(R.id.rv1)
    RecyclerView rv2;

    @BindView(R.id.rv2)
    RecyclerView rv3;

    OrderList2Adapter adapter2;//   订单
    FixInfoListAdapter fixAdapter;//检修单


    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"订单历史", "检修单历史"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void init() {
        tv_title.setText("消费记录");

    }

    @Override
    protected void setUpView() {
        adapter2 = new OrderList2Adapter(null);
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setAdapter(adapter2);


        fixAdapter = new FixInfoListAdapter(R.layout.item_fragment2_main, null, this.getBaseContext());
        rv3.setLayoutManager(new LinearLayoutManager(this));
        rv3.setAdapter(fixAdapter);

        adapter2.setEmptyView(R.layout.order_list_empty_view, rv2);
        fixAdapter.setEmptyView(R.layout.fix_list_empty_view, rv3);
        adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderInfoEntity o = (OrderInfoEntity) adapter.getData().get(position);
                toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, o.getId());

            }
        });

        fixAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                toActivity(FixInfoActivity.class, "id", ((FixInfoEntity) adapter.getData().get(position)).getId());
            }
        });


        for (int i = 0; i < mTitles.length; i++) {

            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                if (position == 0) {
                    rv2.setVisibility(View.VISIBLE);
                    rv3.setVisibility(View.GONE);
                } else {
                    rv3.setVisibility(View.VISIBLE);
                    rv2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    @Override
    protected void setUpData() {


        Api().memberOrderList(new AppPreferences(this).getInt(Configure.user_id, -1)).subscribe(new RxSubscribe<MemberOrder>(this, true) {
            @Override
            protected void _onNext(MemberOrder memberOrder) {
                adapter2.setNewData(memberOrder.getOrderList());
                fixAdapter.setNewData(memberOrder.getFixInfoEntities());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_member_record;
    }
}
