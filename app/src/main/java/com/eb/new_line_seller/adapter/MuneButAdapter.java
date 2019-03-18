package com.eb.new_line_seller.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.BillListActivity;
import com.eb.new_line_seller.activity.CollegeActivity;
import com.eb.new_line_seller.activity.MemberManagementActivity;
import com.eb.new_line_seller.activity.MyBalanceActivity;
import com.eb.new_line_seller.activity.OrderListActivity;
import com.eb.new_line_seller.activity.ProductListActivity;
import com.eb.new_line_seller.activity.RecruitActivity;
import com.eb.new_line_seller.activity.StaffManagementActivity;
import com.eb.new_line_seller.mvp.ActivateCardActivity;
import com.eb.new_line_seller.mvp.FixInfoListActivity;
import com.eb.new_line_seller.mvp.MarketingToolsActivity;
import com.eb.new_line_seller.mvp.MessageMarketingActivity;
import com.eb.new_line_seller.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.MenuBut;

import java.util.List;

public class MuneButAdapter extends BaseQuickAdapter<MenuBut, BaseViewHolder> {

    Activity activity;

    public MuneButAdapter(@Nullable List<MenuBut> data, Activity activity) {
        super(R.layout.fragment1_main_button_item, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MenuBut item) {
        helper.setText(R.id.but, item.getName());

        View ll = helper.getView(R.id.ll);
        ImageView icon = helper.getView(R.id.icon);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toActivity(item.getPerms());
            }
        });

        Glide.with(activity)
                .load(item.getIcon())
                .into(icon);

    }

    private void toActivity(String perms) {

        switch (perms) {
            case "vip":
                activity.startActivity(new Intent(activity, MemberManagementActivity.class));

                break;
            case "user":
                activity.startActivity(new Intent(activity, StaffManagementActivity.class));
                break;
            case "card":
                activity.startActivity(new Intent(activity, ActivateCardActivity.class));
                break;
            case "service":
                ToastUtils.showToast("开发中");

                break;
            case "order":

                activity.startActivity(new Intent(activity, OrderListActivity.class));

                break;
            case "quotation":
                activity.startActivity(new Intent(activity, FixInfoListActivity.class));

                break;
            case "stat":
                Intent intent2 = new Intent(activity, BillListActivity.class);
                intent2.putExtra("isShowAll", 1);
                activity.startActivity(intent2);
                break;
            case "shopAd":
                activity.startActivity(new Intent(activity, MarketingToolsActivity.class));

                break;
            case "sms":

                activity.startActivity(new Intent(activity, MessageMarketingActivity.class));
                break;
            case "activity":
                ToastUtils.showToast("开发中");
                break;
            case "online":

                activity.startActivity(new Intent(activity, CollegeActivity.class));
                break;
            case "offline":

                ToastUtils.showToast("开发中");
                break;
            case "job":
                activity.startActivity(new Intent(activity, RecruitActivity.class));

                break;
            case "maket":
                Intent intent = new Intent(activity, ProductListActivity.class);
                intent.putExtra(Configure.isShow, 0);
                activity.startActivity(intent);

                break;

            case "store":
                ToastUtils.showToast("开发中");
                break;

        }


    }


    private Drawable getBackground(String perms) {
        Drawable drawable = null;
// 这一步必须要做,否则不会显示.
        switch (perms) {
            case "vip":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu1);
                break;
            case "user":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu2);
                break;
            case "card":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu3);
                break;
            case "service":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu4);
                break;
            case "order":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu5);
                break;
            case "quotation":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu6);
                break;
            case "stat":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu7);
                break;
            case "shopAd":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu8);
                break;
            case "sms":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu9);
                break;
            case "activity":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu10);
                break;
            case "online":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu11);
                break;
            case "offline":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu12);
                break;
            case "job":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu13);
                break;
            case "maket":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu14);
                break;

            case "store":
                drawable = activity.getResources().getDrawable(
                        R.mipmap.icon_home_menu15);
                break;

        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        return drawable;
    }

}
