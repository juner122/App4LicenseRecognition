package com.eb.geaiche.activity;


import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.fragment.OrderListFragment;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.activity.StockOutActivity;
import com.eb.geaiche.util.SystemUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderListActivity extends BaseActivity {


    @Override
    protected void init() {

        tv_title.setText("订单管理");
        showIVR();
    }

    @BindView(R.id.top_num1)
    TextView top_num1;
    @BindView(R.id.top_num2)
    TextView top_num2;


    @BindView(R.id.enter)
    View enter;//非订单出库按钮


    @BindView(R.id.st)
    SlidingTabLayout stl;
    @BindView(R.id.vp)
    ViewPager vp;


    ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] title = {"全部", "已预约", "待服务", "服务中", "已完成"};


    public static int view_intent;//页面意图  0=正常查看订单，1=领料出库选择订单


    @OnClick({R.id.ll_day, R.id.ll_moon, R.id.tv_iv_r, R.id.enter})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_day:

                toActivity(OrderList4DayActivity.class, "type", 0);
                break;

            case R.id.ll_moon:
                toActivity(OrderList4DayActivity.class, "type", 1);
                break;

            case R.id.tv_iv_r://搜索
                toActivity(OrderSearch.class);
                break;

            case R.id.enter://非订单出库
                toActivity(StockOutActivity.class, Configure.ORDERINFOID, -1);
                break;
        }
    }


    @Override
    protected void setUpView() {


        view_intent = getIntent().getIntExtra("view_intent", 0);
        if (view_intent != 0) {//正常查看列表不显示按钮
            enter.setVisibility(View.GONE);
        } else
            enter.setVisibility(View.GONE);


        fragments.add(OrderListFragment.newInstance(0));
        fragments.add(OrderListFragment.newInstance(1));
        fragments.add(OrderListFragment.newInstance(2));
        fragments.add(OrderListFragment.newInstance(3));
        fragments.add(OrderListFragment.newInstance(4));


        stl.setViewPager(vp, title, this, fragments);


        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getData(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getData(0);
    }

    private void getData(int position) {
        Api().orderList(position, 1).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(this, false) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {
                setTopNum(basePage.getDayTotal(), basePage.getMonthTotal());
            }


            @Override
            protected void _onError(String message) {
                //判断是否是401 token失效
                SystemUtil.isReLogin(message, OrderListActivity.this);
            }
        });
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_list;
    }

    public void setTopNum(int dayTotal, int monthTotal) {


        top_num1.setText(String.valueOf(dayTotal));
        top_num2.setText(String.valueOf(monthTotal));

    }
}
