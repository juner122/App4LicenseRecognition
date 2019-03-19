package com.eb.geaiche.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.AutoBrandAdapter;
import com.eb.geaiche.adapter.AutoModeladapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.bean.AutoBrand;
import com.juner.mvp.bean.AutoModel;
import com.eb.geaiche.view.CommonPopupWindow;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

import static com.juner.mvp.Configure.brand;
import static com.juner.mvp.Configure.brandModdel;

/**
 * 汽车品牌列表
 */
public class AutoBrandActivity extends BaseActivity {


    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 右侧边栏导航区域
     */
    @BindView(R.id.indexBar)
    IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    @BindView(R.id.tvSideBarHint)
    TextView mTvSideBarHint;

    AutoBrandAdapter adapter;
    List<AutoBrand> list;

    CommonPopupWindow popupWindow;

    AutoModeladapter autoModeladapter;
    RecyclerView commonPopupRecyclerView;


    int brand_id;
    AutoBrand selectAutoBrand;
    private LinearLayoutManager mManager;

    @Override
    protected void init() {
        tv_title.setText("选择车型");

        rv.setLayoutManager(mManager = new LinearLayoutManager(this));

        Api().listByName().subscribe(new RxSubscribe<List<AutoBrand>>(this, true) {
            @Override
            protected void _onNext(List<AutoBrand> autoBrands) {
                Collections.sort(autoBrands);//排序
                list = autoBrands;
                adapter = new AutoBrandAdapter(AutoBrandActivity.this, list);
                rv.setAdapter(adapter);

                mIndexBar.setmSourceDatas(list)//设置数据
                        .invalidate();


                adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                        brand_id = list.get(position).getId();
                        selectAutoBrand = list.get(position);
                        showPopupWindow(brand_id);

                    }
                });
            }

            @Override
            protected void _onError(String message) {

            }
        });

        View ll = getLayoutInflater().inflate(R.layout.common_popup_view, null);


        commonPopupRecyclerView = ll.findViewById(R.id.rv);
        commonPopupRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        autoModeladapter = new AutoModeladapter(null);
        autoModeladapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                popupWindow.dismiss();

                AutoModel autoModel = (AutoModel) adapter.getData().get(position);


                Intent intent = new Intent(AutoBrandActivity.this, CarInfoInputActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(brand, selectAutoBrand);
                bundle.putParcelable(brandModdel, autoModel);
                intent.putExtras(bundle);
                startActivity(intent);

            }

        });
        commonPopupRecyclerView.setAdapter(autoModeladapter);


        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(ll)
                .create();



        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager


    }

    private void showPopupWindow(int brand_id) {


        Api().listByBrand(brand_id).subscribe(new RxSubscribe<List<AutoModel>>(this, false) {
            @Override
            protected void _onNext(List<AutoModel> autoModels) {

                if (autoModels.size() != 0) {
                    //PopupWindow在rb向右弹弹出
                    popupWindow.showAsDropDown(rv, rv.getWidth() / 3, -rv.getHeight());

                    autoModeladapter.setNewData(autoModels);
                } else {
                    Toast.makeText(AutoBrandActivity.this, "查找不到该品牌车型！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void _onError(String message) {

            }
        });


    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_auto_brand_list;
    }
}
