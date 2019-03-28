package com.eb.geaiche.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MemberListAdpter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.Member;
import com.juner.mvp.bean.MemberEntity;
import com.eb.geaiche.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MemberManagementActivity extends BaseActivity {

    private static final String TAG = "MemberManagement";
    @BindView(R.id.tv_number1)
    TextView num1;

    @BindView(R.id.tv_number2)
    TextView num2;

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    MemberListAdpter adpter;
    List<MemberEntity> list = new ArrayList<>();
    int page = 1;//第一页

    @BindView(R.id.et_key)
    EditText et;


    @OnClick({R.id.iv_search})
    public void onClick(View v) {

        switch (v.getId()) {
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
        tv_title.setText("会员管理");

        adpter = new MemberListAdpter(list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adpter.setEmptyView(R.layout.member_list_empty_view, rv);
        rv.setAdapter(adpter);


        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                getList(1, "");
            }

            @Override
            public void onRefreshing() {

                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                getList(0, "");

            }
        });

        adpter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MemberEntity m = (MemberEntity) adapter.getData().get(position);
                toActivity(MemberManagementInfoActivity.class, Configure.user_id, m.getUserId());
            }
        });
    }

    @Override
    protected void setUpView() {


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_member_management;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList(0, "");
    }

    private void getList(final int type, String name) {
        if (type == 0)
            page = 1;
        else
            page++;


        Api().memberList(page, name).subscribe(new RxSubscribe<Member>(this, true) {
            @Override
            protected void _onNext(Member member) {

                num1.setText(String.valueOf(member.getDayMember()));
                num2.setText(String.valueOf(member.getMonthMember()));

                if (type == 0)
                    refreshing(member.getMemberList());
                else
                    loadMoreData(member.getMemberList());

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    private void refreshing(List<MemberEntity> ml) {
        easylayout.refreshComplete();
        list.clear();
        list = ml;
        adpter.setNewData(list);

        if (list.size() < Configure.limit_page)//少于每页个数，不用加载更多
            easylayout.setLoadMoreModel(LoadModel.NONE);
    }


    //加载更多
    private void loadMoreData(List<MemberEntity> ml) {
        easylayout.loadMoreComplete();
        if (ml.size() == 0) {
            ToastUtils.showToast("没有更多了！");
            easylayout.setLoadMoreModel(LoadModel.NONE);
            return;
        }
        list.addAll(ml);
        adpter.setNewData(list);

    }
}
