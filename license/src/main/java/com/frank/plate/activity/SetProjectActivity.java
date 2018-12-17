package com.frank.plate.activity;


import android.content.Intent;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.adapter.SetProjectAdapter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.SetProject;
import com.frank.plate.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class SetProjectActivity extends BaseActivity {



    @BindView(R.id.rv)
    RecyclerView rv;
    SetProjectAdapter setProjectAdapter;
    List<SetProject> setProjects = new ArrayList<>();

    @Override
    protected void init() {
        tv_title.setText("设置主推项目");

        setProjects.add(new SetProject("商品/套餐项目名称"));
        setProjects.add(new SetProject("商品/套餐项目名称"));
        setProjects.add(new SetProject("商品/套餐项目名称"));
        setProjects.add(new SetProject("商品/套餐项目名称"));


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
            protected void _onNext(GoodsListEntity goodsListEntity) {
                setProjects = new ArrayList<>();
                for (GoodsEntity ge : goodsListEntity.getGoodsList()) {


                    SetProject setProject = new SetProject(ge.getEasy_id(), ge.getId(), ge.getName(), 1);
                    setProjects.add(setProject);

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


        int goodId = intent.getIntExtra(Configure.valueId, -1);
        String goodName = intent.getStringExtra(Configure.goodName);
        int position = intent.getIntExtra(Configure.setProject, -1);


        setProjects.get(position).setName(goodName);
        setProjects.get(position).setValueId(goodId);

        setProjectAdapter.setNewData(setProjects);

        if (setProjects.get(position).getId() == 0) {
            //增加
            Api().shopeasySave(setProjects.get(position)).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                @Override
                protected void _onNext(NullDataEntity nullDataEntity) {
                    ToastUtils.showToast("设置成功！");
                }

                @Override
                protected void _onError(String message) {

                    ToastUtils.showToast(message);
                }
            });
        } else {


            //更新
            Api().shopeasyUpdate(setProjects.get(position)).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                @Override
                protected void _onNext(NullDataEntity nullDataEntity) {
                    ToastUtils.showToast("设置成功！");
                }

                @Override
                protected void _onError(String message) {

                    ToastUtils.showToast(message);
                }
            });

        }
    }
}
