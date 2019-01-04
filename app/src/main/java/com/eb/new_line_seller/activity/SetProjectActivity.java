package com.eb.new_line_seller.activity;


import android.content.Intent;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.SetProjectAdapter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.bean.GoodsEntity;
import com.eb.new_line_seller.bean.GoodsListEntity;
import com.eb.new_line_seller.bean.NullDataEntity;
import com.eb.new_line_seller.util.ToastUtils;

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


        setProjectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                showSelectType(position);


            }
        });
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


        toActivity(ProductListActivity.class, Configure.setProject, position);


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
            g.setEasy_id(setProjects.get(position).getEasy_id());
            //更新
            Api().shopeasyUpdate(g).subscribe(new RxSubscribe<Integer>(this, true) {
                @Override
                protected void _onNext(Integer n) {
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
