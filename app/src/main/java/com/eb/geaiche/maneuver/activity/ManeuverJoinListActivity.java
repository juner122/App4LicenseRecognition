package com.eb.geaiche.maneuver.activity;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.maneuver.adapter.ManeuverJoin2Adapter;
import com.eb.geaiche.util.SystemUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Joiner;
import com.juner.mvp.bean.Shop;

import net.grandcentrix.tray.AppPreferences;

import java.util.List;

import butterknife.BindView;


public class ManeuverJoinListActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;//已参加人活动
    String shop_id;//门店id
    ManeuverJoin2Adapter adapter;

    @Override
    protected void init() {
        tv_title.setText("已报名");


    }

    @Override
    protected void setUpView() {
        adapter = new ManeuverJoin2Adapter(null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener((a, view, position) -> {
            toActivity(ManeuverInfoActivity.class, "id", adapter.getItem(position).getUnityId());
        });
    }

    @Override
    protected void setUpData() {

        Api().shopInfo().subscribe(new RxSubscribe<Shop>(this, false) {
            @Override
            protected void _onNext(Shop shop) {

                shop_id = shop.getShop().getId();
                getList();

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });


    }


    private void getList() {
        Api().joinList(shop_id).subscribe(new RxSubscribe<List<Joiner>>(this, true) {
            @Override
            protected void _onNext(List<Joiner> asks) {
                adapter.setNewData(asks);
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("查询失败！" + message);
            }
        });
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_maneuver_join_list;
    }
}
