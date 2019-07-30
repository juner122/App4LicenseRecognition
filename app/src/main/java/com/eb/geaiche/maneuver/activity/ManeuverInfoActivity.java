package com.eb.geaiche.maneuver.activity;


import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.adapter.GoodsPicListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.maneuver.adapter.ManeuverAskAdapter;
import com.eb.geaiche.util.ImageUtils;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.SystemUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.eb.geaiche.view.ConfirmDialogEt;
import com.eb.geaiche.view.GlideImageLoader;
import com.eb.geaiche.view.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.bean.Ask;
import com.juner.mvp.bean.Joiner;
import com.juner.mvp.bean.Maneuver;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Shop;
import com.juner.mvp.bean.UserEntity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class ManeuverInfoActivity extends BaseActivity {

    String id;//活动id;
    UserEntity ue;//当前登录员工信息
    Shop shop;//当前登录的门店信息
    @BindView(R.id.iv)
    Banner banner;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.explain)
    TextView explain;

    @BindView(R.id.joinNum)
    TextView joinNum;


    @BindView(R.id.rv_img)
    RecyclerView rv_img;


    @BindView(R.id.rv_question)
    RecyclerView rv_question;

    GoodsPicListAdapter goodsPicListAdapter;//详情图片列表
    ManeuverAskAdapter askAdapter;//反馈列表

    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"详情说明", "提问反馈"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    Maneuver maneuver;//当前活动对象

    @OnClick({R.id.button1, R.id.button2, R.id.tv_title_r})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:

                final ConfirmDialogEt dialogCanlce = new ConfirmDialogEt(ManeuverInfoActivity.this);
                dialogCanlce.show();
                dialogCanlce.setClicklistener(postscript -> {
                    dialogCanlce.dismiss();

                    //反馈提问
                    feedback(postscript);
                });


                break;
            case R.id.button2://报名
                //弹出对话框
                final ConfirmDialogCanlce c2 = new ConfirmDialogCanlce(this, "\n" +
                        "                            亲，确定报名本次活动吗？\n" +
                        "审核将在三个工作日内完成。\n" +
                        "您可以在[我已报名]查看进度\n" +
                        "                        ", "重要提示！", "再想想", "报名");
                c2.show();
                c2.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        c2.dismiss();
                        //报名

                        signUp();

                    }

                    @Override
                    public void doCancel() {
                        c2.dismiss();
                    }
                });

                break;

            case R.id.tv_title_r:
                toActivity(MyAskActivity.class, "id", id);

                break;
        }
    }


    @Override
    protected void init() {
        id = getIntent().getStringExtra("id");


        Api().getInfo().subscribe(new RxSubscribe<UserEntity>(this, true) {
            @Override
            protected void _onNext(UserEntity u) {
                ue = u;
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("员工信息获取失败！" + message);
            }
        });


        Api().shopInfo().subscribe(new RxSubscribe<Shop>(this, true) {
            @Override
            protected void _onNext(Shop s) {
                shop = s;
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("门店信息获取失败！" + message);
            }
        });

    }

    @Override
    protected void setUpView() {
        tv_title.setText("活动详情");
        setRTitle("我的反馈");

        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);

        rv_img.setLayoutManager(new LinearLayoutManager(this));
        rv_question.setLayoutManager(new LinearLayoutManager(this));


        goodsPicListAdapter = new GoodsPicListAdapter(null, this);
        askAdapter = new ManeuverAskAdapter(null, this);

        rv_img.setAdapter(goodsPicListAdapter);
        rv_question.setAdapter(askAdapter);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                //切换
                if (position == 0) {
                    rv_img.setVisibility(View.VISIBLE);
                    rv_question.setVisibility(View.GONE);
                } else {
                    rv_img.setVisibility(View.GONE);
                    rv_question.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabReselect(int position) {
            }
        });

    }


    @Override
    protected void setUpData() {

        //获取详情
        Api().infoShopunity(id).subscribe(new RxSubscribe<Maneuver>(this, true) {
            @Override
            protected void _onNext(Maneuver m) {
                maneuver = m;
                if (maneuver.getImgOneList().size() > 0) {//轮播图
                    List<String> list = new ArrayList<>();
                    for (Maneuver.ImgOneList mi : maneuver.getImgOneList()) {
                        list.add(mi.getImg());
                    }
                    //设置图片集合
                    banner.setImages(list);
                    //banner设置方法全部调用完毕时最后调用
                    banner.start();
                } else {
                    ToastUtils.showToast("暂无图片！");
                }


                if (maneuver.getImgTwoList().size() > 0) {//详情图

                    List<String> list = new ArrayList<>();
                    for (Maneuver.ImgOneList mi : maneuver.getImgOneList()) {
                        list.add(mi.getImg());
                    }
                    goodsPicListAdapter.setNewData(list);

                } else {
                    ToastUtils.showToast("暂无图片！");
                }


                name.setText(maneuver.getName());
                explain.setText(maneuver.getExplain());
                joinNum.setText(String.format("参与门店(%s家)", maneuver.getJoinNum()));
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("获取详情失败！" + message);
                finish();
            }
        });

        getAskList();

    }

    //获取反馈列表
    private void getAskList() {
        Api().askList(id).subscribe(new RxSubscribe<List<Ask>>(this, true) {
            @Override
            protected void _onNext(List<Ask> asks) {
                askAdapter.setNewData(asks);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("反馈列表获取失败！" + message);
            }
        });
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_maneuver_info;
    }

    private void feedback(String postscript) {
        Api().askTo(getJoiner(postscript)).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                ToastUtils.showToast("反馈成功！");
                getAskList();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("反馈失败！" + message);
            }
        });


    }

    private void signUp() {
        Api().joinIn(getJoiner(null)).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                ToastUtils.showToast("报名成功！");
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("报名失败！" + message);
            }
        });
    }


    private Ask getJoiner(String postscript) {

        Ask ask = new Ask();
        if (!TextUtils.isEmpty(postscript))
            ask.setAskContent(postscript);

        ask.setUnityId(id);
        ask.setUserId(String.valueOf(ue.getUserId()));
        ask.setUnityName(maneuver.getName());
        ask.setUserName(ue.getUsername());
        ask.setShopId(shop.getShop().getId());
        ask.setShopName(shop.getShop().getShopName());
        ask.setUnityImg(maneuver.getImg());
        ask.setStatus("1");

        return ask;
    }


    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

}
