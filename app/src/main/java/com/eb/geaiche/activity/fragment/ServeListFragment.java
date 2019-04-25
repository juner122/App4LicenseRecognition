package com.eb.geaiche.activity.fragment;

import androidx.recyclerview.widget.RecyclerView;
import com.eb.geaiche.R;

import com.eb.geaiche.adapter.ServeListAdapter;
import com.juner.mvp.bean.GoodsEntity;

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
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        serveListAdapter = new ServeListAdapter(this, null);
//        recyclerView.setAdapter(serveListAdapter);
//        serveListAdapter.setEmptyView(R.layout.order_list_empty_view_p, recyclerView);
//
//        serveListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
//
//
//
//                final ConfirmDialog3 confirmDialog = new ConfirmDialog3(getContext(), list.get(position).getRetail_price());
//                confirmDialog.show();
//                confirmDialog.setClicklistener(new ConfirmDialog3.ClickListenerInterface() {
//                    @Override
//                    public void doConfirm(String price) {
//                        confirmDialog.dismiss();
//
//                        GoodsEntity goodsEntity = list.get(position);
//                        goodsEntity.setPrice(price);
//                        MyApplication.cartUtils.addServieData(goodsEntity);
//
//                        ((ServeListActivity) getActivity()).onPulsTotalPrice();
//                    }
//
//                    @Override
//                    public void doCancel() {
//                        confirmDialog.dismiss();
//                    }
//                });
//
//
//            }
//        });

    }



    public static final String TAG = "ServeListFragment";

    @Override
    protected String setTAG() {
        return TAG;
    }

}
