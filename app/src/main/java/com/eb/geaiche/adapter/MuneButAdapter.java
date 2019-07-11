package com.eb.geaiche.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BillListActivity;
import com.eb.geaiche.activity.CollegeActivity;
import com.eb.geaiche.activity.CourseRecordActivity;
import com.eb.geaiche.activity.MemberManagementActivity;
import com.eb.geaiche.activity.OrderListActivity;
import com.eb.geaiche.activity.ProductListActivity;
import com.eb.geaiche.activity.RecruitActivity;
import com.eb.geaiche.activity.StaffManagementActivity;
import com.eb.geaiche.maneuver.activity.ManeuverActivity;
import com.eb.geaiche.mvp.ActivateCardActivity;
import com.eb.geaiche.mvp.FixInfoListActivity;
import com.eb.geaiche.mvp.FixPickPartsActivity;
import com.eb.geaiche.mvp.FixPickServiceActivity;
import com.eb.geaiche.mvp.MarketingToolsActivity;
import com.eb.geaiche.mvp.MessageMarketingActivity;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.MenuBut;

import java.util.List;

public class MuneButAdapter extends BaseQuickAdapter<MenuBut, BaseViewHolder> {

    Context activity;

    public MuneButAdapter(@Nullable List<MenuBut> data, Context activity) {
        super(R.layout.fragment1_main_button_item, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MenuBut item) {
        helper.setText(R.id.but, item.getName());

        View ll = helper.getView(R.id.ll);
        ImageView icon = helper.getView(R.id.icon);

        ll.setOnClickListener(view -> toActivity(item.getAndroidInfo()));

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
                //自定服务
                Intent iss = new Intent(activity, FixPickServiceActivity.class);
                activity.startActivity(iss);

                break;
            case "order":

                activity.startActivity(new Intent(activity, OrderListActivity.class));

                break;
            case "quotation":
                activity.startActivity(new Intent(activity, FixInfoListActivity.class));

                break;
            case "stat":
                Intent intent2 = new Intent(activity, BillListActivity.class);
                activity.startActivity(intent2);
                break;
            case "shopAd":
                activity.startActivity(new Intent(activity, MarketingToolsActivity.class));

                break;
            case "sms":

                activity.startActivity(new Intent(activity, MessageMarketingActivity.class));
                break;
            case "activity":
                activity.startActivity(new Intent(activity, ManeuverActivity.class));
                break;
            case "online":

                activity.startActivity(new Intent(activity, CollegeActivity.class));
                break;
            case "offline":

                ToastUtils.showToast("无权限");
                break;
            case "job":
                activity.startActivity(new Intent(activity, RecruitActivity.class));

                break;
            case "maket":
                Intent intent = new Intent(activity, ProductListActivity.class);
                activity.startActivity(intent);

                break;

            case "store"://自定商品
                Intent is = new Intent(activity, FixPickPartsActivity.class);
                activity.startActivity(is);
                break;
            case "studyLog":
                activity.startActivity(new Intent(activity, CourseRecordActivity.class));
                break;


            default:

                if (perms.equals("com.eb.geaiche.maneuver.activity.ManeuverActivity")) {//活动管理
                    ToastUtils.showToast("无权限");
                    return;
                }

                try {
                    Class clazz = Class.forName(perms);
                    activity.startActivity(new Intent(activity, clazz));


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    ToastUtils.showToast("类名错误,请联系管理员!");
                }

                break;

        }


    }



}
