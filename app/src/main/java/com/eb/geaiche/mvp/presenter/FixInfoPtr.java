package com.eb.geaiche.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.AutographActivity;

import com.eb.geaiche.activity.ProductMealListActivity;
import com.eb.geaiche.adapter.FixInfoPartsItemAdapter;
import com.eb.geaiche.adapter.FixInfoServiceItemAdapter;
import com.eb.geaiche.mvp.FixPickPartsActivity;
import com.eb.geaiche.mvp.FixPickServiceActivity;
import com.eb.geaiche.mvp.contacts.FixInfoContacts;
import com.eb.geaiche.mvp.model.FixInfoMdl;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.ConfirmDialog4;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.eb.geaiche.view.NoticeDialog;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixInfoItem;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.GoodsEntity;
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
    FixInfo fixInfo;//订单信息;
    String iv_lpv_url = "";//签名图片 七牛云url


    public FixInfoPtr(@NonNull FixInfoContacts.FixInfoUI view) {
        super(view);
        context = view.getSelfActivity();
        mdl = new FixInfoMdl(context);

        adapter_service = new FixInfoServiceItemAdapter(null, R.layout.activity_fix_info_item);
        adapter_parts = new FixInfoPartsItemAdapter(null, R.layout.activity_fix_info_item);
    }

    BaseQuickAdapter.OnItemChildClickListener infoItemAdapter = (adapter, view, position) -> {
        boolean item_selected = ((FixInfoItem) (adapter.getData().get(position))).selectde();//获取当前

        switch (view.getId()) {
            case R.id.iv://选择

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

                break;

            case R.id.ll://改变价格
                String price = "";//原价
                int num;//原价
                boolean isChenge;
                if (adapter.getData().get(position) instanceof FixParts) {
                    price = ((FixParts) adapter.getData().get(position)).getRetail_price();
                    num = ((FixParts) adapter.getData().get(position)).getNumber();
                    isChenge = true;
                } else {
                    price = ((FixServie) adapter.getData().get(position)).getPrice();
                    num = ((FixServie) adapter.getData().get(position)).getNumber();
                    isChenge = true;
                }


                final ConfirmDialog4 confirmDialog = new ConfirmDialog4(getView().getSelfActivity(), price, num, isChenge);
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialog4.ClickListenerInterface() {

                    @Override
                    public void doConfirm(String price, int num) {
                        confirmDialog.dismiss();
                        if (adapter.getData().get(position) instanceof FixParts) {

                            ((FixParts) adapter.getData().get(position)).setRetail_price(price);
                            ((FixParts) adapter.getData().get(position)).setNumber(num);
                        } else {
                            ((FixServie) adapter.getData().get(position)).setPrice(price);
                            ((FixServie) adapter.getData().get(position)).setNumber(num);
                        }

                        adapter.notifyDataSetChanged();

                        //更新金额
                        if (adapter.getData().get(position) instanceof FixParts) {
                            getView().setPartsPrice(upDataPartsPrice().toString());
                        } else {
                            getView().setServicePrice(upDataServicePrice().toString());
                        }
                        getView().setAllPrice(String.valueOf(upDataPartsPrice() + upDataServicePrice()));

                    }

                    @Override
                    public void doCancel() {
                        // TODO Auto-generated method stub
                        confirmDialog.dismiss();
                    }
                });
                break;
        }
    };


    @Override
    public void getInfo() {
        mdl.getInfo(getView().getOrderId(), new RxSubscribe<FixInfo>(context, true) {
            @Override
            protected void _onNext(FixInfo info) {

                fixInfo = info;
                entity = fixInfo.getQuotation();
                getView().setInfo(entity);


                upServiceDataList(toServie(getfpType(entity.getOrderGoodsList(), Configure.Goods_TYPE_3)), true);


                upPartsDataList(getfpType(entity.getOrderGoodsList(), Configure.Goods_TYPE_4), true);

                //根据status改变页面
                changeView();


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    @Override
    public FixInfo putInfo() {
        return fixInfo;
    }

    @Override
    public List<FixServie> putServiceData() {
        return adapter_service.getData();
    }

    @Override
    public List<FixParts> putPartsData() {
        return adapter_parts.getData();
    }


    //根据status改变页面
    @Override
    public void changeView() {
        switch (entity.getStatus()) {
            case 0:
            case 1:
                getView().showAddButton();
                getView().setButtonText("生成检修工单");
                adapter_service.setOnItemChildClickListener(infoItemAdapter);
                adapter_parts.setOnItemChildClickListener(infoItemAdapter);
                getView().showSaveButton();

                break;
            case 2:

                adapter_service.setOnItemChildClickListener(infoItemAdapter);
                adapter_parts.setOnItemChildClickListener(infoItemAdapter);

                getView().hideAddButton();
                getView().setButtonText("确认报价");
                getView().showPostFixButton();
                getView().setRTitle();
                getView().hideSaveButton();
                break;
            case 3:
                getView().hideAddButton();
                getView().setButtonText("确认生成订单");
                getView().setRTitle();
                break;
            case 4:
                getView().hideAddButton();
                getView().setButtonText("已出单");
                getView().setRTitle();

                break;

            case -1:
                getView().hideAddButton();
                getView().setButtonText("已取消");
                break;
        }

    }


    //更新工时列表
    public void upServiceDataList(List<FixServie> list, boolean isNewDate) {
        if (null == list) return;
        if (isNewDate)
            adapter_service.setNewData(list);
        else {
            adapter_service.addData(list);
            adapter_service.notifyDataSetChanged();
        }

        adapter_service.setStatus(entity.getStatus());
        getView().setServicePrice(upDataServicePrice().toString());
        getView().setAllPrice(String.valueOf(upDataPartsPrice() + upDataServicePrice()));
    }

    //更新配件列表
    public void upPartsDataList(List<FixParts> list, boolean isNewDate) {
        if (null == list) return;

        if (isNewDate) {
            adapter_parts.setNewData(list);
        } else {
            adapter_parts.addData(list);
            adapter_parts.notifyDataSetChanged();
        }

        adapter_parts.setStatus(entity.getStatus());
        getView().setPartsPrice(upDataPartsPrice().toString());//更新价格
        getView().setAllPrice(String.valueOf(upDataPartsPrice() + upDataServicePrice()));
    }

    @Override
    public void initRecyclerView(RecyclerView rv_service, RecyclerView rv_parts) {
        rv_service.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv_parts.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });

        rv_service.setAdapter(adapter_service);
        rv_parts.setAdapter(adapter_parts);


    }


    ConfirmDialogCanlce confirmDialog;

    @Override
    public void onInform() {
        if (adapter_service.getData().size() == 0 && adapter_parts.getData().size() == 0) {
            ToastUtils.showToast("未选择任何项目，无法生成检修单");
            return;
        }


        final RxSubscribe rxSubscribe = new RxSubscribe<NullDataEntity>(context, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                getView().createOrderSuccess(0, 0);//生成检修单
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        };

        if (entity.getStatus() == 1 || entity.getStatus() == 0) {//重新提交勾选后的各个项目

            //弹出对话框
            confirmDialog = new ConfirmDialogCanlce(getView().getSelfActivity(), "是否确认将该检修工单推送给客户?");
            confirmDialog.show();
            confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    confirmDialog.dismiss();
                    mdl.remakeSelected(createFixInfoEntity(), rxSubscribe);
                }

                @Override
                public void doCancel() {
                    confirmDialog.dismiss();
                }
            });

        } else if (entity.getStatus() == 2) {


            if (createFixInfoEntityConfirm().getOrderGoodsList().size() == 0 && null == createFixInfoEntityConfirm().getDescribe() || "".equals(createFixInfoEntityConfirm().getDescribe())) {
                ToastUtils.showToast("未作任何修改，不能保存退出！");
                return;
            }
            if (null == createFixInfoEntityConfirm().getReplaceSignPic() || "".equals(createFixInfoEntityConfirm().getReplaceSignPic())) {
                ToastUtils.showToast("请上传凭证！");
                return;
            }


            //确认报价
            //弹出对话框
            confirmDialog = new ConfirmDialogCanlce(getView().getSelfActivity(), "确认后将不可再修改，请确认该操作已经获得客户授权，是否继续?");
            confirmDialog.show();
            confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    confirmDialog.dismiss();

                    mdl.replaceConfirm(setS(createFixInfoEntityConfirm()), new RxSubscribe<NullDataEntity>(context, true) {
                        @Override
                        protected void _onNext(NullDataEntity nullDataEntity) {
                            ToastUtils.showToast("检修单已确认");

                            getView().createOrderSuccess(2, 0);//检修单已确认
                        }

                        @Override
                        protected void _onError(String message) {
                            ToastUtils.showToast(message);
                        }
                    });//确认报价
                }

                @Override
                public void doCancel() {
                    confirmDialog.dismiss();
                }
            });
        } else if (entity.getStatus() == 3) {
            mdl.submit(createOrderObj(entity), new RxSubscribe<NullDataEntity>(context, true) {
                @Override
                protected void _onNext(NullDataEntity nullDataEntity) {
                    getView().createOrderSuccess(1, 0);//生成订单
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(message);
                }
            });//生成订单
        } else if (entity.getStatus() == 4) {
            ToastUtils.showToast("已出单");
        } else if (entity.getStatus() == -1) {
            ToastUtils.showToast("已取消");
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
        infoEntity.setPostscript(getView().getDec());
        return infoEntity;


    }


    public Double upDataServicePrice() {
        Double d = 0.00d;
        if (null == adapter_service) return d;

        for (FixServie fixServie : adapter_service.getData()) {
            if (fixServie.selectde())
                d = d + fixServie.getPriceD() * fixServie.getNumber();
        }
        return d;

    }

    public Double upDataPartsPrice() {
        Double d = 0.00d;
        if (null == adapter_parts) return d;

        for (FixParts fixParts : adapter_parts.getData()) {
            if (fixParts.selectde())
                d = d + fixParts.getRetail_priceD() * fixParts.getNumber();
        }

        return d;
    }

    public final static String TYPE_Service = "TYPE_Service";//服务工时页面
    public final static String TYPE_Parts = "TYPE_Parts";//配件页面
    public final static String TYPE = "TYPE";//


    @Override
    public void handleCallback(Intent intent) {


//        List<FixServie> fixServies = intent.getParcelableArrayListExtra(TYPE_Service);
        List<FixServie> fixServies = getFixServies(MyApplication.cartUtils.getServerList());
        List<FixParts> fixParts = getFixParts(MyApplication.cartUtils.getProductList());


        if (null != fixServies) {
            for (int i = 0; i < fixServies.size(); i++) {
                fixServies.get(i).setSelected(1);
            }
        }
        if (null != fixParts) {
            for (int i = 0; i < fixParts.size(); i++) {
                fixParts.get(i).setSelected(1);
            }
        }


        MyApplication.cartUtils.deleteAllData();
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


    }

    private List<FixServie> getFixServies(List<GoodsEntity> pl) {

        List<FixServie> fip = new ArrayList<>();
        for (GoodsEntity ge : pl) {

            FixServie fp = new FixServie();
            fp.setName(ge.getGoodsName());

            fp.setPrice(ge.getRetail_price());
            fp.setMarketPrice(ge.getRetail_price());
            fp.setServiceId(ge.getGoodsId());
            fp.setNumber(ge.getNumber());//数量
            fp.setSelected(1);//默认选择中
            fp.setType(ge.getType());
            fp.setGoods_sn(ge.getGoods_sn());
            fp.setId(ge.getGoodsId());
            fp.setGoods_specifition_ids(String.valueOf(ge.getGoods_specifition_ids()));
            fp.setGoods_specifition_name_value(ge.getGoods_specifition_name_value());
            fip.add(fp);
        }
        return fip;
    }

    private List<FixParts> getFixParts(List<GoodsEntity> pl) {

        List<FixParts> fip = new ArrayList<>();
        for (GoodsEntity ge : pl) {

            FixParts fp = new FixParts();
            fp.setGoods_name(ge.getGoodsName());

            fp.setRetail_price(ge.getRetail_price());
            fp.setGoods_id(ge.getGoodsId());
            fp.setNumber(ge.getNumber());//数量
            fp.setSelected(1);//默认选择中
            fp.setType(ge.getType());
            fp.setGoods_sn(ge.getGoods_sn());
            fp.setId(ge.getGoodsId());
            fp.setGoods_specifition_ids(String.valueOf(ge.getGoods_specifition_ids()));
            fp.setGoods_specifition_name_value(ge.getGoods_specifition_name_value());
            fip.add(fp);
        }
        return fip;
    }

    @Override
    public void toCarInfoActivity() {
        getView().onToCarInfoActivity(entity.getCarId());
    }


    @Override
    public void remakeSave(final int type) {
        //保存退出
        mdl.remakeSave(createFixInfoEntity(), new RxSubscribe<NullDataEntity>(context, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                switch (type) {
                    case 0:
                        finish();
                        break;
                    case 1:
                        Intent intent = new Intent(context, FixPickServiceActivity.class);
                        intent.putExtra(Configure.isShow, 1);
                        getView().getSelfActivity().startActivity(intent);
                        break;
                    case 2:

                        Intent intent2 = new Intent(context, FixPickPartsActivity.class);
                        intent2.putExtra(Configure.isShow, 1);
                        getView().getSelfActivity().startActivity(intent2);


                        break;

                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    @Override
    public void remakeSelected() {
        //弹出对话框
        confirmDialog = new ConfirmDialogCanlce(getView().getSelfActivity(), "请确认该操作已经获得客户授权，是否确认当前操作?");
        confirmDialog.show();
        confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
            @Override
            public void doConfirm() {

                confirmDialog.dismiss();
                //店长跨客户回撤
                mdl.replaceReback(createFixInfoEntity(), new RxSubscribe<NullDataEntity>(context, true) {
                    @Override
                    protected void _onNext(NullDataEntity nullDataEntity) {
                        finish();

                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(message);
                    }
                });
            }

            @Override
            public void doCancel() {
                confirmDialog.dismiss();
            }
        });


    }


    @Override
    public void changeDec() {

        //弹出键盘

    }

    @Override
    public void setlpvUrl(String url) {
        iv_lpv_url = url;
    }


    /**
     * 判断是否是查看凭证，根据检修单状态
     */
    @Override
    public void toAuthorizeActivity() {

        Intent intent = new Intent(getView().getSelfActivity(), AutographActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("class", "UserAuthorize");
        switch (entity.getStatus()) {
            case 2://待确认

                bundle.putInt("type", 0);
                break;
            case 3://已确认
            case 4://已出单
                if (null == entity.getReplaceSignPic() || "".equals(entity.getReplaceSignPic())) {
                    ToastUtils.showToast("该检修单由客户小程序确认，暂无凭证");
                    return;
                }
                bundle.putInt("type", 1);
                bundle.putString("lpv_url", entity.getReplaceSignPic());
                break;
        }
        intent.putExtras(bundle);


        getView().getSelfActivity().startActivity(intent);


    }

    @Override
    public void notice() {

        final NoticeDialog nd = new NoticeDialog(getView().getSelfActivity(), entity.getMobile());
        nd.show();
        nd.setClicklistener(() -> {
            // TODO Auto-generated method stub
            nd.dismiss();
        });


    }

    //创建估价单对象
    private FixInfoEntity createFixInfoEntity() {

        List<FixParts> fixPartsList = new ArrayList<>();
        fixPartsList.addAll(adapter_parts.getData());
        fixPartsList.addAll(toParts(adapter_service.getData()));

        entity.setOrderGoodsList(fixPartsList);


//        entity.setOrderProjectList(adapter_service.getData());
        entity.setDescribe(getView().getDec());
        entity.setReplaceSignPic(iv_lpv_url); //客户签名
        entity.setReplaceOterPic("");
        countAllPrice();

        return entity;
    }

    //创建估价单对象
    private FixInfoEntity createFixInfoEntityConfirm() {

        List<FixParts> fixPartsList = new ArrayList<>();
        fixPartsList.addAll(adapter_parts.getData());
        fixPartsList.addAll(toPartsS2(adapter_service.getData()));

        entity.setOrderGoodsList(fixPartsList);
        entity.setDescribe(getView().getDec());
        entity.setReplaceSignPic(iv_lpv_url); //客户签名
        entity.setReplaceOterPic("");
        countAllPrice();

        return entity;
    }

    //追加项目
    private FixInfoEntity addFixInfoEntity
    (List<FixServie> fixServies, List<FixParts> fixParts) {

        List<FixParts> fixPartsList = new ArrayList<>();
        if (null != fixParts)
            fixPartsList.addAll(fixParts);
        fixPartsList.addAll(toParts(fixServies));

        entity.setOrderGoodsList(fixPartsList);

        countAllPrice();
        return entity;
    }

    private void countAllPrice() {
        Double all = Double.parseDouble(entity.getGoodsPrice());
        entity.setActualPrice(MathUtil.twoDecimal(all.toString()));
    }


    private List<FixParts> toPartsS2(List<FixServie> data) {

        List<FixParts> fixParts = new ArrayList<>();
        if (null == data || data.size() == 0) {
            return fixParts;
        }
        for (FixServie fs : data) {

            FixParts fp = new FixParts();
            fp.setGoods_name(fs.getName());
            fp.setRetail_price(fs.getPrice());
            fp.setGoods_id(fs.getServiceId());
            fp.setNumber(fs.getNumber());//数量
            fp.setSelected(fs.getSelected());//默认选择中
            fp.setType(fs.getType());
            fp.setGoods_sn(fs.getGoods_sn());
            fp.setId(fs.getId());

            fp.setGoods_specifition_name_value(fs.getGoods_specifition_name_value());
            fp.setGoods_specifition_ids(fs.getGoods_specifition_ids());

            fixParts.add(fp);
        }
        return fixParts;
    }

    private List<FixParts> toParts(List<FixServie> data) {

        List<FixParts> fixParts = new ArrayList<>();
        if (null == data || data.size() == 0) {
            return fixParts;
        }
        for (FixServie fs : data) {

            FixParts fp = new FixParts();
            fp.setGoods_name(fs.getName());
            fp.setRetail_price(fs.getPrice());
            fp.setGoods_id(fs.getServiceId());
            fp.setNumber(fs.getNumber());//数量
            fp.setSelected(fs.getSelected());//默认选择中
            fp.setType(fs.getType());
            fp.setGoods_sn(fs.getGoods_sn());
            fp.setId(fs.getId());

            fp.setGoods_specifition_name_value(fs.getGoods_specifition_name_value());
            fp.setGoods_specifition_ids(fs.getGoods_specifition_ids());
            fixParts.add(fp);
        }
        return fixParts;
    }

    private List<FixServie> toServie(List<FixParts> data) {
        List<FixServie> fixServies = new ArrayList<>();

        if (null == data || data.size() == 0) {
            return fixServies;
        }
        for (FixParts fp : data) {
            FixServie fs = new FixServie();
            fs.setName(fp.getGoods_name());
            fs.setPrice(fp.getRetail_price());
            fs.setServiceId(fp.getGoods_id());
            fs.setNumber(fp.getNumber());//数量
            fs.setSelected(fp.getSelected());//默认选择中
            fs.setType(fp.getType());
            fs.setGoods_sn(fp.getGoods_sn());
            fs.setId(fp.getId());

            fs.setGoods_specifition_name_value(fp.getGoods_specifition_name_value());
            fs.setGoods_specifition_ids(fp.getGoods_specifition_ids());
            fixServies.add(fs);
        }
        return fixServies;
    }

    private List<FixParts> getfpType(List<FixParts> parts, int type) {

        List<FixParts> fixParts = new ArrayList<>();
        for (FixParts f : parts) {

            if (f.getType() == type) {
                fixParts.add(f);
            }
        }
        return fixParts;


    }


    //设置选中
    private FixInfoEntity setS(FixInfoEntity entity1) {
        for (int i = 0; i < entity1.getOrderGoodsList().size(); i++) {

            if (entity1.getOrderGoodsList().get(i).getSelected() == 1)
                entity1.getOrderGoodsList().get(i).setSelected(2);
        }
        return entity1;
    }

}
