package com.eb.geaiche.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.ProductListAdpter;

import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsEntity;
import com.eb.geaiche.util.ToastUtils;

import java.math.BigDecimal;
import java.util.List;


public class ProductListDialog extends Dialog {

    private Context context;
    private ClickListenerInterface clickListenerInterface;
    List<Goods.GoodsStandard> valueList;

    Goods.GoodsStandard pick_value;

    Goods goods;
    int cont;//数量
    TextView tv_number;
    TextView tv_value;
    TextView tv_price;
    View ib_plus;
    View ib_reduce;
    ProductListAdpter c;

    public interface ClickListenerInterface {

        void doConfirm(Goods.GoodsStandard productValue);

        void doCancel();
    }

    public ProductListDialog(Context context, List<Goods.GoodsStandard> list, Goods goods) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.valueList = list;
        this.goods = goods;

        this.cont = goods.getNum() == 0 ? 1 : goods.getNum();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_product_list, null);
        setContentView(view);


        RecyclerView recyclerView = view.findViewById(R.id.rv);

        ib_reduce = view.findViewById(R.id.ib_reduce);//减号
        ib_plus = view.findViewById(R.id.ib_plus);//加号
        tv_number = view.findViewById(R.id.tv_number);//数量
        tv_value = view.findViewById(R.id.tv_value);//规格值
        tv_price = view.findViewById(R.id.tv_price);//

        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        c = new ProductListAdpter(valueList);
        recyclerView.setAdapter(c);


        c.setOnItemClickListener((adapter, view1, position) -> setSelect(position));


        tv_confirm.setOnClickListener(new clickListener());
        ib_plus.setOnClickListener(new clickListener());
        ib_reduce.setOnClickListener(new clickListener());

        if (null != goods.getGoodsStandard()) {
            pick_value = goods.getGoodsStandard();
            for (int i = 0; i < valueList.size(); i++) {
                if (valueList.get(i).getId() == pick_value.getId()) {
                    setSelect(i);
                }
            }
        } else {
            setSelect(0);//选中第一项且添加一件商品
        }
    }

    private void setSelect(int position) {
        pick_value = valueList.get(position);
        for (Goods.GoodsStandard c : valueList) {
            c.setSelected(false);
        }
        valueList.get(position).setSelected(true);
        c.notifyDataSetChanged();
        setCont();
    }


    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            if (isNullValue()) {
                ToastUtils.showToast("请选择一个规格！");
                return;
            }
            switch (id) {
                case R.id.tv_confirm:
                    if (cont > 0) {
                        MyApplication.cartUtils.setData(toGood());//一次性添加多个数量
                        clickListenerInterface.doConfirm(pick_value);
                    } else ToastUtils.showToast("请选择数量！");

                    break;
                case R.id.ib_reduce://减
                    if (cont <= 0) return;
                    cont--;
                    setCont();
                    break;

                case R.id.ib_plus://加

                    cont++;
                    setCont();
                    break;
            }
        }
    }

    private void setCont() {
        tv_value.setText(pick_value.getGoodsStandardTitle());
        tv_number.setText(String.valueOf(cont));
        pick_value.setNum(cont);
        setPrice();

    }

    private void setPrice() {


        BigDecimal b = new BigDecimal(pick_value.getGoodsStandardPrice());
        b = b.multiply(new BigDecimal(cont));
        tv_price.setText("￥" + b.toString());

    }

    private boolean isNullValue() {

        if (null != pick_value)
            return false;
        else
            return true;

    }


    private GoodsEntity toGood() {

        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setGoods_id(pick_value.getGoodsId());
        goodsEntity.setId(pick_value.getGoodsId());
        goodsEntity.setName(goods.getGoodsTitle());
        goodsEntity.setGoods_name(goods.getGoodsTitle());
        goodsEntity.setGoodsName(goods.getGoodsTitle());
        goodsEntity.setGoods_sn(goods.getGoodsCode());
        goodsEntity.setType(goods.getType());
        goodsEntity.setProduct_id(pick_value.getId());
        goodsEntity.setNumber(pick_value.getNum());
        goodsEntity.setMarket_price(pick_value.getGoodsStandardPrice());
        goodsEntity.setRetail_price(pick_value.getGoodsStandardPrice());
        goodsEntity.setGoods_specifition_name_value(pick_value.getGoodsStandardTitle());
        goodsEntity.setGoodsStandard(pick_value);

        return goodsEntity;
    }

}