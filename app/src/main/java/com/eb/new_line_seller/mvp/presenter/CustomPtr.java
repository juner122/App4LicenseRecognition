package com.eb.new_line_seller.mvp.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.Brandadapter2;
import com.eb.new_line_seller.adapter.FixPickParts2ItemAdapter;
import com.eb.new_line_seller.adapter.FixPickService2ItemAdapter;
import com.eb.new_line_seller.api.FixService;
import com.eb.new_line_seller.mvp.FixInfoActivity;
import com.eb.new_line_seller.mvp.contacts.CustomContacts;
import com.eb.new_line_seller.mvp.model.CustomMdl;
import com.eb.new_line_seller.util.ToastUtils;
import com.eb.new_line_seller.view.CommonPopupWindow;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.Component;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixParts2item;
import com.juner.mvp.bean.FixPartsListEntity;
import com.juner.mvp.bean.FixService2item;
import com.juner.mvp.bean.FixServiceListEntity;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.ShopProject;

import java.util.ArrayList;
import java.util.List;

import static com.eb.new_line_seller.mvp.presenter.FixInfoPtr.TYPE;
import static com.eb.new_line_seller.mvp.presenter.FixInfoPtr.TYPE_Parts;


public class CustomPtr extends BasePresenter<CustomContacts.CustomUI> implements CustomContacts.CustomPtr {

    CustomContacts.CustomMdl mdl;
    int type;//判断自定义工时0   ， 自定义配件 1

    int pickType;//判断选择一级分类1   ， 二级分类2

    int parent_id1 = -1;//一级分类主键
    int parent_id2 = -1;//二级分类主键

    RecyclerView parentRecyclerView;//pop
    CommonPopupWindow popupWindow;
    FixPickParts2ItemAdapter adapterParts;
    FixPickService2ItemAdapter adapterService;

    public CustomPtr(@NonNull CustomContacts.CustomUI view) {
        super(view);
        mdl = new CustomMdl(view.getSelfActivity());
        type = getIntent().getIntExtra("type", 0);//判断是自定义工时还是自定义配件

        parentRecyclerView = new RecyclerView(getView().getSelfActivity());
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));

        if (type == 0) {

            adapterService = new FixPickService2ItemAdapter(null);
            parentRecyclerView.setAdapter(adapterService);
            adapterService.setOnItemClickListener(listener);
        } else {

            adapterParts = new FixPickParts2ItemAdapter(null);
            parentRecyclerView.setAdapter(adapterParts);
            adapterParts.setOnItemClickListener(listener);
        }

        popupWindow = new CommonPopupWindow.Builder(getView().getSelfActivity())
                .setView(parentRecyclerView)
                .create();

    }


    BaseQuickAdapter.OnItemClickListener listener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            popupWindow.dismiss();
            String name = "";
            int id = 0;
            if (adapter.getData().get(position) instanceof FixParts2item) {
                name = ((FixParts2item) adapter.getData().get(position)).getName();
                id = ((FixParts2item) adapter.getData().get(position)).getId();
            } else if (adapter.getData().get(position) instanceof FixService2item) {
                name = ((FixService2item) adapter.getData().get(position)).getServiceName();
                id = ((FixService2item) adapter.getData().get(position)).getId();
            }


            if (pickType == 1) {
                parent_id1 = id;
                parent_id2 = -1;
                getView().setType1String(name);
                getView().setType2String("选择分类");
            } else {
                parent_id2 = id;
                getView().setType2String(name);
            }


        }
    };


    @Override
    public void changeView() {

        getView().onChangeView(type);
    }

    @Override
    public void confirm(final String dec, final String name, final String price, int number, final boolean isContinue) {

        if (parent_id2 == -1) {
            ToastUtils.showToast("请选择二级分类！");
            return;
        }
        if ("".equals(name)) {
            ToastUtils.showToast("请设置名称！");
            return;
        }
        if ("".equals(price)) {
            ToastUtils.showToast("请设置价格！");
            return;
        }

        RxSubscribe<NullDataEntity> rxSubscribe = new RxSubscribe<NullDataEntity>(getView().getSelfActivity(), true) {
            @Override
            protected void _onNext(NullDataEntity entity) {
                ToastUtils.showToast("添加成功！");
                if (!isContinue) {//确认添加返回详情页面
                    finish();
                    if (type == 0) {
                        List<FixServie> servieList = new ArrayList<>();
                        servieList.add(getFixServie(dec, name, price, entity.getId()));
                        getView().confirm(servieList, type);
                    } else {
                        List<FixParts> fixParts = new ArrayList<>();
                        fixParts.add(getFixParts(dec, name, price, entity.getId()));
                        getView().confirm(fixParts, type);
                    }
                } else {
                    onContinue();
                }
            }


            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("添加失败！" + message);
            }
        };

        if (type == 0) {
            mdl.addShopService(getShopProject(dec, name, price, number), rxSubscribe);
        } else {
            mdl.componentSave(getComponent(dec, name, price, number), rxSubscribe);

        }

    }

    //清空数据
    private void onContinue() {
        parent_id1 = -1;
        parent_id2 = -1;

        getView().onContinue();

    }

    @Override
    public void numberUp(int num) {
        getView().setNumber(++num);

    }

    @Override
    public void numberDown(int num) {
        if (num == 1) return;
        getView().setNumber(--num);
    }

    @Override
    public void pickType1(final View view) {

        pickType = 1;
        if (type == 0) {
            mdl.firstService(new RxSubscribe<List<FixService2item>>(getView().getSelfActivity(), true) {
                @Override
                protected void _onNext(List<FixService2item> listEntities) {
                    adapterService.setNewData(listEntities);
                    popupWindow.showAsDropDown(view, 0, 0);

                }

                @Override
                protected void _onError(String message) {
                    popupWindow.showAsDropDown(view, 0, 0);
                    ToastUtils.showToast(message);
                }
            });
        } else {
            mdl.componentFirstCategory(new RxSubscribe<List<FixParts2item>>(getView().getSelfActivity(), true) {
                @Override
                protected void _onNext(List<FixParts2item> fixParts2items) {
                    adapterParts.setNewData(fixParts2items);
                    popupWindow.showAsDropDown(view, 0, 0);

                }

                @Override
                protected void _onError(String message) {
                    popupWindow.showAsDropDown(view, 0, 0);
                    ToastUtils.showToast(message);
                }
            });

        }


    }

    @Override
    public void pickType2(final View view) {
        pickType = 2;

        if (parent_id1 == -1) {
            ToastUtils.showToast("请先选择一级分类");
            return;
        }

        if (type == 0) {
            mdl.secondService(parent_id1, new RxSubscribe<List<FixService2item>>(getView().getSelfActivity(), true) {
                @Override
                protected void _onNext(List<FixService2item> listEntities) {
                    if (listEntities.size() == 0) {
                        ToastUtils.showToast("没有可选择的分类");
                        return;
                    }
                    adapterService.setNewData(listEntities);
                    popupWindow.showAsDropDown(view, 0, 0);
                }

                @Override
                protected void _onError(String message) {
                    popupWindow.showAsDropDown(view, 0, 0);
                    ToastUtils.showToast(message);
                }
            });
        } else {
            mdl.componentSecondCategory(parent_id1, new RxSubscribe<List<FixParts2item>>(getView().getSelfActivity(), true) {
                @Override
                protected void _onNext(List<FixParts2item> fixParts2items) {
                    if (fixParts2items.size() == 0) {
                        ToastUtils.showToast("没有可选择的分类");
                        return;
                    }

                    adapterParts.setNewData(fixParts2items);
                    popupWindow.showAsDropDown(view, 0, 0);

                }

                @Override
                protected void _onError(String message) {
                    popupWindow.showAsDropDown(view, 0, 0);
                    ToastUtils.showToast(message);
                }
            });

        }

    }

    private ShopProject getShopProject(String dec, String name, String price, int number) {
        ShopProject sp = new ShopProject();
        sp.setName(name);
        sp.setPrice(price);
        sp.setExplain(dec);
        sp.setServiceId(parent_id2);
        return sp;
    }

    private Component getComponent(String dec, String name, String price, int number) {
        Component com = new Component();
        com.setName(name);
        com.setRetailPrice(price);
        com.setGoodsDesc(dec);
        com.setCategoryId(parent_id2);
        return com;
    }


    private FixServie getFixServie(String dec, String name, String price, int id) {
        FixServie fixServie = new FixServie();
        fixServie.setName(name);
        fixServie.setPrice(price);
        fixServie.setExplain(dec);
        fixServie.setServiceId(parent_id2);
        fixServie.setId(id);
        fixServie.setSelected(1);//默认选择中
        return fixServie;
    }

    private FixParts getFixParts(String dec, String name, String price, int id) {
        FixParts fp = new FixParts();
        fp.setGoods_name(name);
        fp.setRetail_price(price);
        fp.setComponent_id(parent_id2);
        fp.setId(id);
        fp.setNumber(1);//数量
        fp.setSelected(1);//默认选择中
        return fp;
    }

}
