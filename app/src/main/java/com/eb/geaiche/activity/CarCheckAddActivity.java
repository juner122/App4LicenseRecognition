package com.eb.geaiche.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.decoration.PickCarCheckListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.activity.StockAddStandardsActivity;
import com.eb.geaiche.stockControl.adapter.PickCategoryListAdapter;
import com.eb.geaiche.util.CarCheckTypeUtil;
import com.eb.geaiche.util.SoftInputUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.CheckOptions;
import com.juner.mvp.bean.NullDataEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CarCheckAddActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    EditText tv_name;//名称
    @BindView(R.id.tv_describe)
    EditText tv_describe;//描述
    @BindView(R.id.tv_type)
    TextView tv_type;//分类

    CheckOptions checkOptions;//对象

    boolean isAdd;

    PickCarCheckListAdapter aba;
    @BindView(R.id.cv)
    CardView cv;//


    @BindView(R.id.rv_category)
    RecyclerView rv_category;//

    int pick_type = 0;//选择的type

    @Override
    protected void init() {
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        if (isAdd)
            tv_title.setText("新增项目");
        else {

            checkOptions = getIntent().getParcelableExtra("CheckOptions");
            tv_title.setText("修改项目");
            tv_name.setText(checkOptions.getName());
            tv_describe.setText(checkOptions.getDescribe());
            tv_type.setText(CarCheckTypeUtil.getType(checkOptions.getType()));

        }
    }

    @Override
    protected void setUpView() {


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_car_check_add;
    }


    @OnClick({R.id.enter, R.id.ll_pick_brand})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter://确认信息

                checkOptions();
                break;
            case R.id.ll_pick_brand://选择品牌
                SoftInputUtil.hideSoftInput(this, view);


                showRv();


                List<CheckOptions> list = new ArrayList<>();
                list.add(new CheckOptions(1));
                list.add(new CheckOptions(2));
                list.add(new CheckOptions(3));
                list.add(new CheckOptions(4));
                list.add(new CheckOptions(5));
                list.add(new CheckOptions(6));
                list.add(new CheckOptions(7));


                aba = new PickCarCheckListAdapter(list, this);
                rv_category.setLayoutManager(new LinearLayoutManager(this));
                rv_category.setAdapter(aba);
                aba.setOnItemClickListener((a, view1, position) -> {//选择分类
                    tv_type.setText(CarCheckTypeUtil.getType(aba.getData().get(position).getType()));


                    pick_type = aba.getData().get(position).getType();

                    hideRv();

                    if (null != checkOptions)
                        checkOptions.setType(aba.getData().get(position).getType());

                });


                break;


        }
    }

    private void checkOptions() {

        if (TextUtils.isEmpty(tv_name.getText())) {
            ToastUtils.showToast("项目名称不能为空！");
            return;
        }
        if (TextUtils.isEmpty(tv_describe.getText())) {
            ToastUtils.showToast("项目描述不能为空！");
            return;
        }


        if (isAdd) {
            if (pick_type == 0) {
                ToastUtils.showToast("请选择项目类型！");
                return;
            }

            CheckOptions addCo = new CheckOptions();
            addCo.setName(tv_name.getText().toString());
            addCo.setDescribe(tv_describe.getText().toString());
            addCo.setType(pick_type);


            Api().addCheckOptions(addCo).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                @Override
                protected void _onNext(NullDataEntity nullDataEntity) {
                    ToastUtils.showToast("新增成功！");
                    finish();
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast("操作失败！" + message);
                }
            });
        } else {
            checkOptions.setName(tv_name.getText().toString());
            checkOptions.setDescribe(tv_describe.getText().toString());

            Api().updateCheckOptions(checkOptions).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                @Override
                protected void _onNext(NullDataEntity nullDataEntity) {
                    ToastUtils.showToast("修改成功！");
                    finish();

                }

                @Override
                protected void _onError(String message) {

                    ToastUtils.showToast("操作失败！" + message);
                }
            });
        }

    }

    //隐藏分类和品牌列表
    private void hideRv() {
        TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        hideAnim.setDuration(300);
        cv.startAnimation(hideAnim);
        cv.setVisibility(View.GONE);
    }

    //显示分类和品牌列表
    private void showRv() {
        TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        hideAnim.setDuration(300);
        cv.startAnimation(hideAnim);
        cv.setVisibility(View.VISIBLE);
    }


}
