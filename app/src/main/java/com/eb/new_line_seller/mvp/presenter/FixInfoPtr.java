package com.eb.new_line_seller.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.adapter.FixInfoItemAdapter;
import com.eb.new_line_seller.adapter.FixInfoPartsItemAdapter;
import com.eb.new_line_seller.adapter.FixInfoServiceItemAdapter;
import com.eb.new_line_seller.mvp.contacts.FixInfoContacts;
import com.eb.new_line_seller.mvp.model.FixInfoMdl;
import com.eb.new_line_seller.util.MathUtil;
import com.eb.new_line_seller.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixInfoItem;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.ArrayList;
import java.util.List;


public class FixInfoPtr extends BasePresenter<FixInfoContacts.FixInfoUI> implements FixInfoContacts.FixInfoPtr {

    private FixInfoContacts.FixInfoMdl mdl;
    private Activity context;
    private FixInfoServiceItemAdapter adapter_service;//工时配件列表
    private FixInfoPartsItemAdapter adapter_parts;//配件列表


    FixInfoEntity entity;//估价单对象

    public FixInfoPtr(@NonNull FixInfoContacts.FixInfoUI view) {
        super(view);
        context = view.getSelfActivity();
        mdl = new FixInfoMdl(context);

        adapter_service = new FixInfoServiceItemAdapter(null);
        adapter_parts = new FixInfoPartsItemAdapter(null);
    }

    BaseQuickAdapter.OnItemChildClickListener infoItemAdapter = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            boolean item_selected = ((FixInfoItem) (adapter.getData().get(position))).selectde();//获取当前
            if (item_selected)
                ((FixInfoItem) (adapter.getData().get(position))).setSelected(0);
            else
                ((FixInfoItem) (adapter.getData().get(position))).setSelected(1);
            adapter.notifyDataSetChanged();


            //更新金额
            if (adapter.getData().get(position) instanceof FixParts) {
                getView().setPartsPrice(upDataPartsPrice().toString());
            } else {
                getView().setServicePrice(upDataServicePrice().toString());
            }
            getView().setAllPrice(String.valueOf(upDataPartsPrice() + upDataServicePrice()));

        }
    };


    @Override
    public void getInfo() {
        int id = context.getIntent().getIntExtra("id", -1);
        mdl.getInfo(id, new RxSubscribe<FixInfo>(context, true) {
            @Override
            protected void _onNext(FixInfo fixInfo) {
                entity = fixInfo.getQuotation();
                getView().setInfo(entity);
                upServiceDataList(entity.getOrderProjectList());
                upPartsDataList(entity.getOrderGoodsList());

                //根据status改变页面
                changeView(entity.getStatus());


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }


    //根据status改变页面
    private void changeView(int status) {
        switch (status) {
            case 0:
            case 1:
                getView().showAddButton();
                getView().setButtonText("生成估价单");

                adapter_service.setOnItemChildClickListener(infoItemAdapter);
                adapter_parts.setOnItemChildClickListener(infoItemAdapter);

                break;
            case 2:

                getView().hideAddButton();
                getView().setButtonText("检修单确认中");
                break;
            case 3:
                getView().hideAddButton();
                getView().setButtonText("确认生成订单");
                break;
            case 4:
                getView().hideAddButton();
                getView().setButtonText("已形成订单");
                break;
        }

    }


    @Override
    public void upServiceDataList(List<FixServie> list) {
        adapter_service.setStatus(entity.getStatus());
        adapter_service.setNewData(list);

        getView().setServicePrice(upDataServicePrice().toString());
        getView().setAllPrice(String.valueOf(upDataPartsPrice() + upDataServicePrice()));
    }

    @Override
    public void upPartsDataList(List<FixParts> list) {
        adapter_parts.setStatus(entity.getStatus());
        adapter_parts.setNewData(list);

        getView().setPartsPrice(upDataPartsPrice().toString());//更新价格
        getView().setAllPrice(String.valueOf(upDataPartsPrice() + upDataServicePrice()));
    }

    @Override
    public void initRecyclerView(RecyclerView rv_service, RecyclerView rv_parts) {

        rv_service.setLayoutManager(new LinearLayoutManager(context));
        rv_parts.setLayoutManager(new LinearLayoutManager(context));

        rv_service.setAdapter(adapter_service);
        rv_parts.setAdapter(adapter_parts);


    }


    @Override
    public void onInform() {

        RxSubscribe rxSubscribe = new RxSubscribe<NullDataEntity>(context, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                getView().createOrderSuccess();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        };

        if (entity.getStatus() == 1) {//重新提交勾选后的各个项目
            mdl.remakeSelected(createFixInfoEntity(), rxSubscribe);
        } else if (entity.getStatus() == 0) {
            mdl.inform(createFixInfoEntity(), rxSubscribe);
        } else if (entity.getStatus() == 2) {
            ToastUtils.showToast("检修单确认中");
        } else if (entity.getStatus() == 3) {
            mdl.submit(createOrderObj(entity), rxSubscribe);//生成订单
        } else if (entity.getStatus() == 4) {

            ToastUtils.showToast("已形成订单");
        }


    }


    //创建订单对象
    private OrderInfoEntity createOrderObj(FixInfoEntity entity) {

        OrderInfoEntity infoEntity = new OrderInfoEntity();

        infoEntity.setQuotation_id(entity.getId());
        infoEntity.setUser_id(entity.getUserId());
        infoEntity.setCar_id(entity.getCarId());
        infoEntity.setSysUserList(entity.getSysUserList());
        infoEntity.setConsignee(entity.getUserName());
        infoEntity.setMobile(entity.getMobile());
        infoEntity.setOrder_price(Double.parseDouble(entity.getActualPrice()));
        infoEntity.setCar_no(entity.getCarNo());
        infoEntity.setPostscript(entity.getDescribe());
        return infoEntity;


    }


    public Double upDataServicePrice() {
        Double d = 0.00d;
        if (null == adapter_service) return d;

        for (FixServie fixServie : adapter_service.getData()) {
            if (fixServie.selectde())
                d = d + fixServie.getPriceD();
        }
        return d;

    }

    public Double upDataPartsPrice() {
        Double d = 0.00d;
        if (null == adapter_parts) return d;

        for (FixParts fixParts : adapter_parts.getData()) {
            if (fixParts.selectde())
                d = d + fixParts.getRetail_priceD();
        }

        return d;
    }

    public final static String TYPE_Service = "TYPE_Service";//服务工时页面
    public final static String TYPE_Parts = "TYPE_Parts";//配件页面
    public final static String TYPE = "TYPE";//


    @Override
    public void handleCallback(Intent intent) {
        List<FixServie> fixServies = new ArrayList<>();
        List<FixParts> fixParts = new ArrayList<>();
        String s = intent.getStringExtra(TYPE);
        try {
            fixServies = intent.getParcelableArrayListExtra(TYPE_Service);
            fixParts = intent.getParcelableArrayListExtra(TYPE_Parts);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entity.getStatus() == 1) {//追加项目
                mdl.addGoodsOrProject(addFixInfoEntity(fixServies, fixParts), new RxSubscribe<NullDataEntity>(context, true) {
                    @Override
                    protected void _onNext(NullDataEntity nullDataEntity) {
                        ToastUtils.showToast("追加项目成功");
                        getInfo();//重新加载
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast("追加项目失败：" + message);
                    }
                });
            } else {

                if (s.equals(TYPE_Parts))
                    upPartsDataList(fixParts);
                else
                    upServiceDataList(fixServies);

            }

        }

    }

    //创建估价单对象
    private FixInfoEntity createFixInfoEntity() {

        entity.setOrderGoodsList(adapter_parts.getData());
        entity.setOrderProjectList(adapter_service.getData());
        countAllPrice();

        return entity;
    }

    //追加项目
    private FixInfoEntity addFixInfoEntity(List<FixServie> fixServies, List<FixParts> fixParts) {
        entity.setOrderGoodsList(fixParts);
        entity.setOrderProjectList(fixServies);
        countAllPrice();
        return entity;
    }

    private void countAllPrice() {
        Double all = Double.parseDouble(entity.getGoodsPrice()) + Double.parseDouble(entity.getServePrice());
        entity.setActualPrice(MathUtil.twoDecimal(all.toString()));
    }

}