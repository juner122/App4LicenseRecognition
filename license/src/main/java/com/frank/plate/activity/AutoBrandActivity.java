package com.frank.plate.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.frank.plate.R;
import com.frank.plate.adapter.AutoBrandAdapter;
import com.frank.plate.adapter.AutoBrandSelectorAdapter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.AutoBrand;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * 汽车品牌列表
 */
public class AutoBrandActivity extends BaseActivity {


    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.lv)
    ListView lv;
    AutoBrandAdapter adapter;
    AutoBrandSelectorAdapter adapter2;

    @Override
    protected void init() {
        setTitle("选择车型");

        rv.setLayoutManager(new LinearLayoutManager(this));

        Api().listByName().subscribe(new RxSubscribe<List<AutoBrand>>(this, true) {
            @Override
            protected void _onNext(List<AutoBrand> autoBrands) {
                Collections.sort(autoBrands);//排序
                adapter = new AutoBrandAdapter(autoBrands);
                adapter2 = new AutoBrandSelectorAdapter(AutoBrandActivity.this, autoBrands);
                lv.setAdapter(adapter2);
            }

            @Override
            protected void _onError(String message) {

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
        return R.layout.activity_auto_brand_list;
    }
}
