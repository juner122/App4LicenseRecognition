package com.frank.plate.activity;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.R;
import com.frank.plate.activity.fragment.ProductListFragment;
import com.frank.plate.adapter.Brandadapter;

import com.frank.plate.view.CommonPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProductListActivity extends BaseActivity {


    @BindView(R.id.rg_type)
    RadioGroup radioGroup;

    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    ProductListFragment fragment;

    CommonPopupWindow popupWindow;

    RecyclerView commonPopupRecyclerView;
    Brandadapter brandadapter;

    String checkedTag = "rb1";


    private void showPopupWindow(View v) {

        if (v.getTag().equals(checkedTag)) {//判断当前选择的View Tag是否为已选中的View

            Log.d("radioGroup", "showPopupWindow+++当前选中的id为：" + v.getTag().toString());
            //PopupWindow在rb向右弹弹出
            popupWindow.showAsDropDown(v, v.getWidth(), -v.getHeight());
        } else {
            checkedTag = v.getTag().toString();

        }
    }

    @Override
    protected void init() {
        tv_title.setText("商品列表");
        replaceFragment();
    }


    @Override
    protected void setUpView() {
        rb1.setTag("rb1");
        rb2.setTag("rb2");
        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupWindow(view);
            }
        });
        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupWindow(view);
            }
        });

        List<String> list = new ArrayList<>();
        list.add("固特异");
        list.add("固特异");
        list.add("固特异");
        list.add("固特异");
        list.add("固特异");
        list.add("固特异");
        list.add("韩泰韩泰韩泰韩泰");
        list.add("固特异");
        list.add("固特异");
        list.add("固特异");
        list.add("固特异");


        commonPopupRecyclerView = new RecyclerView(this);


        commonPopupRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        brandadapter = new Brandadapter(list);

        brandadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.v("BaseQuickAdapter", "第" + position + "项");
                Toast.makeText(ProductListActivity.this, "第" + position + "项", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }

        });
        commonPopupRecyclerView.setAdapter(brandadapter);

        popupWindow = new CommonPopupWindow.Builder(this)
                //设置PopupWindow布局
//                .setView(R.layout.popup)
                .setView(commonPopupRecyclerView)
                //设置动画
//                .setAnimationStyle(R.style.animHorizontal)
                //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
//                .setBackGroundLevel(0.5f)
                //设置PopupWindow里的子View及点击事件
                //开始构建
                .create();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rb1:
                        fragment.switchData(1);
                        break;
                    case R.id.rb2:
                        fragment.switchData(2);
                        break;
                }
            }

        });
    }


    @Override
    protected void setUpData() {

    }

    private void replaceFragment() {


        fragment = new ProductListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_layout, fragment);
        transaction.commit();
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_product_list;
    }

}
