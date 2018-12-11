package com.frank.plate.activity.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.MyApplication;
import com.frank.plate.R;
import com.frank.plate.activity.ProductListActivity;
import com.frank.plate.adapter.ProductListAdapter;
import com.frank.plate.bean.GoodsEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProductListFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ProductListAdapter productListAdapter;
    List<GoodsEntity> list = new ArrayList<>();
    String category_id, brand_id;//种类id,品牌id

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
        productListAdapter = new ProductListAdapter(this, null);
        recyclerView.setAdapter(productListAdapter);
        productListAdapter.setEmptyView(R.layout.order_list_empty_view_p, recyclerView);

        productListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                try {
                    TextView tv_number = (TextView) adapter.getViewByPosition(recyclerView, position, R.id.tv_number);
                    View ib_reduce = adapter.getViewByPosition(recyclerView, position, R.id.ib_reduce);

                    int number = list.get(position).getNumber();//获取
                    switch (view.getId()) {
                        case R.id.ib_plus:
                            if (number == 0) {
                                assert tv_number != null;
                                tv_number.setVisibility(View.VISIBLE);
                                assert ib_reduce != null;
                                ib_reduce.setVisibility(View.VISIBLE);
                            }
                            number++;

                            tv_number.setText(String.valueOf(number));

                            MyApplication.cartUtils.addData(list.get(position));

                            list.get(position).setNumber(number);//设置

                            sendMsg(Double.parseDouble(list.get(position).getRetail_price()));




                            break;

                        case R.id.ib_reduce:
                            number--;

                            tv_number.setText(String.valueOf(number));


                            if (number == 0) {
                                view.setVisibility(View.INVISIBLE);//隐藏减号
                                tv_number.setVisibility(View.INVISIBLE);
                            }

                            MyApplication.cartUtils.reduceData(list.get(position));


                            list.get(position).setNumber(number);//设置
                            sendMsg(-Double.parseDouble(list.get(position).getRetail_price()));
                            break;
                    }


                    ((ProductListActivity) getActivity()).setListMap(category_id, brand_id, list);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }

    @Override
    protected void msgManagement(double what) {


        ((ProductListActivity) getActivity()).onPulsTotalPrice(what);
    }

    public void switchData(String category_id, String brand_id, List<GoodsEntity> l) {
        this.category_id = category_id;
        this.brand_id = brand_id;
        this.list = l;
        productListAdapter.setNewData(list);
    }

    public static final String TAG = "ProductListFragment";

    @Override
    protected String setTAG() {
        return TAG;
    }

}
