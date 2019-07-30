package com.eb.geaiche.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Technician;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

//员工绩效分配
public class MeeitsAdapter extends BaseQuickAdapter<Technician, BaseViewHolder> {

    Context context;


    public MeeitsAdapter(@Nullable List<Technician> data, Context c) {
        super(R.layout.activity_meeits_list_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Technician item) {


        String percentage;//百分比
        String deduction;//提成额
        String deductionBase;//提成基数


        deductionBase = item.getDeductionBase();

        if (null != item.getPercentage())
            percentage = item.getPercentage();
        else
            percentage = "0.0";

        if (null != item.getDeduction())
            deduction = item.getDeduction();
        else {
            deduction = calculation(percentage, deductionBase);
        }


        EditText priceBase = helper.getView(R.id.tv_price);//基数
        EditText meeits = helper.getView(R.id.tv_meeits);//比例


        priceBase.addTextChangedListener(new TextWatcher() {//调整基数
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {


                    helper.setText(R.id.tv_meeits_v, calculation(percentage, s.toString()));

                } catch (Exception e) {
                    ToastUtils.showToast("请输入正确的数值！");
                    helper.setText(R.id.tv_meeits_v, "0.0");
                }
            }
        });

        meeits.addTextChangedListener(new TextWatcher() {//调整比例
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {

                    helper.setText(R.id.tv_meeits_v, calculation(s.toString(), deductionBase));

                } catch (Exception e) {
                    ToastUtils.showToast("请输入正确的数值！");
                    helper.setText(R.id.tv_meeits_v, "0.0");
                }
            }
        });


        helper.setText(R.id.tv_name, item.getNickName());
        helper.setText(R.id.tv_meeits_v, deduction);
        helper.setText(R.id.tv_meeits, percentage);
        helper.setText(R.id.tv_price, item.getDeductionBase());
    }

    /**
     * @param percentage    百分比
     * @param deductionBase 基数
     */
    private String calculation(String percentage, String deductionBase) {
        String deduction;//提成额
        if (null == percentage)
            percentage = "0.0";


        BigDecimal db = new BigDecimal(deductionBase);//基数
        BigDecimal per = new BigDecimal(percentage);//百分比
        deduction = (db.multiply(per)).divide(new BigDecimal(100), 2, RoundingMode.UP).toString();


        return deduction;
    }
}
