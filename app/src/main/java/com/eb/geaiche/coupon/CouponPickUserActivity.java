package com.eb.geaiche.coupon;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

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

    int page = 1;//第一页

    @BindView(R.id.rv)
    RecyclerView rv;

    CouponPickUserAdpter adpter;
    List<MemberEntity> list = new ArrayList<>();
    List<MemberEntity> pick_list;

    @BindView(R.id.et_key)
    EditText et;


    @OnClick({R.id.but_enter, R.id.iv_search})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.but_enter:
                //确认选择
                toActivity(CouponPostActivity.class, pick_list, "Member");

                break;

            case R.id.iv_search:
                if (TextUtils.isEmpty(et.getText())) {
                    ToastUtils.showToast("请输入搜索内容！");
                    return;
                }
                getList(0, et.getText().toString());

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

        getList(0, "");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    private void getList(final int type, String name) {
        if (type == 0)
            page = 1;
        else
            page++;


        Api().memberList(page, name, 200).subscribe(new RxSubscribe<Member>(this, true) {
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
//
//    private void refreshing(List<MemberEntity> ml) {
//        easylayout.refreshComplete();
//        list.clear();
//        list = ml;
//        adpter.setNewData(list);
//
//        if (list.size() < Configure.limit_page)//少于每页个数，不用加载更多
//            easylayotu.setLoadMoreModel(LoadModel.NONE);
//    }
//
//
//    //加载更多
//    private void loadMoreData(List<MemberEntity> ml) {
//        easylayout.loadMoreComplete();
//        if (ml.size() == 0) {
//            ToastUtils.showToast("没有更多了！");
//            easylayout.setLoadMoreModel(LoadModel.NONE);
//            return;
//        }
//        list.addAll(ml);
//        adpter.setNewData(list);
//
//    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_coupon_pick_user;
    }
}
