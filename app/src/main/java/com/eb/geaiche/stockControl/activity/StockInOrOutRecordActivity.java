package com.eb.geaiche.stockControl.activity;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.adapter.StockInOrOutRecordAdapter;
import com.eb.geaiche.stockControl.bean.StockInOrOut;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StockInOrOutRecordActivity extends BaseActivity {


    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.easylayout1)
    EasyRefreshLayout easylayout1;


    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"出库记录", "入库记录"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    StockInOrOutRecordAdapter adapter;

    @Override
    protected void init() {
        tv_title.setText("出入库记录");
    }

    @Override
    protected void setUpView() {
        adapter = new StockInOrOutRecordAdapter(null, this);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {


            Intent intent = new Intent(this, StockInOrOutInfoActivity.class);
            intent.putExtra("id", adapter.getData().get(position).getId());
            intent.putExtra("stockType", adapter.getData().get(position).getType());
            intent.putExtra("phone", adapter.getData().get(position).getUserName());
            startActivity(intent);

        });


        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }


        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                getList(position == 0 ? 1 : 2);//1出库2入库
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    @Override
    protected void setUpData() {

        getList(1);

    }

    private void getList(int type) {
        Api().stockInOrOutRecordList(type).subscribe(new RxSubscribe<List<StockInOrOut>>(this, true) {
            @Override
            protected void _onNext(List<StockInOrOut> stockInOrOuts) {

                adapter.setNewData(stockInOrOuts);
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("记录获取失败！" + message);
            }
        });
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_member_record;
    }
}
