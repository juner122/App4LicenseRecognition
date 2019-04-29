package com.eb.geaiche.mvp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.PickCarOwnerActivity;
import com.eb.geaiche.activity.ResultBack;
import com.eb.geaiche.adapter.Brandadapter2;
import com.eb.geaiche.adapter.MessageModleAdapter2;
import com.eb.geaiche.adapter.QuickTechnicianAdpter;
import com.eb.geaiche.mvp.contacts.MessageMarketingContacts;
import com.eb.geaiche.mvp.presenter.MessageMarketingPtr;
import com.eb.geaiche.util.String2Utils;
import com.eb.geaiche.view.CommonPopupWindow;
import com.eb.geaiche.view.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.bean.MemberEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.tv_list_pick)
    TextView tv_list_pick;
    @BindView(R.id.tv_total_price)
    TextView tv_total_price;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.content)
    TextView content;


    List<MemberEntity> memberEntity;


    @OnClick({R.id.ll_add, R.id.view_pick_templates, R.id.but_enter_order})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_add:
                Intent intent = new Intent(this, PickCarOwnerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("MemberEntity", (ArrayList<? extends Parcelable>) memberEntity);
                intent.putExtras(bundle);
                startActivityForResult(intent, new ResultBack() {
                    @Override
                    public void resultOk(Intent data) {
                        memberEntity = data.getParcelableArrayListExtra("MemberEntity");
                        tv_list_pick.setText(String2Utils.getString2(memberEntity));
                        setSendNum(memberEntity.size());

                    }
                });

                break;

            case R.id.view_pick_templates://弹出模板pop

                getPresenter().showPopUp(view);


                break;

            case R.id.but_enter_order://发送

                getPresenter().sendSms();


                break;

        }


    }


    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"发送短信", "发送记录"};
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


        getPresenter().getModleInfo(rv3);
        getPresenter().getRecordInfo(rv2);


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


    @Override
    public void setTitle(String t) {
        title.setText(t);
    }

    @Override
    public void setContent(String c) {
        content.setText(c);
    }

    @Override
    public List<MemberEntity> getMemberList() {
        return memberEntity;
    }

    @Override
    public void setSendNum(int num) {
        if (num == 0)
            tv_total_price.setText("");
        else
            tv_total_price.setText(String.format("该短信将发送给%s位车主", num));
    }
}
