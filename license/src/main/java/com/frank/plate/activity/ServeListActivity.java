package com.frank.plate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.Configure;
import com.frank.plate.MyApplication;
import com.frank.plate.R;


import com.frank.plate.adapter.ServeListAdapter;
import com.frank.plate.api.RxSubscribe;


import com.frank.plate.bean.Server;
import com.frank.plate.bean.ServerList;

import com.frank.plate.util.MathUtil;
import com.frank.plate.util.ToastUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ServeListActivity extends BaseActivity {


    @BindView(R.id.et_key)
    EditText et_key;


    @BindView(R.id.tv_totalPrice)
    TextView tv_totalPrice;

    @BindView(R.id.ll)
    View ll;
    @BindView(R.id.ll_search)
    View ll_search;
    @BindView(R.id.rv)
    RecyclerView recyclerView;

    ServeListAdapter serveListAdapter;
    List<Server> servers = new ArrayList<>();//所有数据



    @Override
    protected void init() {
        ll_search.setVisibility(View.GONE);



        //计算总价
        count();

        tv_title.setText("服务工时列表");

        serveListAdapter = new ServeListAdapter(servers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(serveListAdapter);


        Api().goodsServeList().subscribe(new RxSubscribe<ServerList>(this, true) {
            @Override
            protected void _onNext(ServerList o) {

                servers = o.getList();
                for (Server ps : MyApplication.cartServerUtils.getServerList()) {
                    for (int i = 0; i < servers.size(); i++) {
                        if (ps.getId() == servers.get(i).getId()) {
                            servers.get(i).setSelected(true);
                        }
                    }
                }

                serveListAdapter.setNewData(servers);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
        serveListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                if (servers.get(position).isSelected()) {
                    servers.get(position).setSelected(false);
                    MyApplication.cartServerUtils.reduceData(servers.get(position));
                } else {
                    servers.get(position).setSelected(true);
                    MyApplication.cartServerUtils.addData(servers.get(position));
                }

                //计算总价
                count();
                adapter.notifyDataSetChanged();


            }
        });


    }

    @OnClick({R.id.but_enter_order, R.id.iv_search})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_enter_order:

                Log.i("TAG", "选择的服务有" + MyApplication.cartServerUtils.getServerList().size() + "个  " + MyApplication.cartServerUtils.getServerList().toString());

                finish();


                break;
            case R.id.iv_search:

                if (!TextUtils.isEmpty(et_key.getText()))
                    onQueryAnyGoods("", et_key.getText().toString());
                else

                    ToastUtils.showToast("请输入搜索关键字！");

                break;
        }

    }


    private void count() {
        tv_totalPrice.setText(String.format("合计：￥%s", MathUtil.twoDecimal(MyApplication.cartServerUtils.getServerPrice())));    //计算总价格

    }

    @Override
    protected void setUpView() {
    }


    private void onQueryAnyGoods(final String category_id, String name) {


    }


    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_server_list;
    }


}
