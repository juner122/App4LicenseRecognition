package com.eb.geaiche.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.LoginActivity;
import com.eb.geaiche.activity.MemberManagementActivity;
import com.eb.geaiche.activity.MemberManagementInfoActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.NullDataEntity;
import com.umeng.commonsdk.debug.I;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class FixNameDialog extends Dialog implements View.OnClickListener {

    private Activity context;


    EditText phone, code, name_new;
    TextView tv_get_code, tv_info1, tv_done;
    Disposable smsDisposable;

    View c1;

    int user_id;

    int con = 60;

    String phone_s, name_s;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_get_code:
                sendCodeMessage();//获取验证码
                break;
            case R.id.tv_done://下一步
                remakeUserName();
                break;
        }

    }


    public FixNameDialog(Activity context, int user_id, String phone_s, String name_s) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.user_id = user_id;
        this.phone_s = phone_s;
        this.name_s = name_s;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();


    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_fixname, null);
        setContentView(view);

        phone = view.findViewById(R.id.et_phone);

        code = view.findViewById(R.id.et_code);
        name_new = view.findViewById(R.id.et_name_new);
        tv_get_code = view.findViewById(R.id.tv_get_code);
        tv_info1 = view.findViewById(R.id.textView2);

        name_new.setText(name_s);
        phone.setText(phone_s);


        tv_done = view.findViewById(R.id.tv_done);

        c1 = view.findViewById(R.id.cl1);


        tv_get_code.setOnClickListener(this);
        tv_done.setOnClickListener(this);


        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }


    private void sendCodeMessage() {
        if (TextUtils.isEmpty(phone.getText())) {
            tv_info1.setVisibility(View.VISIBLE);
            tv_info1.setText("手机号不能为空！");
            return;
        }

        tv_info1.setVisibility(View.INVISIBLE);



        ((MemberManagementInfoActivity) context).Api().updateUserSms(phone.getText().toString()).subscribe(new RxSubscribe<NullDataEntity>(context, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                tv_info1.setText("验证码已发送,请向车主获取验证码.");
                tv_info1.setVisibility(View.VISIBLE);

                smsDisposable = Observable //计时器
                        .interval(0, 1, TimeUnit.SECONDS)
                        .take(con)//次数
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) {
                                Long l = con - aLong;
                                tv_get_code.setText(l + "s");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                tv_get_code.setClickable(true);
                                throwable.printStackTrace();
                            }
                        }, new Action() {
                            @Override
                            public void run() {

                                tv_get_code.setText("获取手机验证码");
                                tv_get_code.setClickable(true);
                            }
                        }, new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) {
                                tv_get_code.setClickable(false);
                            }
                        });


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);

            }
        });

    }


    private void remakeUserName() {
        if (TextUtils.isEmpty(phone.getText())) {
            tv_info1.setVisibility(View.VISIBLE);
            tv_info1.setText("手机号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(name_new.getText())) {
            tv_info1.setText("用户名不能为空！");
            tv_info1.setVisibility(View.VISIBLE);
            return;
        }
        if (code.getText().toString().length() < 4) {
            tv_info1.setText("请输入正确的验证码！");
            tv_info1.setVisibility(View.VISIBLE);
            return;
        }
        tv_info1.setVisibility(View.INVISIBLE);
        ((MemberManagementInfoActivity) context).Api().remakeUserName(user_id, name_new.getText().toString(), code.getText().toString(), phone.getText().toString()).subscribe(new RxSubscribe<NullDataEntity>(context, true) {
            @Override
            protected void _onNext(NullDataEntity entity) {

                ToastUtils.showToast("修改成功！");
                dismiss();
                context.startActivity(new Intent(context, MemberManagementInfoActivity.class));

            }

            @Override
            protected void _onError(String message) {
                dismiss();
                ToastUtils.showToast(message + "：修改失败！");

            }
        });


    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (null != smsDisposable)
            smsDisposable.dispose();
    }


}