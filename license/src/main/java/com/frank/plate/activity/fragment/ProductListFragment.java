package com.frank.plate.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.MyApplication;
import com.frank.plate.R;

import com.frank.plate.activity.ProductListActivity;
import com.frank.plate.activity.ProductMealListActivity;
import com.frank.plate.activity.SetProjectActivity;
import com.frank.plate.adapter.ProductListAdapter;
import com.frank.plate.bean.GoodsEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.frank.plate.Configure.goodName;
import static com.frank.plate.Configure.setProject;
import static com.frank.plate.Configure.valueId;

public class ProductListFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ProductListAdapter productListAdapter;
    List<GoodsEntity> list = new ArrayList<>();
    String category_id, brand_id;//种类id,品牌id

    int isShow = 0;

    public static ProductListFragment getInstance(int show) {
        ProductListFragment sf = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("isShow", show);

        //fragment保存参数，传入一个Bundle对象
        sf.setArguments(bundle);
        return sf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //取出保存的值
            isShow = getArguments().getInt("isShow", -1);
        }

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_product_list_fr;
    }


    @Override
    protected void setUpView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productListAdapter = new ProductListAdapter(this, null, isShow);
        recyclerView.setAdapter(productListAdapter);
        productListAdapter.setEmptyView(R.layout.order_list_empty_view_p, recyclerView);


        if (isShow == 1) {
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

                                MyApplication.cartUtils.addProductData(list.get(position));

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


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
        } else if (ProductListActivity.setProject != -1) {


            productListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    GoodsEntity g = (GoodsEntity) adapter.getData().get(position);
                    Intent intent = new Intent(getContext(), SetProjectActivity.class);
                    intent.putExtra(valueId, g.getId());
                    intent.putExtra(setProject, ProductListActivity.setProject);
                    intent.putExtra(goodName, g.getName());
                    startActivity(intent);


                }
            });


        }


    }

    @Override
    protected void msgManagement(double what) {


        ((ProductMealListActivity) getActivity()).onPulsTotalPrice(what);
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
