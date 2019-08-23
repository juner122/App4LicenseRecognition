package com.eb.geaiche.activity;

import android.content.Intent;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;


import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MallTypeBrandListAdapter;
import com.eb.geaiche.adapter.MallTypeListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.GoodsBrand;
import com.juner.mvp.bean.GoodsCategory;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.geaiche.mall.fragment.MallMainFragment.VIN;
import static com.eb.geaiche.mall.fragment.MallMainFragment.goodsTitle;

//商城商品分类列表
public class MallTypeActivity extends BaseActivity {

    public static final String goodsBrandId = "goodsBrandId";
    public static final String categoryId = "categoryId";

    String vin;//车辆vin码

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall_type;
    }

    @BindView(R.id.rv1)
    RecyclerView rv1;//商品分类
    @BindView(R.id.rv2)
    RecyclerView rv2;//商品品牌

    @BindView(R.id.ll_rv2)
    View ll_rv2;

    @BindView(R.id.et_key)
    EditText et_key;

    MallTypeListAdapter typeListAdapter;//商品分类
    MallTypeBrandListAdapter brandListAdapter;//商品品牌


    boolean isshow;//品牌列表是否显示

    @OnClick({R.id.back2, R.id.iv_scan, R.id.iv_search})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back2:
                hideList();
                break;

            case R.id.iv_scan:
                toActivity(MallGoodsVinScanActivity.class);
                break;
            case R.id.iv_search:

                if (TextUtils.isEmpty(et_key.getText())) {
                    ToastUtils.showToast("搜索内容不能为空！");
                    return;
                }
                Intent intent = new Intent(MallTypeActivity.this, MallGoodsActivity.class);
                intent.putExtra(goodsTitle, et_key.getText().toString());
                intent.putExtra(VIN, vin);
                startActivity(intent);
                break;
        }
    }

    private void showList() {
        TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        hideAnim.setDuration(300);
        ll_rv2.startAnimation(hideAnim);
        ll_rv2.setVisibility(View.VISIBLE);
        isshow = true;


    }

    private void hideList() {
        TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        hideAnim.setDuration(300);
        ll_rv2.startAnimation(hideAnim);
        ll_rv2.setVisibility(View.GONE);
        isshow = false;

    }

    @Override
    protected void init() {
        tv_title.setText("分类列表");
        vin = MyAppPreferences.getString(VIN);
//        setRTitle(String.format("当前车架号:%s", vin));
    }

    @Override
    protected void setUpView() {

        typeListAdapter = new MallTypeListAdapter(null, this);
        brandListAdapter = new MallTypeBrandListAdapter(null, this);

        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(typeListAdapter);

        rv2.setLayoutManager(new GridLayoutManager(this, 3));
        rv2.setAdapter(brandListAdapter);


        typeListAdapter.setOnItemClickListener((adapter, view, position) -> getBrandList(typeListAdapter.getData().get(position).getCategoryId()));

        brandListAdapter.setOnItemClickListener((adapter, view, position) -> {


            Intent intent = new Intent(MallTypeActivity.this, MallGoodsActivity.class);
            intent.putExtra(goodsBrandId, brandListAdapter.getData().get(position).getBrandId());
            intent.putExtra(categoryId, brandListAdapter.getData().get(position).getCategoryId());
            intent.putExtra(VIN, vin);
            startActivity(intent);

        });


    }

    @Override
    protected void setUpData() {
        queryAll();//获取所有分类
    }

    private void queryAll() {

        Api().queryShopcategoryAll("2").subscribe(new RxSubscribe<List<GoodsCategory>>(this, true) {
            @Override
            protected void _onNext(List<GoodsCategory> goodsCategories) {


                typeListAdapter.setNewData(goodsCategories);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    //获取品牌列表
    public void getBrandList(String id) {

        Api().shopcategoryInfo(id).subscribe(new RxSubscribe<List<GoodsBrand>>(this, true) {
            @Override
            protected void _onNext(List<GoodsBrand> goodsBrands) {

                int size = goodsBrands.size();
                if (size == 0) {
                    ToastUtils.showToast("该分类暂无商品！");
                    hideList();
                    return;
                }
                brandListAdapter.setNewData(goodsBrands);


                if (!isshow)
                    showList();

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        vin = MyAppPreferences.getString(VIN);
//        setRTitle(String.format("当前车架号:%s", vin));
    }
}
