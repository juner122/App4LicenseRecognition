package com.eb.geaiche.stockControl.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.adapter.StockInListAdapter;
import com.eb.geaiche.stockControl.bean.StockGoods;
import com.eb.geaiche.stockControl.bean.StockInOrOut;
import com.eb.geaiche.stockControl.bean.Supplier;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.ConfirmDialogStockOut;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Shop;
import com.juner.mvp.bean.UserEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.geaiche.stockControl.activity.StockControlActivity.StockInDone;
import static com.eb.geaiche.stockControl.activity.StockControlActivity.stockCartUtils;

public class StockInActivity extends BaseActivity {


    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.et_remarks)
    EditText et_remarks;

    @BindView(R.id.all_price)
    TextView all_price;

    @BindView(R.id.enter)
    TextView enter;

    StockInListAdapter adapter;
    UserEntity ue;
    Shop shop;//当前登录的门店信息

    @OnClick({R.id.enter, R.id.tv_back})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.enter:
                stockInEnter();

                break;

            case R.id.tv_back:

                //清空入库商品
                stockCartUtils.deleteAllData();
                finish();
                break;
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case 1:
                    all_price.setText(String.format("￥%s", getPrice()));
                    break;
            }
        }
    };


    @Override
    protected void init() {

        tv_title.setText("采购入库");
//        setRTitle("继续入库");


        tv_time.setText(DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    protected void setUpView() {
        adapter = new StockInListAdapter(null, this, handler);
        adapter.setNewData(generateData(stockCartUtils.getDataFromLocal()));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        all_price.setText(String.format("￥%s", getPrice()));
        adapter.setOnItemChildClickListener((a, view, position) -> {


            MultiItemEntity entity = adapter.getItem(position);
            if (entity instanceof Goods.GoodsStandard) {
                Goods.GoodsStandard gs = (Goods.GoodsStandard) entity;
                int num = gs.getNum();
                if (view.getId() == R.id.add) {
                    gs.setNum(num + 1);
                } else {
                    if (num != 0) {
                        gs.setNum(num - 1);
                    }
                }
                adapter.notifyDataSetChanged();
                all_price.setText(String.format("￥%s", getPrice()));
            }


        });
    }


    @Override
    protected void setUpData() {

        //获取当前登录员工
        Api().getInfo().subscribe(new com.eb.geaiche.api.RxSubscribe<UserEntity>(this, true) {
            @Override
            protected void _onNext(UserEntity u) {
                ue = u;
                tv_name.setText(ue.getMobile());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("员工信息获取失败！" + message);
            }
        });


        //当前门店
        Api().shopInfo().subscribe(new com.eb.geaiche.api.RxSubscribe<Shop>(this, true) {
            @Override
            protected void _onNext(Shop s) {
                shop = s;
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("门店信息获取失败！" + message);
            }
        });


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stock_in;
    }

    //确认入库
    private void stockInEnter() {
        Api().inOrOut(getStock()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                toActivity(StockControlActivity.class, "View_type", StockInDone);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("入库失败！" + message);
            }
        });
    }

    private StockInOrOut getStock() {
        StockInOrOut stock = new StockInOrOut();

        stock.setType("2");////1出库2入库

        stock.setShopId(shop.getShop().getId());
        stock.setUserId(String.valueOf(ue.getUserId()));
        stock.setUserName(String.valueOf(ue.getUsername()));
        stock.setRemarks(et_remarks.getText().toString());
        stock.setTotalPrice(getPrice());
        stock.setStockGoodsList(getStockGoodsList());

        return stock;
    }


    private List<MultiItemEntity> generateData(List<Goods.GoodsStandard> list) {
        List<MultiItemEntity> res = new ArrayList<>();
        if (null == list || list.size() == 0) {
            return res;
        }
        SparseArray gl = new SparseArray();//所有规格中有包含的商品

        for (int i = 0; i < list.size(); i++) {

            Goods.GoodsStandard item_gs = list.get(i);
            int gsId = item_gs.getGoodsId();
            Goods lv0 = (Goods) gl.get(gsId);

            if (null != lv0) {//不等于空
                lv0.addSubItem(item_gs);
            } else {
                lv0 = new Goods();
                lv0.setGoodsTitle(item_gs.getGoodsTitle());
                lv0.setNum(item_gs.getNum());
//                lv0.setNum(1);//默认为1
                lv0.setId(gsId);
                lv0.setGoodsDetailsPojoList(item_gs.getGoodsDetailsPojoList());
                lv0.addSubItem(item_gs);
            }
            gl.put(gsId, lv0);
        }


        for (int i = 0; i < gl.size(); i++) {
            Goods lv0 = (Goods) gl.valueAt(i);

            res.add(lv0);

        }
        return res;
    }

    //计算总价
    public String getPrice() {


        List<MultiItemEntity> data = adapter.getData();

        BigDecimal totalprice = new BigDecimal(0);
        for (MultiItemEntity entity : data) {
            if (entity instanceof Goods.GoodsStandard) {
                Goods.GoodsStandard goodsStandard = (Goods.GoodsStandard) entity;
                BigDecimal num = new BigDecimal(goodsStandard.getNum());
                BigDecimal price = new BigDecimal(null == goodsStandard.getGoodsStandardPrice() ? "0" : goodsStandard.getGoodsStandardPrice());
                totalprice = totalprice.add(num.multiply(price));
            }

        }
        return totalprice.toString();
    }

    //返回入库商品对象列表
    public List<StockGoods> getStockGoodsList() {


        List<MultiItemEntity> data = adapter.getData();
        List<StockGoods> stockGoods = new ArrayList<>();


        for (MultiItemEntity entity : data) {
            if (entity instanceof Goods.GoodsStandard) {
                Goods.GoodsStandard gs = (Goods.GoodsStandard) entity;

                if (gs.getNum() == 0)//数量为0则不提交
                    continue;

                StockGoods sg = new StockGoods();
                sg.setGoodsId(String.valueOf(gs.getGoodsId()));
                sg.setGoodsTitle(gs.getGoodsTitle());
                sg.setStandardId(String.valueOf(gs.getId()));
                sg.setStandardTitle(gs.getGoodsStandardTitle());
                sg.setPrice(gs.getGoodsStandardPrice());//销售价
                sg.setStock(gs.getStock());
                sg.setSupplierId(gs.getSupplierId());
                sg.setSupplierName(gs.getSupplierName());
                sg.setStockPrice(gs.getStockPrice());//入库价

                sg.setNumber(gs.getNum());

                if (null != gs.getGoodsDetailsPojoList() && gs.getGoodsDetailsPojoList().size() > 0)
                    sg.setImage(gs.getGoodsDetailsPojoList().get(0).getImage());


                stockGoods.add(sg);

            }
        }
        return stockGoods;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int id = intent.getIntExtra("id", 0);
        Supplier supplier = intent.getParcelableExtra("supplier");

        List<MultiItemEntity> res = adapter.getData();
        for (int i = 0; i < res.size(); i++) {
            if (adapter.getData().get(i) instanceof Goods.GoodsStandard) {
                if (((Goods.GoodsStandard) adapter.getData().get(i)).getId() == id) {
                    ((Goods.GoodsStandard) adapter.getData().get(i)).setSupplierId(supplier.getId());
                    ((Goods.GoodsStandard) adapter.getData().get(i)).setSupplierName(supplier.getName());
                    adapter.notifyDataSetChanged();
                    return;
                }

            }


        }


    }
}
