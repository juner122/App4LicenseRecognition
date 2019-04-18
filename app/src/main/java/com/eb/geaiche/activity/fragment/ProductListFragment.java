package com.eb.geaiche.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;

import com.eb.geaiche.activity.ProductListActivity;
import com.eb.geaiche.activity.ProductMealListActivity;
import com.eb.geaiche.activity.SetProjectActivity;
import com.eb.geaiche.adapter.ProductListAdapter;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsEntity;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.ProductListDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.juner.mvp.Configure.ORDERINFO;
import static com.juner.mvp.Configure.setProject;

public class ProductListFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ProductListAdapter productListAdapter;

    List<Goods> goodsList = new ArrayList<>();//新商品Goods


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
        productListAdapter = new ProductListAdapter(this, goodsList, isShow);
        recyclerView.setAdapter(productListAdapter);
        productListAdapter.setEmptyView(R.layout.order_list_empty_view_p, recyclerView);


        if (isShow == 1) {
            productListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                    TextView tv_number = (TextView) adapter.getViewByPosition(recyclerView, position, R.id.tv_number);
                    TextView view_value = (TextView) adapter.getViewByPosition(recyclerView, position, R.id.tv_product_value);
                    View ib_reduce = adapter.getViewByPosition(recyclerView, position, R.id.ib_reduce);//减号


                    int number = goodsList.get(position).getNum();//获取
                    switch (view.getId()) {
                        case R.id.ib_plus:
                            if (null == goodsList.get(position).getGoodsStandard() || goodsList.get(position).getGoodsStandard().getId() == 0) {
                                ToastUtils.showToast("请选择规格");
                                return;
                            }
                            number++;
                            if (number > 0) {
                                ib_reduce.setVisibility(View.VISIBLE);//显示减号
                                tv_number.setVisibility(View.VISIBLE);
                            }

                            MyApplication.cartUtils.addProductData(toGoodsEntity(goodsList.get(position)));
                            tv_number.setText(String.valueOf(number));


                            goodsList.get(position).setNum(number);//设置
                            sendMsg(MyApplication.cartUtils.getProductPrice());


                            break;

                        case R.id.ib_reduce:
                            number--;
                            if (number == 0) {
                                ib_reduce.setVisibility(View.INVISIBLE);//隐藏减号
                                tv_number.setVisibility(View.INVISIBLE);
                            }

                            MyApplication.cartUtils.reduceData(toGoodsEntity(goodsList.get(position)));
                            tv_number.setText(String.valueOf(number));


                            goodsList.get(position).setNum(number);//设置
                            sendMsg(MyApplication.cartUtils.getProductPrice());


                            break;

                        case R.id.tv_product_value:

                            getProductValue(view_value, productListAdapter.getData().get(position).getXgxGoodsStandardPojoList(), position, productListAdapter.getData().get(position));
                            break;
                    }
                }
            });
        } else if (ProductListActivity.setProject != -1) {
            productListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Goods g = (Goods) adapter.getData().get(position);
                    Intent intent = new Intent(getContext(), SetProjectActivity.class);
                    intent.putExtra(ORDERINFO, toGoodsEasyEntity(g));
                    intent.putExtra(setProject, ProductListActivity.setProject);
                    startActivity(intent);
                }
            });


        }





    }

    //获取规格列表
    private void getProductValue(final TextView view, final List<Goods.GoodsStandard> goodsStandards, final int positions, Goods goods) {


        final ProductListDialog confirmDialog = new ProductListDialog(getContext(), goodsStandards, goods);
        confirmDialog.show();
        confirmDialog.setClicklistener(new ProductListDialog.ClickListenerInterface() {
            @Override
            public void doConfirm(Goods.GoodsStandard pick_value) {
                confirmDialog.dismiss();
                view.setText(pick_value.getGoodsStandardTitle());

                goodsList.get(positions).setGoodsStandard(pick_value);
                goodsList.get(positions).setNum(pick_value.getNum());
                productListAdapter.setNewData(goodsList);
                //按确认才保存
                MyApplication.cartUtils.commit();
                sendMsg(MyApplication.cartUtils.getProductPrice());

            }

            @Override
            public void doCancel() {
                confirmDialog.dismiss();
            }
        });
    }


    @Override
    protected void msgManagement(double what) {


        ((ProductMealListActivity) getActivity()).onPulsTotalPrice(what);
    }

    public void switchData(String category_id, String brand_id, List<GoodsEntity> l) {
        this.category_id = category_id;
        this.brand_id = brand_id;
//        this.list = l;
//        productListAdapter.setNewData(list);
    }

    public void switchData(List<Goods> list) {

        this.goodsList = list;
        productListAdapter.setNewData(list);
    }

    public static final String TAG = "ProductListFragment";

    @Override
    protected String setTAG() {
        return TAG;
    }


    private GoodsEntity toGoodsEntity(Goods goods) {
        GoodsEntity goodsEntity = new GoodsEntity();

        goodsEntity.setGoodsId(goods.getId());
        goodsEntity.setId(goods.getId());
        goodsEntity.setName(goods.getGoodsTitle());
        goodsEntity.setGoods_name(goods.getGoodsTitle());
        goodsEntity.setGoodsName(goods.getGoodsTitle());
        goodsEntity.setGoods_sn(goods.getGoodsCode());
        goodsEntity.setGoodsSn(goods.getGoodsCode());
        goodsEntity.setType(goods.getType());
        goodsEntity.setProduct_id(goods.getGoodsStandard().getId());
        goodsEntity.setNumber(goods.getNum());
        goodsEntity.setMarket_price(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setRetail_price(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setMarketPrice(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setRetailPrice(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setGoods_specifition_name_value(goods.getGoodsStandard().getGoodsStandardTitle());
        goodsEntity.setFirstCategoryId(goods.getFirstCategoryId());
        return goodsEntity;
    }


    //设置快捷商品
    private GoodsEntity toGoodsEasyEntity(Goods goods) {
        GoodsEntity goodsEntity = new GoodsEntity();

        Goods.GoodsStandard goodsStandard = goods.getXgxGoodsStandardPojoList().get(0);


        goodsEntity.setGoodsId(goods.getId());
        goodsEntity.setGoods_id(goods.getId());
        goodsEntity.setId(goods.getId());
        goodsEntity.setName(goods.getGoodsTitle());
        goodsEntity.setGoods_name(goods.getGoodsTitle());
        goodsEntity.setGoodsName(goods.getGoodsTitle());
        goodsEntity.setGoods_sn(goods.getGoodsCode());
        goodsEntity.setGoodsSn(goods.getGoodsCode());
        goodsEntity.setType(goods.getType());
        goodsEntity.setProduct_id(goodsStandard.getId());
//        goodsEntity.setPrimary_pic_url(goods.getpic);//图片
//        goodsEntity.setNumber(goods.getNum());
        goodsEntity.setMarket_price(goodsStandard.getGoodsStandardPrice());
        goodsEntity.setRetail_price(goodsStandard.getGoodsStandardPrice());
        goodsEntity.setMarketPrice(goodsStandard.getGoodsStandardPrice());
        goodsEntity.setRetailPrice(goodsStandard.getGoodsStandardPrice());
//        goodsEntity.setGoods_specifition_name_value(goods.getGoodsStandard().getGoodsStandardTitle());
        goodsEntity.setFirstCategoryId(goods.getFirstCategoryId());
        return goodsEntity;
    }

}
