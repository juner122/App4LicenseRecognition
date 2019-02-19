package com.eb.new_line_seller.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.FixInfoListAdapter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.mvp.FixInfoActivity;
import com.eb.new_line_seller.util.ToastUtils;
import com.eb.new_line_seller.view.ConfirmDialogCanlce;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixInfoList;
import com.juner.mvp.bean.NullDataEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FixInfoListFragment extends BaseFragment {

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    List<FixInfoEntity> list = new ArrayList<>();
    FixInfoListAdapter adapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    int status;

    public static FixInfoListFragment newInstance(int position) {
        FixInfoListFragment fragmentOne = new FixInfoListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", position);
        //fragment保存参数，传入一个Bundle对象
        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //取出保存的值
            status = getArguments().getInt("status");
        }

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        getData();
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_orderlist;
    }

    @Override
    protected void setUpView() {

        initData();


    }

    private void initData() {
        adapter = new FixInfoListAdapter(R.layout.item_fragment2_main, list, getContext());


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.order_list_empty_view, recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                toActivity(FixInfoActivity.class, "id", ((FixInfoEntity) adapter.getData().get(position)).getId());
            }
        });
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                page++;
                loadMoreData();
            }

            @Override
            public void onRefreshing() {

                page = 1;
                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                getData();

            }
        });


        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                int status = list.get(position).getStatus();
                if (status == 4 || status == -1) return true;


                //弹出对话框
                final ConfirmDialogCanlce confirmDialog = new ConfirmDialogCanlce(getContext(),"是否要取消该检修单?");
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        cancle(list.get(position).getId());
                        confirmDialog.dismiss();
                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.dismiss();
                    }
                });

                return true;
            }
        });


    }
    int page = 1;//第一页
    private void getData() {
        Api().quotationList(status, page).subscribe(new RxSubscribe<FixInfoList>(mContext, true) {
            @Override
            protected void _onNext(FixInfoList infoList) {
                easylayout.refreshComplete();
                list.clear();
                list = infoList.getQuotationList();
                adapter.setNewData(list);

                if (list.size() < Configure.limit_page)
                    easylayout.setLoadMoreModel(LoadModel.NONE);


            }

            @Override
            protected void _onError(String message) {
                easylayout.refreshComplete();
            }
        });
    }



    private void loadMoreData() {
        Api().quotationList(status, page).subscribe(new RxSubscribe<FixInfoList>(mContext, true) {
            @Override
            protected void _onNext(FixInfoList infoList) {

                easylayout.loadMoreComplete();

                if (infoList.getQuotationList().size() == 0) {
                    ToastUtils.showToast("没有更多了！");
                    easylayout.setLoadMoreModel(LoadModel.NONE);
                    return;
                }

                list.addAll(infoList.getQuotationList());
                adapter.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                easylayout.loadMoreComplete();
            }
        });
    }

    //取消单
    private void cancle(int id) {
        Api().quotationCancle(id).subscribe(new RxSubscribe<NullDataEntity>(mContext, true) {
            @Override
            protected void _onNext(NullDataEntity infoList) {
                ToastUtils.showToast("取消成功！");

                getData();//刷新
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("取消失败！");
            }
        });
    }


    @Override
    protected String setTAG() {
        return "OrderListFragment";
    }
}
