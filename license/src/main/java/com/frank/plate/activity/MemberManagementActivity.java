package com.frank.plate.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.adapter.MemberListAdpter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.Member;
import com.frank.plate.bean.MemberEntity;

import butterknife.BindView;

public class MemberManagementActivity extends BaseActivity {

    private static final String TAG = "MemberManagement";
    @BindView(R.id.tv_number1)
    TextView num1;

    @BindView(R.id.tv_number2)
    TextView num2;

    @BindView(R.id.rv)
    RecyclerView rv;

    MemberListAdpter adpter;

    @Override
    protected void init() {
        tv_title.setText("会员管理");

        adpter = new MemberListAdpter(null);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adpter.setEmptyView(R.layout.member_list_empty_view, rv);

        rv.setAdapter(adpter);


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
        memberList();
    }

    private void memberList() {

        Api().memberList().subscribe(new RxSubscribe<Member>(this, true) {
            @Override
            protected void _onNext(Member member) {

                num1.setText(String.valueOf(member.getDayMember()));
                num2.setText(String.valueOf(member.getMonthMember()));
                adpter.setNewData(member.getMemberList());
            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);
            }
        });

    }

}
