package com.eb.geaiche.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.StoreListAdpter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Store;
import com.juner.mvp.bean.Token;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangeStoreActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    StoreListAdpter storeListAdpter;

    @OnClick({R.id.tv_confirm})
    public void onClick(View v) {

        if (null == storeListAdpter.getPickStore()) {
            ToastUtils.showToast("请选择门店！");
            return;
        }
        //登录
        Api().login(storeListAdpter.getPickStore().getPhone(), storeListAdpter.getPickStore().getPwd()).subscribe(new RxSubscribe<Token>(this, true) {
            @Override
            protected void _onNext(Token token) {
                new AppPreferences(ChangeStoreActivity.this).put(Configure.Token, token.getToken().getToken());
                new AppPreferences(ChangeStoreActivity.this).put(Configure.moblie, storeListAdpter.getPickStore().getPhone());
                Toast.makeText(ChangeStoreActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                toActivity(StartActivity.class);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });


    }

    @Override
    protected void init() {
        tv_title.setText("选择登录门店");

        storeListAdpter = new StoreListAdpter(getStoreList());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(storeListAdpter);

        storeListAdpter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                int s = adapter.getData().size();
                for (int i = 0; i < s; i++) {
                    ((Store) adapter.getData().get(i)).setSelected(false);
                }
                ((Store) adapter.getData().get(position)).setSelected(true);

                adapter.notifyDataSetChanged();

            }
        });

    }

    private List<Store> getStoreList() {
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("五羊店", "12345678903", "8888"));
        stores.add(new Store("二沙汇店", "12345678904", "8888"));
        stores.add(new Store("金碧店", "12345678905", "8888"));
        stores.add(new Store("政通店", "12345678901", "8888"));
        stores.add(new Store("岗顶店", "12345678902", "8888"));
        stores.add(new Store("法政店", "12345678906", "8888"));
        stores.add(new Store("童心店", "12345678907", "8888"));
        stores.add(new Store("天河店", "12345678908", "8888"));
        stores.add(new Store("汽车港", "12345678909", "8888"));
        stores.add(new Store("海印又一城店", "12345678910", "8888"));
        stores.add(new Store("临江店", "12345678911", "8888"));
        stores.add(new Store("周门店", "12345678912", "8888"));
        stores.add(new Store("江南西店", "12345678913", "8888"));
        stores.add(new Store("圣丰店", "12345678914", "8888"));
        stores.add(new Store("华南新城店", "12345678915", "8888"));
        stores.add(new Store("顺德碧桂园店", "12345678916", "8888"));
        stores.add(new Store("东华南", "12345678917", "8888"));

        return stores;
    }


    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_change_store;
    }
}
