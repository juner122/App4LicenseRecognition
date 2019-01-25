package com.eb.new_line_seller.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.FixInfoServiceItemAdapter;
import com.eb.new_line_seller.adapter.FixPickService2ItemAdapter;
import com.eb.new_line_seller.mvp.contacts.FixPickServiceContacts;
import com.eb.new_line_seller.mvp.model.FixPickServiceMdl;
import com.eb.new_line_seller.util.MathUtil;
import com.eb.new_line_seller.util.ToastUtils;
import com.eb.new_line_seller.view.MyRadioButton;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.FixParts2item;
import com.juner.mvp.bean.FixService2item;
import com.juner.mvp.bean.FixServiceList;
import com.juner.mvp.bean.FixServiceListEntity;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.FixServieEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FixPickServicePtr extends BasePresenter<FixPickServiceContacts.FixPickServiceUI> implements FixPickServiceContacts.FixPickServicePtr {


    FixPickServiceContacts.FixPickServiceMdl mdl;


    FixPickService2ItemAdapter adapter_s2;//2级分类
    FixInfoServiceItemAdapter adapter_item;//工时服务


    //    List<FixServie> pick_servieList;//点击过工时服务
    Set<FixServie> pick_servieList;//点击过工时服务

    int id = -1;//当前选择分类id

    public FixPickServicePtr(@NonNull FixPickServiceContacts.FixPickServiceUI view) {
        super(view);
        mdl = new FixPickServiceMdl(view.getSelfActivity());
        pick_servieList = new HashSet<>();
        adapter_s2 = new FixPickService2ItemAdapter(null);
        adapter_item = new FixInfoServiceItemAdapter(null);
        adapter_s2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //查找配件 根据id
                id = ((FixService2item) (adapter.getData().get(position))).getId();


                List<FixServie> list = ((FixService2item) (adapter.getData().get(position))).getProjectList();
                if (list.size() == 0) {
                    ToastUtils.showToast("该类别暂无可选服务");
                    return;
                }

                set2Data(list);
                getView().showServiceList();

            }
        });

        adapter_item.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (((FixServie) (adapter.getData().get(position))).selectde())//是否已选择
                    ((FixServie) (adapter.getData().get(position))).setSelected(0);
                else
                    ((FixServie) (adapter.getData().get(position))).setSelected(1);
                adapter.notifyDataSetChanged();

                pick_servieList.add((FixServie) (adapter.getData().get(position)));


                countPrice(); //计算选择的总价格


            }
        });

    }


    @Override
    public void onGetData(final RadioGroup rg) {
        rg.removeAllViews();
        mdl.getServiceData(new RxSubscribe<FixServiceList>(getView().getSelfActivity(), true) {
            @Override
            protected void _onNext(FixServiceList list) {

                init0Data(rg, list.getServiceList());//根据第一级类别数量 创建RadioButton
                set1Data(list.getServiceList().get(0).getSonList());//设置第二级类别

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    public void init0Data(RadioGroup rg, final List<FixServiceListEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            MyRadioButton radioButton = new MyRadioButton(getView().getSelfActivity(), list.get(i).getServiceName(), i);

            final int finalI = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    set1Data(list.get(finalI).getSonList());
                    getView().showService2List();
                }
            });
            rg.addView(radioButton);
        }


    }

    public void set1Data(List<FixService2item> list) {
        adapter_s2.setNewData(list);


    }

    public void set2Data(List<FixServie> list) {


        adapter_item.setNewData(list);
    }

    @Override
    public void initRecyclerView(RecyclerView rv_2item, RecyclerView rv_service) {
        rv_2item.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));
        rv_service.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));

        rv_2item.setAdapter(adapter_s2);
        rv_service.setAdapter(adapter_item);
        adapter_item.setEmptyView(R.layout.order_list_empty_view_p, rv_service);
        adapter_s2.setEmptyView(R.layout.order_list_empty_view_p, rv_2item);
    }


    @Override
    public void confirm() {
        List<FixServie> fixServieList = new ArrayList<>();
        for (FixServie fx : pick_servieList) {
            if (fx.selectde())
                fixServieList.add(fx);
        }

        if (fixServieList.size() == 0) {
            ToastUtils.showToast("请最少选择一个项目！");
            return;

        }
        getView().onConfirm(fixServieList);


    }

    @Override
    public void seekServerforKey(String key) {


        if (key.equals("")) {
            ToastUtils.showToast("请输入内容！");
            return;
        }


        mdl.searchServer(id, key, new RxSubscribe<FixServieEntity>(getView().getSelfActivity(), true) {
            @Override
            protected void _onNext(FixServieEntity fixServieEntity) {

                getView().showServiceList();
                set2Data(fixServieEntity.getProjectList());

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
        for (FixServie fx : pick_servieList) {
            if (fx.selectde())
                allPrice = allPrice + fx.getPriceD();
        }

        getView().setPickAllPrice("已选择：￥" + MathUtil.twoDecimal(allPrice));

    }

}
