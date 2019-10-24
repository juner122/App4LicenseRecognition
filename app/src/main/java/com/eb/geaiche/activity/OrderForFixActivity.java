package com.eb.geaiche.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.FixInfoListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.FixInfoActivity;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.MyTimePickerView;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixInfoList;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.geaiche.util.DateUtil.getFormatedDateTime;


//检修单搜索页面
public class OrderForFixActivity extends BaseActivity {


    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.et_key)
    EditText et;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    @BindView(R.id.v_date1)
    TextView v_date1;
    @BindView(R.id.v_date2)
    TextView v_date2;

    MyTimePickerView pvTimeStart, pvTimeEnd;

    Calendar startShowDate = Calendar.getInstance();
    Calendar endShowDate = Calendar.getInstance();
    boolean isdate;
    int page = 1;

    String name;//关键字

    FixInfoListAdapter ola;

    @Override
    protected void init() {

    }

    @Override
    protected void setUpView() {

        ola = new FixInfoListAdapter(R.layout.item_fragment2_main, null, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ola);

        ola.setEmptyView(R.layout.order_list_empty_view, recyclerView);
        ola.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                toActivity(FixInfoActivity.class, "id", ((FixInfoEntity) adapter.getData().get(position)).getId());
            }
        });

        easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                page++;
                searchData();
            }

            @Override
            public void onRefreshing() {

                page = 1;
                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                searchData();

            }
        });


        pvTimeStart = new MyTimePickerView(this);
        pvTimeEnd = new MyTimePickerView(this);

        startShowDate.set(startShowDate.get(Calendar.YEAR), startShowDate.get(Calendar.MONTH), 1);
        endShowDate.set(startShowDate.get(Calendar.YEAR), startShowDate.get(Calendar.MONTH), 31);

        v_date1.setText(getFormatedDateTime(startShowDate.getTime()));
        v_date2.setText(getFormatedDateTime(endShowDate.getTime()));


        pvTimeStart.init(startShowDate, (date, v) -> {
            ((TextView) v).setText(getFormatedDateTime(date));
            startShowDate.setTime(date);
            isdate = true;//设置时间后
            searchData();
            page = 1;
        });

        pvTimeEnd.init(endShowDate, (date, v) -> {

            ((TextView) v).setText(getFormatedDateTime(date));
            endShowDate.setTime(date);
            isdate = true;//设置时间后
            searchData();
            page = 1;
        });
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_search;
    }


    @OnClick({R.id.iv_search, R.id.back, R.id.v_date1, R.id.v_date2})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_search:

                searchData();
                break;
            case R.id.back:
                finish();
                break;

            case R.id.v_date1:

                pvTimeStart.show(v);
                break;

            case R.id.v_date2:

                pvTimeEnd.show(v);

                break;
        }
    }

    private void searchData() {
//        if (TextUtils.isEmpty(et.getText())) {
//            easylayout.refreshComplete();
//            easylayout.loadMoreComplete();
//            ToastUtils.showToast("请输入搜索内容！");
//            return;
//        }
        name = et.getText().toString();
        Api().quotationList(startShowDate.getTime(), endShowDate.getTime(), isdate, name, page).subscribe(new RxSubscribe<FixInfoList>(this, true) {
            @Override
            protected void _onNext(FixInfoList basePage) {

                if (null == basePage.getQuotationList() || basePage.getQuotationList().size() == 0) {
                    ToastUtils.showToast("没有记录！");
                }


                if (page == 1) {
                    easylayout.refreshComplete();
                    ola.setNewData(basePage.getQuotationList());
                    if (basePage.getQuotationList().size() < Configure.limit_page)
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                } else {
                    easylayout.loadMoreComplete();
                    if (basePage.getQuotationList().size() == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }
                    ola.addData(basePage.getQuotationList());
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

}
