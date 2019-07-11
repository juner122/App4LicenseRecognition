package com.eb.geaiche.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.MyApplication;
import com.eb.geaiche.activity.fragment.MainFragment1New;
import com.eb.geaiche.activity.fragment.MainFragment5New;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.FixInfoActivity;
import com.eb.geaiche.mvp.FixInfoListActivity;
import com.eb.geaiche.stockControl.activity.StockControlActivity;
import com.eb.geaiche.util.FileUtil;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.SystemUtil;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.eb.geaiche.view.DownLodingDialog;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;

import com.eb.geaiche.activity.fragment.MainFragment5;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.eb.geaiche.activity.fragment.MainFragmentPlate;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.PushMessage;
import com.juner.mvp.bean.Shop;
import com.juner.mvp.bean.VersionInfo;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public static final String action = "getMessage_quantity";
    public static final String action_down = "ACTION_DOWNLOAD_COMPLETE";


    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.tv_shopName)
    TextView tv_shopName;

    @BindView(R.id.number)
    TextView number;

    @BindView(R.id.tv_is_new_order)
    TextView tv_is_new_order;
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

    @OnClick({R.id.ll, R.id.ll2, R.id.iv1, R.id.tv_shopName})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll:
                toActivity(PreviewActivity2.class);
                break;
            case R.id.iv1:
                toActivity(OrderNewsListActivity.class);//新消息
                break;

            case R.id.tv_shopName:
//                toActivity(StockControlActivity.class);//出入库
                break;

        }
    }


    @Override
    protected void init() {
        hideHeadView();

        toInfoActivity(getIntent());

        //注册广播
        registBroadcast();
        checkVersionUpDate();


        if (MyAppPreferences.getShopType())
            tv_is_new_order.setVisibility(View.GONE);
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
//        mFragments.add(new MainFragment5());
        mFragments.add(new MainFragment5New());
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


    }


    @Override
    protected void onResume() {
        super.onResume();
        needRead();


        //获取门店信息
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

        if (null != intent.getStringExtra("type") && intent.getStringExtra("type").equals("toOrder")) {
            toActivity(OrderListActivity.class);
        }
        if (null != intent.getStringExtra("type") && intent.getStringExtra("type").equals("toOrderInfo")) {
            toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, intent.getIntExtra("orderId", -1));
        }
        if (null != intent.getStringExtra("type") && intent.getStringExtra("type").equals("toFix")) {
            toActivity(FixInfoListActivity.class);
        }


    }


    MyBroadcastReceiver broadcastReceiver; //个推广播接收器

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            //每接收到一次广播就执行一次方法
            needRead();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    public void registBroadcast() {
        //实例化广播对象
        broadcastReceiver = new MyBroadcastReceiver();//更新未读消息数
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

                    if (integer > 9)
                        number.setText("...");
                    else
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


    //检查版本更新
    private void checkVersionUpDate() {
        Api().checkVersionUpDate().subscribe(new RxSubscribe<VersionInfo>(this, false) {
            @Override
            protected void _onNext(final VersionInfo versionInfo) {

                if (versionInfo.getLast() > SystemUtil.packaGetCode()) {

                    DownLodingDialog dialog = new DownLodingDialog(MainActivity.this, null == versionInfo.getRemark() || "".equals(versionInfo.getRemark()) ? Configure.UPDATAREMARK : versionInfo.getRemark(), versionInfo.getUrl(), versionInfo.getVersionName());
                    dialog.setClicklistener(() -> {
                        dialog.dismiss();
                        finish();
                    });
                    dialog.show();
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }


    /**
     * 安装apk
     */
    private static void openAPK(Context context, String apkPath) {


        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = (new File(apkPath));
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //参数1:上下文, 参数2:Provider主机地址 和配置文件中保持一致,参数3:共享的文件
        Uri apkUri;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//版本在7.0以上是不能直接通过uri访问的
            apkUri = FileProvider.getUriForFile(context, "com.eb.geaiche.fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            apkUri = Uri.fromFile(file);
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


}
