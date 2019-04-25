package com.eb.geaiche.mvp.presenter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.FixInfoServiceItemAdapter;
import com.eb.geaiche.adapter.FixPickService2ItemAdapter;
import com.eb.geaiche.mvp.contacts.FixPickServiceContacts;
import com.eb.geaiche.mvp.model.FixPickServiceMdl;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.MyRadioButton;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.FixService2item;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsList;

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
    int page = 1;//第一页
    EasyRefreshLayout easylayout;
    String categoryId;//当前选的大分类索引id

    public FixPickServicePtr(@NonNull FixPickServiceContacts.FixPickServiceUI view, int layout) {
        super(view);
        mdl = new FixPickServiceMdl(view.getSelfActivity());
        pick_servieList = new HashSet<>();
        adapter_s2 = new FixPickService2ItemAdapter(null);
        adapter_item = new FixInfoServiceItemAdapter(null, layout);
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
        mdl.getServiceData(new RxSubscribe<List<GoodsCategory>>(getView().getSelfActivity(), true) {
            @Override
            protected void _onNext(List<GoodsCategory> list) {

                if (null == list || list.size() == 0) {
                    ToastUtils.showToast("暂无分类！");
                    rg.setVisibility(View.GONE);
                    return;
                }

                rg.setVisibility(View.VISIBLE);
                init0Data(rg, list);//根据第一级类别数量 创建RadioButton

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
        getGoodList(null, page, "");

    }

    public void init0Data(RadioGroup rg, final List<GoodsCategory> list) {
        for (int i = 0; i < list.size(); i++) {
            MyRadioButton radioButton = new MyRadioButton(getView().getSelfActivity(), list.get(i).getName(), i);


            final int finalI = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    set1Data(list.get(finalI).getSonList());
                    categoryId = list.get(finalI).getCategoryId();
                    page = 1;
                    getGoodList(null, page, categoryId);

                }
            });

            rg.addView(radioButton);
        }


    }


    public void set2Data(List<FixServie> list) {


        adapter_item.setNewData(list);
    }

    @Override
    public void initRecyclerView(RecyclerView rv_2item, RecyclerView rv_service, EasyRefreshLayout e) {
        rv_2item.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));
        rv_service.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));

        rv_2item.setAdapter(adapter_s2);
        rv_service.setAdapter(adapter_item);
        adapter_item.setEmptyView(R.layout.order_list_empty_view_p, rv_service);
        adapter_s2.setEmptyView(R.layout.order_list_empty_view_p, rv_2item);
        easylayout = e;
        easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }

            @Override
            public void onRefreshing() {

                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                getData();

            }
        });
    }

    public void loadMoreData() {
        page++;
        getGoodList(getView().getKey(), page, categoryId);
    }

    public void getData() {
        page = 1;
        getGoodList(getView().getKey(), page, categoryId);

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
        page = 1;
        getGoodList(key, page, "");
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


    //查找所有数据
    private void getGoodList(String key, final int page, String categoryId) {
        mdl.getGoodList(new RxSubscribe<GoodsList>(getView().getSelfActivity(), page == 1) {
            @Override
            protected void _onNext(GoodsList goodsList) {

                if (page == 1) {//不等于1 显示更多
                    easylayout.refreshComplete();
                    adapter_item.setNewData(toFixParts(goodsList.getList()));
                    if (goodsList.getList().size() < Configure.limit_page)
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                } else {

                    easylayout.loadMoreComplete();
                    if (goodsList.getList().size() == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }

                    adapter_item.addData(toFixParts(goodsList.getList()));
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        }, key, page, categoryId);
    }


    private List<FixServie> toFixParts(List<Goods> data) {
        List<FixServie> parts = new ArrayList<>();

        for (Goods goods : data) {
            FixServie fp = new FixServie();
            fp.setName(goods.getGoodsTitle());
            fp.setPrice(goods.getXgxGoodsStandardPojoList().get(0).getGoodsStandardPrice());
            fp.setServiceId(goods.getId());
            fp.setNumber(1);//数量
            fp.setType(goods.getType());
            fp.setGoods_sn(goods.getGoodsCode());
            parts.add(fp);
        }
        //遍历选选择了的商品
        for (FixServie parts1 : pick_servieList) {
            for (int i = 0; i < parts.size(); i++) {

                if (parts1.getServiceId() == parts.get(i).getServiceId() && parts1.getSelected() == 1)
                    parts.get(i).setSelected(1);
            }


        }
        return parts;

    }
}
