package com.eb.geaiche.coupon;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Coupon2;
import com.juner.mvp.bean.CouponRecode;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.UserEntity;

import butterknife.BindView;
import butterknife.OnClick;

public class CouponPostActivity extends BaseActivity {

    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.term)
    TextView term;
    @BindView(R.id.time)
    TextView time;


    @BindView(R.id.et_info)
    EditText et_info;


    @BindView(R.id.iv_pick_all)
    ImageView iv_pick_all;


    @BindView(R.id.num)
    TextView num;

    @BindView(R.id.ll_post)
    View ll_post;

    @BindView(R.id.rv)
    RecyclerView rv;

    CouponPostUserAdapter adapter;

    Coupon2 coupon;//要派发的优惠劵对象

    int view_type;//页面类型 1：派发优惠劵，2：查看记录
    boolean withMsg;//是否短信通知

    UserEntity user;//当前登录员工对象


    @OnClick({R.id.tv_total, R.id.iv_pick_all, R.id.tv_iv_r})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_total:

                //派发
                post();
                break;
            case R.id.tv_iv_r:
                //添加用户
                toActivity(CouponPickUserActivity.class, adapter.getData(), "Member");
                break;
            case R.id.iv_pick_all:
                if (withMsg) {
                    withMsg = false;
                    iv_pick_all.setImageResource(R.drawable.icon_unpick2);
                } else {
                    withMsg = true;
                    iv_pick_all.setImageResource(R.drawable.icon_pick2);
                }


                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        adapter.setNewData(intent.getParcelableArrayListExtra("Member"));
        setNum(adapter.getData().size());
    }

    @Override
    protected void init() {
        view_type = getIntent().getIntExtra("view_type", -1);


    }

    public void setNum(int n) {
        num.setText(String.format("是否短信通知%s位车主", n));
    }

    private void post() {
        if (adapter.getData().size() <= 0) {
            ToastUtils.showToast("最少派发一位车主！");
            return;
        }

        Api().pushCoupon(createRecode()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                ToastUtils.showToast("操作成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("操作失败！" + message);
            }
        });
    }

    private CouponRecode createRecode() {
        CouponRecode recode = new CouponRecode();


        recode.setXgxCouponPushUsersList(adapter.getData());


        recode.setDeptId(coupon.getDept_id());
        recode.setCouponId(coupon.getId());


        recode.setUserId(String.valueOf(user.getUserId()));//操作人
        recode.setUserName(user.getUsername());


        recode.setCouponName(coupon.getName());//券名
        recode.setTypeMoney(coupon.getType_money());
        recode.setMinAmount(coupon.getMin_amount());
        recode.setCouponType(coupon.getType());
        recode.setSuperposition(coupon.getSuperposition());
        recode.setCycle(coupon.getCycle());
        recode.setDealNum(String.valueOf(adapter.getData().size()));
        recode.setCreateTime(String.valueOf(System.currentTimeMillis()));
        recode.setRemark(et_info.getText().toString());
        recode.setWithMsg(withMsg ? "1" : "0");

        return recode;

    }


    //获取优惠劵派发记录信息
    private void getCouponInfo(String id) {

        Api().pushLogInfo(id).subscribe(new RxSubscribe<CouponRecode>(this, true) {
            @Override
            protected void _onNext(CouponRecode cr) {

                price.setText(String.format("￥%s", cr.getTypeMoney()));
                term.setText(String.format("满%s可使用", cr.getMinAmount()));
                name.setText(cr.getCouponName());
                time.setText(String.format("有效期至：%s天", cr.getCycle()));

                adapter.setNewData(cr.getXgxCouponPushUsersList());

                setNum(adapter.getData().size());
                if (null == cr.getRemark() || cr.getRemark().equals(""))
                    et_info.setText("-");
                else
                    et_info.setText(cr.getRemark());
            }


            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("操作失败！" + message);
            }
        });

    }

    @Override
    protected void setUpView() {

        rv.setLayoutManager(new LinearLayoutManager(this));

        if (view_type == 1) {
            tv_title.setText("派发优惠劵");
            IvRSetSrc(R.mipmap.icon_add2);
            ll_post.setVisibility(View.VISIBLE);
            coupon = getIntent().getParcelableExtra("Coupon");


            price.setText(String.format("￥%s", coupon.getType_money()));
            term.setText(String.format("满%s可使用", coupon.getMin_amount()));
            name.setText(coupon.getName());
            time.setText(String.format("有效期：%s天", coupon.getCycle()));

            adapter = new CouponPostUserAdapter(null, true);
            rv.setAdapter(adapter);

            adapter.setOnItemChildClickListener((adapter, view, position) -> {
                //删除一个用户
                adapter.remove(position);
                setNum(adapter.getData().size());

            });

        } else {
            tv_title.setText("派发记录");
            et_info.setFocusable(false);
            ll_post.setVisibility(View.GONE);
            adapter = new CouponPostUserAdapter(null, false);
            getCouponInfo(getIntent().getStringExtra("id"));

        }
        rv.setAdapter(adapter);


    }

    @Override
    protected void setUpData() {

        Api().getInfo().subscribe(new RxSubscribe<UserEntity>(this, true) {
            @Override
            protected void _onNext(UserEntity ue) {
                user = ue;
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("员工信息获取失败！" + message);
            }
        });


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_coupon_post;
    }


}
