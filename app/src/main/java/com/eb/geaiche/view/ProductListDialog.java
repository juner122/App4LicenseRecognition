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
        this.goods= goods;
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


        c.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                setSelect(position);
            }
        });


        tv_confirm.setOnClickListener(new clickListener());
        ib_plus.setOnClickListener(new clickListener());
        ib_reduce.setOnClickListener(new clickListener());

        cont = Integer.parseInt(tv_number.getText().toString());
        setSelect(0);//选中第一项且添加一件商品
        MyApplication.cartUtils.addDataNoCommit(toGood(pick_value));//添加不提交
    }

    private void setSelect(int position) {
        pick_value = valueList.get(position);
        tv_value.setText(pick_value.getGoodsStandardTitle());
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
                    if (cont > 0)
                        clickListenerInterface.doConfirm(pick_value);
                    else ToastUtils.showToast("请选择数量！");

                    break;
                case R.id.ib_reduce://减
                    if (cont <= 0) return;
                    cont--;
                    setCont();
                    MyApplication.cartUtils.reduceDataNoCommit(toGood(pick_value));//添加不提交


                    break;

                case R.id.ib_plus://加

                    cont++;
                    setCont();
                    MyApplication.cartUtils.addDataNoCommit(toGood(pick_value));//添加不提交
                    break;
            }
        }
    }

    private void setCont() {

        tv_number.setText(String.valueOf(cont));
        pick_value.setNum(cont);
        setPrice();

    }

    private void setPrice() {

        double parseDouble = Double.parseDouble(pick_value.getGoodsStandardPrice());
        parseDouble = parseDouble * cont;
        tv_price.setText("￥" + String.valueOf(parseDouble));

    }

    private boolean isNullValue() {

        if (null != pick_value)
            return false;
        else
            return true;

    }


    private GoodsEntity toGood(Goods.GoodsStandard pv) {

        GoodsEntity goodsEntity = new GoodsEntity();

//        goodsEntity.setId(pv.getGoodsId());
//        goodsEntity.setProduct_id(pv.getId());
//        goodsEntity.setGoods_specifition_ids("");
//        goodsEntity.setRetail_price("0");
//        goodsEntity.setMarket_price("0");
//        goodsEntity.setPrimary_pic_url("");
//        goodsEntity.setGoods_specifition_name_value(pv.getGoodsStandardPrice());
//        goodsEntity.setGoods_sn("");
//        goodsEntity.setGoodsName(pv.getGoodsStandardTitle());
//        goodsEntity.setName(pv.getGoodsStandardTitle());
//        goodsEntity.setNumber(pv.getNum());


        goodsEntity.setGoods_id(pv.getGoodsId());
        goodsEntity.setId(pv.getGoodsId());
        goodsEntity.setName(goods.getGoodsTitle());
        goodsEntity.setGoods_name(goods.getGoodsTitle());
        goodsEntity.setGoodsName(goods.getGoodsTitle());
        goodsEntity.setGoods_sn(goods.getGoodsCode());
        goodsEntity.setType(goods.getType());
        goodsEntity.setProduct_id(pv.getId());
        goodsEntity.setNumber(pv.getNum());
        goodsEntity.setMarket_price(pv.getGoodsStandardPrice());
        goodsEntity.setRetail_price(pv.getGoodsStandardPrice());
        goodsEntity.setGoods_specifition_name_value(pv.getGoodsStandardTitle());

        return goodsEntity;
    }

}