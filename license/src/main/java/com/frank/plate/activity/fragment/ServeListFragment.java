package com.frank.plate.activity.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.MyApplication;
import com.frank.plate.R;

import com.frank.plate.activity.ServeListActivity;
import com.frank.plate.adapter.ProductListAdapter;
import com.frank.plate.adapter.ServeListAdapter;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.view.ConfirmDialog;
import com.frank.plate.view.ConfirmDialog3;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ServeListFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ServeListAdapter serveListAdapter;
    List<GoodsEntity> list = new ArrayList<>();
    String category_id;//种类id,品牌id

    public static ServeListFragment getInstance() {
        ServeListFragment sf = new ServeListFragment();
        return sf;
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_product_list_fr;
    }


    @Override
    protected void setUpView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        serveListAdapter = new ServeListAdapter(this, null);
        recyclerView.setAdapter(serveListAdapter);
        serveListAdapter.setEmptyView(R.layout.order_list_empty_view_p, recyclerView);

        serveListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {



                final ConfirmDialog3 confirmDialog = new ConfirmDialog3(getContext(), list.get(position).getRetail_price());
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialog3.ClickListenerInterface() {
                    @Override
                    public void doConfirm(String price) {
                        confirmDialog.dismiss();

                        GoodsEntity goodsEntity = list.get(position);
                        goodsEntity.setPrice(price);
                        MyApplication.cartUtils.addServieData(goodsEntity);

                        ((ServeListActivity) getActivity()).onPulsTotalPrice();
                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.dismiss();
                    }
                });


            }
        });

    }

    public void switchData(String category_id, List<GoodsEntity> l) {
        this.category_id = category_id;
        this.list = l;
        serveListAdapter.setNewData(list);
    }

    public static final String TAG = "ServeListFragment";

    @Override
    protected String setTAG() {
        return TAG;
    }

}
