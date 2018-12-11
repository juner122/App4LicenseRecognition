package com.frank.plate.activity;

import android.view.View;
import android.widget.TextView;

import com.frank.plate.R;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.ActivityEntityItem;

import butterknife.BindView;
import butterknife.OnClick;

public class ActivityInfoActivity extends BaseActivity {

    @BindView(R.id.tv_button)
    TextView tv_button;
    int id;

    @Override
    protected void init() {
        tv_title.setText("购胎送洗车");
    }

    @Override
    protected void setUpView() {


        Api().activityDetail(id).subscribe(new RxSubscribe<ActivityEntityItem>(this,true) {
            @Override
            protected void _onNext(ActivityEntityItem activityEntityItem) {

            }

            @Override
            protected void _onError(String message) {

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





    }
}
