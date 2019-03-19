package com.eb.geaiche.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;

import com.eb.geaiche.adapter.MemberPickListAdpter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Member;
import com.juner.mvp.bean.MemberEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PickCarOwnerActivity extends BaseActivity {
    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_pick_car_owner;
    }

    @BindView(R.id.tv_total_price)
    TextView tv_total_price;

    @BindView(R.id.rv)
    RecyclerView rv;
    MemberPickListAdpter adpter;

    List<MemberEntity> list = new ArrayList<>();
    List<MemberEntity> pick_list;


    @OnClick({R.id.but_enter_order})
    public void onClick(View view) {

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("MemberEntity", (ArrayList) pick_list);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void init() {
        tv_title.setText("选择车主");
        pick_list = getIntent().getParcelableArrayListExtra("MemberEntity");
        if (null == pick_list)
            setCarNum(0);
        else
            setCarNum(pick_list.size());

    }

    @Override
    protected void setUpView() {


        adpter = new MemberPickListAdpter(list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adpter);
        getList();


        adpter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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


                setCarNum(pick_list.size());

            }
        });
    }

    private void setCarNum(int num) {
        tv_total_price.setText(String.format("已选择%d位车主", num));
    }

    @Override
    protected void setUpData() {

    }

    private void getList() {


        Api().memberList(1, "").subscribe(new RxSubscribe<Member>(this, true) {
            @Override
            protected void _onNext(Member member) {
                list = member.getMemberList();
                setPick();
                adpter.setNewData(list);

                adpter.setNewData(member.getMemberList());

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
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

}
