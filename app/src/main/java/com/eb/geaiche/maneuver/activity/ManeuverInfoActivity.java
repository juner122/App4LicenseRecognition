package com.eb.geaiche.maneuver.activity;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ImageUtils;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.eb.geaiche.view.ConfirmDialogEt;
import com.eb.geaiche.view.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.bean.Joiner;
import com.juner.mvp.bean.Maneuver;
import com.juner.mvp.bean.NullDataEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ManeuverInfoActivity extends BaseActivity {

    String id;//活动id;

    @BindView(R.id.iv)
    ImageView iv;

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


    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"详情说明", "提问反馈"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();


    @OnClick({R.id.button1, R.id.button2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:

                final ConfirmDialogEt dialogCanlce = new ConfirmDialogEt(ManeuverInfoActivity.this);
                dialogCanlce.show();
                dialogCanlce.setClicklistener(postscript -> {
                    dialogCanlce.dismiss();
                    //反馈提问
                    feedback();
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
        }
    }


    @Override
    protected void init() {
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void setUpView() {
        tv_title.setText("活动详情");
        setRTitle("我的反馈");


        rv_img.setLayoutManager(new LinearLayoutManager(this));
        rv_question.setLayoutManager(new LinearLayoutManager(this));


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
            protected void _onNext(Maneuver maneuver) {
                ImageUtils.load(ManeuverInfoActivity.this, maneuver.getImg(), iv);
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


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_maneuver_info;
    }

    private void feedback() {
        Api().askTo(getJoiner()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                ToastUtils.showToast("反馈成功！");
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("反馈失败！" + message);
            }
        });


    }

    private void signUp() {
        Api().joinIn(getJoiner()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
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


    private Joiner getJoiner() {

        return null;
    }
}
