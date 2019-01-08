package com.eb.new_line_seller.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.bean.ActivityEntity;
import com.eb.new_line_seller.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ActivityInfoActivity extends BaseActivity {

    @BindView(R.id.tv_button)
    TextView tv_button;


    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_join)
    TextView tv_join;
    @BindView(R.id.iv)
    ImageView iv;


    int id;

    @Override
    protected void init() {
        tv_title.setText("购胎送洗车");
    }

    @Override
    protected void setUpView() {
        id = getIntent().getIntExtra("activity_id", -1);
        Api().activityDetail(id).subscribe(new RxSubscribe<ActivityEntity>(this, true) {
            @Override
            protected void _onNext(ActivityEntity a) {
                tv1.setText(a.getActivity().getActivityName());
                tv_price.setText(String.valueOf("￥" + a.getActivity().getActivityPrice()));
                tv_join.setText(String.format("%s人", a.getActivity().getJoinNum()));


                Glide.with(ActivityInfoActivity.this)
                        .load(a.getActivity().getActivityImage())
                        .into(iv);


            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("活动查询失败：" + message);

            }
        });

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_info;
    }


    @OnClick({R.id.tv_button})
    public void onClick(View v) {
        ToastUtils.showToast("报名成功");
        tv_button.setText("已报名");
        tv_button.setBackground(getResources().getDrawable(R.drawable.button_background_g));


//        Api().activityDetail(id).subscribe(new RxSubscribe<ActivityEntityItem>(this, true) {
//            @Override
//            protected void _onNext(ActivityEntityItem activityEntityItem) {
//                tv_button.setText("已报名");
//            }
//
//            @Override
//            protected void _onError(String message) {
//
//                ToastUtils.showToast("报名失败");
//            }
//        });
//
    }
}
