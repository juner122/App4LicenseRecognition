package com.eb.geaiche.coupon;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.activity.ResultBack;
import com.eb.geaiche.activity.TechnicianListActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.FixInfoDescribeActivity;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.String2Utils;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Coupon2;
import com.juner.mvp.bean.CouponRecode;
import com.juner.mvp.bean.NullDataEntity;

import java.util.ArrayList;

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

    CouponRecode couponRecode;//查看记录对象


    int view_type;//页面类型 1：派发优惠劵，2：查看记录
    boolean withMsg;//是否短信通知

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
    }

    @Override
    protected void init() {
        view_type = getIntent().getIntExtra("view_type", -1);

        if (view_type == 1) {
            tv_title.setText("派发优惠劵");
            IvRSetSrc(R.mipmap.icon_add2);
            ll_post.setVisibility(View.VISIBLE);
            coupon = getIntent().getParcelableExtra("Coupon");


            price.setText(String.format("￥%s", coupon.getType_money()));
            term.setText(String.format("满%s可使用", coupon.getMin_amount()));
            name.setText(coupon.getName());
            time.setText(String.format("有效期至：%s", coupon.getUse_end_date()));
            adapter = new CouponPostUserAdapter(null, true);
        } else {
            tv_title.setText("派发记录");
            ll_post.setVisibility(View.GONE);
            couponRecode = getIntent().getParcelableExtra("CouponRecode");

            price.setText(String.format("￥%s", couponRecode.getTypeMoney()));
            term.setText(String.format("满%s可使用", couponRecode.getMinAmount()));
            name.setText(couponRecode.getCouponName());
            time.setText(String.format("有效期至：%s", DateUtil.getFormatedDateTime(couponRecode.getCreateTime())));
            adapter = new CouponPostUserAdapter(couponRecode.getXgxCouponPushUsersList(), false);
            setNum(adapter.getData().size());
        }

    }

    public void setNum(int n) {
        num.setText(String.format("是否短信通知%s位车主", n));
    }

    private void post() {
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


        if (withMsg)
            recode.setXgxCouponPushUsersList(adapter.getData());


        recode.setDeptId(coupon.getDept_id());
        recode.setCouponId(coupon.getId());


        recode.setUserId(coupon.getId());//操作人
        recode.setUserName(coupon.getId());//操作人


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

    @Override
    protected void setUpView() {

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            //删除一个用户
            adapter.remove(position);
            setNum(adapter.getData().size());

        });


    }

    @Override
    protected void setUpData() {


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_coupon_post;
    }


}
