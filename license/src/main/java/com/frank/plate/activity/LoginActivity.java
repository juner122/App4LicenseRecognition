package com.frank.plate.activity;

import android.view.View;
import android.widget.TextView;

import com.frank.plate.R;
import com.frank.plate.api.MySubscriber;
import com.frank.plate.api.SubscribeOnNextListener;
import com.frank.plate.bean.UserInfo;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.btu_get_phone_code)
    protected TextView tv_code;

    @Override
    protected void init() {
        super.hideHeadView();
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_login;
    }


    @OnClick({R.id.but_login, R.id.btu_get_phone_code})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btu_get_phone_code:

                tv_code.setClickable(false);
                final Observable observable = Observable //计时器
                        .interval(0, 1, TimeUnit.SECONDS)
                        .take(61)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                MySubscriber mySubscriber = new MySubscriber<>(this, new SubscribeOnNextListener<UserInfo>() {
                    @Override
                    public void onNext(UserInfo responseBody) {

                        observable.subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {

                                Long l = 59 - aLong;

                                tv_code.setText(l + "");
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {

                            }
                        }, new Action0() {
                            @Override
                            public void call() {
                                tv_code.setText("发送验证码");
                                tv_code.setClickable(true);
                            }
                        });
                    }
                });
                Api().getUserInfo(mySubscriber, "1");

                break;

            case R.id.but_login:
                toActivity(MainActivity.class);
                break;
        }


    }
}
