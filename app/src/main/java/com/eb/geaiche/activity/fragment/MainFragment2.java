package com.eb.geaiche.activity.fragment;


import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.activity.OrderList4DayActivity;
import com.eb.geaiche.activity.OrderSearch;
import com.eb.geaiche.util.SystemUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.eb.geaiche.R;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 主页页面：订单
 */
public class MainFragment2 extends BaseFragment {

    public static final String TAG = "MainFragment2";

    @BindView(R.id.top_num1)
    TextView top_num1;
    @BindView(R.id.top_num2)
    TextView top_num2;


    @BindView(R.id.st)
    SlidingTabLayout stl;
    @BindView(R.id.vp)
    ViewPager vp;


    ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] title = {"全部", "已预约", "待服务", "服务中", "已完成"};


    @OnClick({R.id.ll_day, R.id.ll_moon,R.id.iv_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_day:

                toActivity(OrderList4DayActivity.class, "type", 0);
                break;

            case R.id.ll_moon:
                toActivity(OrderList4DayActivity.class, "type", 1);
                break;

            case R.id.iv_search://搜索
                toActivity(OrderSearch.class);
                break;
        }
    }

    @Override
    protected void setUpView() {

        fragments.add(OrderListFragment.newInstance(0));
        fragments.add(OrderListFragment.newInstance(1));
        fragments.add(OrderListFragment.newInstance(2));
        fragments.add(OrderListFragment.newInstance(3));
        fragments.add(OrderListFragment.newInstance(4));


        stl.setViewPager(vp, title, getActivity(), fragments);


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

    }


    @Override
    protected void onVisible() {
        super.onVisible();
        getData(0);
    }

    private void getData(int position) {
        Api().orderList(position, 1).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(mContext, false) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {
                setTopNum(basePage.getDayTotal(), basePage.getMonthTotal());
            }


            @Override
            protected void _onError(String message) {
                //判断是否是401 token失效
                SystemUtil.isReLogin(message,getActivity());
            }
        });
    }


    @Override
    protected String setTAG() {
        return TAG;
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment2_main;
    }


    public void setTopNum(int dayTotal, int monthTotal) {


        top_num1.setText(String.valueOf(dayTotal));
        top_num2.setText(String.valueOf(monthTotal));

    }


}
