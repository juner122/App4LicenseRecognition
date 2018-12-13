package com.frank.plate.activity.fragment;


import android.view.View;
import android.widget.TextView;

import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.activity.AboutActivity;
import com.frank.plate.activity.AuthenActivity;
import com.frank.plate.activity.MyBalanceActivity;
import com.frank.plate.activity.SetProjectActivity;
import com.frank.plate.activity.ShopInfoActivity;
import com.frank.plate.activity.UserReportActivity;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.Shop;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页页面：扫描
 */
public class MainFragment5 extends BaseFragment {
    @BindView(R.id.tv_name)
    TextView tv_name;


    @BindView(R.id.tv_phone_number)
    TextView tv_phone_number;

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment5_main;
    }

    @Override
    protected void setUpView() {

        Api().shopInfo().subscribe(new RxSubscribe<Shop>(getContext(), true) {
            @Override
            protected void _onNext(Shop shop) {

                tv_name.setText(shop.getShop().getShopName());
                tv_phone_number.append(shop.getShop().getPhone());
            }

            @Override
            protected void _onError(String message) {

            }
        });

    }

    @OnClick({R.id.tv_my_balance, R.id.rl_to_info, R.id.auth, R.id.project, R.id.about, R.id.updata, R.id.tv_user_report, R.id.tv_out})
    public void onclick(View v) {

        switch (v.getId()) {

            case R.id.tv_my_balance:

                toActivity(MyBalanceActivity.class);

                break;

            case R.id.rl_to_info:

                toActivity(ShopInfoActivity.class);

                break;
            case R.id.auth:

                toActivity(AuthenActivity.class);

                break;
            case R.id.project:

                toActivity(SetProjectActivity.class);

                break;
            case R.id.about:

                toActivity(AboutActivity.class);

                break;
            case R.id.updata:


                break;
            case R.id.tv_user_report:

                toActivity(UserReportActivity.class);

                break;

            case R.id.tv_out:
                new AppPreferences(getContext()).remove(Configure.Token);
                getActivity().finish();
                break;


        }


    }

    public static final String TAG = "MainFragment5";

    @Override
    protected String setTAG() {
        return TAG;
    }
}
