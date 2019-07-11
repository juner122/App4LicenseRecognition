package com.eb.geaiche.activity;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.RolesAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.CommonPopupWindow;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Roles;
import com.juner.mvp.bean.Technician;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StaffInfoFixActivity extends BaseActivity {

    @BindView(R.id.tv_phone)
    EditText tv_phone;

    @BindView(R.id.tv_name)
    EditText tv_name;

    @BindView(R.id.tv_number)
    EditText tv_number;

    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.tv_eduction)
    EditText tv_eduction;//提成比例

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_staff_info_fix;
    }

    Technician sysUser = new Technician();

    int type;//页面类型  修改，添加

    List<Integer> roleList = new ArrayList<>();

    @Override
    protected void init() {
        type = getIntent().getIntExtra("type", 0);


        if (type == 0) {
            tv_title.setText("修改员工信息");
            setRTitle("权限说明");

            sysUser = getIntent().getParcelableExtra("sysUser");
            tv_phone.setText(sysUser.getMobile());
            tv_name.setText(sysUser.getNickName());
            tv_number.setText(sysUser.getUserSn());
            tv_eduction.setText(sysUser.getPercentage());//提成比例
            roleList = sysUser.getRoleList();
        } else {
            tv_title.setText("添加员工信息");
            setRTitle("权限说明");
        }


    }

    @OnClick({R.id.tv_title_r, R.id.tv_cancel, R.id.tv_enter, R.id.tv1})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_title_r:

                break;
            case R.id.tv_cancel:
                deleteTechnician();

                break;

            case R.id.tv_enter:
                setInfo();


                break;
            case R.id.tv1:
                //选择职位
                //功能按钮

                popupWindow.showAsDropDown(v, -10, 0);

                break;
        }
    }

    RolesAdapter adapter;//弹出框
    CommonPopupWindow popupWindow;

    @Override
    protected void setUpView() {


        View ll = getLayoutInflater().inflate(R.layout.popup3_rv, null);
        RecyclerView rv = ll.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RolesAdapter(null);
        rv.setAdapter(adapter);


        adapter.setOnItemClickListener((a, view, position) -> {
            roleList.clear();
            tv1.setText(adapter.getData().get(position).getRoleName());
            int roleId = adapter.getData().get(position).getRoleId();

            roleList.add(roleId);

            popupWindow.dismiss();
        });

        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(ll)
                .create();
    }

    @Override
    protected void setUpData() {

        Api().queryRoles().subscribe(new RxSubscribe<List<Roles>>(this, true) {
            @Override
            protected void _onNext(List<Roles> roles) {

                adapter.setNewData(roles);
                for (Roles roles1 : roles) {
                    for (int i : roleList) {
                        if (roles1.getRoleId() == i) {
                            tv1.setText(roles1.getRoleName());
                            break;
                        }

                    }
                }

                if (type == 1) {
                    tv1.setText(roles.get(0).getRoleName());
                    roleList.add(roles.get(0).getRoleId());
                }
            }

            @Override
            protected void _onError(String message) {

            }
        });

    }


    private void setInfo() {

        if (TextUtils.isEmpty(tv_name.getText())) {
            ToastUtils.showToast("名字不能为空！");
            return;
        }
        if (TextUtils.isEmpty(tv_phone.getText())) {
            ToastUtils.showToast("手机号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(tv_eduction.getText())) {
            ToastUtils.showToast("提成比例不能为空！");
            return;
        }

        sysUser.setNickName(tv_name.getText().toString());
        sysUser.setMobile(tv_phone.getText().toString());
        sysUser.setUsername(tv_phone.getText().toString());
        sysUser.setUserSn(tv_number.getText().toString());
        sysUser.setPercentage(tv_eduction.getText().toString());
        sysUser.setRoleList(roleList);

        if (type == 0)
            sysuserUpdate();
        else
            sysuserSave();

    }

    private void sysuserUpdate() {
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

    private void sysuserSave() {

        Api().sysuserSave(sysUser).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity entity) {
                ToastUtils.showToast("添加成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("添加失败！" + message);
                finish();
            }
        });

    }


    //删除员工
    private void deleteTechnician() {
        //弹出对话框
        final ConfirmDialogCanlce dialogCanlce = new ConfirmDialogCanlce(StaffInfoFixActivity.this, "是否要删除该员工!");
        dialogCanlce.show();
        dialogCanlce.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                dialogCanlce.dismiss();

                Api().sysuserDelete(sysUser.getUserId()).subscribe(new RxSubscribe<NullDataEntity>(StaffInfoFixActivity.this, true) {
                    @Override
                    protected void _onNext(NullDataEntity nullDataEntity) {
                        ToastUtils.showToast("删除成功!");
                        finish();

                    }

                    @Override
                    protected void _onError(String message) {

                        ToastUtils.showToast("删除失败!" + message);

                    }
                });


            }

            @Override
            public void doCancel() {
                dialogCanlce.dismiss();
            }
        });

    }
}
