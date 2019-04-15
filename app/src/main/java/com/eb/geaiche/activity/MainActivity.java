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
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.activity.fragment.MainFragment1New;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.FixInfoActivity;
import com.eb.geaiche.mvp.FixInfoListActivity;
import com.eb.geaiche.service.GeTuiIntentService;
import com.eb.geaiche.service.GeTuiPushService;
import com.eb.geaiche.util.SystemUtil;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.igexin.sdk.PushManager;
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

        //注册广播
        registBroadcast();
        checkVersionUpDate();
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

        if (null != intent.getStringExtra("type") && intent.getStringExtra("type").equals("toOrder")) {
            toActivity(OrderListActivity.class);
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


    //检查版本更新
    private void checkVersionUpDate() {
        Api().checkVersionUpDate().subscribe(new RxSubscribe<VersionInfo>(this, false) {
            @Override
            protected void _onNext(final VersionInfo versionInfo) {

                if (versionInfo.getLast() > SystemUtil.packaGetCode()) {

                    //弹出对话框
                    final ConfirmDialogCanlce confirmDialog = new ConfirmDialogCanlce(MainActivity.this, String.format("这次我们做了一个非常重大的决定，您只需要点击确定在线升级之后就能体验哦。"), "重要更新通知！");
                    confirmDialog.show();
                    confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            confirmDialog.dismiss();

                            starDownload(versionInfo);
                            ToastUtils.showToast("下载中...");
                            finish();
                        }

                        @Override
                        public void doCancel() {
                            confirmDialog.dismiss();
                            finish();
                        }
                    });
                }


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }


    static String apkPath;

    private void starDownload(VersionInfo versionInfo) {
        apkPath = String.valueOf(getString(R.string.app_name) + "-v" + versionInfo.getVersionName() + "_upDate" + ".apk");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionInfo.getUrl()));
        request.setDescription("下载中");
        request.setTitle("软件更新");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

        }
        request.allowScanningByMediaScanner();//设置可以被扫描到
        request.setVisibleInDownloadsUi(true);// 设置下载可见
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);//下载完成后通知栏任然可见
        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, apkPath);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        // manager.enqueue(request);
        long Id = manager.enqueue(request);
        //listener(Id);
        SharedPreferences sPreferences = getSharedPreferences(
                "downloadapk", 0);
        sPreferences.edit().putLong("apk", Id).commit();//保存此次下载ID

    }

    DownLoadBroadcastReceiver downLoadBroadcastReceiver;

    public static class DownLoadBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent i) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            //版本在7.0以上是不能直接通过uri访问的
////            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
////                File file = (new File(apkPath));
////                // 由于没有在Activity环境下启动Activity,设置下面的标签
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                //参数1:上下文, 参数2:Provider主机地址 和配置文件中保持一致,参数3:共享的文件
////                Uri apkUri = FileProvider.getUriForFile(context, "com.xxxxx.fileprovider", file);
////                //添加这一句表示对目标应用临时授权该Uri所代表的文件
////                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
////            } else {
//            intent.setDataAndType(Uri.fromFile(new File(apkPath)),
//                    "application/vnd.android.package-archive");
////            }
//            context.startActivity(intent);

            ToastUtils.showToast("新版本下载完成，请点击安装");
        }
    }
}
