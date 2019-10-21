package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;
import com.eb.geaiche.bean.MyMultipleItem;
import com.eb.geaiche.stockControl.activity.StockInActivity;
import com.eb.geaiche.stockControl.activity.SupplierListActivity;
import com.eb.geaiche.util.ImageUtils;
import com.eb.geaiche.view.ConfirmDialogStockOut;
import com.juner.mvp.bean.Goods;

import java.util.List;


public class StockInListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    Context context;
    Handler handler;

    public StockInListAdapter(@Nullable List<MultiItemEntity> data, Context c, Handler handler) {
        super(data);

        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.activity_stock_in_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.activity_stock_in_item_item);
        this.context = c;
        this.handler = handler;

    }


    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {

        Goods goods = null;
        switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                goods = (Goods) item;

                String goodsTitle = goods.getGoodsTitle();


                helper.setText(R.id.tv_name, goodsTitle);
                if (null == goods.getXgxGoodsStandardPojoList() || goods.getXgxGoodsStandardPojoList().size() == 0)
                    helper.setText(R.id.tv_price, "暂无报价");
                else
                    helper.setText(R.id.tv_price, String.format("￥%s", goods.getXgxGoodsStandardPojoList().get(0).getGoodsStandardPrice()));


                if (null != goods.getGoodsDetailsPojoList() && goods.getGoodsDetailsPojoList().size() > 0) {
                    ImageUtils.load(context, goods.getGoodsDetailsPojoList().get(0).getImage(), helper.getView(R.id.iv_src));
                }

                expandAll();


                break;
            case MyMultipleItem.SECOND_TYPE:
                final Goods.GoodsStandard gs = (Goods.GoodsStandard) item;

                helper.addOnClickListener(R.id.reduce);
                helper.addOnClickListener(R.id.add);


                helper.setText(R.id.name, gs.getGoodsStandardTitle()).setText(R.id.num, String.valueOf(gs.getNum())).setText(R.id.price, String.format("￥%s", gs.getGoodsStandardPrice())).setText(R.id.stock, null == gs.getStock() ? "库存:0" : "库存:" + gs.getStock());

                View ll_item = helper.getView(R.id.ll_item);
                TextView num = helper.getView(R.id.num);
                TextView et_price1 = helper.getView(R.id.et_price1);
                TextView et_price2 = helper.getView(R.id.et_price2);
                TextView tv_business = helper.getView(R.id.tv_business);

                View ll1 = helper.getView(R.id.ll1);

                if (gs.getIsShowItem() == 0) {
                    ll_item.setVisibility(View.GONE);
                } else {
                    ll_item.setVisibility(View.VISIBLE);
                }
                ll1.setOnClickListener(v -> {

                    if (gs.getIsShowItem() == 0)
                        gs.setIsShowItem(1);
                    else
                        gs.setIsShowItem(0);

                    notifyDataSetChanged();

                });

                num.setOnClickListener(v -> {
                    //弹出选择领料数量
                    final ConfirmDialogStockOut dialog = new ConfirmDialogStockOut(context, num.getText().toString());
                    dialog.show();
                    dialog.setClicklistener(new ConfirmDialogStockOut.ClickListenerInterface() {
                        @Override
                        public void doConfirm(String code) {
                            gs.setNum(Integer.valueOf(code));
                            notifyDataSetChanged();
                            dialog.dismiss();
                            Message msg = Message.obtain();
                            msg.what = 1;   //标志消息的标志
                            handler.sendMessage(msg);

                        }

                        @Override
                        public void doCancel() {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });


                });
//                et_price1.setOnClickListener(v -> {
//                    //弹出选择领料数量
//                    final ConfirmDialogStockOut dialog = new ConfirmDialogStockOut(context, et_price1.getText().toString());
//                    dialog.show();
//                    dialog.setClicklistener(new ConfirmDialogStockOut.ClickListenerInterface() {
//                        @Override
//                        public void doConfirm(String code) {
//                            gs.setGoodsStandardPrice(code);
//                            notifyDataSetChanged();
//                            dialog.dismiss();
//                            Message msg = Message.obtain();
//                            msg.what = 1;   //标志消息的标志
//                            handler.sendMessage(msg);
//
//                        }
//
//                        @Override
//                        public void doCancel() {
//                            // TODO Auto-generated method stub
//                            dialog.dismiss();
//                        }
//                    });
//                });
                et_price2.setOnClickListener(v -> {
                    //弹出选择领料数量
                    final ConfirmDialogStockOut dialog = new ConfirmDialogStockOut(context, et_price2.getText().toString());
                    dialog.show();
                    dialog.setClicklistener(new ConfirmDialogStockOut.ClickListenerInterface() {
                        @Override
                        public void doConfirm(String code) {
                            gs.setStockPrice(code);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void doCancel() {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
                });
                tv_business.setOnClickListener(v -> {

                    Intent intent = new Intent(context, SupplierListActivity.class);
                    intent.putExtra("id", gs.getId());
                    intent.putExtra("view_type", 1);
                    context.startActivity(intent);


                });


                helper.setText(R.id.et_price1, gs.getGoodsStandardPrice());
                helper.setText(R.id.et_price2, null==gs.getStockPrice()||gs.getStockPrice().equals("")?"0":gs.getStockPrice());
                helper.setText(R.id.tv_business, gs.getSupplierName());


                break;


        }
    }

}
