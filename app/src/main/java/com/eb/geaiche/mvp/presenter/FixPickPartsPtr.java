package com.eb.geaiche.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.FixInfoPartsItemAdapter;

import com.eb.geaiche.adapter.FixPickParts2ItemAdapter;
import com.eb.geaiche.mvp.contacts.FixPickPartsContacts;

import com.eb.geaiche.mvp.model.FixPickPartsMdl;

import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.MyRadioButton;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixParts2item;
import com.juner.mvp.bean.FixPartsEntityList;
import com.juner.mvp.bean.FixPartsList;
import com.juner.mvp.bean.FixPartsListEntity;
import com.juner.mvp.bean.FixPartsSeek;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FixPickPartsPtr extends BasePresenter<FixPickPartsContacts.FixPickPartsUI> implements FixPickPartsContacts.FixPickPartsPtr {


    FixPickPartsContacts.FixPickPartsMdl mdl;


    FixPickParts2ItemAdapter adapter_s2;//2级分类
    FixInfoPartsItemAdapter adapter_item;//配件


    Set<FixParts> pick_partsList;//点击配件

    int id;//当前选择分类id

    public FixPickPartsPtr(@NonNull FixPickPartsContacts.FixPickPartsUI view,int layout) {
        super(view);
        mdl = new FixPickPartsMdl(view.getSelfActivity());
        pick_partsList = new HashSet<>();
        adapter_s2 = new FixPickParts2ItemAdapter(null);
        adapter_item = new FixInfoPartsItemAdapter(null,layout);
        adapter_s2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //查找配件 根据id
                id = ((FixParts2item) (adapter.getData().get(position))).getId();
                seekParts();

                getView().showPartsList();
            }
        });

        adapter_item.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (((FixParts) (adapter.getData().get(position))).selectde())//是否已选择
                    ((FixParts) (adapter.getData().get(position))).setSelected(0);
                else
                    ((FixParts) (adapter.getData().get(position))).setSelected(1);
                adapter.notifyDataSetChanged();

                pick_partsList.add((FixParts) (adapter.getData().get(position)));
                countPrice(); //计算选择的总价格
            }
        });


    }


    @Override
    public void onGetData(final RadioGroup rg) {

        mdl.getPartsData(new RxSubscribe<FixPartsList>(getView().getSelfActivity(), true) {
            @Override
            protected void _onNext(FixPartsList list) {

                init0Data(rg, list.getCategoryList());//根据第一级类别数量 创建RadioButton
                set1Data(list.getCategoryList().get(0).getSubCategoryList());//设置第二级类别
                set2Data(toFixPartsList(list.getFirstComponent()));
                getView().showPartsList();//直接显示10个默认配件

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    public void init0Data(RadioGroup rg, final List<FixPartsListEntity> list) {
        rg.removeAllViews();//清除所有
        for (int i = 0; i < list.size(); i++) {
            MyRadioButton radioButton = new MyRadioButton(getView().getSelfActivity(), list.get(i).getName(), i);
            final int finalI = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    set1Data(list.get(finalI).getSubCategoryList());
                    id = list.get(finalI).getId();//一级分类id
                    getView().showParts2List();
                }
            });


            rg.addView(radioButton);
        }


    }

    public void set1Data(List<FixParts2item> list) {

        adapter_s2.setNewData(list);
    }

    public void set2Data(List<FixParts> list) {


        adapter_item.setNewData(list);
    }

    @Override
    public void initRecyclerView(RecyclerView rv_2item, RecyclerView rv_Parts) {
        rv_2item.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));
        rv_Parts.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));

        rv_2item.setAdapter(adapter_s2);
        rv_Parts.setAdapter(adapter_item);
        adapter_item.setEmptyView(R.layout.order_list_empty_view_p, rv_Parts);
        adapter_s2.setEmptyView(R.layout.order_list_empty_view_p, rv_2item);


    }


    @Override
    public void confirm() {
        List<FixParts> fixPartsList = new ArrayList<>();
        for (FixParts fx : pick_partsList) {
            if (fx.selectde())
                fixPartsList.add(fx);
        }


        if (fixPartsList.size() == 0) {
            ToastUtils.showToast("请最少选择一个项目！");
            return;

        }

        getView().onConfirm(fixPartsList);


    }

    public void seekParts() {
        mdl.seekPartsforKey(id, "", new RxSubscribe<FixPartsEntityList>(getView().getSelfActivity(), true) {
            @Override
            protected void _onNext(FixPartsEntityList fixPartsEntityList) {
                set2Data(toFixPartsList(fixPartsEntityList.getProjectList()));
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);

            }
        });

    }

    //搜索
    @Override
    public void seekPartsforKey(String key) {
        if (key.equals("")) {
            ToastUtils.showToast("请输入内容！");
            return;
        }

        mdl.seekPartsforKey(-1, key, new RxSubscribe<FixPartsEntityList>(getView().getSelfActivity(), true) {
            @Override
            protected void _onNext(FixPartsEntityList fixPartsEntityList) {
                getView().showPartsList();
                set2Data(toFixPartsList(fixPartsEntityList.getProjectList()));
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);

            }
        });
    }


    //计算选择的总价格
    private void countPrice() {
        Double allPrice = 0.00d;
        for (FixParts fx : pick_partsList) {
            if (fx.selectde())
                allPrice = fx.getRetail_priceD() * fx.getNumber() + allPrice;
        }

        getView().setPickAllPrice("已选择：￥" + MathUtil.twoDecimal(allPrice));

    }

    private List<FixParts> toFixPartsList(List<FixPartsSeek> fpsl) {

        List<FixParts> list1 = new ArrayList<>();

        for (FixPartsSeek fps : fpsl) {
            FixParts fp = new FixParts();
            fp.setGoods_id(fps.getId());
            fp.setGoods_name(fps.getName());
            fp.setRetail_price(fps.getRetailPrice());
            fp.setNumber(1);
            fp.setSelected(fps.getSelected());
            list1.add(fp);
        }

        return list1;

    }

}
