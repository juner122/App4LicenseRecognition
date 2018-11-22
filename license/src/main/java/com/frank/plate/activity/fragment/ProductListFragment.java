package com.frank.plate.activity.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frank.plate.R;
import com.frank.plate.adapter.ProductListAdapter;
import com.frank.plate.bean.ProductListItemEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProductListFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<ProductListItemEntity> list, list2;
    ProductListAdapter productListAdapter;




    public static ProductListFragment getInstance() {
        ProductListFragment sf = new ProductListFragment();
        return sf;
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_product_list_fr;
    }


    @Override
    protected void setUpView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        list = new ArrayList<>();
        list2 = new ArrayList<>();
        list.add(new ProductListItemEntity("倍耐力P7防爆轮胎/……", "", "225 235 245 255 275/40 45 50 55 R17 18 19 20", "$500"));
        list.add(new ProductListItemEntity("倍耐力P7防爆轮胎/……", "", "225 235 245 255 275/40 45 50 55 R17 18 19 20", "$500"));
        list.add(new ProductListItemEntity("倍耐力P7防爆轮胎/……", "", "225 235 245 255 275/40 45 50 55 R17 18 19 20", "$500"));
        list.add(new ProductListItemEntity("倍耐力P7防爆轮胎/……", "", "225 235 245 255 275/40 45 50 55 R17 18 19 20", "$500"));
        list.add(new ProductListItemEntity("倍耐力P7防爆轮胎/……", "", "225 235 245 255 275/40 45 50 55 R17 18 19 20", "$500"));
        list.add(new ProductListItemEntity("倍耐力P7防爆轮胎/……", "", "225 235 245 255 275/40 45 50 55 R17 18 19 20", "$500"));
        list2 = new ArrayList<>();
        list2.add(new ProductListItemEntity("!!!!!!倍耐力P7防爆轮胎/……", "", "225 235 245 255 275/40 45 50 55 R17 18 19 20", "$500"));
        list2.add(new ProductListItemEntity("!!!!!!倍耐力P7防爆轮胎/……", "", "225 235 245 255 275/40 45 50 55 R17 18 19 20", "$500"));
        list2.add(new ProductListItemEntity("!!!!!!倍耐力P7防爆轮胎/……", "", "225 235 245 255 275/40 45 50 55 R17 18 19 20", "$500"));
        list2.add(new ProductListItemEntity("!!!!!!倍耐力P7防爆轮胎/……", "", "225 235 245 255 275/40 45 50 55 R17 18 19 20", "$500"));
        list2.add(new ProductListItemEntity("!!!!!!倍耐力P7防爆轮胎/……", "", "225 235 245 255 275/40 45 50 55 R17 18 19 20", "$500"));
        productListAdapter = new ProductListAdapter(list);
        recyclerView.setAdapter(productListAdapter);







    }





    public void switchData(int index) {

        if (index == 2) productListAdapter.setNewData(list2);

        if (index == 1) productListAdapter.setNewData(list);


    }


}
