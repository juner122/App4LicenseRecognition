package com.eb.geaiche.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.eb.geaiche.MyApplication;
import com.eb.geaiche.activity.fragment.MainFragment1New;
import com.eb.geaiche.activity.fragment.MainFragment5New;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.FixInfoActivity;
import com.eb.geaiche.mvp.FixInfoListActivity;

import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.SoundUtils;
import com.eb.geaiche.util.StringUtil;
import com.eb.geaiche.util.SystemUtil;


import com.eb.geaiche.vehicleQueue.StartAlprUtil;
import com.eb.geaiche.vehicleQueue.VehicleBroadcastReceiver;
import com.eb.geaiche.vehicleQueue.VehicleQueueActivity;
import com.eb.geaiche.view.DownLodingDialog;
import com.eb.geaiche.zbar.CaptureActivity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;


import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.eb.geaiche.activity.fragment.MainFragmentPlate;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Plate;
import com.juner.mvp.bean.PlateInfo;
import com.juner.mvp.bean.PushMessage;
import com.juner.mvp.bean.Shop;
import com.juner.mvp.bean.UserEntity;
import com.juner.mvp.bean.VersionInfo;
import com.zkzh.alpr.jni.AlprSDK;
import com.zkzh.alpr.jni.DEVINFO;
import com.zkzh.alpr.jni.IRecogAllInfoCallback;
import com.zkzh.alpr.jni.RECOG_ALL_INFO;
import com.zkzh.alpr.jni.RecogAllInfoCallback;
import com.zkzh.alpr.jni.StreamDataCallBack;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.geaiche.vehicleQueue.VehicleBroadcastReceiver.STATICACTION;

public class MainActivity extends BaseActivity {

    public static final String action = "getMessage_quantity";//个推
    public static final String action_down = "ACTION_DOWNLOAD_COMPLETE";


    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.tv_shopName)
    TextView tv_shopName;

    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.number2)
    TextView number2;//待服务车辆数量
    @BindView(R.id.iv2)
    View iv2;//待服务车辆列表按钮

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

    @OnClick({R.id.ll, R.id.ll2, R.id.iv1, R.id.iv2, R.id.tv_shopName, R.id.scan})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll:
                toActivity(PreviewActivity2.class);
                break;
            case R.id.iv1:
                toActivity(OrderNewsListActivity.class);//新消息
                break;

            case R.id.iv2:
                toActivity(VehicleQueueActivity.class);//待服务车辆列表
                break;

            case R.id.scan:
                //动态权限申请
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    goScan();
                }
                break;
            case R.id.tv_shopName:
//                toActivity(StockControlActivity.class);//出入库
//                toActivity(CouponListActivity.class);//优惠劵管理
//                toActivity(ManeuverActivity.class);//活动管理
//                toActivity(MeritsDistriListActivity.class);//绩效管理


                //模拟车牌识别成功
//                savePlate("京A88888");
                break;

        }
    }


    /**
     * 跳转到扫码界面扫码
     */
    private void goScan() {
        toActivity(CaptureActivity.class);


    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1://更新数量
//                    needcartRead();
                    break;
                case 2://发送车到店广播
                    savePlate((String) msg.obj);
                    break;
            }


        }
    };


    static String shopId = "163";//黄村店id


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
        mFragments.add(new MainFragmentPlate());
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
//        needcartRead();

        //获取门店信息
        Api().shopInfo().subscribe(new RxSubscribe<Shop>(this, false) {
            @Override
            protected void _onNext(Shop shop) {
                tv_shopName.setText(shop.getShop().getShopName());


                if (shopId.equals(shop.getShop().getId())) {//登录的是黄村才启动中控识别sdk
                    //注册车辆进店广播
                    registVehicleBroadcast();
                    iv2.setVisibility(View.VISIBLE);
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
                //判断是否是401 token失效
                SystemUtil.isReLogin(message, MainActivity.this);
            }
        });
        cl.setVisibility(View.VISIBLE);
    }


    MyBroadcastReceiver broadcastReceiver; //个推广播接收器

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            needRead();
        }
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }


    //注册新订单广播
    public void registBroadcast() {
        //新订单广播对象
        broadcastReceiver = new MyBroadcastReceiver();//更新未读消息数
        //实例化广播过滤器，只拦截指定的广播
        IntentFilter filter = new IntentFilter(action);
        //注册广播
        this.registerReceiver(broadcastReceiver, filter);


    }

    //注册车辆进店广播
    public void registVehicleBroadcast() {

        //车辆入店广播
        IntentFilter filter = new IntentFilter(STATICACTION);
        //注册广播
        this.registerReceiver(new VehicleBroadcastReceiver(), filter);

        //开启中控车牌识别sdk
        StartAlprUtil.startAlprOperation(this, new MyRecogAllInfoCallback());
    }


    //中控车牌识别回调
    public class MyRecogAllInfoCallback implements IRecogAllInfoCallback {
        @Override
        public void recogAllInfoCallback(RECOG_ALL_INFO r) {
            Log.e("AlprSDK", "My recogAllInfo callback data:" + r);
            //r.toString()为key不带双引号的不标准json
            //直接匹配车牌号
            String regex = "license:(.+?),";
            String plate_num = StringUtil.getMatcher(regex, r.toString());

            //匹配到车牌
            if (!plate_num.equals("")) {
                Message message = new Message();
                message.what = 2;
                message.obj = plate_num;
                handler.sendMessage(message);
            }
        }
    }

    //发送车辆入店广播
    public void sendVehicleBroadcast(String plate) {


        Intent intent = new Intent();
        //设置Action
        intent.setAction(STATICACTION);
        //Intent携带数据
        intent.putExtra("plate", plate);
        //发送广播--普通
        //sendBroadcast(intent);
        //发送有序广播
        sendOrderedBroadcast(intent, null);


//        //添加到等待服务车辆队列里
//        MyApplication.vehicleQueueUtils.addVehicleData(plate);


        //更新数量
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);

    }

    //添加车辆到的车辆池
    private void savePlate(String plate) {

        Api().savePlate(plate).subscribe(new RxSubscribe<NullDataEntity>(this, false) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                //播放声音
                SoundUtils.playSound(R.raw.soued);

                sendVehicleBroadcast(plate);
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.showToast(message);
            }
        });

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

    private void needcartRead() {
//        int i = MyApplication.vehicleQueueUtils.getDataFromLocal().size();
        //待服务车辆数量
//        if (i > 0) {
//            if (i > 9)
//                number2.setText("...");
//            else
//                number2.setText(String.valueOf(i));
//
//            number2.setVisibility(View.VISIBLE);
//
//        } else {
//            number2.setVisibility(View.GONE);
//
//        }

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


}
