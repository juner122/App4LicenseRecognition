package com.eb.geaiche.activity.fragment;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;


import com.eb.geaiche.adapter.Brandadapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.SoftInputUtil;
import com.eb.geaiche.view.MyRadioButton;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Category;
import com.juner.mvp.bean.CategoryBrandList;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.GoodsList;
import com.juner.mvp.bean.GoodsListEntity;
import com.juner.mvp.bean.SubCategoryEntity;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.CommonPopupWindow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ProductFragment extends BaseFragment {
    public static final int type = 4;

    @BindView(R.id.rg_type)
    RadioGroup radioGroup;
    @BindView(R.id.et_key)
    EditText et_key;

    ProductListFragment fragment;
    String categoryId;//当前选的大分类索引id
    int page = 1;//第一页



    public static ProductFragment getInstance() {
        ProductFragment sf = new ProductFragment();

        return sf;
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        replaceFragment();

        //获取分类
        Api().queryShopcategoryAll(String.valueOf(Configure.Goods_TYPE_4)).subscribe(new RxSubscribe<List<GoodsCategory>>(getContext(), true) {
            @Override
            protected void _onNext(List<GoodsCategory> list) {
                if (null == list || list.size() == 0) {
                    ToastUtils.showToast("暂无分类！");
                    radioGroup.setVisibility(View.GONE);
                    return;
                }

                radioGroup.setVisibility(View.VISIBLE);
                init0Data(radioGroup, list);//根据第一级类别数量 创建RadioButton
            }

            @Override
            protected void _onError(String message) {

            }
        });


        //新接口
        xgxshopgoodsList(null,null);
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_product_list_fr;
    }


    @Override
    protected void setUpView() {





    }
    public void init0Data(RadioGroup rg, final List<GoodsCategory> list) {
        rg.removeAllViews();//清除所有
        for (int i = 0; i < list.size(); i++) {
            MyRadioButton radioButton = new MyRadioButton(getActivity(), list.get(i).getName(), i);
            final int finalI = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    categoryId = list.get(finalI).getCategoryId();//一级分类id

                    xgxshopgoodsList(null, categoryId);
                }
            });

            rg.addView(radioButton);
        }


    }
    /**
     * 新商品接口
     *
     * @param key 搜索关键字
     */

    private void xgxshopgoodsList(String key,String categoryId) {
        Api().xgxshopgoodsList(key, null, categoryId, 1, type,50).subscribe(new RxSubscribe<GoodsList>(getContext(), true) {
            @Override
            protected void _onNext(GoodsList goods) {
                fragment.switchData(goods.getList());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }



    @OnClick({R.id.iv_search})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_search:

                if (!TextUtils.isEmpty(et_key.getText())) {
//                    onQueryAnyGoods4Key(et_key.getText().toString());
                    xgxshopgoodsList(et_key.getText().toString(),null);
                    SoftInputUtil.hideSoftInput(getContext(), et_key);
                } else
                    ToastUtils.showToast("请输入搜索关键字！");

                break;

        }


    }

    private void replaceFragment() {
        fragment = ProductListFragment.getInstance(1);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_layout, fragment);
        transaction.commit();
    }


    public static final String TAG = "ProductListFragment";

    @Override
    protected String setTAG() {
        return TAG;
    }

}
