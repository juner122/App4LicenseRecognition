package com.eb.new_line_seller.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.bean.NullDataEntity;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseEnrolmentActivity extends BaseActivity {

    private static final String TAG = "CourseEnrolment";
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;


    @Override
    protected void init() {
        tv_title.setText("课程报名");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {


    }

    @OnClick({R.id.tv_button})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_button:

                if (TextUtils.isEmpty(et1.getText()) || TextUtils.isEmpty(et2.getText())) {
                    Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                Api().coursejoinnameSave(et1.getText().toString(), et2.getText().toString()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                    @Override
                    protected void _onNext(NullDataEntity nullDataEntity) {
                        Toast.makeText(CourseEnrolmentActivity.this, "报名成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void _onError(String message) {

                        Log.e(TAG, message);
                    }
                });
                break;


        }

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_course_enrolment;
    }
}
