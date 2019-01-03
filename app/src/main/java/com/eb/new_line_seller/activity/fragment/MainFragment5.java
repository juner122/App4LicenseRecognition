package com.eb.new_line_seller.activity.fragment;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eb.new_line_seller.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.AboutActivity;
import com.eb.new_line_seller.activity.AuthenActivity;
import com.eb.new_line_seller.activity.LoginActivity;
import com.eb.new_line_seller.activity.MyBalanceActivity;
import com.eb.new_line_seller.activity.SetProjectActivity;
import com.eb.new_line_seller.activity.ShopInfoActivity;
import com.eb.new_line_seller.activity.UserReportActivity;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.bean.Shop;
import com.eb.new_line_seller.util.ToastUtils;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页页面：扫描
 */
public class MainFragment5 extends BaseFragment {
    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.iv_user_pic)
    ImageView iv_user_pic;

    @BindView(R.id.tv_phone_number)
    TextView tv_phone_number;

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment5_main;
    }

    @Override
    protected void setUpView() {
        tv_phone_number.append(new AppPreferences(getContext()).getString(Configure.moblie, ""));
        Api().shopInfo().subscribe(new RxSubscribe<Shop>(getContext(), true) {
            @Override
            protected void _onNext(Shop shop) {
                tv_name.setText(shop.getShop().getShopName());


                Glide.with(getActivity())//门店图片
                        .load(shop.getShop().getImage())
                        .into(iv_user_pic);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
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

                ToastUtils.showToast("已是最新版本！");

                break;
            case R.id.tv_user_report:

                toActivity(UserReportActivity.class);

                break;

            case R.id.tv_out:
                new AppPreferences(getContext()).remove(Configure.Token);
                toActivity(LoginActivity.class);
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
