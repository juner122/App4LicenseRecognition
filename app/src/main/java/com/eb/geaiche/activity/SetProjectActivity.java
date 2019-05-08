package com.eb.geaiche.activity;


import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.SetProjectAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.GoodsListEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.eb.geaiche.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class SetProjectActivity extends BaseActivity {


    @BindView(R.id.rv)
    RecyclerView rv;
    SetProjectAdapter setProjectAdapter;
    List<GoodsEntity> setProjects = new ArrayList<>();

    @Override
    protected void init() {
        tv_title.setText("设置主推项目");


        setProjectAdapter = new SetProjectAdapter(setProjects);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(setProjectAdapter);

        setProjectAdapter.setOnItemClickListener(
                (adapter, view, position) -> showSelectType(position));
    }

    @Override
    protected void setUpView() {
        Api().shopeasyList().subscribe(new RxSubscribe<GoodsListEntity>(this, true) {
            @Override
            protected void _onNext(GoodsListEntity o) {
                setProjects = new ArrayList<>();
                if (null == o.getGoodsList() || o.getGoodsList().size() == 0) {
                    setProjects.add(new GoodsEntity("商品/套餐项目名称", false));
                    setProjects.add(new GoodsEntity("商品/套餐项目名称", false));
                    setProjects.add(new GoodsEntity("商品/套餐项目名称", false));
                    setProjects.add(new GoodsEntity("商品/套餐项目名称", false));

                } else {
                    if (o.getGoodsList().size() == 1) {
                        setProjects.add(o.getGoodsList().get(0));
                        setProjects.add(new GoodsEntity("商品/套餐项目名称", false));
                        setProjects.add(new GoodsEntity("商品/套餐项目名称", false));
                        setProjects.add(new GoodsEntity("商品/套餐项目名称", false));

                    }
                    if (o.getGoodsList().size() == 2) {

                        setProjects.add(o.getGoodsList().get(0));
                        setProjects.add(o.getGoodsList().get(1));
                        setProjects.add(new GoodsEntity("商品/套餐项目名称", false));
                        setProjects.add(new GoodsEntity("商品/套餐项目名称", false));

                    }
                    if (o.getGoodsList().size() == 3) {
                        setProjects.add(o.getGoodsList().get(0));
                        setProjects.add(o.getGoodsList().get(1));
                        setProjects.add(o.getGoodsList().get(2));
                        setProjects.add(new GoodsEntity("商品/套餐项目名称", false));
                    } else if (o.getGoodsList().size() > 3) {
                        setProjects.add(o.getGoodsList().get(0));
                        setProjects.add(o.getGoodsList().get(1));
                        setProjects.add(o.getGoodsList().get(2));
                        setProjects.add(o.getGoodsList().get(3));
                    }

                }
                setProjectAdapter.setNewData(setProjects);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);

            }
        });


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_set_project;
    }

    public void showSelectType(int position) {


        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra(Configure.setProject, position);
        intent.putExtra(Configure.Goods_TYPE, Configure.Goods_TYPE_3);
        startActivity(intent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final GoodsEntity g = intent.getParcelableExtra(Configure.ORDERINFO);
        final int position = intent.getIntExtra(Configure.setProject, -1);


        if (!setProjects.get(position).isSet()) {
            //增加
            Api().shopeasySave(g).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                @Override
                protected void _onNext(NullDataEntity n) {
                    ToastUtils.showToast("设置成功！");


                    setProjects.remove(position);
                    setProjects.add(position, g);

                    setProjectAdapter.setNewData(setProjects);
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(message);
                }
            });
        } else {
            g.setId(setProjects.get(position).getEasy_id());
            //更新
            Api().shopeasyUpdate(g).subscribe(new RxSubscribe<Integer>(this, true) {
                @Override
                protected void _onNext(Integer n) {
                    if (n == -1) {
                        ToastUtils.showToast("更新失败！");
                        return;
                    }
                    ToastUtils.showToast("更新成功！");

                    setProjects.remove(position);
                    g.setEasy_id(n);
                    setProjects.add(position, g);
                    setProjectAdapter.setNewData(setProjects);
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(message);
                }
            });

        }
    }


}
