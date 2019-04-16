package com.eb.geaiche.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.FixInfoPartsItemAdapter;

import com.eb.geaiche.adapter.FixPickParts2ItemAdapter;
import com.eb.geaiche.mvp.contacts.FixPickPartsContacts;

import com.eb.geaiche.mvp.model.FixPickPartsMdl;

import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.MyRadioButton;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixParts2item;
import com.juner.mvp.bean.FixPartsEntityList;
import com.juner.mvp.bean.FixPartsList;
import com.juner.mvp.bean.FixPartsListEntity;
import com.juner.mvp.bean.FixPartsSeek;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FixPickPartsPtr extends BasePresenter<FixPickPartsContacts.FixPickPartsUI> implements FixPickPartsContacts.FixPickPartsPtr {


    FixPickPartsContacts.FixPickPartsMdl mdl;


    FixPickParts2ItemAdapter adapter_s2;//2级分类
    FixInfoPartsItemAdapter adapter_item;//配件


    HashSet<FixParts> pick_partsList;//点击配件

    int id;//当前选择分类id
    int page = 1;//第一页
    EasyRefreshLayout easylayout;
    String categoryId;//当前选的大分类索引id


    public FixPickPartsPtr(@NonNull FixPickPartsContacts.FixPickPartsUI view, int layout) {
        super(view);
        mdl = new FixPickPartsMdl(view.getSelfActivity());
        pick_partsList = new HashSet<>();
        adapter_s2 = new FixPickParts2ItemAdapter(null);
        adapter_item = new FixInfoPartsItemAdapter(null, layout);


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
        rg.removeAllViews();
        mdl.getPartsData(new RxSubscribe<List<GoodsCategory>>(getView().getSelfActivity(), true) {
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
        rg.removeAllViews();//清除所有
        for (int i = 0; i < list.size(); i++) {
            MyRadioButton radioButton = new MyRadioButton(getView().getSelfActivity(), list.get(i).getName(), i);
            final int finalI = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    categoryId = list.get(finalI).getCategoryId();//一级分类id
                    page = 1;
                    getGoodList(null, page, categoryId);
                }
            });


            rg.addView(radioButton);
        }
    }

    public void set2Data(List<FixParts> list) {


        adapter_item.setNewData(list);
    }

    @Override
    public void initRecyclerView(RecyclerView rv_2item, RecyclerView rv_Parts, EasyRefreshLayout e) {
        rv_2item.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));
        rv_Parts.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));

        rv_2item.setAdapter(adapter_s2);
        rv_Parts.setAdapter(adapter_item);
        adapter_item.setEmptyView(R.layout.order_list_empty_view_p, rv_Parts);
        adapter_s2.setEmptyView(R.layout.order_list_empty_view_p, rv_2item);
        easylayout = e;
        // 普通加载
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

        page = 1;
        getGoodList(key, page, null);
    }

    @Override
    public void loadMoreData() {
        page++;
        getGoodList(getView().getKey(), page, categoryId);
    }

    @Override
    public void getData() {
        page = 1;
        getGoodList(getView().getKey(), page, categoryId);

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

    private List<FixParts> toFixParts(List<Goods> data) {
        List<FixParts> parts = new ArrayList<>();

        for (Goods goods : data) {
            FixParts fp = new FixParts();
            fp.setGoods_name(goods.getGoodsTitle());
            fp.setRetail_price(goods.getXgxGoodsStandardPojoList().get(0).getGoodsStandardPrice());
            fp.setGoods_id(goods.getId());
            fp.setNumber(1);//数量

            fp.setType(goods.getType());
            fp.setGoods_sn(goods.getGoodsCode());


            parts.add(fp);
        }

        //遍历选选择了的商品
        for (FixParts parts1 : pick_partsList) {
            for (int i = 0; i < parts.size(); i++) {
                if (parts1.getGoods_id() == parts.get(i).getGoods_id() && parts1.getSelected() == 1)
                    parts.get(i).setSelected(1);
            }


        }

        return parts;

    }
}
