package com.eb.geaiche.mall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.MallActivity;
import com.eb.geaiche.activity.MallGoodsActivity;
import com.eb.geaiche.activity.MallGoodsInfoActivity;
import com.eb.geaiche.activity.MallGoodsVinScanActivity;
import com.eb.geaiche.activity.MallMakeOrderActivity;
import com.eb.geaiche.activity.MallTypeActivity;
import com.eb.geaiche.activity.fragment.BaseFragment;
import com.eb.geaiche.adapter.MallMuneButAdapter;
import com.eb.geaiche.adapter.MallTypeGoodsListAdapter;
import com.eb.geaiche.adapter.ShoppingCartListAdapter;
import com.eb.geaiche.mvp.ShoppingCartActivity;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.bean.CartItem;
import com.juner.mvp.bean.CartList;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsList;
import com.juner.mvp.bean.NullDataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购物车
 */

public class MallCartFragment extends BaseFragment {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_price_all)
    TextView tv_price_all;

    @BindView(R.id.iv_pick_all)
    ImageView iv_pick_all;

    boolean isAllPick;
    ShoppingCartListAdapter adapter;

    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.tv_title_r)
    protected TextView tv_title_r;
    @BindView(R.id.tv_back)
    protected View tv_back;

    @OnClick({R.id.iv_pick_all, R.id.tv_total, R.id.tv_title_r})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pick_all:
                if (isAllPick) {
                    isAllPick = false;
                    iv_pick_all.setImageResource(R.drawable.icon_unpick2);
                    tv_price_all.setText("0.00");
                    allPick(isAllPick);
                } else {
                    isAllPick = true;
                    iv_pick_all.setImageResource(R.drawable.icon_pick2);
                    allPick(isAllPick);
                }
                break;

            case R.id.tv_total://结算
                if (getCartItemNum() <= 0) {
                    ToastUtils.showToast("请最少选择一件商品！");
                    return;
                }
                Intent intent = new Intent(getActivity(), MallMakeOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("cart_goods", (ArrayList<? extends Parcelable>) getCartItemList());
                bundle.putInt("buyType", 1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.tv_title_r:


                //弹出对话框
                final ConfirmDialogCanlce confirmDialog = new ConfirmDialogCanlce(getActivity(), "是否要删除选中的商品？", "提示");
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        confirmDialog.dismiss();
                        delete();
                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.dismiss();
                    }
                });


                break;
        }
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    protected void setUpView() {
        tv_back.setVisibility(View.INVISIBLE);
        tv_title.setText("购物车");
        tv_title_r.setText("删除");
        tv_title_r.setVisibility(View.VISIBLE);





        adapter = new ShoppingCartListAdapter(null, getActivity());

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        adapter.setOnItemChildClickListener((a, view, position) -> {


            if (view.getId() == R.id.tv_title) {//查看商品详情

                toActivity(MallGoodsInfoActivity.class, MallGoodsActivity.goodsId, adapter.getData().get(position).getGoods_id());
            } else {
                int num = adapter.getData().get(position).getNumber();//当前数量
                if (view.getId() == R.id.add_btn) {//增加数量
                    num++;
                } else if (view.getId() == R.id.reduce_btn) {//减少数量
                    if (num != 1) {
                        num--;
                    } else
                        ToastUtils.showToast("数量不能少于0！");

                }
                adapter.getData().get(position).setNumber(num);
                adapter.notifyDataSetChanged();
                upDataPrice();

                onUpDateCart(adapter.getData().get(position).getGoods_id(), adapter.getData().get(position).getProduct_id(), num);
            }
        });


        //用于计算总价格
        adapter.setOnItemClickListener((a, view, position) -> {

            List<CartItem> cartItems = new ArrayList<>();//用于计算总价格

            if (adapter.getData().get(position).isSelected()) {
                adapter.getData().get(position).setSelected(false);
                cartItems.remove(adapter.getData().get(position));
            } else {
                adapter.getData().get(position).setSelected(true);
                cartItems.add(adapter.getData().get(position));
            }
            adapter.notifyDataSetChanged();
            upDataPrice();


            boolean is = true;
            //判断是否取消全选
            for (CartItem cartItem : adapter.getData()) {

                if (!cartItem.isSelected()) {
                    is = false;
                    break;
                }
            }
            if (is) {
                iv_pick_all.setImageResource(R.drawable.icon_pick2);
            } else {
                iv_pick_all.setImageResource(R.drawable.icon_unpick2);
            }

            isAllPick = is;

        });


    }

    @Override
    protected void onVisible() {
        super.onVisible();
        getShoppingCartInfo();
    }

    @Override
    protected String setTAG() {
        return "MallMainFragment";
    }


    private void allPick(boolean isAllPick) {
        for (int i = 0; i < adapter.getData().size(); i++) {
            adapter.getData().get(i).setSelected(isAllPick);
        }
        adapter.notifyDataSetChanged();

        upDataPrice();
    }

    public void upDataPrice() {
        if (adapter.getData().size() == 0)

            tv_price_all.setText("0.00");
        else {
            BigDecimal allPrice = new BigDecimal(0);

            for (CartItem cartItem : adapter.getData()) {
                if (cartItem.isSelected()) {
                    BigDecimal price = new BigDecimal(cartItem.getRetail_product_price());
                    BigDecimal num = new BigDecimal(cartItem.getNumber());
                    allPrice = allPrice.add(price.multiply(num));
                }
            }

            tv_price_all.setText(MathUtil.twoDecimal(allPrice.doubleValue()));
        }
    }

    private int getCartItemNum() {
        int num = 0;
        for (CartItem cartItem : getCartItemList()) {
            num = num + cartItem.getNumber();
        }
        return num;
    }

    private List<CartItem> getCartItemList() {
        List<CartItem> cartItems = new ArrayList<>();

        for (CartItem cartItem : adapter.getData()) {
            if (cartItem.isSelected()) {
                cartItems.add(cartItem);
            }
        }

        return cartItems;
    }

    private void delete() {
        if (getCartItemList().size() == 0) {
            ToastUtils.showToast("请选择要删除的商品！");
            return;
        }


        Api().deleteCard(getCartIds()).subscribe(new RxSubscribe<NullDataEntity>(getActivity(), true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                ToastUtils.showToast("删除成功！");
                getShoppingCartInfo();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("删除失败：" + message);
            }
        });


    }

    //获取商品id数组
    private int[] getCartIds() {

        int ids[] = new int[getCartItemList().size()];
        for (int i = 0; i < getCartItemList().size(); i++) {
            ids[i] = getCartItemList().get(i).getId();
        }
        return ids;
    }


    /**
     * 获取购物车信息
     */

    private void getShoppingCartInfo() {
        Api().getShoppingCartInfo().subscribe(new RxSubscribe<CartList>(getActivity(), true) {
            @Override
            protected void _onNext(CartList cartList) {
                adapter.setNewData(cartList.getCartList());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    private void onUpDateCart(Integer goodsId, Integer productId, int number) {
        Api().shoppingCartUpdate(goodsId, productId, number).subscribe(new RxSubscribe<NullDataEntity>(getActivity(), true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("修改失败！" + message);
            }
        });
    }
}
