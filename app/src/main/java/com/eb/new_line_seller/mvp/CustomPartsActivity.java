package com.eb.new_line_seller.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.mvp.contacts.CustomContacts;
import com.eb.new_line_seller.mvp.presenter.CustomPtr;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.new_line_seller.mvp.presenter.FixInfoPtr.TYPE;
import static com.eb.new_line_seller.mvp.presenter.FixInfoPtr.TYPE_Parts;
import static com.eb.new_line_seller.mvp.presenter.FixInfoPtr.TYPE_Service;


//自定义商品功能页面
public class CustomPartsActivity extends BaseActivity<CustomContacts.CustomPtr> implements CustomContacts.CustomUI {


    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv_type1)
    TextView tv_type1;

    @BindView(R.id.tv_type2)
    TextView tv_type2;

    @BindView(R.id.tv_num)
    TextView tv_num;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_price)
    EditText et_price;

    @BindView(R.id.et)
    EditText et;


    @OnClick({R.id.tv_confirm, R.id.tv_type1, R.id.tv_type2, R.id.iv_reduce, R.id.iv_plus})
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.tv_confirm:
                getPresenter().confirm(et.getText().toString(), et_name.getText().toString(), et_price.getText().toString(), Integer.parseInt(tv_num.getText().toString()));//提交
                break;

            case R.id.tv_type1:
                getPresenter().pickType1(tv_type1);//选择一级分类
                break;

            case R.id.tv_type2:
                getPresenter().pickType2(tv_type2);//选择二级分类
                break;
            case R.id.iv_reduce:

                getPresenter().numberDown(Integer.parseInt(tv_num.getText().toString()));//减

                break;
            case R.id.iv_plus:
                getPresenter().numberUp(Integer.parseInt(tv_num.getText().toString()));//加
                break;
        }


    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_custom_parts;
    }

    @Override
    protected void init() {
        getPresenter().changeView();
    }


    @Override
    public CustomContacts.CustomPtr onBindPresenter() {
        return new CustomPtr(this);
    }

    @Override
    public void onChangeView(int type) {

        if (type == 0) {
            tv_title.setText("自定义工时");
            tv1.setText("工时名称");
            tv2.setText("工时价格");
        } else {
            tv_title.setText("自定义商品");
            tv1.setText("商品名称");
            tv2.setText("商品价格");
        }
    }

    @Override
    public void setNumber(int num) {
        tv_num.setText(String.valueOf(num));
    }

    @Override
    public void setType1String(String name) {
        tv_type1.setText(name);
    }

    @Override
    public void setType2String(String name) {
        tv_type2.setText(name);
    }

    @Override
    public void confirm(List list, int type) {

        Intent intent = new Intent(this, FixInfoActivity.class);
        Bundle bundle = new Bundle();
        if (type == 0) {
            bundle.putParcelableArrayList(TYPE_Service, (ArrayList) list);
            bundle.putString(TYPE, TYPE_Service);
        } else {
            bundle.putParcelableArrayList(TYPE_Parts, (ArrayList) list);
            bundle.putString(TYPE, TYPE_Parts);
        }
        intent.putExtras(bundle);
        startActivity(intent);


    }
}
