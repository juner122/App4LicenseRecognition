package com.eb.geaiche.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;

import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Member;
import com.juner.mvp.bean.MemberEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CouponPickUserActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    CouponPickUserAdpter adpter;
    List<MemberEntity> list = new ArrayList<>();
    List<MemberEntity> pick_list;

    @OnClick({R.id.but_enter})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.but_enter:

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("Member", (ArrayList) pick_list);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;


        }
    }

    @Override
    protected void init() {
        tv_title.setText("选择用户");

        pick_list = getIntent().getParcelableArrayListExtra("Member");
        rv.setLayoutManager(new LinearLayoutManager(this));
        adpter = new CouponPickUserAdpter(list);
        rv.setAdapter(adpter);

        Api().memberList(1, 200).subscribe(new RxSubscribe<Member>(this, true) {
            @Override
            protected void _onNext(Member member) {
                list = member.getMemberList();
                setPick();
                adpter.setNewData(list);


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

        adpter.setOnItemClickListener((adapter, view, position) -> {

            if (null == pick_list) {
                pick_list = new ArrayList<>();
            }
            if (list.get(position).isSelected()) {
                list.get(position).setSelected(false);
                pick_list.remove(list.get(position));
            } else {
                list.get(position).setSelected(true);
                pick_list.add(list.get(position));
            }
            adapter.notifyDataSetChanged();


        });


    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }
    //设置选择的项
    private void setPick() {
        if (null != pick_list && pick_list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                for (int p = 0; p < pick_list.size(); p++) {
                    if (pick_list.get(p).getUserId() == list.get(i).getUserId()) {
                        list.get(i).setSelected(true);
                        pick_list.remove(p);
                        pick_list.add(p, list.get(i));
                    }
                }

            }
        }
    }
    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_coupon_pick_user;
    }
}
