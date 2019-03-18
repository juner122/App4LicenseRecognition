package com.eb.new_line_seller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.eb.new_line_seller.activity.fragment.MainFragment1New;
import com.eb.new_line_seller.api.ApiLoader;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.mvp.FixInfoActivity;
import com.eb.new_line_seller.service.GeTuiIntentService;
import com.eb.new_line_seller.service.GeTuiPushService;
import com.eb.new_line_seller.util.SystemUtil;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.igexin.sdk.PushManager;
import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.fragment.MainFragment1;
import com.eb.new_line_seller.activity.fragment.MainFragment2;

import com.eb.new_line_seller.activity.fragment.MainFragment4;
import com.eb.new_line_seller.activity.fragment.MainFragment5;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.eb.new_line_seller.activity.fragment.MainFragmentPlate;
import com.eb.new_line_seller.util.ToastUtils;
import com.juner.mvp.bean.AppMenu;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.PushMessage;
import com.juner.mvp.bean.Shop;
import com.juner.mvp.bean.VersionInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public static final String action = "getMessage_quantity";


    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.tv_shopName)
    TextView tv_shopName;

    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.cl)
    View cl;



    private String[] mTitles = {"工作台", "", "我的"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private int[] mIconUnselectIds = {
            R.mipmap.icon_bottom_button1_unselect,
            R.color.fff, R.mipmap.icon_bottom_button5_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.icon_bottom_button1_select,
            R.color.fff, R.mipmap.icon_bottom_button5_select};

    @OnClick({R.id.ll, R.id.ll2, R.id.iv1})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll:
                toActivity(PreviewActivity2.class);
                break;
            case R.id.iv1:
                toActivity(OrderNewsListActivity.class);//新消息
                break;

        }
    }


    @Override
    protected void init() {
        hideHeadView();

        toInfoActivity(getIntent());
        //初始化个推
        PushManager.getInstance().initialize(this.getApplicationContext(), GeTuiPushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);

        //注册广播
        registBroadcast();
    }


    @Override
    public void setUpView() {

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mFragments.add(new MainFragment1New());
//        mFragments.add(new MainFragment2());
//        mFragments.add(new MainFragment3());
        mFragments.add(new MainFragmentPlate());
//        mFragments.add(new MainFragment4());
        mFragments.add(new MainFragment5());
        commonTabLayout.setTabData(mTabEntities, this, R.id.fragment, mFragments);
        setCurrentTab(0);

        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    cl.setVisibility(View.VISIBLE);
                } else {
                    cl.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


    }


    @Override
    protected void setUpData() {
        Api().shopInfo().subscribe(new RxSubscribe<Shop>(this, false) {
            @Override
            protected void _onNext(Shop shop) {
                tv_shopName.setText(shop.getShop().getShopName());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
                //判断是否是401 token失效
                SystemUtil.isReLogin(message, MainActivity.this);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        needRead();
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_main;
    }


    class TabEntity implements CustomTabEntity {
        public String title;
        public int selectedIcon;
        public int unSelectedIcon;

        public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
            this.title = title;
            this.selectedIcon = selectedIcon;
            this.unSelectedIcon = unSelectedIcon;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return selectedIcon;
        }

        @Override
        public int getTabUnselectedIcon() {
            return unSelectedIcon;
        }
    }

    public void setCurrentTab(int i) {
        commonTabLayout.setCurrentTab(i);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int fragment = intent.getIntExtra(Configure.show_fragment, 0);//显示哪个fragment
        setCurrentTab(fragment);


        toInfoActivity(intent);

    }


    //检查版本更新
    private void checkVersionUpDate() {
        Api().checkVersionUpDate().subscribe(new RxSubscribe<VersionInfo>(this, false) {
            @Override
            protected void _onNext(VersionInfo versionInfo) {


            }

            @Override
            protected void _onError(String message) {

            }
        });
    }

    MyBroadcastReceiver broadcastReceiver;

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            //没接收到一次广播就执行一次方法
            needRead();
        }
    }

    public void registBroadcast() {
        //实例化广播对象
        broadcastReceiver = new MyBroadcastReceiver();
        //实例化广播过滤器，只拦截指定的广播
        IntentFilter filter = new IntentFilter(action);
        //注册广播
        this.registerReceiver(broadcastReceiver, filter);
    }

    private void needRead() {
        //未读新消息数量
        Api().needRead().subscribe(new RxSubscribe<Integer>(this, false) {
            @Override
            protected void _onNext(Integer integer) {
                if (integer > 0) {
                    number.setText(String.valueOf(integer));
                    number.setVisibility(View.VISIBLE);
                } else {
                    number.setVisibility(View.GONE);
                }
            }

            @Override
            protected void _onError(String message) {
                number.setVisibility(View.GONE);
            }
        });
    }


    private void toInfoActivity(Intent i) {
        if (i.getBooleanExtra("push", false)) {
            PushMessage pm = i.getParcelableExtra("PushMessage");
            updateRead(pm);
        }


    }

    //标记已读
    private void updateRead(final PushMessage pm) {
        Api().updateRead(pm.getId()).subscribe(new RxSubscribe<NullDataEntity>(this, false) {
            @Override
            protected void _onNext(NullDataEntity entity) {
                Intent intent;
                if (pm.getType() == 1) {
                    intent = new Intent(MainActivity.this, OrderInfoActivity.class);
                    intent.putExtra(Configure.ORDERINFOID, pm.getOrderId());
                } else {
                    intent = new Intent(MainActivity.this, FixInfoActivity.class);
                    intent.putExtra("id", pm.getOrderId());
                }
                startActivity(intent);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }
}
