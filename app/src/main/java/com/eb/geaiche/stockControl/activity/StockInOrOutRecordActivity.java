package com.eb.geaiche.stockControl.activity;

import androidx.recyclerview.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

import butterknife.BindView;

public class StockInOrOutRecordActivity extends BaseActivity {


    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.easylayout1)
    EasyRefreshLayout easylayout1;

    @BindView(R.id.rv2)
    RecyclerView rv2;

    @BindView(R.id.easylayout2)
    EasyRefreshLayout easylayout2;


    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"入库记录", "出库记录"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();


    @Override
    protected void init() {
        tv_title.setText("出入库记录");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_member_record;
    }
}
