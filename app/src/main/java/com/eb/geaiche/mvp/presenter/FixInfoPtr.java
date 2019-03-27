package com.eb.geaiche.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.UserAuthorizeActivity;
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
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixInfoItem;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.List;


public class FixInfoPtr extends BasePresenter<FixInfoContacts.FixInfoUI> implements FixInfoContacts.FixInfoPtr {

    private FixInfoContacts.FixInfoMdl mdl;
    private Activity context;
    private FixInfoServiceItemAdapter adapter_service;//工时配件列表
    private FixInfoPartsItemAdapter adapter_parts;//配件列表


    FixInfoEntity entity;//估价单对象
    String iv_lpv_url = "";//签名图片 七牛云url


    public FixInfoPtr(@NonNull FixInfoContacts.FixInfoUI view) {
        super(view);
        context = view.getSelfActivity();
        mdl = new FixInfoMdl(context);

        adapter_service = new FixInfoServiceItemAdapter(null,R.layout.activity_fix_info_item);
        adapter_parts = new FixInfoPartsItemAdapter(null,R.layout.activity_fix_info_item);
    }

    BaseQuickAdapter.OnItemChildClickListener infoItemAdapter = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
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


                upServiceDataList(entity.getOrderProjectList(), true);
                upPartsDataList(entity.getOrderGoodsList(), true);

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
                getView().createOrderSuccess(0);//生成检修单
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
//            ToastUtils.showToast("确认报价");

            //弹出对话框
            confirmDialog = new ConfirmDialogCanlce(getView().getSelfActivity(), "确认后将不可再修改，请确认该操作已经获得客户授权，是否继续?");
            confirmDialog.show();
            confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    confirmDialog.dismiss();
                    //默认将选择的项目变成已确认
                    for (FixServie fixServie : entity.getOrderProjectList()) {
                        if (fixServie.getSelected() == 1)
                            fixServie.setSelected(2);
                    }
                    for (FixParts fixParts : entity.getOrderGoodsList()) {
                        if (fixParts.getSelected() == 1)
                            fixParts.setSelected(2);
                    }
                    mdl.replaceConfirm(createFixInfoEntity(), new RxSubscribe<NullDataEntity>(context, true) {
                        @Override
                        protected void _onNext(NullDataEntity nullDataEntity) {
                            ToastUtils.showToast("检修单已确认");
                            finish();
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
                    getView().createOrderSuccess(1);//生成订单
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


        List<FixServie> fixServies = intent.getParcelableArrayListExtra(TYPE_Service);
        List<FixParts> fixParts = intent.getParcelableArrayListExtra(TYPE_Parts);
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
        mdl.addGoodsOrProject(addFixInfoEntity(fixServies, fixParts), new RxSubscribe<NullDataEntity>(context, false) {
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

                        getView().getSelfActivity().startActivity(new Intent(context, FixPickServiceActivity.class));
                        break;
                    case 2:
                        getView().getSelfActivity().startActivity(new Intent(context, FixPickPartsActivity.class));
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

        Intent intent = new Intent(getView().getSelfActivity(), UserAuthorizeActivity.class);
        Bundle bundle = new Bundle();
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
        nd.setClicklistener(new NoticeDialog.ClickListenerInterface() {
            @Override
            public void doCancel() {
                // TODO Auto-generated method stub
                nd.dismiss();
            }
        });


    }

    //创建估价单对象
    private FixInfoEntity createFixInfoEntity() {

        entity.setOrderGoodsList(adapter_parts.getData());
        entity.setOrderProjectList(adapter_service.getData());
        entity.setDescribe(getView().getDec());
        entity.setReplaceSignPic(iv_lpv_url); //客户签名
        entity.setReplaceOterPic("");
        countAllPrice();

        return entity;
    }

    //追加项目
    private FixInfoEntity addFixInfoEntity
    (List<FixServie> fixServies, List<FixParts> fixParts) {


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
