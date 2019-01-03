package com.eb.new_line_seller.activity;

import android.view.View;
import android.widget.TextView;

import com.eb.new_line_seller.R;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseInfoActivity extends BaseActivity {

    @BindView(R.id.tv_button)
    TextView tv_button;

    @Override
    protected void init() {
        tv_title.setText("");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_course_info;
    }

    @OnClick({R.id.tv_button})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_button:


                toActivity(CourseEnrolmentActivity.class);


                break;
        }

    }
}
