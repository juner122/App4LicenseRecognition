package com.eb.geaiche.activity;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


import com.eb.geaiche.R;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Technician;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class StaffInfoFixActivity extends BaseActivity {

    @BindView(R.id.tv_phone)
    EditText tv_phone;

    @BindView(R.id.tv_name)
    EditText tv_name;

    @BindView(R.id.tv_number)
    EditText tv_number;

    Technician sysUser;

    @Override
    protected void init() {
        tv_title.setText("修改员工信息");
        setRTitle("权限说明");
    }

    @OnClick({R.id.tv_title_r, R.id.tv_cancel, R.id.tv_enter})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_title_r:

                break;
            case R.id.tv_cancel:
                finish();
                break;

            case R.id.tv_enter:
                fix();
                break;
        }
    }


    @Override
    protected void setUpView() {
        sysUser = getIntent().getParcelableExtra("sysUser");
        tv_phone.setText(sysUser.getMobile());
        tv_name.setText(sysUser.getUsername());

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_staff_info_fix;
    }


    private void fix() {

        if (TextUtils.isEmpty(tv_name.getText())) {
            ToastUtils.showToast("名字不能为空！");
            return;
        }
        if (TextUtils.isEmpty(tv_phone.getText())) {
            ToastUtils.showToast("手机号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(tv_number.getText())) {
            ToastUtils.showToast("工号不能为空！");
            return;
        }

        sysUser.setUsername(tv_name.getText().toString());
        sysUser.setMobile(tv_phone.getText().toString());
        sysUser.setUserSn(tv_number.getText().toString());
        sysUser.setRoleList(new ArrayList<Long>());

        Api().sysuserUpdate(sysUser).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity entity) {

                ToastUtils.showToast("修改成功！");

                finish();
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("修改失败！" + message);

                finish();
            }
        });

    }
}
