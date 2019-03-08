package com.eb.new_line_seller.mvp;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.mvp.contacts.MessageMarketingContacts;
import com.eb.new_line_seller.mvp.presenter.MessageMarketingPtr;
import com.eb.new_line_seller.view.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;

public class MessageMarketingActivity extends BaseActivity<MessageMarketingContacts.MessageMarketingPtr> implements MessageMarketingContacts.MessageMarketingUI {

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_message_marketing;
    }


    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.rv2)
    RecyclerView rv2;
    @BindView(R.id.rv3)
    RecyclerView rv3;


    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"发送短信", "发送记录", "模板设置"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();


    @Override
    protected void init() {
        tv_title.setText("短信营销");


        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showTab(position);

            }

            @Override
            public void onTabReselect(int position) {

            }
        });


    }

    private void showTab(int position) {

        switch (position) {
            case 0:
                ll1.setVisibility(View.VISIBLE);
                rv2.setVisibility(View.GONE);
                rv3.setVisibility(View.GONE);
                break;
            case 1:
                ll1.setVisibility(View.GONE);
                rv2.setVisibility(View.VISIBLE);
                rv3.setVisibility(View.GONE);
                break;

            case 2:
                ll1.setVisibility(View.GONE);
                rv2.setVisibility(View.GONE);
                rv3.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public MessageMarketingContacts.MessageMarketingPtr onBindPresenter() {
        return new MessageMarketingPtr(this);
    }
}
