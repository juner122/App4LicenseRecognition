package com.eb.geaiche.mvp.presenter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.adapter.FixPickParts2ItemAdapter;
import com.eb.geaiche.adapter.FixPickService2ItemAdapter;
import com.eb.geaiche.mvp.contacts.CustomContacts;
import com.eb.geaiche.mvp.model.CustomMdl;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.CommonPopupWindow;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.Component;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixParts2item;
import com.juner.mvp.bean.FixService2item;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.Project;
import com.juner.mvp.bean.ShopProject;

import java.util.ArrayList;
import java.util.List;


public class CustomPtr extends BasePresenter<CustomContacts.CustomUI> implements CustomContacts.CustomPtr {

    CustomContacts.CustomMdl mdl;
    int type;//判断自定义工时3   ， 自定义配件 4

    int pickType;//判断选择一级分类1   ， 二级分类2

    int parent_id1 = -1;//一级分类主键
    int parent_id2 = -1;//二级分类主键

    RecyclerView parentRecyclerView;//pop
    CommonPopupWindow popupWindow;
    FixPickParts2ItemAdapter adapterParts;
    FixPickService2ItemAdapter adapterService;


    List<FixServie> servieList;//添加工时列表
    List<FixParts> fixParts;//添加配件 列表

    public CustomPtr(@NonNull CustomContacts.CustomUI view) {
        super(view);
        mdl = new CustomMdl(view.getSelfActivity());
        type = getIntent().getIntExtra("type", 0);//判断是自定义工时还是自定义配件

        parentRecyclerView = new RecyclerView(getView().getSelfActivity());
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));

        if (type == Configure.Goods_TYPE_3) {

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
        servieList = new ArrayList<>();
        fixParts = new ArrayList<>();
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
    public void confirm(final String name, final String price, final boolean isContinue) {


        if ("".equals(name)) {
            ToastUtils.showToast("请设置名称！");
            return;
        }
        if ("".equals(price)) {
            ToastUtils.showToast("请设置价格！");
            return;
        }


        mdl.xgxshopgoodsSave(getProject(name, price), new RxSubscribe<String>(getView().getSelfActivity(), true) {
            @Override
            protected void _onNext(String s) {
                ToastUtils.showToast("添加成功！");

                if (!isContinue) {//确认添加返回详情页面
                    finish();
                } else {
                    onContinue();
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("添加失败！" + message);
            }
        });

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
        if (type == Configure.Goods_TYPE_3) {
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

    private Project getProject(String name, String price) {
        Project p = new Project();

        p.setGoodsTitle(name);
        p.setPrice(price);
        p.setType(type);
        return p;
    }

    private ShopProject getShopProject(String dec, String name, String price, int openStatus) {
        ShopProject sp = new ShopProject();
        sp.setName(name);
        sp.setPrice(price);
        sp.setExplain(dec);
        sp.setOpenStatus(openStatus);
        sp.setServiceId(parent_id2);
        return sp;
    }

    private Component getComponent(String dec, String name, String price, int openStatus) {
        Component com = new Component();
        com.setName(name);
        com.setRetailPrice(price);
        com.setGoodsDesc(dec);
        com.setOpenStatus(openStatus);
        com.setCategoryId(parent_id2);
        return com;
    }


    private FixServie getFixServie(ShopProject shopProject) {
        FixServie fixServie = new FixServie();
        fixServie.setName(shopProject.getName());
        fixServie.setPrice(shopProject.getPrice());
        fixServie.setExplain(shopProject.getExplain());
        fixServie.setServiceId(parent_id2);
        fixServie.setId(shopProject.getServiceId());
        fixServie.setSelected(1);//默认选择中
        return fixServie;
    }

    private FixParts getFixParts(Component component) {
        FixParts fp = new FixParts();
        fp.setGoods_name(component.getName());
        fp.setRetail_price(component.getRetailPrice());
        fp.setComponent_id(parent_id2);
        fp.setId(component.getCategoryId());
        fp.setNumber(1);//数量
        fp.setSelected(1);//默认选择中
        return fp;
    }

}
