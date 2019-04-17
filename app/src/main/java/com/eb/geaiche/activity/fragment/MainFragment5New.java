package com.eb.geaiche.activity.fragment;



import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.AboutActivity;

import com.eb.geaiche.activity.ChangeStoreActivity;
import com.eb.geaiche.activity.CourseRecordActivity;
import com.eb.geaiche.activity.MallOrderAllListActivity;
import com.eb.geaiche.activity.MyBalanceActivity;
import com.eb.geaiche.activity.SetProjectActivity;
import com.eb.geaiche.activity.ShopInfoActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.LoginActivity2;
import com.eb.geaiche.mvp.ShoppingCartActivity;
import com.eb.geaiche.util.SystemUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Shop;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;

import static com.juner.mvp.Configure.shop_address;
import static com.juner.mvp.Configure.shop_name;
import static com.juner.mvp.Configure.shop_phone;
import static com.juner.mvp.Configure.shop_user_name;

/**
 * 主页页面：我的
 */
public class MainFragment5New extends BaseFragment {

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.iv_user_pic)
    ImageView iv_user_pic;

    @BindView(R.id.tv_phone_number)
    TextView tv_phone_number;

    @BindView(R.id.tv_change_store)
    TextView tv_change_store;//
    String phone;


    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment5_main_new;
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        phone = new AppPreferences(getContext()).getString(Configure.moblie_s, "");
        tv_phone_number.setText("手机号码：" + phone);
        //超级管理员权限
        if (phone.contains("123456789") || phone.equals("13412513007") || phone.equals("13602830779") || phone.equals("13826241081")) {//老板:13602830779
            tv_change_store.setVisibility(View.VISIBLE);
        } else {
            tv_change_store.setVisibility(View.GONE);
        }

        Api().shopInfo().subscribe(new RxSubscribe<Shop>(getContext(), true) {
            @Override
            protected void _onNext(Shop shop) {
                tv_name.setText(shop.getShop().getShopName());


                new AppPreferences(getContext()).put(shop_name, shop.getShop().getShopName());
                new AppPreferences(getContext()).put(shop_address, shop.getShop().getAddress());
                new AppPreferences(getContext()).put(shop_phone, shop.getShop().getPhone());
                new AppPreferences(getContext()).put(shop_user_name, shop.getShop().getName());


                Glide.with(getActivity())//门店图片
                        .load(shop.getShop().getImage())
                        .into(iv_user_pic);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
                //判断是否是401 token失效
                SystemUtil.isReLogin(message, getActivity());
            }
        });

    }

    @OnClick({R.id.tv_my_balance, R.id.rl_to_info, R.id.project, R.id.tv_out, R.id.tv_change_store, R.id.mystudy, R.id.my_cart, R.id.my_collection, R.id.my_youj, R.id.my_rowse_record, R.id.stay_pay, R.id.stay_send, R.id.stay_collect, R.id.after_sale, R.id.tv_all_order})
    public void onclick(View v) {

        switch (v.getId()) {

            case R.id.tv_my_balance:

                toActivity(MyBalanceActivity.class);

                break;

            case R.id.rl_to_info:

                toActivity(ShopInfoActivity.class);

                break;

            case R.id.project:

                toActivity(SetProjectActivity.class);

                break;
            case R.id.about:

                toActivity(AboutActivity.class);

                break;

            case R.id.tv_out:
                new AppPreferences(getContext()).remove(Configure.Token);
                toActivity(LoginActivity2.class);
                getActivity().finish();
                break;

            case R.id.tv_change_store:

                toActivity(ChangeStoreActivity.class);
                break;
            case R.id.mystudy:
                toActivity(CourseRecordActivity.class);

                break;

            case R.id.tv_all_order:
                toActivity(MallOrderAllListActivity.class);

                break;

            case R.id.my_cart:
                toActivity(ShoppingCartActivity.class);

                break;


        }


    }

    public static final String TAG = "MainFragment5";

    @Override
    protected String setTAG() {
        return TAG;
    }


}
