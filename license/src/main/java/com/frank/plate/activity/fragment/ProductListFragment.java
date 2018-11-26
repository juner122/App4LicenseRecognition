package com.frank.plate.activity.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frank.plate.R;
import com.frank.plate.adapter.ProductListAdapter;
import com.frank.plate.bean.GoodsEntity;
import java.util.List;

import butterknife.BindView;

public class ProductListFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

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

        productListAdapter = new ProductListAdapter(this,null);
        recyclerView.setAdapter(productListAdapter);


    }


    public void switchData(List<GoodsEntity> list) {

     productListAdapter.setNewData(list);




    }

    public static final String TAG = "ProductListFragment";

    @Override
    protected String setTAG() {
        return TAG;
    }

}
