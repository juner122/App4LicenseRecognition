package com.frank.plate.activity;

import android.view.View;
import android.widget.TextView;

import com.frank.plate.R;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.ActivityEntityItem;
import com.frank.plate.util.MathUtil;
import com.frank.plate.util.ToastUtils;

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


    int id;

    @Override
    protected void init() {
        tv_title.setText("购胎送洗车");

    }

    @Override
    protected void setUpView() {
        id = getIntent().getIntExtra("activity_id", -1);
        Api().activityDetail(id).subscribe(new RxSubscribe<ActivityEntityItem>(this, true) {
            @Override
            protected void _onNext(ActivityEntityItem a) {
//                tv1.setText(a.getActivity().getActivityName());
//                tv_price.setText(a.getActivity().getActivityPrice());
//                tv_join.setText(String.format("%s人", a.getActivity().getJoinNum()));

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

//
//        ToastUtils.showToast("报名失败");

//
//        Api().activityDetail(id).subscribe(new RxSubscribe<ActivityEntityItem>(this, true) {
//            @Override
//            protected void _onNext(ActivityEntityItem activityEntityItem) {
//            }
//
//            @Override
//            protected void _onError(String message) {
//
//
//            }
//        });

    }
}
