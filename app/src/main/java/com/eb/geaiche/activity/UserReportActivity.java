package com.eb.geaiche.activity;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eb.geaiche.R;
import com.eb.geaiche.api.RxSubscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class UserReportActivity extends BaseActivity {

    @BindView(R.id.et)
    EditText editText;

    @Override
    protected void init() {

        tv_title.setText("用户反馈");
    }

    @Override
    protected void setUpView() {


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_user_report;
    }

    @OnClick({R.id.tv_button})
    public void onClick(View v){


        if (TextUtils.isEmpty(editText.getText())) {

            Toast.makeText(this, "内容不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        Api().feedbackSave(editText.getText().toString()).subscribe(new RxSubscribe<String>(this, true) {
            @Override
            protected void _onNext(String s) {
                Toast.makeText(UserReportActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(UserReportActivity.this, "反馈失败：" + message, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
