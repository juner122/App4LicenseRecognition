package com.eb.geaiche.activity;

import androidx.recyclerview.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.eb.geaiche.R;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

import butterknife.BindView;

public class MeritsDistriListActivity extends BaseActivity {


    @BindView(R.id.rv)
    RecyclerView rv1;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout1;


    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"入库记录", "出库记录"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void init() {


    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_merits_distri_list;
    }
}
